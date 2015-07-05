(function() {
	var root = this;

	$(document).ready(function(){
		
		$(document).ready(function(){
			$('.logo').click(function () {
				window.location='/';
			});
		});
		
		$('.q').click(function () {
			if (!App.preventTouchClick) {
				var qid = $(this).attr('data-q-id');
				window.location=App.contextPath+'/question/'+qid;
			}
		});
		
		$('.hover').bind('touchstart touchend', function(e) {
			App.preventTouchClick = true;
			$(this).toggleClass('touch_as_hover');
		});
		
	});

}());