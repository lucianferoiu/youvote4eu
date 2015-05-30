[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[#-- Variables: --]
[#--     [@content for="title"]You Vote For Europe[/@content] --]
[#--     [@content for="footer_script"]<script src='${context_path}/additional.js'></script>[/@content] --]
[#--     [@content for="crtMenu"]home[/@content]  --]
[#-- ============================================================================= --]

[@content for="title"]You Vote For Europe - Partners Management[/@content]
[@content for="crtMenu"]partners[/@content]

[@content for="footer_script"]<script src='${context_path}/app/platform/partners/main.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/platform/partners/partnersCtrl.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/platform/partners/partnersDS.js'></script>[/@content]

<div ng-app="app.partners" ng-controller="PartnersCtrl as vm">

	[#include "partners.ftl"]

	[#include "partner.ftl"]
	
</div>