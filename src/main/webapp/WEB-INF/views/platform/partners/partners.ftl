[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="footer_script"]<script src='${context_path}/app/platform/partners/partnersCtrl.js'></script>[/@content]


<div class="container-fluid" ng-controller="PartnersCtrl as partners" ng-show="appCtrl.ctx.panel==='list'">
	
	<table class="table table-hover table-responsive">
		<thead>
			<tr>
				<th>E-Mail</th>
				<th>Verified</th>
				<th>Enabled</th>
				<th>Last Login</th>
				<th>Questions Editor</th>
				<th>Partners Manager (admin)</th>
			</tr>
		</thead>
		<tbody>
			<tr ng-repeat="p in partners.partnersList" ng-click="appCtrl.editPartner(p.id)">
				<td>{{p.email}}</td>
				<td><span ng-show="p.verified===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
				<td><span ng-show="p.enabled===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
				<td>{{p.last_login | date}}</td>
				<td><span ng-show="p.can_edit_any_question===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
				<td><span ng-show="p.can_manage_partners===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
			</tr>
		</tbody>
	</table>
	
	<div class="text-center">
		<ul class="pagination">
			<li class="disabled"><a href="?page=0" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
			<li class="active"><a href="?page=1">1</a></li>
			<li><a href="?page=2">2</a></li>
			<li><a href="?page=3">3</a></li>
			<li><a href="?page=4">4</a></li>
			<li><a href="?page=5">5</a></li>
			<li class="disabled"><a href="?page=2" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
		</ul>
	</div>
</div>





