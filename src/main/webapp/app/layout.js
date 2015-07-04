(function() {
	var root = this;
	var YV = {
		init: init,
		resize: resize,
		layout: layout
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
		var oldGridSize = YV.dim?YV.dim.gridSize:null;
		
		var ww = $(window).width();
		var wh = $(window).height();
		YV.dim = {
			windowWidth: ww,
			windowHeight: wh,
			gridSize: ((ww>=1320)?5:((ww>=1000)?3:(ww>=768?2:1))),
			aspectRatio: 1.5,
			containersTop: (ww>=768?10:4),
			qContainerWidth: Math.floor( ww * (ww>=768?0.75:0.96) ),
			qContainerHeight: Math.floor(1.5*wh),
			aqContainerWidth: Math.floor( ww * (ww>=768?0.25:0) ),
			aqContainerHeight: Math.floor(1.5*wh),
		};
		YV.dim.gridCellWidth = Math.floor( YV.dim.qContainerWidth/YV.dim.gridSize );
		YV.dim.gridCellHeight = Math.floor( (YV.dim.qContainerWidth/YV.dim.gridSize)/YV.dim.aspectRatio );
		// YV.dim.leftGutter = Math.floor(YV.dim.gridCellWidth * (ww>=992?0.02:0.02));//TODO: maybe use padding here...
		$('.q-cont').css('width',''+YV.dim.qContainerWidth+'px').css('height',''+YV.dim.qContainerHeight+'px').css('left','0px');
		$('.side-cont').css('width',''+YV.dim.aqContainerWidth+'px').css('height',''+(wh-120)+'px').css('left',''+YV.dim.qContainerWidth+'px');
		// var logoClass = (ww>=920)?'logo-big':(ww>=768?'logo-med':'logo-sml');
		// $('.logo').removeClass('logo-big logo-med logo-sml').addClass(logoClass);
		
		if ( (!YV.cells) || (YV.dim.gridSize!=oldGridSize) ) {
			$('.q').hide();
			YV.layout();
		}
		resizeCells();
		$('.q-cont').show();
	};
	
	function resizeCells() {
		if (YV.dim.maxY) {
			YV.dim.qContainerHeight = YV.dim.maxY*YV.dim.gridCellHeight;
			$('.q-cont').css('height',''+YV.dim.qContainerHeight+'px');
		}
		
		var cellsCount = YV.cells.length;
		for(var i=0;i<cellsCount;i++) {
			var c = YV.cells[i];
			var sel = c[3];
			if (sel) {
				var x = c[0]*YV.dim.gridCellWidth;
				var y = c[1]*YV.dim.gridCellHeight;
				var w = c[2]*YV.dim.gridCellWidth;
				var h = c[2]*YV.dim.gridCellHeight;
				$(sel).css('top',''+y+'px').css('left',''+x+'px').css('height',''+h+'px').css('width',''+w+'px').show();
			}
		}
		
	};
	
	
	function layout() {
		
		YV.cells = [];
		var gridSize = YV.dim.gridSize;
		var maxY = 0;
		var smallSquarePopularChance = 1/Math.pow(2,gridSize);//chances that a small square is a popular question instead of a newer question
		var newerQuestions = $('.q[data-q-sort=newer]');
		var popularQuestions = $('.q[data-q-sort=popular]');
		var newerQuestionsCount = newerQuestions.length;
		var popularQuestionsCount = popularQuestions.length;
		var noMoreNewerQuestions = (newerQuestionsCount<=0);
		var noMorePopularQuestions = (popularQuestionsCount<=0);
		var newerQuestionsIndex = 0;
		var popularQuestionsIndex = 0;
		var howManyPerSquaresBatch = Math.max(6,Math.min(17,Math.floor(newerQuestionsCount/4)));
		
		while(newerQuestionsIndex<newerQuestionsCount || popularQuestionsIndex<popularQuestionsCount) {
			var squares = randomSquares(gridSize,maxY,howManyPerSquaresBatch);
			var squaresCount = squares.length;
			for(var i=0;i<squaresCount;i++) {
				var square = squares[i];
				var x=square[0],y=square[1],sz=square[2];
				var q = null;
				maxY = Math.max(maxY,y+sz);
				
				if (sz===1 && (Math.random()<smallSquarePopularChance) ) {//small square
					if (noMoreNewerQuestions) {//popular questions when the newer ones are done...
						if (!noMorePopularQuestions) {
							q = popularQuestions[popularQuestionsIndex];
							popularQuestionsIndex++;
						}
					} else {//newer question
						q = newerQuestions[newerQuestionsIndex];
						newerQuestionsIndex++; 
					}
				} else {//popular question
					if (!noMorePopularQuestions) {
						q = popularQuestions[popularQuestionsIndex];
						popularQuestionsIndex++;
					}
				}
				if (q==null) {
					if (noMoreNewerQuestions && noMorePopularQuestions) break;
					if (noMoreNewerQuestions && !noMorePopularQuestions) {
						q = popularQuestions[popularQuestionsIndex];
						popularQuestionsIndex++;
					} else if (noMorePopularQuestions && !noMoreNewerQuestions) {
						q = newerQuestions[newerQuestionsIndex];
						newerQuestionsIndex++; 
					}
					if (q==null) break;
				}
				
				if (q) {//finally
					var qid = $(q).attr('data-q-id');
					var sel = '#q'+qid;
					var selqq = '#qq'+qid;
					YV.cells.push([x,y,sz,sel,qid]);
					var clr = randomInt(1,6);
					$(q).attr('data-q-x',x).attr('data-q-y',y).attr('data-q-sz',sz).removeClass('q-sz-1 q-sz-2 q-sz-3').addClass('q-sz-'+sz).addClass('q-bs-'+clr);
					$(selqq).addClass('qq-bg-'+clr);
				}
			
				if (newerQuestionsIndex>=newerQuestionsCount) {
					noMoreNewerQuestions = true;
				}
				if (popularQuestionsIndex>=popularQuestionsCount) {
					noMorePopularQuestions = true;
				}
				if (noMoreNewerQuestions && noMorePopularQuestions) break;
			}
		}
		
		YV.dim.maxY = maxY;
	};
	
	var layoutTemplates = {
		// {squares: [[x,y,size]], height: h}
		'grid-1': [
			{ sq:[ [0,0,1] ], h:2 }
		],
		'grid-2': [
			{ sq:[ [0,0,2] ], h:3 }, 
			{ sq:[ [0,1,2] ], h:5 }
		],
		'grid-3': [
			{ sq:[ [0,0,2] ], h:3 },
			{ sq:[ [1,0,2] ], h:2 },
			{ sq:[ [0,1,2] ], h:4 },
			{ sq:[ [1,1,2] ], h:4 }
		],
		'grid-5': [
			// with big square
			{ sq:[ [0,0,3], [3,1,2] ], h:4 }, { sq:[ [0,0,3], [3,2,2] ], h:4 },
			{ sq:[ [1,0,3] ], h:4 },
			{ sq:[ [2,0,3] ], h:4 }, { sq:[ [2,0,3], [0,1,2] ], h:4 }, { sq:[ [2,0,3], [0,2,2] ], h:4 }, 
			{ sq:[ [3,0,2], [0,2,3] ], h:5 },
			{ sq:[ [1,0,2], [0,2,2], [2,2,3] ], h:5 },
			{ sq:[ [0,0,2], [2,1,3] ], h:4 },
			{ sq:[ [0,2,2], [2,1,3] ], h:4 },
			{ sq:[ [1,0,2], [1,2,3] ], h:5 },
			
			// only medium squares
			{ sq:[ [0,1,2], [2,0,2], [3,2,2] ], h:4 },
			{ sq:[ [0,0,2], [3,1,2] ], h:3 },
			{ sq:[ [1,0,2], [3,1,2], [1,2,2] ], h:4 },
			{ sq:[ [0,0,2], [3,0,2] ], h:3 },
			{ sq:[ [0,0,2], [0,2,2], [3,1,2] ], h:4 }
		]
	};
	
	function randomSquares(gridSize, initialY, howManyPerSquaresBatch) {
		var howMany = howManyPerSquaresBatch;
		var offset = 0;
		var templates = layoutTemplates['grid-'+gridSize];
		var maxTemplates = templates.length;
		var ret = [];
		
		//special dummy case
		if (gridSize==1) {
			for(var i=0;i<howMany;i++) {
				ret.push([0,initialY+i,1]);
			}
			return ret;
		}
		
		var bitmap = [];
		for (var k=0;k<gridSize;k++) {
			bitmap[k] = [];
			// for(var l=0;l<howMany+initialY;l++) {
			// 	bitmap[k][l] = 0;
			// }
		}
		
		
		var count = 0;
		for(var i=0;i<howMany;i++) {
			var idx = randomInt(0,maxTemplates-1);
			var template = templates[idx];
			var sqaresCount = template.sq.length;
			for(var j=0;j<sqaresCount;j++) {
				var square = template.sq[j];
				var x = square[0];
				var y = square[1]+offset+initialY;
				var sz = square[2];
				ret.push([x,y,sz]);
				count = count+1;
				for (var k=x;k<x+sz;k++) {
					if (!bitmap[k]) bitmap[k] = [];
					for(var l=y;l<y+sz;l++) {
						bitmap[k][l-initialY] = 1;
					}
				}
			}
			offset += template.h;
			if (count>=howMany) break;
		}
		
		//add 1-size random squares for the newer questions
		for (var k=0;k<gridSize;k++) {
			for(var l=0;l<offset;l++) {
				var bit = bitmap[k][l];
				if (bit!==1) {
					var emptyRow = true;
					for(r=0;r<gridSize;r++) {
						if (bitmap[r][l]===1) emptyRow=false;
					}
					if (emptyRow) {
						var randCol = randomInt(k,gridSize-1);
						bitmap[randCol][l]=1;
						ret.push([randCol,l+initialY,1]);
					} else if ( Math.random() < ( (1/gridSize) + (0.3*(offset-l)/offset)) ) {//%chances
						ret.push([k,l+initialY,1]);
					}
				}
			}
		}
		
		return ret;
	};
	
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