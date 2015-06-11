[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

[@content for="header_css"]<link href='${context_path}/css/font-awesome.min.css' rel="stylesheet">[/@content]
[@content for="header_css"]<link href='${context_path}/css/summernote.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/summernote.min.js'></script>[/@content]

<div class="container">
	<div id="editPartnerModel" class="modal editing-dialog" tabindex="-1" role="dialog" aria-hidden="true" ng-class="{'show': vm.editingQuestion}">
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
							<a href="#" class="q-pill" ng-click="vm.switCrtQPanel('main')">Main Details</a>
						</li>
						<li role="presentation" ng-class="{'active': vm.crtQActivePanel==='meta'}" style="padding-left:10px;">
							<a href="#" class="q-pill" ng-click="vm.switCrtQPanel('meta')">Metadata</a>
						</li>
						<li role="presentation" ng-class="{'active': vm.crtQActivePanel==='stats'}" style="padding-left:10px;">
							<a href="#" class="q-pill" ng-click="vm.switCrtQPanel('stats')">Statistics</a>
						</li>
					</ul>

					<hr/>
					<!-- main question panel -->
					<div class="q-panel">
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
					<div class="q-panel">
						<!-- tags cloud -->
						<div class="col-xs-9">
							<div class="tags-cloud" style="padding:6px;">
								<span class="label label-info" ng-repeat="t in vm.crtQuestion.tags">{{t.label}} <span ng-click="removeTag(t.id)">&times;</span></span>
							</div>
							<br/>
							<ul class="list-group">
								<li class="list-group-item" ng-repeat="c in vm.crtQuestion.comments">{{c.text}}</li>
							</ul>
						</div>
						<div class="col-xs-3">
							<div class="dropup">
								<button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" style="display: block; width: 100%;"
									aria-expanded="false"> Tags &nbsp;<span class="caret"></span></button>
								<ul class="dropdown-menu" role="menu">
									<li><a href="#">environment</a></li>
									<li><a href="#">trade</a></li>
									<li><a href="#">social</a></li>
									<li><a href="#">privacy</a></li>
									<li class="divider"></li>
									<li><a href="#">2015 EP Session</a></li>
									<li><a href="#">TTIP-Awareness Campain</a></li>
								</ul>
							</div>
							<br/>
							<div class="dropup">
								<button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" style="display: block; width: 100%;"
									aria-expanded="false"> Flags &nbsp;<span class="caret"></span></button>
								<ul class="dropdown-menu" role="menu">
									<li><a href="#">promoted</a></li>
									<li class="divider"></li>
									<li><a href="#">duplicate question</a></li>
									<li><a href="#">offensive language</a></li>
									<li><a href="#">mistranslated</a></li>
									<li><a href="#">misleading references</a></li>
									<li><a href="#">misleading content</a></li>
									<li><a href="#">broken links</a></li>
								</ul>
							</div>
						</div>
					</div>
					
					
					<!-- statistics question panel -->
					<div class="q-panel">
						
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" ng-click="vm.cancelEdit()">Close</button>
					<button type="button" class="btn btn-default" ng-click="vm.saveQuestion()" ng-disabled="vm.crtQuestionInalid()">Save Question</button>
					<button type="button" class="btn btn-default" ng-click="vm.publishQuestion()" ng-show="!(vm.crtQuestion.is_published)" ng-disabled="vm.crtQuestionInalid()">Publish</button>
					<button type="button" class="btn btn-default" ng-click="vm.archiveQuestion()" ng-show="vm.crtQuestion.is_published">Archive</button>
					<button type="button" class="btn btn-default" ng-click="vm.deleteQuestion()" ng-disabled="!(vm.crtQuestion.is_published)">Delete</button>
				</div>
			</div>
		</div>
	</div>
</div>


[@content for="footer_script"]<script  type="text/javascript">
	$('#questionContent').summernote({
		height: 200,
		maxHeight: 800,
		keyMap: {pc:{},mac:{}},//the only way to turn off both shortcuts and squigly chars in tooltips
		toolbar: [
			['style', ['bold', 'italic', 'underline', 'fontsize','strikethrough', 'superscript', 'subscript','clear']],
			['para', ['ul', 'ol', 'paragraph']],
			['insert', ['hr','link','table','video']],
			['misc', ['codeview','undo']]
		]
	});
</script>[/@content]