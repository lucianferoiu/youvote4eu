(function() {
	var root = this;
	var YV = {
		randomSquares:randomSquares,
		init: init,
		resize: resize,
		layout: layout,
		onScroll: onScroll
		
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
			gridSize: ((ww>=1200)?5:((ww>=992)?3:(ww>=768?2:1))),
			aspectRatio: 1.5,
			qcontainerWidth: Math.floor(ww*.76),
			qcontainerHeight: Math.floor(1.5*wh)
		};
		YV.dim.gridCellWidth = Math.floor( YV.dim.qcontainerWidth/YV.dim.gridSize );
		YV.dim.gridCellHeight = Math.floor( (YV.dim.qcontainerWidth/YV.dim.gridSize)/YV.dim.aspectRatio );
		YV.dim.leftGutter = Math.floor(YV.dim.gridCellWidth*0.10);
		$('.q-cont').css('width',''+YV.dim.qcontainerWidth+'px').css('height',''+YV.dim.qcontainerHeight+'px').css('left',''+YV.dim.leftGutter+'px');
		
		if ( (!YV.cells) || (YV.dim.gridSize!=oldGridSize) ) {
			YV.layout();
		}
		resizeCells();
		$('.q-cont').show();
	};
	
	function layout() {
		/*
		random layout batch
		do while more questions and layout squares
			next square
			if square is for new
				extract new question
				assign square to it
			else
				extract new question
				assign square to it
			endif
			build cell and add it to the batch
			if no more layout options => random layout batch
		done
		*/
		
		YV.cells = [];
		var gridSize = YV.dim.gridSize;
		var offset = 0;
		var smallSquarePopularChance = 1/Math.pow(2,gridSize);//chances that a small square is a popular question instead of a newer question
		var noMoreNewerQuestions = false;
		var noMorePopularQuestions = false;
		var newerQuestions = $('.q[data-q-sort=newer]');
		var popularQuestions = $('.q[data-q-sort=popular]');
		var newerQuestionsCount = newerQuestions.length;
		var popularQuestionsCount = popularQuestions.length;
		var newerQuestionsIndex = 0;
		var popularQuestionsIndex = 0;
		
		while(newerQuestionsIndex<newerQuestionsCount || popularQuestionsIndex<popularQuestionsCount) {
			var squares = randomSquares(gridSize,offset);
			var squaresCount = squares.length;
			var maxY = offset;
			for(var i=0;i<squaresCount;i++) {
				var square = squares[i];
				maxY = Math.max(maxY,square[1]+square[2]);
				var q = null;
				if (square[2]===1) {//small square
					if ( (!noMorePopularQuestions) && (Math.random()<smallSquarePopularChance) ) {//popular question
						q = popularQuestions[popularQuestionsIndex];
						popularQuestionsIndex++;
					} else {//newer question
						if (!noMoreNewerQuestions) {
							q = newerQuestions[newerQuestionsIndex];
							newerQuestionsIndex++; 
						}
					}
				} else {//popular question
					if (!noMorePopularQuestions) {
						q = popularQuestions[popularQuestionsIndex];
						popularQuestionsIndex++;
					}
				}
				if (q==null) {
					if (noMoreNewerQuestions && !noMorePopularQuestions) {
						q = popularQuestions[popularQuestionsIndex];
						popularQuestionsIndex++;
					}
					if (noMorePopularQuestions && !noMoreNewerQuestions) {
						q = newerQuestions[newerQuestionsIndex];
						newerQuestionsIndex++; 
					}
					if (q==null) break;
				}
				
				if (q) {//finally
					var qid = $(q).attr('data-q-id');
					var sel = '#'+$(q).attr('id');
					YV.cells.push([square[0],square[1],square[2],sel,qid]);
					//TODO: remove these...
					$(q).attr('data-q-x',square[0]);
					$(q).attr('data-q-y',square[1]);
					$(q).attr('data-q-sz',square[2]);
				}
			
				if (newerQuestionsIndex>=newerQuestionsCount) {
					noMoreNewerQuestions = true;
				}
				if (popularQuestionsIndex>=popularQuestionsCount) {
					noMorePopularQuestions = true;
				}
				if (noMoreNewerQuestions && noMorePopularQuestions) break;
			}
			offset = maxY;
		}
		
		YV.dim.maxY = offset;
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
			{ sq:[ [0,0,3], [3,1,2] ], h:4 }, { sq:[ [0,0,3], [3,2,2] ], h:4 },
			{ sq:[ [1,0,3] ], h:4 },
			{ sq:[ [2,0,3] ], h:4 }, { sq:[ [2,0,3], [0,1,2] ], h:4 }, { sq:[ [2,0,3], [0,2,2] ], h:4 }, 
			//...
			{ sq:[ [0,0,2], [0,1,2] ], h:4 }
		]
	};
	
	function randomSquares(gridSize, initialY) {
		var HOW_MANY = 25;
		var offset = initialY || 0;
		var templates = layoutTemplates['grid-'+gridSize];
		var maxTemplates = templates.length;
		
		var bitmap = [];
		for (var k=0;k<gridSize;k++) {
			bitmap[k] = [];
			for(var l=0;l<=HOW_MANY;l++) {
				bitmap[k][l] = 0;
			}
		}
		
		var ret = [];
		
		for(var i=0;i<HOW_MANY;i++) {
			var idx = randomInt(0,maxTemplates-1);
			var template = templates[idx];
			var sqaresCount = template.sq.length;
			for(var j=0;j<sqaresCount;j++) {
				var square = template.sq[j];
				var x = square[0];
				var y = square[1]+offset;
				var sz = square[2];
				ret.push([x,y,sz]);
				for (var k=x;k<x+sz;k++) {
					for(var l=y;l<y+sz;l++) {
						bitmap[k][l] = 1;
					}
				}
			}
			offset += template.h;
			if (offset>=HOW_MANY) break;
		}
		
		//add 1-size random squares for the newer questions
		//var txt = '';
		for(var l=0;l<=HOW_MANY;l++) {
			//txt +='\n';
			for (var k=0;k<gridSize;k++) {
				var bit = bitmap[k][l];
				if (bit===0) {
					if (Math.random()<1/gridSize) {//gridSize% chances
						ret.push([k,l,1]);
						//{txt+='.';}
					} //else {txt+=' ';}
				} //else {txt+='*';}
			}
		}
		//console.log(txt);
		
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
	
	function resizeCells() {
		/*
		for each cell
			compute position and dimensions
			set css of question div
		*/
		if (YV.dim.maxY) {
			YV.dim.qcontainerHeight = YV.dim.maxY*YV.dim.gridCellHeight;
			$('.q-cont').css('height',''+YV.dim.qcontainerHeight+'px');
		}
		
		var cellsCount = YV.cells.length;
		for(var i=0;i<cellsCount;i++) {
			var c = YV.cells[i];
			if (c[3]) {
				var x = c[0]*YV.dim.gridCellWidth;
				var y = c[1]*YV.dim.gridCellHeight;
				var w = c[2]*YV.dim.gridCellWidth;
				var h = c[2]*YV.dim.gridCellHeight;
				$(c[3]).css('top',''+y+'px').css('left',''+x+'px').css('height',''+h+'px').css('width',''+w+'px').show();
			}
		}
		
	};
	
	function onScroll() {
		
	};

	
	
}());