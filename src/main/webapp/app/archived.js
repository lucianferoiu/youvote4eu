(function() {
	var root = this;

	$(document).ready(function(){
		
		$('.logo').click(function () {
			window.location='/';
		});
		
		$('.arch-q-summary').click(function () {
			var qid = $(this).attr('data-q-id');
			window.location=App.contextPath+'/archived/'+qid;
		});
		
	});

}());