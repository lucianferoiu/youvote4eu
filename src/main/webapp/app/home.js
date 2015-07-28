(function() {
	var root = this;

	$(document).ready(function(){
		
		$('.logo').click(function () {
			window.location='/';
		});
		
		$('.q').on('mouseenter', function (e) {
			// console.log('entering question ',$(this).data('q-id'));
			showVotingBooth(this,e);
		});
		$('.q').on('mouseleave', function (e) {
			// console.log('leaving question ',$(this).data('q-id'));
			// hideVotingBooth(this,e);
			var qId = $(this).data('q-id');
			if (App.activeQuestion==null||App.activeQuestion!=qId) {
				$('#votingBoothFlyweight').hide();
				$(this).removeClass('q-hover');
			}
		});

		$('#votingBoothFlyweight').on('mouseenter', function (e) {
			// console.log('entering voting booth');
		});
		$('#votingBoothFlyweight').on('mouseleave', function (e) {
			// console.log('leaving voting booth');
			$('#votingBoothFlyweight').hide();
			$(('.q[data-q-id="'+App.activeQuestion+'"]')).removeClass('q-hover');
			App.activeQuestion=null;
		});
		
		$('#votingBoothFlyweight .question-details button').click(function () {
			var qId = $(this).data('q-id');
			var isArchived = $(this).data('q-archived')||false;
			questionDetails(qId,isArchived);
		});
		
		$('#votingBoothFlyweight .can-vote .vote-yes').click(function () {
			var qId = $(this).data('q-id');
			voteQuestion(qId,1);
		});
		
		$('#votingBoothFlyweight .can-vote .vote-no').click(function () {
			var qId = $(this).data('q-id');
			voteQuestion(qId,0);
		});
		
		
		if (App.isMobileAgent) {//no mouse, fingers only...
			$('.carousel').bind('swipeleft',function () {
				$('.carousel').carousel('next');
			});
			$('.carousel').bind('swiperight',function () {
				$('.carousel').carousel('prev');
			});
			
			var carousel_lastX,carousel_lastY,carousel_timer;
			$('.carousel').bind('touchmove', function(e) {//swipe left/right on a carousel
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
		} else {//we have a mouse...
			$('.q').click(function () {
				var qId = $(this).data('q-id');
				if (qId) {
					var qInfo = App.questions[qId];
					if (qInfo) {
						questionDetails(qId,qInfo.archived);
					}
				}
			});
		
			$('.aq').click(function () {
				var qId = $(this).data('q-id');
				if (qId) {
					questionDetails(qId,true);
				}
			});
		}
			
		$('body').popover({selector: '.vote-validation-pending span',trigger:'hover',container:'body', placement:'auto'});
		
		init();
		
	});
	
	function showVotingBooth(me,e) {
		var qId = $(me).addClass('q-hover').data('q-id');
		
		if (App.activeQuestion!=qId) {
			App.activeQuestion=qId;
			var square = $(me).data('q-square');
			var qInfo = App.questions[qId];
			var qpos = $(me).offset();
			var qw = $(me).outerWidth();
			var qh = $(me).outerHeight();
			var isArchivedQuestion = (qInfo==undefined)||(qInfo.archived==true)||($(me).hasClass('aq'));
			$('#votingBoothFlyweight .question-details button').data('q-id',qId).data('q-archived',isArchivedQuestion);
			if (qInfo) {
				$('#votingClosed').hide();
				if (qInfo.canVote) {//voting enabled
					$('#votingBoothFlyweight .vote-yes').data('q-id',qId);
					$('#votingBoothFlyweight .vote-no').data('q-id',qId);
					$('#votingBoothFlyweight .already-voted').hide();
					$('#votingBoothFlyweight .can-vote').show();
				} else {//already voted
					var voteTally = qInfo.voteTally?qInfo.voteTally:0;
					var yesVotes = voteTally>=0? (new Number(voteTally * 100).toPrecision(3))+'%' : '-';
					var noVotes = voteTally>=0? (new Number((1-voteTally) * 100).toPrecision(3))+'%' : '-';
					$('#votingBoothFlyweight .yes-tally').text(yesVotes);
					$('#votingBoothFlyweight .no-tally').text(noVotes);
					if (qInfo.voted!=undefined) {
						$('#citizen-vote-pending').hide();
						if (qInfo.voted==1) {
							$('#citizen-voted-yes').show();
							$('#citizen-voted-no').hide();
						} else if (qInfo.voted==0) {
							$('#citizen-voted-yes').hide();
							$('#citizen-voted-no').show();
						}
					} else {
						$('#citizen-voted-yes').hide();
						$('#citizen-voted-no').hide();
						$('#citizen-vote-pending').show();
					}
					$('#votingBoothFlyweight .can-vote').hide();
					$('#votingBoothFlyweight .already-voted').show();
				}
			}
			$('#votingBoothFlyweight').removeClass('q-sz-1 q-sz-2 q-sz-3');
			if (square) {
				$('#votingBoothFlyweight').addClass('q-sz-'+square[2]);
			}
			$('#votingBoothFlyweight').css(qpos).css('width',qw).css('height',qh).show();
			if (isArchivedQuestion) {
				$('#votingClosed').show();
				$('#citizen-vote-pending').hide();
				$('#votingBoothFlyweight .already-voted').hide();
				$('#votingBoothFlyweight .can-vote').hide();
			}
		}
	}

	function hideVotingBooth(me,e) {
		var qId = $(me).data('q-id');
		if (App.activeQuestion!=qId) {
			$('#votingBoothFlyweight').hide();
			$(me).removeClass('q-hover');
		}
	}
	
	function questionDetails(qId,archived) {
		if (archived) {
			window.location=App.reqHostname+'/archived/'+qId;
		} else {
			window.location=App.reqHostname+'/question/'+qId;
		}
	}
	
	function voteQuestion(qId,voteValue) {
		console.log('Voting '+voteValue+' on question '+qId);
		$('#votingSpinner').show();
		$('#votingBoothFlyweight .can-vote').hide();
		$('#votingBoothFlyweight .can-vote button').attr('disabled','disabled');
		var url = App.reqHostname+'/vote/'+qId+'/'+voteValue;
		$.ajax({
			url:url,
			type:'PUT',
			success: function (data,stat,xhr) {
				if (!App.validatedCitizen) {
					$('#validate-citizen').modal();
				}
				App.questions[qId].canVote=false;
				App.questions[qId].voted=voteValue;
				App.questions[qId].votes = data.votes;
				App.questions[qId].voteTally = data.voteTally;
				$(('.q[data-q-id="'+qId+'"] .q-popular-votes')).text(''+data.votes+' ');
				if (App.activeQuestion==qId) {//mouse still hovering the same question
					var yesVotes = data.voteTally>=0? (new Number(data.voteTally * 100).toPrecision(3))+'%' : '-';
					var noVotes = data.voteTally>=0? (new Number((1-data.voteTally) * 100).toPrecision(3))+'%' : '-';
					$('#votingBoothFlyweight .yes-tally').text(yesVotes);
					$('#votingBoothFlyweight .no-tally').text(noVotes);
					$('#citizen-vote-pending').hide();
					if (voteValue==1) {
						$('#citizen-voted-yes').show();
						$('#citizen-voted-no').hide();
					} else if (voteValue==0) {
						$('#citizen-voted-yes').hide();
						$('#citizen-voted-no').show();
					}
				}
				
				$('#votingBoothFlyweight .can-vote button').removeAttr('disabled');
				$('#votingSpinner').hide();
				
			},
			error: function (xhr,stat,err) {
				$('#votingBoothFlyweight .can-vote button').removeAttr('disabled');
				$('#votingBoothFlyweight .can-vote').show();
				$('#votingSpinner').hide();
			}
		});
		if (App.pendingValidation==true) {
			$('#votingBoothFlyweight .voting-email-pending').show();
		} else {
			$('#votingBoothFlyweight .voting-email-pending').hide();
		}
	}
	
	function init() {
		if (App.validatedCitizen==true) {
			if (App.pendingValidation==true) {
				$('#votingBoothFlyweight .voting-email-pending').show();
			} else {
				$('#votingBoothFlyweight .voting-email-pending').hide();
			}
		} else {
			$('#votingBoothFlyweight .voting-email-pending').hide();
		}
		$('#votingSpinner').hide();
		$('#votingClosed').hide();
		
	}


}());