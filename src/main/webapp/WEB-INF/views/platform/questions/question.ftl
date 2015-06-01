[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

[@content for="header_css"]<link href='${context_path}/css/font-awesome.min.css' rel="stylesheet">[/@content]
[@content for="header_css"]<link href='${context_path}/css/summernote.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/summernote.min.js'></script>[/@content]

<div class="container">
	<div id="editPartnerModel" class="modal editing-dialog" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="vm.cancelEdit()"><span aria-hidden="true">&times;</span></button>
					<h3>Question: <em>{questionTitle}</em></h3>
				</div>
				<div class="modal-body">
					<!-- tabs -->
					<ul class="nav nav-tabs">
						<li role="presentation" class="active"><a href="#">English</a></li>
						<li role="presentation"><a href="#">French</a></li>
						<li role="presentation" class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-expanded="false">Translations <span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								[#list languages as lang]
									<li role="presentation"><a role="menuitem" tabindex="-1" href="#">${lang.label_en}</a></li>
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
								<input type="text" class="form-control" id="questionTitle" placeholder="Short title" required>
							</div>
							<div class="form-group">
								<label for="questionDescription" class="control-label">Short Description</label>
								<textarea class="form-control" rows="2" id="questionDescription" placeholder="Concise formulation of the question" maxlength="280"></textarea>
							</div>
							<div class="form-group">
								<label for="questionContent" class="control-label">Question Content</label>
								<div class="form-control" id="questionContent">Elaboration of the question arguments, supporting references and links.</div>
							</div>
						</form>
					</div>
					<!-- tags panel -->
					<hr/>
					<div class="row">
						<!-- tags cloud -->
						<div class="col-xs-9">
							<div class="tags-cloud" style="padding:6px;">
								<span class="label label-info">environment &times;</span>
								<span class="label label-info">social &times;</span>
								<span class="label label-info">privacy &times;</span>
								<span class="label label-info">2015 EP Session &times;</span>
								<span class="label label-info">law &times;</span>
								<span class="label label-info">euroskeptic &times;</span>
								<span class="label label-info">energy &times;</span>
								<span class="label label-info">WHO HHR2 Campain &times;</span>
							</div>
							<br/>
							<div class="flags-cloud">
								<span class="label label-success">promoted &times;</span>
								<span class="label label-danger">duplicate &times;</span>
								<span class="label label-danger">offensive &times;</span>
								<span class="label label-danger">misleading references &times;</span>
								<span class="label label-danger">mistranslated &times;</span>
							</div>
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