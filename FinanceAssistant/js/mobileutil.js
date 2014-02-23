
(function(){
    var quickExp = /^(!?)(-?)([0-9]+)/;
    var toString = Object.prototype.toString;
    var hsbcip = window.hsbcip =  function(root,targetList,targetListClass,targetIndex,targetIsRefresh,targetDirect,refreshCallback,listShowDivId,ulStyleClass,optionTitles){
    	return new hsbcip.fn.init(root,targetList,targetListClass,targetIndex,targetIsRefresh,targetDirect,refreshCallback,listShowDivId,ulStyleClass,optionTitles);
    };
     
    hsbcip.prototype = hsbcip.fn = {
    	init : function(root,targetList,targetListClass,targetIndex,targetIsRefresh,targetDirect,refreshCallback,listShowDivId,ulStyleClass,optionTitles){

    	  targetList = targetList||[];
    	  this._targetList_ = targetList;
    	  this._targetListClass_ = targetListClass;
    	  this._targetIndex_ = targetIndex;
    	  this._targetIsRefresh_ = targetIsRefresh
    	  this._targetDirect_ = targetDirect;
    	  this._refreshCallback_ = refreshCallback;
    	  this._optionTitles_ = optionTitles;
    	  this._listShowDivId_ = listShowDivId;
    	  this._ulStyleClass_ = ulStyleClass;
    	  this._root_ = root;

    	  var $this = this;
    	  $.each(targetList,function(index){

    	  	$this.synDataAndCommonBoxes(root[index],targetListClass[index],this,listShowDivId[index],ulStyleClass[index],targetIndex[index],targetDirect[index],targetIsRefresh[index],refreshCallback[index],optionTitles[index]);
    	  });
	  		return this;    	  	
    	},

    	synDataAndCommonBoxes : function(root,targetClass,targetName,listShowDivId,ulStyleClass,index,targetDirect,refresh,fn,title){
    		var elem = $("select[name=" + targetName + "]",root);

    		var $this = this;

    		$this.listenComboBox(elem,"change",refresh,fn);
    		
			//
			$this.loadOptions(root,listShowDivId,ulStyleClass,elem,targetClass,index,refresh,targetDirect,title);
			
    		$("a."+ targetClass,root).click(function(){
    			$this.loadOptions(root,listShowDivId,ulStyleClass,elem,targetClass,index,refresh,targetDirect,title);
    		});
    		
    	},

    	listenComboBox : function(elem,eventType,refresh,excuteFunction){
    		if(refresh){
    			elem.unbind(eventType);
    			elem.bind(eventType,function(){
    				if(excuteFunction){
    					excuteFunction.apply(this,arguments);
    				}
				});
    		}	
    	},

    	loadOptions : function(root,listShowDivId,ulStyleClass,elem,targetClass,index,refresh,targetDirect,title){
    		var self = this;
    		var ul = $("#" + listShowDivId).find("ul." + ulStyleClass);
    		ul.prev("div").html(title?title:"");

    		ul.html("");
				var optionsArr = elem.find("option");
				var len = optionsArr.length;

				if(toString.call(index)!=="[object String]"){
						index = new String(index);
				}
				var _match = quickExp.exec(index);
				var end = 0, start = -1, stop1 = -1,stop2 = 0;		

				if(_match[3]){
						_match[3] = _match[3] - 0;
						if(len > _match[3] || _match[3] == len){

								if(_match[1]){

									if(_match[2]){
										stop2	= len -  _match[3];
									}else{
										stop1 = _match[3];
									}
								}else{
									if(_match[2]){
										end	= len -  _match[3];
									}else{
										start = _match[3];
									}
								}
						}
				}
    		optionsArr.each(function(j){	
			if((start>-1&&((j == start || j > start)))||(end>0&&j<end)||(stop1>-1&&j!=stop1)||(stop2>0&&j!=stop2)){
    				var li = $("<li></li>");
    				var _hash = refresh?"javascript:;":"#";

    				if(targetDirect){
    					if(targetDirect.indexOf("@")!=-1){
    						_hash = targetDirect.split("@")[0];
    					}else{
    						_hash = targetDirect;
    					}
    				}
    				var a = $("<a href="+_hash+"></a>");

    				if(targetDirect&&targetDirect.indexOf("@")==-1){
    					a.addClass("slideback");
    				}else{ 
    				      a.addClass("slide");
    				 } 
    				a.html($(this).html());
    				li.attr("lang",$(this).val());

    				a.click(function(event){
    					var $this = $(this);
    					var selectIndex = self.fillSelectBox(root,targetClass,elem,$this.html());

    					if(refresh){

    						elem.trigger("change",[function(){
    							self.fillSelectBox(root,targetClass,elem,$this.html());
    							 if(_hash&&_hash!="#"&&_hash[0]=="#"){
		    					    	if(targetDirect.indexOf("@")!=-1){
		    					    		var _index_ = self.find(targetDirect.split("@")[1]);
		    					    		if(_index_!=-1){
		    					    			var _elem = $("select[name=" + targetDirect.split("@")[1] + "]",root);
		    					    			self.listenComboBox(_elem,"change",self._targetIsRefresh_[_index_],self._refreshCallback_[_index_]);
		    					    			self.loadOptions(self._root_[_index_],self._listShowDivId_[_index_],self._ulStyleClass_[_index_],_elem,self._targetListClass_[_index_],self._targetIndex_[_index_],self._targetIsRefresh_[_index_],self._targetDirect_[_index_],self._optionTitles_[_index_]);
		    					    			
		    					    		}
		    					    	}	
		    					    }
    						},selectIndex]);
    					}else{
    					    if(_hash&&_hash!="#"&&_hash[0]=="#"){
    					    	if(targetDirect.indexOf("@")!=-1){
    					    		    var _index_ = self.find(targetDirect.split("@")[1]);
		    					    if(_index_!=-1){
		    					    	var _elem = $("select[name=" + targetDirect.split("@")[1] + "]",root);
    					    			self.listenComboBox(_elem,"change",self._targetIsRefresh_[_index_],self._refreshCallback_[_index_]);
    					    			self.loadOptions(self._root_[_index_],self._listShowDivId_[_index_],self._ulStyleClass_[_index_],_elem,self._targetListClass_[_index_],self._targetIndex_[_index_],self._targetIsRefresh_[_index_],self._targetDirect_[_index_],self._optionTitles_[_index_]);
		    					    			
		    					    }
    					    	}	
    					    }
    					}	
    				});
    				li.append(a);
						ul.append(li);
    			}
    		});
    	},

    	fillSelectBox : function(root,targetClass,elem,content){
    		var index = 0;
    		$("a."+ targetClass,root).html(content).parent().addClass("filled");
			elem.find("option").each(function(j){
			if($(this).html()== content){
				$(this).parent().val($(this).val());
				index  = j;
			}else{
				$(this).removeAttr("selected");		
			}	
		});
		return index;		
    	},

    	find : function(targetName){
    		var index  = -1;
    		$.each(this._targetList_,function(i){
    			if(this==targetName){
    				index = i;
    				return false;
    			}
    		});
    		return index;
    	}
    };	

    hsbcip.fn.init.prototype = hsbcip.fn;
    
    
    hsbcip.fn.reviewAllSubmitFields = function(root,ulStyle,liStyle,fieldsName){
    	fieldsName = fieldsName||[];
    	var root = root;
	var liList = root.find("ul." + ulStyle).find("li." + liStyle);
	var self = this;
	$.each(fieldsName,function(){
		var $this = this;
		var temp = root.find("*[name=" + this + "]:last");
		if(temp.val()&&temp.val()!=""){
			var fillLi = null;
			$.each(liList,function(){
			     if($(this).hasClass($this)){
			     	fillLi = $(this);
			     	return false;
			     }
			});
			if(temp[0].nodeName&&temp[0].nodeName=="SELECT"){
			   (fillLi&&fillLi.find("a").html(temp.find("option[value*="+temp.val()+"]").html()));
			   (fillLi&&fillLi.addClass("filled"));
			}
			if(temp[0].nodeName&&temp[0].nodeName=="INPUT"){
				if(fillLi&&fillLi.find("input").size()>0){
					fillLi.find("input").attr("placeholder",temp.val());
			     		fillLi.addClass("filled");
				}else if(fillLi&&fillLi.find("input").size()==0){
					fillLi.find("a").html(temp.val());
			     		fillLi.addClass("filled");
				}
			   	
			}
			fillLi = null;	
		}
	});
    }
})();