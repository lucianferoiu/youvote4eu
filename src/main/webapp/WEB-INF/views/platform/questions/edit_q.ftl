[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

[@content for="header_css"]<link href='${context_path}/css/font-awesome.min.css' rel="stylesheet">[/@content]
[@content for="header_css"]<link href='${context_path}/css/summernote.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/summernote.min.js'></script>[/@content]

[@content for="header_css"]<link href='${context_path}/css/bootstrap-datetimepicker.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/moment.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/js/bootstrap-datetimepicker.min.js'></script>[/@content]

[#-- [@content for="footer_script"]<script src='${context_path}/js/bootstrap-typeahead.min.js'></script>[/@content] --]
[@content for="header_css"]<link href='${context_path}/css/select2.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/select2.min.js'></script>[/@content]

[@content for="header_css"]<link href='${context_path}/css/bootstrap-slider.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/bootstrap-slider.min.js'></script>[/@content]


<div class="container">
	<div id="editPartnerModel" class="modal editing-dialog" tabindex="-1" role="dialog" aria-hidden="true" ng-class="{'show': vm.crtQActivePanel}">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="vm.cancelEdit()"><span aria-hidden="true">&times;</span></button>
					<h3>Question [{{vm.crtQuestion.id|pad:3:'0'}}]: <em>{{vm.crtQuestion.title}}</em></h3>
				</div>
				<div class="modal-body">
					<!-- question detail sections -->
					<ul class="nav nav-pills nav-justified">
						<li role="presentation" ng-class="{'active': vm.crtQActivePanel==='main'}" style="padding-left:10px;">
							<a href="#" class="q-pill" ng-click="vm.crtQActivePanel='main'">Main Details</a>
						</li>
						<li role="presentation" ng-class="{'active': vm.crtQActivePanel==='meta'}" style="padding-left:10px;">
							<a href="#" class="q-pill" ng-click="vm.crtQActivePanel='meta'">Metadata</a>
						</li>
						<li role="presentation" ng-class="{'active': vm.crtQActivePanel==='stats'}" style="padding-left:10px;">
							<a href="#" class="q-pill" ng-click="vm.crtQActivePanel='stats'">Voting & Statistics</a>
						</li>
					</ul>

					<hr/>
					<!-- main question panel -->
					<div class="q-panel" ng-show="vm.crtQActivePanel=='main'">
						<!-- question body translation tabs -->
						<ul class="nav nav-tabs">
							<li role="presentation" 
								ng-class="{'active':vm.crtTranslation.lang==='en'}"
								ng-click="vm.switchTranslation('en')" 
									><a href="#">English</a></li>
							<li role="presentation" 
								ng-repeat="t in vm.translationsTab" 
								ng-click="vm.switchTranslation(t.lang)" 
								ng-class="{'active':vm.crtTranslation.lang===t.lang}">
									<a href="#">{{t.label}}</a>
							</li>
							<li role="presentation" class="dropdown">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-expanded="false">Translations <span class="caret"></span></a>
								<ul class="dropdown-menu" role="menu">
										<li role="presentation"><a role="menuitem" tabindex="-1" href="#" 
											ng-repeat="l in vm.translationsDropdown"
											ng-click="vm.switchTranslation(l.code)"
											>{{l.label}} <span class="glyphicon glyphicon-pencil" ng-show="l.completed"></span></a></li>
								</ul>
							</li>
						</ul>
					
						<!-- form -->
						<div class="">
							<br/>
							<form name="qTrans" class="">
								<div class="form-group">
									<label for="questionTitle" class="control-label">Title</label>
									<input type="text" class="form-control" id="questionTitle" 
										ng-model="vm.crtTranslation.title" 
										ng-required="vm.crtTranslation.lang==='en'" 
										placeholder="Short title (mandatory)" >
								</div>
								<div class="form-group">
									<label for="questionDescription" class="control-label">Short Description</label>
									<textarea class="form-control" rows="2" id="questionDescription" 
										ng-model="vm.crtTranslation.description" 
										ng-required="vm.crtTranslation.lang==='en'"
										placeholder="Concise formulation of the question (mandatory: at least a few words)" maxlength="280"></textarea>
								</div>
								<div class="form-group">
									<label for="questionContent" class="control-label">Question Content</label>
									<div class="form-control" id="questionContent" placeholder="Elaboration of the question arguments, supporting references and links"></div>
								</div>
							</form>
						</div>
					</div>
					
					<!-- metadata question panel -->
					<div class="q-panel" ng-show="vm.crtQActivePanel=='meta'">
						<div class="panel">
							<h4><span class="glyphicon glyphicon-cog"></span>&nbsp;&nbsp; Status</h4>
							<div class="form-inline">
								<div class="form-group disabled">
									<label class="control-label" for="questionIsPublished">Published &nbsp;&nbsp;&nbsp;</label>
									<input type="checkbox" class="form-control disabled" id="questionIsPublished" ng-model="vm.crtQuestion.is_published" disabled>
								</div>
								<div class="form-control-static">
									&nbsp;&nbsp;&nbsp;
								</div>
								<div class="form-group" ng-class="{'disabled':vm.crtQuestion.is_archived}">
									<label for="openAtDP" class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Open for Voting On&nbsp;</label>
									<div class="input-group date" id="openAtDP">
										<input type='text' class="form-control" ng-disabled="vm.crtQuestion.is_archived"/>
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
								<div class="form-control-static">
									&nbsp;
								</div>
								<div class="form-group" ng-class="{'disabled':vm.crtQuestion.is_archived}">
									<label for="closedAtDP" class="control-label">&nbsp;&nbsp;&nbsp;Voting Closed On&nbsp;</label>
									<div class="input-group date" id="closedAtDP">
										<input type='text' class="form-control" ng-disabled="vm.crtQuestion.is_archived"/>
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
							<br/>
							<div class="form-inline">
								<div class="form-group">
									<label class="control-label" for="questionIsPublicAgenda">Public Agenda&nbsp;</label>
									<input type="checkbox" class="form-control" id="questionIsPublicAgenda" 
										ng-model="vm.crtQuestion.is_public_agenda" ng-change="vm.togglePubAgenda()">
								</div>
								<div class="form-control-static">
									&nbsp;&nbsp;&nbsp;
								</div>
								<div class="form-group" ng-class="{'disabled':!vm.crtQuestion.is_public_agenda}">
									<label for="officialVoteResults" class="control-label">Official Vote Result&nbsp;&nbsp;&nbsp;&nbsp;</label>
									<input id="officialVoteResults" type="text" class="" 
										data-slider-min="0" data-slider-id='officialVoteSlider'
										data-slider-max="100" data-slider-step="1" data-slider-value="0"/>
									&nbsp;&nbsp;&nbsp;
									<p id="officialVoteLabel" class="form-control-static" ng-class="{'disabled':!vm.crtQuestion.is_public_agenda}">No voting registered</p>
								</div>
							</div>
							<br/>
							<div class="form-inline">
								<div class="form-group disabled">
									<label class="control-label" for="questionIsArchived">Archived &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
									<input type="checkbox" class="form-control disabled" id="questionIsArchived" ng-model="vm.crtQuestion.is_archived" disabled>
								</div>
								<div class="form-control-static">
									&nbsp;&nbsp;&nbsp;
								</div>
								<div class="form-group" ng-class="{'disabled':true}">
									<label for="archivedAtDP" class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Archived On&nbsp;</label>
									<div class="input-group date" id="archivedAtDP">
										<input type='text' class="form-control" ng-disabled="true"/>
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
							<br/>
						</div>	
							
						<!-- tags cloud -->
						<div class="panel">
							<h4><span class="glyphicon glyphicon-tags"></span>&nbsp;&nbsp; Tags</h4>
							<select id="tagsSelector" class="form-control input-lg" multiple="multiple" style="width:100%;"></select>
							<br/>
						</div>
						
						<!-- comments -->
						<div class="panel">
							<h4><span class="glyphicon glyphicon-comment"></span>&nbsp;&nbsp; Flags and Comments</h4>
							<div class="scroll-block">
								<ul class="list-group">
									<li class="list-group-item" style="min-height:22px;"
										ng-repeat="c in vm.crtQuestion.children.comments "
										ng-class="{'text-primary':c.id==null}">
										{{c.text}}
										<button type="button" class="close pull-right" 
											ng-click="vm.deleteComment($index)">&nbsp;&nbsp;<span aria-hidden="true" class="text-danger">&times;</span></button>
										<span class="label pull-right" style="padding: 6px;"
											ng-class="{'label-warning':c.comment_type==='duplicate'||c.comment_type==='mistranslated'||c.comment_type==='misleading',
												 'label-danger':c.comment_type==='offensive','label-success':c.comment_type==='promoted'}">
												{{c.comment_type}}</span>
									</li>
								</ul>
								<div class="bottom-comments">
									<div class="muted" ng-show="!(vm.crtQuestion.children.comments.length>0)">
										<em>Comments from other Partners ... </em>
									</div>
								</div>
							</div>
							<br/>
							<div class="dropup">
								<button type="button" class="btn dropdown-toggle" data-toggle="dropdown"> Add a new Comment or Flag &nbsp;&nbsp;<span class="caret"></span></button>
								<ul class="dropdown-menu" role="menu">
									<li><a href="#bottom-comments" ng-click="vm.addComment('promoted')">Flag the question as <strong>promoted</strong></a></li>
									<li class="divider"></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('duplicate')">Flag <strong>duplicate</strong> question <em>(indicate which question)</em></a></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('offensive')">Flag <strong>offensive</strong> language</a></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('mistranslated')">Flag <strong>mistranslation</strong> <em>(indicate the language)</em></a></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('misleading')">Flag a <strong>misleading reference</strong></a></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('misleading')">Flag <strong>misleading content</strong></a></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('broken link')">Flag <strong>broken links</strong> inside the question</a></li>
									<li class="divider"></li>
									<li><a href="#bottom-comments" ng-click="vm.addComment('comment')">Add a short <strong>comment</strong></a></li>
								</ul>
								
								<input type="text" class="form-control" id="commentText" maxlength="100"
									ng-model="vm.crtComment.text"
									ng-show="vm.crtComment && vm.crtComment.id==null" 
									placeholder="Write a short comment or provide details for the flag" >
								
							</div>
							
						</div>

					</div>
					
					
					<!-- statistics question panel -->
					<div class="q-panel" ng-show="vm.crtQActivePanel=='stats'">
						<h4><span class="glyphicon glyphicon-thumbs-up"></span>&nbsp;&nbsp; Votes</h4>
						
						<div class="form-horizontal">
							<div class="form-group">
								<label class="control-label col-sm-3" for="questionSupport">Support from the Partners</label>
								<p id="questionSupport" class="form-control-static col-sm-8">{{vm.crtQuestion.support}} upvotes</p>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="questionPopVote">Popular votes</label>
								<p id="questionPopVote" class="form-control-static col-sm-8">{{vm.crtQuestion.popular_votes}}</p>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="questionPopTally">Popular vote tally</label>
								[#-- <div class="progress col-sm-8" id="questionPopTally" ng-show="vm.crtQuestion.popular_vote_tally>0">
									<div class="progress-bar progress-bar-success" role="progressbar" aria-valuemin="0" aria-valuemax="100"
										ng-style="{'width': vm.crtQuestion.popular_vote_tally*100 + '%;'} "
										aria-valuenow="{{Math.ceil(vm.crtQuestion.popular_vote_tally*100)}}"
										>Yeas: {{vm.crtQuestion.popular_vote_tally.toFixed(2)*100}}% / Nays: {{(1-vm.crtQuestion.popular_vote_tally.toFixed(2))*100}}%</div>
								</div>  --]
								<div class="form-control-static col-sm-8" ng-show="vm.crtQuestion.popular_vote_tally>0">
									Yeas: {{vm.crtQuestion.popular_vote_tally.toFixed(2)*100}}% / Nays: {{ (1-vm.crtQuestion.popular_vote_tally.toFixed(2))*100}}%
								</div>
								<span id="questionPopTally2" class="form-control-static col-sm-8" ng-show="!(vm.crtQuestion.popular_vote_tally>0)">No votes yet..</span>
							</div>
						</div>
						
						<br/>
						
						<h4><span class="glyphicon glyphicon-stats"></span>&nbsp;&nbsp; Statistics</h4>
						
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" ng-click="vm.cancelEdit()">Close</button>
					<button type="button" class="btn btn-default" ng-click="vm.saveQuestion()" ng-disabled="vm.crtQuestionInalid()">Save Question</button>
					<button type="button" class="btn btn-default" ng-click="vm.publishQuestion()" ng-show="vm.crtQuestion.id>0 && !(vm.crtQuestion.is_published)" ng-disabled="vm.crtQuestionInalid()">Publish</button>
					<button type="button" class="btn btn-default" ng-click="vm.archiveQuestion()" ng-show="vm.crtQuestion.is_published">Archive</button>
					<button type="button" class="btn btn-default" ng-click="vm.deleteQuestion()" ng-disabled="!(vm.crtQuestion.is_published)">Delete</button>
				</div>
			</div>
		</div>
	</div>
</div>


[@content for="footer_script"]<script  type="text/javascript">
	$(function () {
		//HTML editor
		$('#questionContent').summernote({
			height: 280,
			maxHeight: 800,
			keyMap: {pc:{},mac:{}},//the only way to turn off both shortcuts and squigly chars in tooltips
			toolbar: [
				['style', ['bold', 'italic', 'underline', 'fontsize','strikethrough', 'superscript', 'subscript','clear']],
				['para', ['ul', 'ol', 'paragraph']],
				['insert', ['hr','link','table','video']],
				['misc', ['codeview','undo']]
			]
		});
	
		//calendar widgets
		$('#openAtDP').datetimepicker({
			format: 'DD/MM/YYYY HH:mm'
		});
		$('#closedAtDP').datetimepicker({
			format: 'DD/MM/YYYY HH:mm'
		});
		$("#openAtDP").on("dp.change", function (e) {
			$('#closedAtDP').data("DateTimePicker").minDate(e.date);
		});
		$('#archivedAtDP').datetimepicker({
			format: 'DD/MM/YYYY HH:mm'
		});
	
		//tags typeahead -> see questionsCtrl::initTags
		
		//vote results slider
		$('#officialVoteResults').slider({
			formatter: function(value) {
				return 'Yes: ' + value+'% - No: '+ (100-value)+'%';
			}
			// ,tooltip: 'always'
		});
		$('#officialVoteResults').on("slide", function(slideEvt) {
			var txt = 'No voting registered';
			if (slideEvt.value>0) {
				txt = ' Yes/No: ' + slideEvt.value+'/'+ (100-slideEvt.value)+' %';
			} else {
			}
			$("#officialVoteLabel").text(txt);
		});
		
	});
</script>[/@content]