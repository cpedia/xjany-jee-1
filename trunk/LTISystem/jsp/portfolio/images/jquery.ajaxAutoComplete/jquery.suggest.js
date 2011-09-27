	
	/*
	 *	jquery.suggest 1.1 - 2007-08-06
	 *	
	 *	Uses code and techniques from following libraries:
	 *	1. http://www.dyve.net/jquery/?autocomplete
	 *	2. http://dev.jquery.com/browser/trunk/plugins/interface/iautocompleter.js	
	 *
	 *	All the new stuff written by Peter Vulgaris (www.vulgarisoip.com)	
	 *	Feel free to do whatever you want with this file
	 *
	 */
	
	(function(){
		var _st = window.setTimeout;
		window.setTimeout = function(fRef, mDelay) {
			if(typeof fRef == 'function'){
				var argu = Array.prototype.slice.call(arguments,2);
				var f = (function(){ fRef.apply(null, argu); });
				return _st(f, mDelay);
			}
			return _st(fRef,mDelay);
		}
    })();
	
	(function($) {

		$.suggest = function(input, options) {
	
			var $input = $(input).attr("autocomplete", "off");
			var $results = $(document.createElement("ul"));

			var timeout = false;		// hold timeout ID for suggestion results to appear	
			var prevLength = 0;			// last recorded length of $input.val()
			var cache = [];				// cache MRU list
			var cacheSize = 0;			// size of cache in chars (bytes?)
			
			var firstRun=true;
			
			var timestamp=0;

			$results.addClass(options.resultsClass).appendTo('body');
				

			resetPosition();
			$(window)
				.load(resetPosition)		// just in case user is changing size of page while loading
				.resize(resetPosition);

			$input.blur(function() {
				setTimeout(function() { $results.hide() }, 200);
			});
			
			
			// help IE users if possible
			try {
				$results.bgiframe();
			} catch(e) { }


			// I really hate browser detection, but I don't see any other way
			if ($.browser.mozilla)
				$input.keypress(processKey);	// onkeypress repeats arrow keys in Mozilla/Opera
			else
				$input.keydown(processKey);		// onkeydown repeats arrow keys in IE/Safari
			



			function resetPosition() {
				// requires jquery.dimension plugin
				var offset = $input.offset();
				$results.css({
					top: (offset.top + input.offsetHeight) + 'px',
					left: offset.left + 'px'
				});
				
			}
			
			function resetPositionWithBrowser(items) {
				// requires jquery.dimension plugin
				//alert($results.attr("clientHeight"));
				var offset = $input.offset();
				var maxHeight; 
				
				var actualHeight; 
				if(options.haveSubTokens == true){
					maxHeight = options.maxShowSize * 2 * 18 - 7;
					actualHeight = items.length * 2 * 18;
				}
				else
				{
					maxHeight = options.maxShowSize * 20 - 5;
					actualHeight = items.length * 20;
				}
				var offsetheight;
				if(maxHeight > actualHeight)
					offsetheight = actualHeight;
				else
					offsetheight = maxHeight;
				//alert(document.body.scrollHeight + "input: " + offset.top);
				var browser = document.body.scrollHeight;
				//alert(offset.top + offsetheight);
				if((offset.top + offsetheight) > browser){
					//alert("hello!");
					$results.css({
						top: (offset.top - offsetheight) + 'px',
						left: offset.left + 'px'
					});
				}
				else
				{
					//alert(input.offsetHeight);
					$results.css({
						top: (offset.top + input.offsetHeight) + 'px',
						left: offset.left + 'px'
					});
				}
				
			}
			
			function processKey(e) {				
				// handling up/down/escape requires results to be visible
				// handling enter/tab requires that AND a result to be selected
				if ((/27$|38$|40$/.test(e.keyCode) && $results.is(':visible')) ||
					(/^13$|^9$/.test(e.keyCode) && getCurrentResult())) {
		            
		            if (e.preventDefault)
		                e.preventDefault();
					if (e.stopPropagation)
		                e.stopPropagation();

	                e.cancelBubble = true;
	                e.returnValue = false;
				
					switch(e.keyCode) {
	
						case 38: // up
							prevResult();
							break;
				
						case 40: // down
							nextResult();
							break;
	
						case 9:  // tab
						case 13: // return
							selectCurrentResult();
							break;
							
						case 27: //	escape
							$results.hide();
							break;
							
						case 13://enter
							$input.val($results.children('li:first-child').text());
							$results.hide();
							break;
	
					}
					
				} else if ($input.val().length != prevLength) {

					if (timeout) 
						clearTimeout(timeout);
					timestamp=(new Date()).getTime();
					timeout = window.setTimeout(suggest, options.delay,""+timestamp);
					prevLength = $input.val().length;
					
					
				}else if(firstRun){
					if (timeout) 
						clearTimeout(timeout);
					timestamp=(new Date()).getTime();
					timeout = window.setTimeout(suggest, options.delay,""+timestamp);
					prevLength = $input.val().length;
					firstRun=false;
				}					
					
				
			}
			
			
			function suggest(ts) {
				
				//alert("ts and timestamp:"+ts+","+timestamp);
				
				if(ts!=timestamp)return;
			
				var q = $.trim($input.val());

				if (q.length >= options.minchars) {
					
					cached = checkCache(q);
					
					if (cached) {
					
						displayItems(cached['items']);
						
					} else {
					
						$.get(options.source, {q: q}, function(txt) {
							
							if(ts!=timestamp)return;
							
							$results.hide();
							
							var items = parseTxt(txt, q);
							
							displayItems(items);
							addToCache(q, items, txt.length);
							
						});
						
					}
					
				} else {
				
					$results.hide();
					
				}
				
				//setTimeout(hide, 50);
					
			}
			
			function hide(){
				$results.hide();
			}
			
			function checkCache(q) {

				for (var i = 0; i < cache.length; i++)
					if (cache[i]['q'] == q) {
						cache.unshift(cache.splice(i, 1)[0]);
						return cache[0];
					}
				
				return false;
			
			}
			
			function addToCache(q, items, size) {

				while (cache.length && (cacheSize + size > options.maxCacheSize)) {
					var cached = cache.pop();
					cacheSize -= cached['size'];
				}
				
				cache.push({
					q: q,
					size: size,
					items: items
					});
					
				cacheSize += size;
			
			}
			
			function displayItems(items) {
				
				if (!items)
					return;
					
				if (!items.length) {
					$results.hide();
					return;
				}
				
				var html = '';
				
				for (var i = 0; i < items.length; i++){
					if(options.haveSubTokens){
						var tokens=items[i].split(options.subDelimiter);
						html += '<li><font color="red">' + tokens[1] + ' : </font><div><span>'+ tokens[0]+'</span></div></li>';
					}
					else html+='<li>'+items[i]+'</li>';
					if( i==(items.length-1) || i==(options.maxShowSize-1) ){
						break;
					}
				}
				
				
				$results.html(html);
				resetPositionWithBrowser(items);
				$results.show();
				$results.children('li:eq(0)').addClass(options.selectClass);
				
				$results
					.children('li')
					.mouseover(function() {
						$results.children('li').removeClass(options.selectClass);
						$(this).addClass(options.selectClass);
					})
					.click(function(e) {
						e.preventDefault(); 
						e.stopPropagation();
						selectCurrentResult();
					});
							
			}
			
			function parseTxt(txt, q) {
				
				var items = [];
				var tokens = txt.split(options.delimiter);
				
				// parse returned data for non-empty items
				for (var i = 0; i < tokens.length; i++) {
					var token = $.trim(tokens[i]);
					if (token) {
						token = token.replace(
							new RegExp(q, 'ig'), 
							function(q) { return '<span class="' + options.matchClass + '">' + q + '</span>' }
							);
						items[items.length] = token;
					}
				}
				
				return items;
			}
			
			function getCurrentResult() {
			
				if (!$results.is(':visible'))
					return false;
			
				var $currentResult = $results.children('li.' + options.selectClass);
				
				if (!$currentResult.length)
					$currentResult = $results.children('li:first-child');
					
				return $currentResult;

			}
			
			function selectCurrentResult() {
			
				$currentResult = getCurrentResult();
			
				if ($currentResult) {
					if(options.haveSubTokens)$input.val($currentResult.children('div').children('span').text() );
					else $input.val($currentResult.text() );
					
					$results.hide();
					
					if (options.onSelect)
						options.onSelect.apply($input[0]);
						
					if(options.runOnSelect2){
						onSelect2();
					}	
					
					if(options.runOnSelect3){
						onSelect3();
					}
					
					if(options.getFactorList){
						getFactorList();
					}
					
					if(options.getFactorListForRAA){
						getFactorListForRAA();
					}
					
					if(options.getValidStartDate){
						getValidStartDate();
					}
					
					if(options.setDefaultDate){
						setDefaultDate();
					}
					
					if(options.setDefaultDate){
						setDefaultDate();
					}
					
					if(options.afterSelect){
						options.afterFunc();
					}
				}
			
			}
			
			function nextResult() {
			
				$currentResult = getCurrentResult();
			
				if ($currentResult)
					$currentResult
						.removeClass(options.selectClass)
						.next()
							.addClass(options.selectClass);
				else
					$results.children('li:first-child').addClass(options.selectClass);
			
			}
			
			function prevResult() {
			
				$currentResult = getCurrentResult();
			
				if ($currentResult)
					$currentResult
						.removeClass(options.selectClass)
						.prev()
							.addClass(options.selectClass);
				else
					$results.children('li:last-child').addClass(options.selectClass);
			
			}
			function onSelect2() {
				var $pane=$(options.parameter1);
				var $ajaxContent=$(options.parameter2);
				var $hidden = $(options.parameter4);
				var name = options.parameter3;
				$pane.show();
				$ajaxContent.attr({
					href: '../ajax/GetStrategyParameter.action?name='+name+'&q='+$input.val()
				});
				$ajaxContent.trigger('click');
				$.ajax({
					type:	"post",
					url:	"../ajax/GetStrategyID.action?strategyName=" + $input.val(),
					datatype: "html",
					success: function(result){
						var id = $hidden.attr("id");
						var cut = id.indexOf("D");
						var checkID = id.substr(cut+1);
						if(result!=null){
							$hidden.val(result);
							$("#check"+checkID).attr({href:"../strategy/View.action?ID=" + result + "&action=view"});
						}
						else
						{
							$input.val('STATIC');
							$hidden.val(0);
							$("#check"+checkID).attr({href:"../strategy/View.action?ID=0&action=view"});
						}
					}
				})
			}
			/* update the factors according to the RAA factor*/
			/* modified by ccd  at 08/31/2009*/
			function getFactorList(){
	
				while(indexCount>0)
				{
					$("#index"+indexCount).remove();
					$("#type"+indexCount).remove();
					$("#button"+indexCount).remove();
					--indexCount;
				}
				$.ajax({type: "post",  
	             url:'GetFactorList.action?Symbol='+$('#symbol').val(),  
	             dataType: "html",  
	             success: function(result){  
	             		if(result!=null && result!="")
	             		{
	             			var pairs=result.split("#");
	             			for(var i=0;i<pairs.length;i++){
	             				var pair=pairs[i].split(",");
	             				if(pair!=null&&pair.length==2){
	             					addNewIndex(pair[0], pair[1]);
	             				}
	             			}
	             		}	
	               }
	             })
			}
			/*  because the index is different,above is for name,type,and this is for name,lower,upper*/
			/* added by ccd at 08/31/2009 */
			function getFactorListForRAA(){
				while(indexCount>0)
				{
					$("#index"+indexCount).remove();
					$("#lower"+indexCount).remove();
					$("#upper"+indexCount).remove();
					$("#button"+indexCount).remove();
					--indexCount;
				}
				$.ajax({type: "post",  
	             url:'../betagain/GetFactorList.action?Symbol='+$('#symbol').val(),  
	             dataType: "html",  
	             success: function(result){ 
	             		if(result!=null && result!="")
	             		{
	             			var pairs=result.split("#");
	             			for(var i=0;i<pairs.length;i++){
	             			
	             				var pair=pairs[i].split(",");
	             				if(pair!=null&&pair.length==2){
	             				/* here is the difference */
	             					addNewIndex(pair[0],0,1);
	             				}
	             			}
	             			//pairs[length-1] is null
	             			if(pairs.length>3)
	             				$("#interval").val("30");
	             			else
	             				$("#interval").val("20");
	             		}	
	               }
	             })
			}
			
			/* set the dafault date for Realtime asset allocation and beta gain analyse*/
			/* added by ccd at 08/31/2009 */
			function setDefaultDate(){
				$.ajax({type: "post",  
	             url:'../mutualfund/SetDefaultDate.action?symbol='+$('#symbol').val()+'&interval='+$('#interval').val(),  
	             dataType: "html",  
	             success: function(result){ 
	             		if(result!=null)
	             		{
	             			var pairs=result.split("#");
	             			if(pairs.length==2)
	             			{
	             				$('#startDate').val(pairs[0]);
								$('#endDate').val(pairs[1]);
	             			}
	             		}	
	               }
	             })
			
			}
			
			function onSelect3(){
				var symbol = $input.val();
				var id = $input.attr("id");
				//get the post of the id
				var cut_index = id.indexOf("-");
				var post = id.substr(cut_index);
				$.ajax({
					type:	"post",
					url:	"../ajax/GetPrice.action?Symbol=" + symbol + "&StartingDate =" +  $("#startingDate").val(),
					datatype: "html",
					success: function(result){
						if(result!=null && result!=0.0){
							$("#"+'securityPrice'+ post).val(result);
							var amount = $("#"+'securityTotalAmount' + post).val();
							if(amount!=null && amount!="" && checkRate(amount)==true){
								var shares = amount / result;
								$('#'+'securityShares'+ post).val(shares);
							}
						}
						else
						{
							$("#"+'securityPrice'+ post).val("");
							$('#'+'securityShares'+ post).val("");
						}
					}
				})
			}
			
			function checkRate(input)
			{
			     var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/   
			     if (!re.test(input))
			    {
			        alert("please input the number(e.g:0.02)");
			        return false;
			     }
			     else
			     	return true;
			} 
	
		}
		
		$.fn.suggest = function(source, options) {
		
			if (!source)
				return;
		
			options = options || {};
			options.source = source;
			options.delay = options.delay || 100;
			options.resultsClass = options.resultsClass || 'ac_results';
			options.selectClass = options.selectClass || 'ac_over';
			options.matchClass = options.matchClass || 'ac_match';
			options.minchars = options.minchars || 1;
			options.delimiter = options.delimiter || '\n';
			options.subDelimiter = options.subDelimiter || '#';
			options.onSelect = options.onSelect || false;
			options.maxShowSize = options.maxShowSize || 5;
			options.maxCacheSize = options.maxCacheSize || 65536;
			options.haveSubTokens = options.haveSubTokens ||false;
			
			options.parameter1 = options.parameter1 || '';
			options.parameter2 = options.parameter2 || '';
			options.parameter3 = options.parameter3 || '';
			options.parameter4 = options.parameter4 || '';	//the hidden object to record the strategy ID
			options.runOnSelect2 = options.runOnSelect2 || false;
			
			options.afterSelect = options.afterSelect || false;
			options.afterFunc = options.afterFunc || function(){};
			
			//set the price to change
			options.runOnSelect3 = options.runOnSelect3 || false;
			
			options.getFactorList = options.getFactorList || false;
			
			options.getStartDate = options.getStartDate || false;
			
			options.getFactorListForRAA = options.getFactorListForRAA || false;
			
			options.setDefaultDate = options.setDefaultDate || false;
			
			this.each(function() {
				new $.suggest(this, options);
			});
	
			return this;
			
		};
		
	})(jQuery);
	
