(function() {
	var root = this;

	$(document).ready(function(){
		
		$('.logo').click(function () {
			window.location='/';
		});
		
		onResize();
		$(window).resize(function () { onResize(); });
		
		
	});
	
	function onResize() {
		var navBarH = $('.navbar').height();
		$('.nav-buffer').css('height',''+(navBarH+20)+'px');
	}

}());