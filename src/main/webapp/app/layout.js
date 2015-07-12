(function() {
	var root = this;
	var YV = {
		init: init,
		resize: resize,
		layout: layout,
		gridSize: 1,
		grids: {
			'1': {},
			'2': {},
			'3': {pageSize:4},
			'4': {pageSize:5},
			'5': {pageSize:6}
		}
		
	};
	root.YV = YV;
	$(document).ready(function () {
		YV.init();
	});
	
	///////////////////////////////
	function init() {
		
		//retrieve the quesions
		var url = App.reqHostname+App.reqURI+(App.reqURI.length>1?'/':'')+'as/objects'+(App.reqQuery.length>0?'?'+App.reqQuery:'');
		$.get(url,null,function (data,stat,xhr) {
			YV.questions=data;
			YV.resize();
		},'json');
		
		$(document).ready(function(){
			YV.resize();
			
			//handlers
			$(window).resize(function () { YV.resize(); });
			// $(window).scroll(function () { onScroll(); });
		});
	};
	
	//	----- resizing -----
	
	function resize() {
		var ww = $(window).width();
		var wh = $(window).height();
		var navBarH = $('.navbar').height();
		$('.nav-buffer').css('height',''+(navBarH+20)+'px');
		relayout(ww,wh);
		if (YV.gridSize==='3'||YV.gridSize==='4'||YV.gridSize==='5') {
			resizeCells(YV.gridSize,ww,wh);
		} else {
			
		}
	};
	
	function resizeCells(gridSize,winW,winH) {
		var gridInfo = YV.grids[gridSize];
		var qGridWidth = Math.min(Math.floor( (winW-30) * 0.90),1600);
		var qCellWidth = Math.floor(qGridWidth/gridSize);
		var qCellHeight = Math.floor(qCellWidth/1.5);
		var qGridHeight = Math.ceil(qCellHeight*gridInfo.pageSize);
		$('#qContainerGrid').css('height',qGridHeight+60);
		$('#qCarousel').css('height',qGridHeight+60);
		$('#qCarousel .carousel-inner').css('height',qGridHeight+60);
		$('#qCarousel .q-page').css('width',qGridWidth-10).css('height',qGridHeight);
		
		var questions = $('#qCarousel .carousel-inner .q').each(function (idx,q) {
			var square = $(q).data('square');
			var x=square[0]*qCellWidth;var y=square[1]*qCellHeight;var sz=square[2];
			var w = sz*qCellWidth;var h = sz*qCellHeight;
			$(q).css('top',''+(y+5)+'px').css('left',''+(x+5)+'px').css('height',''+(h-15)+'px').css('width',''+(w-15)+'px').show();
		});
		
		$('.aq').height((qGridWidth-20)/4);
		
	}
	
	//	----- layouting -----
	
	function relayout(winW,winH) {
		var appropriateLayout = '1';
		if (winW<=480) {
			appropriateLayout = '1';
		} else if (winW<768) {
			appropriateLayout = '2';
		} else if (winW<992) {
			appropriateLayout = '3';
		} else if (winW<1300) {
			appropriateLayout = '4';
		} else {
			appropriateLayout = '5';
		}
		
		if (YV.gridSize!==appropriateLayout) {
			YV.gridSize=appropriateLayout;
			layout(YV.gridSize,winW,winH);
		}
		
	}
	
	function layout(gridSize,winW,winH) {
		if (gridSize==='1') {
			layoutAsSimpleFlow(winW);
		} else if (gridSize==='2') {
			layoutAsDoubleFlow(winW);
		} else {
			$('#qCarousel').hide();
			layoutAsGridCarousel(gridSize,winW,winH);
			$('#qCarousel').show();
		} 
	}
	
	function layoutAsSimpleFlow(winW) {
		var allQuestions = $('#qContainerFlow .q').detach().removeClass('col-xs-6').addClass('row').css('height','100%');
		$('#qContainerFlow').empty().append(allQuestions);
		colorQuestionsBg('#qContainerFlow');
		
	}
	
	function layoutAsDoubleFlow(winW) {
		var allQuestions = $('#qContainerFlow .q').removeClass('row').detach();
		var qCont = $('#qContainerFlow');		qCont.empty();
		var questionsCount = allQuestions.length;
		var i=0;
		while (i<questionsCount) {
			var bigQuestionChances = Math.random();
			if (bigQuestionChances<0.332 || i+1==questionsCount) {//set a big question
				var q = allQuestions[i];
				$(q).removeClass('col-xs-6').addClass('row').css('height','100%');
				$(qCont).append($(q));
				i=i+1;
			} else {//set a side-by-side pair of questions
				var q1 = allQuestions[i];
				var q2 = allQuestions[i+1];
				$(q1).addClass('col-xs-6');
				$(q2).addClass('col-xs-6');
				$('<div class="row"></div>').append(q1,q2).appendTo(qCont);
				i=i+2;
			}
		}
		setTimeout( function(){
			$('#qContainerFlow .row').each(function (idx) {
				var children = $(this).children('.q');
				var q1 = children[0];
				var q2 = children[1];
				var maxHeight = Math.max($(q1).height(),$(q2).height())+10;
				$(q1).css('height',maxHeight);$(q2).css('height',maxHeight);
			});
			
			colorQuestionsBg('#qContainerFlow');
		},150);
	}
	
	function layoutAsGridCarousel(gridSize,winW,winH) {
		var gridInfo = YV.grids[gridSize];
		$('#qCarousel').carousel('pause');
		var questions = $('#qContainerFlow .q');
		if (questions) {
			var carouselInner = $('#qCarousel .carousel-inner').first();
			var carouselIndicators = $('#qCarousel .carousel-indicators').first();
			$('#qCarousel .carousel-inner .item').remove();
			$(carouselIndicators).empty();
			// var qTempl = $('#qContainerFlow .q').first().clone(true,true);
			
			var idx=0;
			var pagesCount=0;
			var questionsInPageCount=0;
			var questionsInTemplate=0;
			var page=null;
			var questionsCount = questions.length;
			while(idx<questionsCount) {
				var template=randomTemplate(gridSize,gridInfo.pageSize);
					$('<div class="item"><div id="q-pg-'+pagesCount+'" class="q-page"></div></div>').appendTo(carouselInner);
					$('<li data-target="#qCarousel" data-slide-to="'+pagesCount+'"></li>').appendTo(carouselIndicators);
					page = $(('#q-pg-'+pagesCount));
					pagesCount++;
				for(var i=0;i<template.capacity;i++) {
					if (idx>=questionsCount) break;
					var square = template.squares[i];
					var q = $(questions[idx]).clone(true,true);
					$(q).attr('id','q-'+gridSize+'-'+idx);
					//setup question
					$(q).css('position','absolute').data('square',square).addClass(('q-sz-'+square[2]));
					$(q).appendTo(page);
					idx++;
				}
			}
			$('#qCarousel .q .q-desc').removeClass('visible-xs-block');
			$('#qCarousel .carousel-inner .item').first().addClass('active');
			
			colorQuestionsBg('#qCarousel');
			
		} else {//wait for the request to come through...
			YV.gridSize=null;
		}
		setTimeout( function(){
			$('#qCarousel').carousel('cycle');
		},200);
	}
	
	var layoutTemplates = {
		'3': [
			[[0,0,2],[1,2,2]],[[1,0,2],[0,2,2]],[[0,1,2]],[[1,1,2]],[[0,0,2],[0,2,2]],[[1,0,2],[1,2,2]],[[0,1,3]]
		],
		'4': [
			[[0,0,3],[1,3,2]],[[0,0,3],[0,3,2],[2,3,2]],[[0,0,3],[2,3,2]],[[1,0,3],[0,3,2]],[[1,1,3]],[[0,0,2],[1,2,3]],
			[[0,0,2],[2,0,2],[1,2,2]],[[0,0,2],[2,0,2],[1,3,2]],[[0,0,2],[2,0,2],[2,2,2],[0,3,2]],[[0,0,2],[2,0,2],[0,2,2],[2,3,2]],[[0,0,2],[2,1,2],[0,2,2]],[[0,0,2],[1,2,2]],
			[[2,1,2],[0,2,2],[2,3,2]],[[2,0,2],[2,2,2],[0,3,2]],[[0,1,2],[2,2,2],[0,3,2]],[[0,1,2],[2,1,2],[2,3,2]],[[2,0,2],[0,2,2],[2,2,2]],[[0,1,2],[2,0,2],[0,3,2],[2,3,2]]
		],
		'5': [
			[[0,0,3],[3,1,2],[0,3,2],[2,3,3]],[[0,0,3],[3,1,2],[1,3,3]],[[1,0,3],[0,3,3],[3,3,2]],[[1,0,3],[0,3,2],[2,4,2]],[[0,0,2],[2,0,3],[0,3,3],[3,4,2]],
			[[0,0,2],[2,0,3],[0,2,2],[3,3,2]],[[1,0,2],[3,0,2],[2,2,3],[0,3,2]],[[1,0,2],[0,2,2],[2,2,3],[0,4,2]],[[0,0,2],[2,0,2],[0,3,2],[2,3,3]],[[0,0,2],[2,1,2],[0,4,2],[2,3,3]],
			[[0,0,3],[3,0,2],[0,3,2],[2,3,2]],[[0,0,3],[3,1,2],[0,3,2],[3,4,2]],[[0,1,3],[3,0,2],[3,3,2],[1,4,2]],[[1,0,2],[3,1,2],[0,2,3],[3,4,2]],[[1,0,2],[0,2,3],[3,1,2],[3,3,2]]
		]
	}
	
	function randomTemplate(gridSize,pageSize) {
		var gridTemplates=layoutTemplates[gridSize];
		var idx = randomInt(0,gridTemplates.length-1);//random template
		var template = gridTemplates[idx];
		var ret = [];
		var count=0;
		
		var bitmap = [];
		for (var k=0;k<gridSize;k++) {
			bitmap[k] = [];
		}
		
		var sqaresCount = template.length;
		for(var j=0;j<sqaresCount;j++) {
			var square = template[j];
			var x = square[0];
			var y = square[1];
			var sz = square[2];
			ret.push([x,y,sz]);
			count = count+1;
			for (var k=x;k<x+sz;k++) {
				if (!bitmap[k]) bitmap[k] = [];
				for(var l=y;l<y+sz;l++) {
					bitmap[k][l] = 1;
				}
			}
		}
	
		//add 1-size random squares for the additional questions
		var oneSizeCount=0;
		for (var k=0;k<gridSize;k++) {
			for(var l=0;l<pageSize;l++) {
				var bit = bitmap[k][l];
				if (bit!==1) {
					if ( Math.random() < ( (1/gridSize) + (0.7*(pageSize-l)/pageSize)) ) {//%chances
						ret.push([k,l,1]);
						count++;oneSizeCount++;
					}
				}
			}
		}
		
		return {
			capacity: count,
			smallSquares:oneSizeCount,
			squares: ret
		};
	}
	
	//	----- UI veneer -----

	function colorQuestionsBg(container) {
		var gs = YV.gridSize=='1'||YV.gridSize=='2'?parseInt(YV.gridSize)+1:1;
		$((container+' .q')).each(function (idx) {
			var randCol = (idx%gs)==0?randomInt(1,5):0;
			var colorClass = 'q-bg-'+(randCol+1);
			$(this).removeClass('q-bg-1 q-bg-2 q-bg-3 q-bg-4 q-bg-5 q-bg-6').addClass(colorClass);
		});
	}
	
	
	//	----- event handlers -----
	
	
	//	----- utils -----

	function randomInRange(min, max) {
		return Math.random() * (max - min) + min;
	}
	
	function randomBool() {
		return Math.random() >= 0.50;
	}

	function randomInt(min, max) {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	}

}());