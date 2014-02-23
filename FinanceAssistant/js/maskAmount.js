
(function($){
var __iphone5___ = /iPhone\s*\w*\s*5\.*/.test(navigator.userAgent);

$.fn.maskInput = function(symbol){
    var symbol = !symbol;
    var quickExpr = symbol?/[0-9]|\./:/[0-9]/,
	/** HBCA Web Touch 1.1 add begin - iOS 5 amount issue **/
	//check 0.[0-9]
    rfewtenths = /^0\.[0-9]$/,
    //check 0.
    rzerodecimal = /^0\.$/;
	/** HBCA Web Touch 1.1 add end   - iOS 5 amount issue **/
    
    function listenKeyPress(e){
    	e = e||window.event;
    	var k = e.charCode||e.keyCode||e.which;
    	var key = String.fromCharCode(k);
		/** HBCA Web Touch 1.1 change begin - iOS 5 amount issue
    	if(k!=8&&!quickExpr.test(key)){
    		e.preventDefault();
    	}
    	var value = e.target.value;
    	if(!value&&key=="."){
    		e.preventDefault();
    	}
    	if(value=="0"&&key!="."){
    		e.preventDefault();
    	}
    	if(value.indexOf(".")>-1&&key=="."){
    		e.preventDefault();
    	}
		**/
		
		if(!quickExpr.test(key)){
    		e.preventDefault();
    		return false;
    	}
    	var value = e.target.value;
    	if(value==""&&key=="."){
    		e.preventDefault();
    		return false;
    	}
    	if(value=="0"){
    		//The first "."
    		if(key=="."&&!e.target.unrecognized){
    			e.target.unrecognized = true;
    			return true;
    		}
    		//"." already existed
    		if(key=="."&&e.target.unrecognized){
    			e.preventDefault();
    			return false;
    		}
    		//[0-9]
    		if(key!="."){
    			//
    			if(e.target.unrecognized){
    				e.target.unrecognized = null;
    				return true;
    			}
    			e.preventDefault();
    			return false;
    		}
    	}
    	//avoid input "." if "." has already existed
    	if(value.indexOf(".")>-1&&key=="."){
    		e.preventDefault();
    		return false;
    	}
		/** HBCA Web Touch 1.1 change end   - iOS 5 amount issue **/
    }
    
     
    function listenKeyDown(e){
      e = e||window.event;
      var k = e.charCode||e.keyCode||e.which;  
    	if(k==13||k==10){
        	e.preventDefault();
        	return false;
      }
	  /** HBCA Web Touch 1.1 add begin - iOS 5 amount issue **/
	  var val = e.target.value;
      //if 0. when input or  0.[0-9] when you press backspace
      if(rzerodecimal.test(val) || (k==8&&rfewtenths.test(val))){
      	//deem that the key will not be prevented in keypress phase
      	e.target.unrecognized = true;
      }
      //trim out
      //cover IOS4
      if(k==8&&(val=="0"||val=="0.")){
      	//clean up
      	e.target.unrecognized = null;
      } 
	  
	  //handle maxLength of number type
    	if(__iphone5___){
    		var type = e.target.type,ml = e.target.maxLength;
    		if(type=="number"&& ml && val.length > (ml-1)){
    			e.preventDefault();
    			return false;
    		}
    	} 
	  
	  /** HBCA Web Touch 1.1 add end   - iOS 5 amount issue **/
    }
       
    function isFilled(e){
    	 e = e||window.event;
    	 var val = e.target.value;
    	 if(val){
    	 	var parentNode = e.target.parentNode;
    	 	while(parentNode&&parentNode.nodeName!="LI"){
    	 			parentNode = parentNode.parentNode;
    		}
    	 	if(parentNode&&parentNode.className&&parentNode.className.indexOf("filled")==-1){
    	 		parentNode.className = parentNode.className + " filled";
    	 	}
		 /** HBCA Web Touch 1.1 add begin - iOS 5 amount issue **/
    	 }else if(val==''){
    	 		//clear the placehodler
    	 		e.target.placeholder = '';
    	 }
		 /** HBCA Web Touch 1.1 add end   - iOS 5 amount issue **/
    }
    
    function handlePalceHodler(e){
    	e = e||window.event;
    	var val = e.target.value;
    	if(!val){
    		e.target.value = '';
    	}
    }
    
    $(this).bind("keypress",listenKeyPress);
    $(this).bind("keydown",listenKeyDown);
    $(this).bind("keyup",isFilled);
    $(this).bind("focus",handlePalceHodler);
    $(this).one("unmaskInput",function(){
        $(this).unbind("keypress",listenKeyPress);
        $(this).unbind("keydown",listenKeyDown);
        $(this).unbind("keyup",isFilled);
        $(this).unbind("focus",handlePalceHodler);
    });
}

$.fn.unmaskInput=function() {
  return this.trigger('unmaskInput');
};

})(jQuery);