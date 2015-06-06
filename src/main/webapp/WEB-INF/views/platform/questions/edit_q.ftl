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
					<h3>Question: <em>{{vm.crtQuestion.title}}</em></h3>
				</div>
				<div class="modal-body">
					<!-- tabs -->
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
								[#list languages as lang]
								[#if (lang.code!='en')]
									<li role="presentation"><a role="menuitem" tabindex="-1" href="#" 
										ng-click="vm.switchTranslation('${lang.code}')"
										
										>${lang.label_en}</a></li>
								[/#if]
								[/#list]
							</ul>
						</li>
					</ul>
					
					<!-- form -->
					<div class="">
						<br/>
						<form class="">
							<div class="form-group">
								<label for="questionTitle" class="control-label">Title</label>
								<input type="text" class="form-control" id="questionTitle" ng-model="vm.crtTranslation.title" placeholder="Short title" required>
							</div>
							<div class="form-group">
								<label for="questionDescription" class="control-label">Short Description</label>
								<textarea class="form-control" rows="2" id="questionDescription" ng-model="vm.crtTranslation.description" placeholder="Concise formulation of the question" maxlength="280"></textarea>
							</div>
							<div class="form-group">
								<label for="questionContent" class="control-label">Question Content</label>
								<div class="form-control" id="questionContent" placeholder="Elaboration of the question arguments, supporting references and links"></div>
							</div>
							<div class="">
								{{vm.crtTranslation.html_content}}
							</div>
						</form>
					</div>
					<!-- tags panel -->
					<hr/>
					<div class="row">
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
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" ng-click="vm.cancelEdit()">Close</button>
					<button type="button" class="btn btn-primary" ng-click="vm.saveQuestion()">Save Question</button>
					<button type="button" class="btn btn-default" ng-click="vm.publishQuestion()">Publish</button>
					<button type="button" class="btn btn-default" ng-click="vm.archiveQuestion()">Archive</button>
					<button type="button" class="btn btn-default" ng-click="vm.deleteQuestion()">Delete</button>
				</div>
			</div>
		</div>
	</div>
</div>


[@content for="footer_script"]<script  type="text/javascript">
	$('#questionContent').summernote({
		height: 100,//TODO: make it 360
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