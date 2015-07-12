(function() {
	var root = this;

	$(document).ready(function(){
		
		$('.logo').click(function () {
			window.location='/';
		});
		
		$('.vote-validation-pending span').click(function () {
			// TODO: show validation by email info
		});
		
		if (App.isMobileAgent) {
			$('.hover').bind('tap', function(e) {
				$('.hover').removeClass('touch');
				if (App.tappedQuestion) {
					$(App.tappedQuestion).children('.q-voting-booth').remove();
				}
				$(this).addClass('touch');
				var votingBooth = $('.templates .q-voting-booth').clone(true,true);
				$(votingBooth).css('left','10px').css('right','10px').appendTo(this);
				App.tappedQuestion = this;
			});
			
			$('.carousel').bind('swipeleft',function () {
				$('.carousel').carousel('next');
			});
			$('.carousel').bind('swiperight',function () {
				$('.carousel').carousel('prev');
			});
		} else {
			$('.q').on('mouseenter', function () {
				var votingBooth = $('.templates .q-voting-booth').clone(true,true);
				$(votingBooth).css('left','2px').css('right','2px').appendTo(this);
			});
			$('.q').on('mouseleave', function () {
				$(this).children('.q-voting-booth').remove();
			});
		}
		
		
	});

}());