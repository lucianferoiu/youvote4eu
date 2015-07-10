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
			'4': {pageSize:5},
			'5': {}
		}
		
	};
	root.YV = YV;
	$(document).ready(function () {
		YV.init();
	});
	
	///////////////////////////////
	function init() {
		
		$(document).ready(function(){
			YV.resize();
			
			//handlers
			$(window).resize(function () { YV.resize(); });
			$(window).scroll(function () { onScroll(); });
		});
	};
	
	function resize() {
		var ww = $(window).width();
		var wh = $(window).height();
		relayout(ww,wh);
		if (YV.gridSize==='3'||YV.gridSize==='4'||YV.gridSize==='5') {
			resizeCells(YV.gridSize);
		} else {
			
		}
	};
	
	function relayout(winW,winH) {
		var appropriateLayout = '1';
		if (winW<=480) {
			appropriateLayout = '1';
		} else if (winW<=768) {
			appropriateLayout = '2';
		} else if (winW<=992) {
			appropriateLayout = '3';
		} else if (winW<=1400) {
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
			
		} 
	}
	
	function layoutAsSimpleFlow(winW) {
		var allQuestions = $('#qContainerFlow .q').detach().removeClass('col-xs-6').addClass('row').css('height','100%');
		$('#qContainerFlow').empty().append(allQuestions);
		colorQuestionsBg();
		
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
			
			colorQuestionsBg();
		},500);
	}
	
	function colorQuestionsBg() {
		var gs = parseInt(YV.gridSize)+1;
		$('#qContainerFlow .q').each(function (idx) {
			var randCol = (idx%gs)==0?randomInt(1,5):0;
			var colorClass = 'q-bg-'+(randCol+1);
			$(this).removeClass('q-bg-1 q-bg-2 q-bg-3 q-bg-4 q-bg-5 q-bg-6').addClass(colorClass);
		});
	}
	
	function resizeCells(gridSize) {
		
	}
	
	

	function randomInRange(min, max) {
		return Math.random() * (max - min) + min;
	}
	
	function randomBool() {
		return Math.random() >= 0.50;
	}

	function randomInt(min, max) {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	}
	
	
	function onScroll() {
		
	};

	
	
}());