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
			// $('.q').bind('touchstart touchend', function(e) {
			// 	$('.q.tap-in').trigger('mouseleave');
			// 	$(this).trigger('mouseenter').addClass('tap-in');
			// });
			
			$('.carousel').bind('swipeleft',function () {
				$('.carousel').carousel('next');
			});
			$('.carousel').bind('swiperight',function () {
				$('.carousel').carousel('prev');
			});
			
			var carousel_lastX,carousel_lastY,carousel_timer;
			$('.carousel').bind('touchmove', function(e) {
				clearTimeout(carousel_timer);
				var currentX = e.originalEvent.touches ? e.originalEvent.touches[0].pageX : e.pageX;
				var currentY = e.originalEvent.touches ? e.originalEvent.touches[0].pageY : e.pageY;
				if (carousel_lastX>0) {
					var dx = Math.abs(currentX-carousel_lastX);
					var dy = Math.abs(currentY-carousel_lastY);
					if (dx < 42 || (dy>dx)) { return; }
					e.preventDefault();
					if (currentX > carousel_lastX) {
						$('.carousel').carousel('prev');
						carousel_lastX = 0;
						carousel_lastY = 0;
					} else {
						$('.carousel').carousel('next');
						carousel_lastX = 0;
						carousel_lastY = 0;
					}
				}
				carousel_lastX = currentX;
				carousel_lastY = currentY;
				carousel_timer = setTimeout(function(){
					carousel_lastX = 0;
					carousel_lastY = 0;
				}, 400);
		});
		} 
			
		$('body').popover({selector: '.vote-validation-pending span',trigger:'hover',container:'body', placement:'auto'});
		
		$('.q').on('mouseenter', function () {
			setupVotingBooth(this);
		});
		$('.q').on('mouseleave', function () {
			$(this).children('.q-voting-booth').remove();
			$(this).children('.trailing-space').show();
		});
		
		
		
	});
	
	
	function setupVotingBooth(me) {
		var qId = $(me).data('q-id');
		var isArch = $(me).data('q-archived');
		var canVote = $(me).data('q-can-vote');
		var voteTally = $(me).data('q-vote-tally');
		var voted = $(me).data('q-voted');
		var voteCount = $(me).data('q-votes');
		
		var votingBooth = $('.templates .q-voting-booth').clone(true,true);
		var trailingSp = $(me).find('.trailing-space').hide();
		$(votingBooth).css('left','10px').css('right','10px').insertBefore($(trailingSp));
		if (App.validatedCitizen) {
		} else {//not validated
			if (App.pendingValidation) {
				$(votingBooth).find('.vote-validation-pending').show();
				$(me).popover({selector: '.vote-validation-pending span',trigger:'hover',container:'body', placement:'auto'});
			} else {//before first vote
			}
		}
		if (canVote) {//do nothing... this is the initial state
		} else {//already voted on this question
			$(votingBooth).find('.voting-buttons').hide();
			if (isArch) {
				var voteInfo = 'Votig closed';
				$(votingBooth).find('.already-voted').html(voteInfo).show();
			} else {
				var yesVotes = new Number(voteTally * 100).toPrecision(3);
				var noVotes = new Number((1-voteTally) * 100).toPrecision(3);
				var myVote= voted==1?'YES':'NO';
				var voteInfo = 'You voted <strong>'+myVote+'</strong>';
				$(votingBooth).find('.already-voted').html(voteInfo).show();
				$(votingBooth).find('.aftervote-vote-tally').html('<div class="col-xs-12">Yes: '+yesVotes+'% -  No: '+noVotes+'%</div>').show();
			}
		}
		
		if (canVote) {
			$(votingBooth).find('.vote-yes').one( "click",function () {
				var voteValue='1';
				if (canVote&&(!isArch)) {
					var url = App.reqHostname+'/vote/'+qId+'/'+voteValue;
					$.ajax({
						url:url,
						type:'PUT',
						success: function (data,stat,xhr) {
							$('.q[data-q-id='+qId+']').data('q-can-vote',false);
							$('.q[data-q-id='+qId+']').data('q-voted',voteValue);
							var votesCountTxt = '<span>'+(voteCount+1)+'&nbsp;&nbsp;</span><br/>votes<br/>';
							$('.q[data-q-id='+qId+']').data('q-votes',voteCount+1);
							$('.q[data-q-id='+qId+']').find('.q-votes').html(votesCountTxt);
							var newVoteTally=voteTally+(1/voteCount);
							$('.q[data-q-id='+qId+']').data('q-vote-tally', newVoteTally);
							//
							var yesVotes = new Number(newVoteTally * 100).toPrecision(3);
							var noVotes = new Number((1-newVoteTally) * 100).toPrecision(3);
							var voteInfo = 'You voted <strong>YES</strong>';
							$(votingBooth).find('.voting-buttons').hide();
							$(votingBooth).find('.already-voted').html(voteInfo).show();
							$(votingBooth).find('.aftervote-vote-tally').html('<div class="col-xs-12">Yes: '+yesVotes+'% -  No: '+noVotes+'%</div>').show();
							
							if (!App.validatedCitizen) {
								$('#validate-citizen').modal();
								
							}
							if (App.pendingValidation) {
								$(votingBooth).find('.vote-validation-pending').show();
							}
						}
					});
				} 
			});
			$(votingBooth).find('.vote-no').one( "click",function () {
				var voteValue='0';
				if (canVote&&(!isArch)) {
					var url = App.reqHostname+'/vote/'+qId+'/'+voteValue;
					$.ajax({
						url:url,
						type:'PUT',
						success: function (data,stat,xhr) {
							$('.q[data-q-id='+qId+']').data('q-can-vote',false);
							$('.q[data-q-id='+qId+']').data('q-voted',voteValue);
							var votesCountTxt = '<span>'+(voteCount+1)+'&nbsp;&nbsp;</span><br/>votes<br/>';
							$('.q[data-q-id='+qId+']').data('q-votes',voteCount+1);
							$('.q[data-q-id='+qId+']').find('.q-votes').html(votesCountTxt);
							var newVoteTally=voteTally-(1/voteCount);
							$('.q[data-q-id='+qId+']').data('q-vote-tally', newVoteTally);
							//
							var yesVotes = new Number(newVoteTally * 100).toPrecision(3);
							var noVotes = new Number((1-newVoteTally) * 100).toPrecision(3);
							var voteInfo = 'You voted <strong>NO</strong>';
							$(votingBooth).find('.voting-buttons').hide();
							$(votingBooth).find('.already-voted').html(voteInfo).show();
							$(votingBooth).find('.aftervote-vote-tally').html('<div class="col-xs-12">Yes: '+yesVotes+'% -  No: '+noVotes+'%</div>').show();
							
							if (!App.validatedCitizen) {
								$('#validate-citizen').modal();
							}
							if (App.pendingValidation) {
								$(votingBooth).find('.vote-validation-pending').show();
							}
						}
					});
				} 
			});
		} else {
		
		}
		$(votingBooth).find('.vote-details').click(function () {
			if (isArch==='yes') {
				window.location=App.reqHostname+'/archived/'+qId;
			} else {
				window.location=App.reqHostname+'/question/'+qId;
			}
		});
	}

}());