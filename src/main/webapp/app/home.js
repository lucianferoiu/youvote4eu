(function() {
	var root = this;
	var YV = function(obj) {
		if (obj instanceof YV) return obj;
		if (!(this instanceof YV)) return new YV(obj);
		this._wrapped = obj;
	};
	
	YV.init = function (cells, opts) {
		YV.ds = {
			cells:cells,
			revealed: 0,
			palette: ['#AA4639','#C57267','#B85B4E','#9C3325','#BA2B18','#AA7139','#C59667','#B8834E','#9C6025','#BA6818','#255C69','#416F7A','#326672','#195361','#126173','#2A7F41','#4D945F','#3B894F','#1C7533','#128B32']
		};
		$(document).ready(function(){
			YV.resize(opts);
			YV.revealCells(20);
			$(window).resize(function() {
				YV.resize();
			});
			$(window).scroll(function() {
				YV.scroll();
			});
		});
	};
	
	YV.revealCells = function (howMany) {
		if (YV.ds) {
			var len = YV.ds.cells.length;
			var from = YV.ds.revealed;
			var to = Math.min(YV.ds.revealed+howMany,len);
			var maxY = Math.max(0,YV.dim.maxY);
			for (var i=from;i<to;i++) {
				var c = YV.ds.cells[i];
				var qid = '#q'+c.q;
				var h = YV.dim.gh*c.sz;
				var w = YV.dim.gw*c.sz;
				var y = YV.dim.gh*c.y;
				var x = YV.dim.gw*c.x;
				maxY = Math.max(maxY,(c.y+c.sz));
				$(qid).css('top',''+y+'px').css('left',''+x+'px').css('height',''+h+'px').css('width',''+w+'px').show();//'scale',{ percent: 80 },500+(i*25));
				
				var qqid = '#qq'+c.q;
				var randomColor = YV.ds.palette[Math.floor(Math.random() * YV.ds.palette.length)];
				$(qqid).css('background',randomColor).css('z-index',10);	
				$(qqid).addClass('q-sz-'+c.sz);
			}
			YV.ds.revealed = to;
			YV.dim.maxY = maxY;
			YV.dim.h = (YV.dim.gh*maxY)+100;
			$('.q-cont').css('height',''+YV.dim.h+'px');
		}
	};
	
	YV.resize = function (opts) {
		var ww = $(window).width();
		var wh = $(window).height();
		YV.dim = {
			W: ww,
			H: wh,
			grid: ((ww>992)?5:(ww>768?3:1)),
			aspect: 1.5,
			w: Math.floor(ww*.76),
			h: Math.floor(1.5*wh),
			mh: (opts?opts.maxOffset:10)*wh
			
		};
		YV.dim.gw = Math.floor(YV.dim.w/YV.dim.grid);
		YV.dim.gh = Math.floor((YV.dim.w/YV.dim.grid)/YV.dim.aspect);
		$('.q-cont').css('width',''+YV.dim.w+'px').css('height',''+YV.dim.h+'px').show();

		if (YV.ds) {
			var maxY = 0;
			for (var i=0;i<YV.ds.revealed;i++) {
				var c = YV.ds.cells[i];
				var qid = '#q'+c.q;
				var h = YV.dim.gh*c.sz;
				var w = YV.dim.gw*c.sz;
				var y = YV.dim.gh*c.y;
				var x = YV.dim.gw*c.x;
				$(qid).css('top',''+y+'px').css('left',''+x+'px').css('height',''+h+'px').css('width',''+w+'px');
				maxY = Math.max(maxY,(c.y+c.sz));
			}
			YV.dim.maxY = maxY;
			YV.dim.h = (YV.dim.gh*maxY)+100;
			$('.q-cont').css('height',''+YV.dim.h+'px');
		}
	};
	
	YV.scroll = function () {
		var pos = window.scrollY;
		if (pos>YV.dim.h-(YV.dim.H*1.2)) {
			$('#q-loading').show();
			YV.revealCells(15);
			$('#q-loading').hide();
		}
	};
	
	
	root.YV = YV;
}());