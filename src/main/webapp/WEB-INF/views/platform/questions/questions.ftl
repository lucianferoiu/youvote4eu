[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container">
	<div class="row">
		<div class="">
			<ul class="nav nav-pills nav-justified">
				<li role="presentation" ng-class="{'active': vm.activePanel==='pubQ'}" style="padding-left:10px;">
					<a href="#" class="q-pill" ng-click="vm.switchPanel('pubQ')"><span class="glyphicon glyphicon-star-empty"></span>&nbsp; Published Questions</a>
				</li>
				<li role="presentation" ng-class="{'active': vm.activePanel==='archQ'}">
					<a href="#" class="q-pill" ng-click="vm.switchPanel('archQ')"><span class="glyphicon glyphicon-inbox"></span>&nbsp; Archived Questions</a>
				</li>
				<li role="presentation" ng-class="{'active': vm.activePanel==='propQ'}">
					<a href="#" class="q-pill" ng-click="vm.switchPanel('propQ')"><span class="glyphicon glyphicon-comment"></span>&nbsp; Proposed Questions</a>
				</li>
				<li role="presentation" ng-class="{'active': vm.activePanel==='myQ'}">
					<a href="#" class="q-pill" ng-click="vm.switchPanel('myQ')"><span class="glyphicon glyphicon-user"></span>&nbsp; My Questions</a>
				</li>
				<li role="presentation" id="addQuestionBtn" style="padding-right:10px;">
					<a href="#" class="q-pill q-pill-button btn-success" ng-click="vm.addQuestion()">
						<span class="glyphicon glyphicon-plus"></span>&nbsp; Propose new Question</a>
				</li>
			</ul>
		</div>
	</div>
	<br/>
	<hr/>
	<div class="row" style="margin-top:8px;">
		[#-- <div class="col-xs-3">
			<button type="button" class="btn btn-success" data-toggle="dropdown" style="display: block; width: 100%;"
				aria-expanded="false">&nbsp;<span class="glyphicon glyphicon-plus"></span>&nbsp;Propose new Question </button>
		</div> --]
		<div class="pull-right col-xs-3">
			<form class="">
				<div class="input-group">
					<input type="text" class="form-control" id="questionSearch" placeholder="search by key word">
					<span class="input-group-btn">
						<button class="btn btn-default" type="button"><span class="glyphicon glyphicon-search"></button>
					</span>
				</div>
			</form>
		</div>
	</div>
<div>
<br/>

[#include "q_pub.ftl"]

[#include "q_arch.ftl"]

[#include "q_prop.ftl"]

[#include "q_my.ftl"]

