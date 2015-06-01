[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe - Questions Management[/@content]
[@content for="crtMenu"]questions[/@content]

[@content for="footer_script"]<script src='${context_path}/app/platform/questions/main.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/platform/questions/questionsCtrl.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/platform/questions/questionsDS.js'></script>[/@content]

<div ng-app="app.questions" ng-controller="QuestionsCtrl as vm">

	[#include "questions.ftl"]

	[#include "edit_q.ftl"]
	
</div>