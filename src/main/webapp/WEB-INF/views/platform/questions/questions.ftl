[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container">
	<div class="row">
		<div class="">
			<ul class="nav nav-pills nav-justified">
				<li role="presentation" class="active" style="padding-left:10px;">
					<a href="#" class="q-pill"><span class="glyphicon glyphicon-star-empty"></span> Published Questions</a>
				</li>
				<li role="presentation">
					<a href="#" class="q-pill"><span class="glyphicon glyphicon-inbox"></span> Archived Questions</a>
				</li>
				<li role="presentation">
					<a href="#" class="q-pill"><span class="glyphicon glyphicon-comment"></span> Proposed Questions</a>
				</li>
				<li role="presentation">
					<a href="#" class="q-pill"><span class="glyphicon glyphicon-user"></span> My Questions</a>
				</li>
				<li role="presentation" id="addQuestionBtn" style="padding-right:10px;">
					<a href="#" class="q-pill q-pill-button btn-success"><span class="glyphicon glyphicon-plus"></span> Propose new Questions</a>
				</li>
			</ul>
		</div>
	</div>
	<div class="row" style="margin-top:8px;">
		[#-- <div class="col-xs-3">
			<button type="button" class="btn btn-success" data-toggle="dropdown" style="display: block; width: 100%;"
				aria-expanded="false">&nbsp;<span class="glyphicon glyphicon-plus"></span>&nbsp;Propose new Question </button>
		</div> --]
		<div class="pull-right col-xs-5">
			<form class="form-inline">
				<div class="input-group" style="width:100%;">
					<input type="text" class="form-control" id="questionSearch" placeholder="type a word to search for questions containing it..">
					<div class="input-group-addon"><span class="glyphicon glyphicon-search"></span></div>
				</div>
			</form>
		</div>
	</div>

<div>


[#include "q_pub.ftl"]

[#include "q_arch.ftl"]

[#include "q_prop.ftl"]

[#include "q_my.ftl"]

