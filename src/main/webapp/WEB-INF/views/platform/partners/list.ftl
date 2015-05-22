[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[#-- Variables: --]
[#--     [@content for="title"]You Vote For Europe[/@content] --]
[#--     [@content for="app_path"]path/to/app/js[/@content] --]
[#--     [@content for="footer_script"]<script src='${context_path}/additional.js'></script>[/@content] --]
[#-- ============================================================================= --]

[@content for="title"]You Vote For Europe - Partners[/@content]
[@content for="crtMenu"]partners[/@content]

<div class="container">
	
<h2>Platform Partners Management</h2>
<br/>
<table class="table table-hover table-bordered table-responsive">
	<thead>
		<tr>
			<th>E-Mail</th>
			<th>Verified</th>
			<th>Enabled</th>
			<th>Last Login</th>
			[#-- <th>Can add questions</th> --]
			<th>Questions Editor</th>
			[#-- <th>Can change translation</th> --]
			[#-- <th>Can approve question</th> --]
			[#-- <th>Can archive any question</th> --]
			[#-- <th>Can view statistics</th> --]
			<th>Partners Manager (admin)</th>
		</tr>
	</thead>
	<tbody>
[#list partners as p]
	<tr class="[#if p.verified==false||p.enabled==false] dis [/#if] [#if p.can_manage_partners==true] info [/#if]">
		<td>${p.email}</td>
		<td>[#if p.verified==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td>
		<td>[#if p.enabled==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td>
		<td>[#if p.last_login??]${p.last_login?string["yyyy/MM/dd HH:mm"]}[/#if]</td>
		[#-- <td>[#if p.can_add_question==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td> --]
		<td>[#if p.can_edit_any_question==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td>
		[#-- <td>[#if p.can_change_translation==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td> --]
		[#-- <td>[#if p.can_approve_question==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td> --]
		[#-- <td>[#if p.can_archive_any_question==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td> --]
		[#-- <td>[#if p.can_view_statistics==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td> --]
		<td>[#if p.can_manage_partners==true] <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> [/#if]</td>
	</tr>
[/#list]
	</tbody>
</table>
</div>

<nav class="container">
	<ul class="pagination">
		<li [#if crtPage<=1 ] class="disabled" [/#if] ><a href="?page=${crtPage-1}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
		[#list 1..cntPages as p]
		<li [#if crtPage==p ]class="active"[/#if]><a href="?page=${p}">${p}</a></li>
		[/#list]
		<li [#if crtPage>cntPages ] class="disabled" [/#if] ><a href="?page=${crtPage+1}" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
	</ul>
</nav>
