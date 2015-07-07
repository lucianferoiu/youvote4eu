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
				var isArch = $(this).attr('data-q-archived');
				if (isArch==='yes') {
					window.location=App.contextPath+'/archived/'+qid;
				} else {
					window.location=App.contextPath+'/question/'+qid;
				}
			}
		});
		
		$('.q-details-btn').click(function () {
			var qid = $(this).attr('data-q-id');
			var isArch = $(this).attr('data-q-archived');
			if (isArch==='yes') {
				window.location=App.contextPath+'/archived/'+qid;
			} else {
				window.location=App.contextPath+'/question/'+qid;
			}
		});
		
		$('.hover').bind('touchstart touchend', function(e) {
			App.preventTouchClick = true;
			$(this).toggleClass('touch_as_hover');
		});
		
	});

}());