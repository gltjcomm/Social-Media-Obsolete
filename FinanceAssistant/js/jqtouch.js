

(function($) {

		Array.prototype.shiftArray = function(arr){
			if(!arr) return this;
			var obj = {},len = this.length,len2 = arr.length,i=0;	 	
			for(; i<len; i++){
				if(typeof obj[this[i]] === "undefined"){
					obj[this[i]] = "suki";
				}
			}
			for(i=0; i < len2; i++){
				if(obj[arr[i]] == "suki"){
					obj[arr[i]] = "marked";
				}
			}
			//
			this.length = 0;
			for(var name in obj){
				if(obj[name]&&obj[name]=="suki"){
					this[this.length] = name;
				}
			}
			return this;
		};
		
    $.jQTouch = function(options) {

        // Initialize internal jQT variables
        var $body,
            $head=$('head'),
            initialPageId='',
            hist=[],
            trace = [], // for cache the pages
            duplicated = [],
            newPageCount=0,
            jQTSettings={},
            currentPage='',
            orientation='portrait',
            tapReady=true,
            lastTime=0,
            lastAnimationTime=0,
            touchSelectors=[],
            indexDB = null, //for database testing.
            publicObj={},
            tapBuffer=351,
            extensions=$.jQTouch.prototype.extensions,
            animations=[],
            hairExtensions='', 
            iOS5 = /(iPhone|iPod|iPad)\s*\D*\s*5\.*/.test(navigator.userAgent),//iOS5 hacking  
            isNative = (document.cookie.indexOf('native-app')!=-1),          
            defaults = {
                addGlossToIcon: true,
                backSelector: '.back, .cancel, .goback',
                cacheGetRequests: true,
                debug: true,
                fallback2dAnimation: 'fade', 
                fixedViewport: true,
                formSelector: 'form',
                fullScreen: true,
                fullScreenClass: 'fullscreen',
                hoverDelay: 150,
                keep : null, //if null, keep all(on change) #001 
                spot : null, //if null, not ponit to  #001
                icon: null,
                icon4: null, // experimental
                moveThreshold: 10,
                preloadImages: false,
                pressDelay: 1000,
                startupScreen: null,
                statusBar: 'default', // other options: black-translucent, black
               	detectionFormat : false, //disable the detectionFormat (like telephone no.)
                submitSelector: '.submit',
                touchSelector: 'a, .touch',
                unloadMessage: 'Are you sure you want to leave this page? Doing so will log you out of the app.', 
                useAnimations: true,
                preventNode : 'noscript', //#00012
                useFastTouch: false, // experimental
                animations: [ // highest to lowest priority
                    {selector:'.slideleft', name:'slideleft', is3d:false},                
                    {selector:'.cube', name:'cubeleft', is3d:true},
                    {selector:'.cubeleft', name:'cubeleft', is3d:true},
                    {selector:'.cuberight', name:'cuberight', is3d:true},
                    {selector:'.dissolve', name:'fade', is3d:false},
                    {selector:'.fade', name:'fade', is3d:false},
                    {selector:'.flip', name:'flipleft', is3d:true},
                    {selector:'.flipleft', name:'flipleft', is3d:true},
                    {selector:'.flipright', name:'flipright', is3d:true},
                    {selector:'.pop', name:'pop', is3d:true},
                    {selector:'.slide', name:'slideleft', is3d:false},
                    {selector:'.slidedown', name:'slidedown', is3d:false},
                    {selector:'.slideright', name:'slideright', is3d:false},
                    {selector:'.slideup', name:'slideup', is3d:false},
                    {selector:'.swap', name:'swapleft', is3d:true},
                    {selector:'#jqt > * > ul li a', name:'slideleft', is3d:false}
                ]
            };
        
        function _debug(message) {
           /* now = (new Date).getTime();
            delta = now - lastTime;
            lastTime = now;
            if (jQTSettings.debug) {
                if (message) {
                    console.log(delta + ': ' + message);
                } else {
                    console.log(delta + ': ' + 'Called ' + arguments.callee.caller.name);
                }
            }*/
        }
        function addAnimation(animation) {
            _debug();
            if (typeof(animation.selector) === 'string' && typeof(animation.name) === 'string') {
                animations.push(animation);
            }
        }
        function addPageToHistory(page, animation, reverse) {
            _debug();
            hist.unshift({
                page: page,
                animation: animation,
				hash : '#' + page.attr('id'),
                id: page.attr('id')
            });
        }
        function clickHandler(e) {
            _debug();
			if(!tapReady){
                _debug('ClickHandler handler aborted because tap is not ready');
				e.preventDefault();
				return false;
			}
			//Find the nearest touch Element
			var target = $(e.target);
			if(!target.is(touchSelectors)){
				target = target.closest(touchSelectors);
			}
			//If the ancestor is not an external link
			if(target[0]&&target.attr('href')&&!target.isExternalLink()){
				//need to prevent default click behavior
				if(target.attr('href')!='javascript:;' && target.attr('href').indexOf('javascript:') == 0){
					//do nothing
					//trigger the defautl event
				}else{
					e.preventDefault();
				}

			}
            if ($.support.touch) {
                // Touch handler will trigger tap handler
            } else {
                // Convert the click to a tap
                target.makeActive();
                target.trigger('tap', e);
            }
        }
        function doNavigation(fromPage, toPage, animation, backwards) {
            _debug();
            // _debug('animation.name: ' + animation.name + '; backwards: ' + backwards);

            // Error check for target page
            if (toPage.length === 0) {
                $.fn.unselect();
                _debug('Target element is missing.');
                return false;
            }

            // Error check for fromPage===toPage
            if (toPage.hasClass('current')&&fromPage.attr("id")==toPage.attr("id")) {
                $.fn.unselect();
                _debug('You are already on the page you are trying to navigate to.');
                return false;
            }

            // Collapse the keyboard
            $(':focus').blur();
            //kill the keyborad #00014
						$(window.document.activeElement).blur();						
						if(isNative&&iOS5){
							toPage.find('div.toolbar').css('position','absolute');
						}
						if(iOS5){
							toPage.css('top',window.pageYOffset);
						}						
						//
            // Make sure we are scrolled up to hide location bar
            // toPage.css('top', window.pageYOffset);

            fromPage.trigger('pageAnimationStart', { direction: 'out' });
            toPage.trigger('pageAnimationStart', { direction: 'in' });
            $body.trigger('navigationStart',{from:fromPage,to:toPage});          
            if ($.support.animationEvents && animation && jQTSettings.useAnimations) {
                tapReady = false;

                // Fail over to 2d animation if need be
                if (!$.support.transform3d && animation.is3d) {
                    animation.name = jQTSettings.fallback2dAnimation;
                }
                
                // Reverse animation if need be
                var finalAnimationName;
                if (backwards) {
                    if (animation.name.indexOf('left') > 0) {
                        finalAnimationName = animation.name.replace(/left/, 'right');
                    } else if (animation.name.indexOf('right') > 0) {
                        finalAnimationName = animation.name.replace(/right/, 'left');
                    } else if (animation.name.indexOf('up') > 0) {
                        finalAnimationName = animation.name.replace(/up/, 'down');
                    } else if (animation.name.indexOf('down') > 0) {
                        finalAnimationName = animation.name.replace(/down/, 'up');
                    } else {
                        finalAnimationName = animation.name;
                    }
                } else {
                    finalAnimationName = animation.name;
                }
               
                // Bind internal "cleanup" callback
                fromPage.bind('webkitAnimationEnd', navigationEndHandler);             
                // Trigger animations
                toPage.addClass(finalAnimationName + ' in current');
                //
                fromPage.addClass(finalAnimationName + ' out');

            } else {
                toPage.addClass('current');
                navigationEndHandler();
            }
            //#00015
           	$("#loading").css({
	        		height : $(document).height()
	        	}); 	
						if(iOS5){
							toPage.css('top',0);
							window.scrollTo(0,0);
						}		        	        	
            // Define private navigationEnd callback
            function navigationEndHandler(event) {
                _debug();

                if ($.support.animationEvents && animation && jQTSettings.useAnimations) {
                    fromPage.unbind('webkitAnimationEnd', navigationEndHandler);
                    //fromPage.attr('class', '');
                    //toPage.attr('class', 'current');
                    //#006
                    fromPage.removeClass(finalAnimationName + ' current out');
                    toPage.removeClass(finalAnimationName + ' in');
                   // toPage.addClass('current');
                    // toPage.css('top', 0);
                } else {
                    //fromPage.attr('class', '');
                    //#006
                    fromPage.removeClass('current');
                }
                //remove the duplicated pages #0011
								while(duplicated.length){
									$("#"+duplicated.shift()).remove();
								}
								//take care the spot #0012
								if(jQTSettings.spot&&toPage.attr("id")==jQTSettings.spot){
										//
										trace.shiftArray(jQTSettings.keep);
										//remove what we do not wanna keep
										while(trace.length){
											$("#" + trace.shift()).remove();										
										}
										//merge the keep
										trace = $.merge(trace,jQTSettings.keep);
								}
                // Housekeeping
                currentPage = toPage;
                if (backwards) {
                    hist.shift();
                } else {
                    addPageToHistory(currentPage, animation);
                }
                fromPage.unselect();
                lastAnimationTime = (new Date()).getTime();
                setHash(currentPage.attr('id'));
                tapReady = true;
                // Finally, trigger custom events
                toPage.trigger('pageAnimationEnd', {direction:'in', animation:animation});
                fromPage.trigger('pageAnimationEnd', {direction:'out', animation:animation});
								//$body.trigger('pageLoaded',{});
            }
            
            // We's out
            return true;
        }
        function getOrientation() {
            _debug();
            return orientation;
        }
        function goBack() {
            _debug();

            // Error checking
            if (hist.length < 1 ) {
                _debug('History is empty.');
            }
            
            if (hist.length === 1 ) {
                _debug('You are on the first panel.');
            }
            
            var from = hist[0],
                to = hist[1];

            if (doNavigation(from.page, to.page, from.animation, true)) {
                return publicObj;
            } else {
                _debug('Could not go back.');
                return false;
            }
            
            // Prevent default behavior
            return false;
        }
        function goTo(toPage, animation, reverse) {
            _debug();
				
            if (reverse) {
                console.warn('The reverse parameter was sent to goTo() function, which is bad.');
            }
            var fromPage = hist[0].page,aNi = false;
            if (typeof animation === 'string') {
            	  if(animation=="slide"){
            	  	animation = {selector:'.slideleft', name:'slideleft', is3d:false};
            	  	aNi = true;
            	  }else if(animation=="slideback"){
            	  	animation = {selector:'.slideright', name:'slideright', is3d:false};
            	  	aNi = true;
            	  }else{
	                for (var i=0, max=animations.length; i < max; i++) {
	                    if (animations[i].name === animation) {
	                        animation = animations[i];
	                        aNi = true;
	                        break;
	                    }
	                }            	  	
            	  }
            	  if(!aNi){
            	  	animation = null;
            	  }
            }
          
            if (typeof(toPage) === 'string') {
                var nextPage = $(toPage);
                if (nextPage.length < 1) {
                    showPageByHref(toPage, {
                        'animation': animation
                    });
                    return;
                } else {
                    toPage = nextPage;
                }
                
            }
            if (doNavigation(fromPage, toPage, animation, reverse)) {
                return publicObj;
            } else {
                _debug('Could not animate pages.');
                return false;
            }
        }
        function hashChange(e) {
            /*_debug();
            if (location.href == hist[1].href) {
                goBack();
            } else {
                _debug(location.href+' == '+hist[1].href);
            }*/
            if (hist[1] === undefined) {
                _debug('There is no previous page in history');
            } else {
                if(location.hash === hist[1].hash) {
                    goBack();
                } else {
                    _debug(location.hash + ' !== ' + hist[1].hash);
                }
            }
        }
        function init(options) {
            _debug();
            jQTSettings = $.extend({}, defaults, options);

            // Preload images
            if (jQTSettings.preloadImages) {
                for (var i = jQTSettings.preloadImages.length - 1; i >= 0; i--) {
                    (new Image()).src = jQTSettings.preloadImages[i];
                };
            }

            // Set appropriate icon (retina display stuff is experimental)
            if (jQTSettings.icon || jQTSettings.icon4) {
                var precomposed, appropriateIcon;
                if (jQTSettings.icon4 && window.devicePixelRatio && window.devicePixelRatio === 2) {
                    appropriateIcon = jQTSettings.icon4;
                } else if (jQTSettings.icon) {
                    appropriateIcon = jQTSettings.icon;
                } else {
                    appropriateIcon = false;
                }
                if (appropriateIcon) {
                    precomposed = (jQTSettings.addGlossToIcon) ? '' : '-precomposed';
                    hairExtensions += '<link rel="apple-touch-icon' + precomposed + '" href="' + appropriateIcon + '" />';
                }
            }

            // Set startup screen
            if (jQTSettings.startupScreen) {
                hairExtensions += '<link rel="apple-touch-startup-image" href="' + jQTSettings.startupScreen + '" />';
            }

            // Set viewport
            if (jQTSettings.fixedViewport) {
                hairExtensions += '<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;"/>';
            }

            // Set full-screen
            if (jQTSettings.fullScreen) {
                hairExtensions += '<meta name="apple-mobile-web-app-capable" content="yes" />';
                 //hide the status bar by homescreen
                if (jQTSettings.statusBar) {
                    hairExtensions += '<meta name="apple-mobile-web-app-status-bar-style" content="' + jQTSettings.statusBar + '" />';
                }
            }
						//if true, disable the format-detection #003
						if(jQTSettings.detectionFormat){
							hairExtensions += '<meta name="format-detection" content="telephone=no" />';
						}
            // Attach hair extensions
            if (hairExtensions) {
                $head.prepend(hairExtensions);
            }
            //
            if(jQTSettings.spot){
            	//we do not take the judgement for the type of the data, please do pass the String type, use "," to seperate
            	if(jQTSettings.keep){
            		//to Array
            		jQTSettings.keep = jQTSettings.keep.split(",");
            	}
            }
        }
        function insertPages(nodes, animation,firstPage) {
            //_debug();
            var targetPage = null,id,_index,cId,$node,forJude,findTargetPageByTargetDivId=false,firstPageFlag=false;
            $(nodes).each(function(index, node) {
            		_index = -1;
                $node = $(this);
                id = $node.attr("id");
                firstPageFlag=(firstPage&&id===firstPage);
                if($node.is(jQTSettings.preventNode)||this.nodeType == 8){
                	return;
                }
                forJude = false;
                if (!id) {
                    $node.attr('id', 'page-' + (++newPageCount));
                    forJude = true;
                }
                //#009
                if(!forJude){
                	if(id=="jqt"){
                		$($node.html()).each(function(){
                			if(this.id){
                				_index = $.inArray(this.id,trace);
												if( _index < 0){
													trace.push(this.id);
												}else{
													cId = this.id + $.now();
													var tempObj = $("#"+this.id);
													tempObj.find("[id]").attr("id","");
													tempObj.attr("id",cId);
													duplicated.push(cId);
													tempObj = null;
												}                				
                			}
											$body.append($(this)); 
											if(this.id){
												var $page = $(this);
												$body.trigger("pageInserted",{page:$page});
												$page = null;	
											}
											if($(this).hasClass('current')||!targetPage ||firstPageFlag){
												$(this).removeClass('current');
												if(firstPageFlag){
													targetPage = $(this);
													findTargetPageByTargetDivId=true;
												}else if(!findTargetPageByTargetDivId){
													targetPage = $(this);
												}
											}          			
                		});
                	}else{
	                	_index = $.inArray(id,trace);
										if( _index < 0){
											trace.push(id);
										}else{
											cId = id + $.now();
											var tempObj = $("#"+id);
											tempObj.find("[id]").attr("id","");
											tempObj.attr("id",cId);
											duplicated.push(cId);
											tempObj = null;
										}                      		
                	}        	
                }
      				  //	
                //the event will not trigger here
                //#0010	
		        		//$body.trigger('pageInserted', {page: $node.appendTo($body)});
								if(id!=="jqt"){
									$body.append($node);
								}
		        		if(id&&id!="jqt"){
		        			$body.trigger("pageInserted",{page:$node});
		        		}
								//$node.appendTo();
								//
                if ($node.hasClass('current') || !targetPage || firstPageFlag ) {
                	 	//remove the current class whatever has or not
                    $node.removeClass('current');
                    if(firstPageFlag){
                    	targetPage = $("#"+firstPage);
                    	findTargetPageByTargetDivId=true;
                    }else if(!findTargetPageByTargetDivId){
                    	targetPage = $node;
                    }                    
                }
                $node = null;
            });
            findTargetPageByTargetDivId=false;
            //
  					$body.trigger("pageLoaded",{page:targetPage});
  					
            if (targetPage !== null && !targetPage.is("#empty")) {
                goTo(targetPage, animation);
                return targetPage;
            } else {
                return false;
            }
        }
        function mousedownHandler(e) {
            var timeDiff = (new Date()).getTime() - lastAnimationTime;
            if (timeDiff < tapBuffer) {
                return false;
            }
        }
        function orientationChangeHandler() {
            _debug();

            orientation = Math.abs(window.orientation) == 90 ? 'landscape' : 'portrait';
            $body.removeClass('portrait landscape').addClass(orientation).trigger('turn', {orientation: orientation});
        }
        
        function setHash(hash) {
            _debug();
            if(isNative){
            	return;
            }
            // trim leading # if need be
			//remove to not filter the hash
            /*hash = hash.replace(/^#/, ''),*/ 
            
            // remove listener
            // window.removeEventListener('hashchange', hashChange, false);
            window.onhashchange = null;
            
            // change hash
            location.hash = '#' + hash;
            // add listener
            // window.addEventListener('hashchange', hashChange, false);
            window.onhashchange = hashChange;

        }
        function showPageByHref(href, options) {
            _debug();

            var defaults = {
                data: null,
                method: 'GET',
                animation: null,
                callback: null,
                $referrer: null
            };

            var settings = $.extend({}, defaults, options);

            if (href != '#') {
                // show the fully overlayer                 
                $body.addClass('loading');
                //            	
                $.ajax({
                    url: href,
                    data: settings.data,
                    type: settings.method,
                    success: function (data, textStatus) {                    	
						/** HBCA Web Touch 1.1 iOS 5 hot fix begin - remove header contain for Ajax response **/
						if(typeof data === "string"){
							data = data.replace(/(?:<\s*(head)[^>]*>[\w\W]*(?:<\/\1>))/i, '');
						}
						/** HBCA Web Touch 1.1 iOS 5 hot fix end   **/
                        var firstPage = insertPages(data, settings.animation,settings.firstPage);
                        if (firstPage) {
                            if (settings.method == 'GET' && jQTSettings.cacheGetRequests === true && settings.$referrer) {
                                settings.$referrer.attr('href', '#' + firstPage.attr('id'));
                            }
                            if (settings.callback) {
                                settings.callback(true);
                            }
                        }
                    },
                    error: function (data) {
                        if (settings.$referrer) {
                            settings.$referrer.unselect();
                        }
                        if (settings.callback) {
                            settings.callback(false);
                        }
                    },
                    complete : function(){
                    	//$body.removeClass("loading");
                    }
                });
            } else if (settings.$referrer) {
                settings.$referrer.unselect();
            }
        }
        function submitForm(e, callback, firstPage) {
            _debug();       
						//will keep the original
            $(':focus').blur();
						//kill the keyboard
						//#00013
						$(window.document.activeElement).blur();
            var $form = (typeof(e)==='string') ? $(e).eq(0) : (e.target ? $(e.target) : $(e));
            if ($form.length && $form.is(jQTSettings.formSelector) && $form.attr('action')) {
                showPageByHref($form.attr('action'), {
                    data: $form.serialize(),
                    method: $form.attr('method') || "POST",
                    animation: animations[0] || null,
                    callback: function(){
                    	if(callback&&$.isFunction(callback)){
                    		callback.apply($body,[]);
                    	}
                    	$body.removeClass("loading");
                    },
                    firstPage : firstPage
                });
                return false;
            }
            return false;
        }
        function submitParentForm($el) {
            _debug();

            var $form = $el.closest('form');
            if ($form.length === 0) {
                _debug('No parent form found');
            } else {
                _debug('About to submit parent form');
                var evt = $.Event('submit');
                evt.preventDefault();
                $form.trigger(evt);
                return false;
            }
            return true;
        }
        function supportForAnimationEvents() {
            _debug();

            return (typeof WebKitAnimationEvent == 'object');
        }
        function supportForCssMatrix() {
            _debug();

            return (typeof WebKitCSSMatrix != 'undefined');
        }
        function supportForTouchEvents() {
            _debug();
            
            // If dev wants fast touch off, shut off touch whether device supports it or not
            if (!jQTSettings.useFastTouch) {
                return false
            }
            
            // Dev must want touch, so check for support
            if (typeof TouchEvent != 'undefined') {
                if (window.navigator.userAgent.indexOf('Mobile') > -1) { // Grrrr...
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        };
        function supportForTransform3d() {
            _debug();
            
            var head, body, style, div, result;

            head = document.getElementsByTagName('head')[0];
            body = document.body;

            style = document.createElement('style');
            style.textContent = '@media (transform-3d),(-o-transform-3d),(-moz-transform-3d),(-ms-transform-3d),(-webkit-transform-3d),(modernizr){#jqtTestFor3dSupport{height:3px}}';

            div = document.createElement('div');
            div.id = 'jqtTestFor3dSupport';

            // Add to the page    
            head.appendChild(style);
            body.appendChild(div);

            // Check the result
            result = div.offsetHeight === 3;

            // Clean up
            style.parentNode.removeChild(style);
            div.parentNode.removeChild(div);

            // Pass back result
            // _debug('Support for 3d transforms: ' + result);
            return result;
        };
        function tapHandler(e){
            _debug(); 
			if (!tapReady) {
                _debug('Tap is not ready');
                return false;
            }			
            // Grab the target element
            var $el = $(e.target);
						//whatever, caculate the height of document to syn loading
						//consider to remove that in future
            $("#loading").css({
		        	height : $(document).height()
		        });  						
            // Find the nearest tappable ancestor
            if (!$el.is(touchSelectors)) {
                $el = $el.closest(touchSelectors);
            }
            // Make sure we have a tappable element
            if (!$el.length || !$el.attr('href')) {
                _debug('Could not find a link related to tapped element');
                return false;
            }
			//now we can go and handle the tap event
            var target = $el.attr('target'),
                hash = $el.attr('hash'),href = $el.attr('href'),
                animation = null;

            if (!$el.length) {
                _debug('Nothing tappable here');
                return false;
            }
			// Make the tap elemet active
            $el.makeActive();
            //
            if ($el.isExternalLink()) {
            		if(href!="javascript:;"){
            			$el.unselect();
            		}
                return true;
            }
			if ($el.is(jQTSettings.backSelector)) {
                // User clicked or tapped a back button
                goBack(hash);
            } else if ($el.is(jQTSettings.submitSelector)) {
                // User clicked or tapped a submit element
                submitParentForm($el);
            } else if (target == '_webapp') {
                // User clicked or tapped an internal link, fullscreen mode
                window.location = href;
                return false;

            } else if (href === '#') {
                // Allow tap on item with no href
                $el.unselect();
                return true;
            } else {
                //convert the animation with the version 1.0
				if($el.is(".slideback")){
					animation = {selector:'.slideright', name:'slideright', is3d:false};
				}else if($el.is(".slide")){
					animation = {selector:'.slideleft', name:'slideleft', is3d:false};
				}else{
					// Figure out the animation to use
					for (var i=0, max=animations.length; i < max; i++) {
						if ($el.is(animations[i].selector)) {
							animation = animations[i];
							break;
						}
					};            			
				}            	
                if (!animation) {
                    console.warn('Animation could not be found. Using slideleft.');
                    animation = 'slideleft';
                }
                //#101229
                if (hash && hash!='#' && href===hash) {
                    // Internal href
                    goTo($(hash).data('referrer', $el), animation, $(this).hasClass('reverse'));
                    return false;

                } else if(href.indexOf("javascript:")==0){
                		//handle the href handle javascript execute
						return true;
                }else {
                    // External href
                    showPageByHref(href, {
                        animation: animation,
                        callback: function() {
                        		$body.removeClass('loading');
                            setTimeout($.fn.unselect, 250, $el);
                        },
                        $referrer: $el
                    });
                    return false;
                }
            }
        }
        
        // Get the party started
        init(options);
        
        // Document ready stuff
        $(document).ready(function() {

            // Store some properties in the jQuery support object
            $.support.animationEvents = supportForAnimationEvents();
            $.support.cssMatrix = supportForCssMatrix();
            $.support.touch = supportForTouchEvents();
            $.support.transform3d = supportForTransform3d();
            //$.support.dataBase = ('openDatabase' in window);
           	//
            
            if (!$.support.touch) {
                console.warn('This device does not support touch interaction, or it has been deactivated by the developer. Some features might be unavailable.');
            }
            if (!$.support.transform3d) {
                console.warn('This device does not support 3d animation. 2d animations will be used instead.');
            }
            
            // Define public jQuery functions
            $.fn.isExternalLink = function() {
                var $el = $(this);
                return ($el.attr('target') == '_blank' || $el.attr('rel') == 'external' || $el.is('input[type="checkbox"], input[type="radio"], a[href^="http://maps.google.com"], a[href^="mailto:"], a[href^="tel:"], a[href^="javascript:;"], a[href*="youtube.com/v"], a[href*="youtube.com/watch"]'));
            }
            $.fn.makeActive = function() {
                return $(this).addClass('active');
            }
            $.fn.press = function(fn) {
                if ($.isFunction(fn)) {
                    return $(this).live('press', fn);
                } else {
                    return $(this).trigger('press');
                }
            }
            $.fn.swipe = function(fn) {
                if ($.isFunction(fn)) {
                    return $(this).live('swipe', fn);
                } else {
                    return $(this).trigger('swipe');
                }
            }
            $.fn.tap = function(fn) {
                if ($.isFunction(fn)) {
                    return $(this).live('tap', fn);
                } else {
                    return $(this).trigger('tap');
                }
            }
            $.fn.unselect = function(obj) {
                if (obj) {
                    obj.removeClass('active');
                } else {
                    $('.active').removeClass('active');
                }
            }

            // Add extensions
            for (var i=0, max=extensions.length; i < max; i++) {
                var fn = extensions[i];
                if ($.isFunction(fn)) {
                    $.extend(publicObj, fn(publicObj));
                }
            }
            
            // Set up animations array
            if (jQTSettings['cubeSelector']) {
                console.warn('NOTE: cubeSelector has been deprecated. Please use cubeleftSelector instead.');
                jQTSettings['cubeleftSelector'] = jQTSettings['cubeSelector'];
            }
            if (jQTSettings['flipSelector']) {
                console.warn('NOTE: flipSelector has been deprecated. Please use flipleftSelector instead.');
                jQTSettings['flipleftSelector'] = jQTSettings['flipSelector'];
            }
            if (jQTSettings['slideSelector']) {
                console.warn('NOTE: slideSelector has been deprecated. Please use slideleftSelector instead.');
                jQTSettings['slideleftSelector'] = jQTSettings['slideSelector'];
            }
            for (var i=0, max=defaults.animations.length; i < max; i++) {
                var animation = defaults.animations[i];
                if(jQTSettings[animation.name + 'Selector'] !== undefined){
                    animation.selector = jQTSettings[animation.name + 'Selector'];
                }
                addAnimation(animation);
            }
            
            // Create an array of stuff that needs touch event handling
            touchSelectors.push('input');
            touchSelectors.push(jQTSettings.touchSelector);
            touchSelectors.push(jQTSettings.backSelector);
            touchSelectors.push(jQTSettings.submitSelector);
            /* prevent callout to copy image, etc when tap to hold */ 
			touchSelectors = touchSelectors.join(', ');
            $(touchSelectors).css('-webkit-touch-callout', 'none');
            // Make sure we have a jqt element
            $body = $('#jqt');
            if ($body.length === 0) {
                console.warn('Could not find an element with the id "jqt", so the body id has been set to "jqt". This might cause problems, so you should prolly wrap your panels in a div with the id "jqt".');
                $body = $('body').attr('id', 'jqt');
            }

            // Add some 3d specific css if need be
            if ($.support.transform3d) {
                $body.addClass('supports3d');
            }
						//standalone -- true  if the application is started  by homescreen
            if (jQTSettings.fullScreenClass && window.navigator.standalone == true) {
                $body.addClass(jQTSettings.fullScreenClass + ' ' + jQTSettings.statusBar);
            }
            
            //attach touchstart event
						if(isNative&&iOS5){
							$body.bind('touchstart',function(e){
								currentPage.find('div.toolbar').css('position','fixed');
							});
						}
            // Bind events
            $body.bind('click', clickHandler);
            $body.bind('mousedown', mousedownHandler);
            $body.bind('orientationchange', orientationChangeHandler);
            $body.bind('submit', submitForm);
            $body.bind('tap', tapHandler);
            $body.trigger('orientationchange');


            // Normalize href
            // remove to fix the bug of reloading when the hash has been set to same value
            /*if (location.hash.length) {
                location.replace(location.href.split('#')[0]);
            }*/

            // Make sure exactly one child of body has "current" class
            /*
            if ($('#jqt > .current').length == 0) {
                currentPage = $('#jqt > *:first');
            } else {
                currentPage = $('#jqt > .current:first');
                $('#jqt > .current').removeClass('current');
            }*/
            var pages = $body.find(">.current");
						if(pages.length == 0){
							currentPage = $body.find('> *:first');
						} else {
                currentPage = $body.find('> .current:first');
                pages.removeClass('current');
            }
						//release the memory
						pages = null;
            // Go to the top of the "current" page
            $(currentPage).addClass('current');
            setHash($(currentPage).attr('id'));
            initialPageId = $(currentPage).attr('id');
            addPageToHistory(currentPage);
            //I am not sure this will work
            scrollTo(0, 0);
            //Cache the pages loaded
            $body.find(">div").each(function(){
            	var id = this.id;
            	if($.inArray(id,trace) < 0){
            		trace.push(id);
            	}
            });
        });

        // Expose public methods and properties
        publicObj = {
            animations: animations, 
            hist: hist, 
            settings: jQTSettings,
            support: $.support,
            getOrientation: getOrientation,
            goBack: goBack,
            goTo: goTo,
            addAnimation: addAnimation,
            submitForm: submitForm
        }
        return publicObj;
    }

    // Extensions directly manipulate the jQTouch object, before it's initialized.
    $.jQTouch.prototype.extensions = [];
    $.jQTouch.addExtension = function(extension) {
        $.jQTouch.prototype.extensions.push(extension);
    }

})(jQuery);
