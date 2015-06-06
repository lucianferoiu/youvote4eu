[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container" [#-- ng-show="vm.ctx.panel==='list'" --]>
	
	<div class="row">
		<div class="">
			<ul class="nav nav-pills">
				<li role="presentation" class="active">
					<a href="${context_path}/platform/partners" class="q-pill"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;All Platform Partners</a>
				</li>
				<li role="presentation" id="addPartnerBtn" class="pull-right">
					<a href="#" class="q-pill q-pill-button btn-success" ng-click="vm.addPartner()">
						<span class="glyphicon glyphicon-plus"></span>&nbsp; Endorse New Partner</a>
				</li>
			</ul>
		</div>
	</div>
	<hr/>
	<div class="row">
		
		<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th>
						<span ng-click="vm.sortPartners('email')">E-Mail</span>
						<span>&nbsp;&nbsp;&nbsp;</span>
						<span ng-show="vm.ctx.sortDir && vm.ctx.sortBy==='email'" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
						<span ng-show="!vm.ctx.sortDir && vm.ctx.sortBy==='email'" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</th>
					<th>
						<span ng-click="vm.sortPartners('verified')">Verified</span>
						<span>&nbsp;&nbsp;&nbsp;</span>
						<span ng-show="vm.ctx.sortDir && vm.ctx.sortBy==='verified'" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
						<span ng-show="!vm.ctx.sortDir && vm.ctx.sortBy==='verified'" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</th>
					<th>
						<span ng-click="vm.sortPartners('enabled')">Enabled</span>
						<span>&nbsp;&nbsp;&nbsp;</span>
						<span ng-show="vm.ctx.sortDir && vm.ctx.sortBy==='enabled'" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
						<span ng-show="!vm.ctx.sortDir && vm.ctx.sortBy==='enabled'" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</th>
					<th>
						<span ng-click="vm.sortPartners('last_login')">Last Login</span>
						<span>&nbsp;&nbsp;&nbsp;</span>
						<span ng-show="vm.ctx.sortDir && vm.ctx.sortBy==='last_login'" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
						<span ng-show="!vm.ctx.sortDir && vm.ctx.sortBy==='last_login'" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</th>
					<th>
						<span ng-click="vm.sortPartners('can_edit_any_question')">Editor</span>
						<span>&nbsp;&nbsp;&nbsp;</span>
						<span ng-show="vm.ctx.sortDir && vm.ctx.sortBy==='can_edit_any_question'" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
						<span ng-show="!vm.ctx.sortDir && vm.ctx.sortBy==='can_edit_any_question'" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</th>
					<th>
						<span ng-click="vm.sortPartners('can_manage_partners')">Admin</span>
						<span>&nbsp;&nbsp;&nbsp;</span>
						<span ng-show="vm.ctx.sortDir && vm.ctx.sortBy==='can_manage_partners'" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
						<span ng-show="!vm.ctx.sortDir && vm.ctx.sortBy==='can_manage_partners'" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="p in vm.partners" ng-click="vm.editPartner(p.id)">
					<td>{{p.email}} <small class="text-uppercase text-muted" ng-show="p.name!==null && p.name.length>1"> &nbsp;&nbsp;&nbsp;[ {{p.name}} ]</small></td>
					<td><span ng-show="p.verified===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
					<td><span ng-show="p.enabled===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
					<td>{{p.last_login | date:'short'}}</td>
					<td><span ng-show="p.can_edit_any_question===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
					<td><span ng-show="p.can_manage_partners===true" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td>
				</tr>
			</tbody>
		</table>
	
		<div class="text-center">
			<ul class="pagination">
				<li ng-class="{'disabled': (vm.ctx.crtPage<=1)} ">
					<a href="#" ng-click="vm.loadPage(vm.ctx.crtPage-1)" aria-label="Previous"><span aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
				</li>
				<li ng-repeat="pg in vm.ctx.pagesRange track by $index" ng-class="{'active': (vm.ctx.crtPage==($index+1))} ">
					<a href="#" aria-label="{{$index+1}}" ng-click="vm.loadPage($index+1)">{{$index+1}}</a>
				</li>
				<li ng-class="{'disabled': (vm.ctx.crtPage>=vm.ctx.totalPages)} ">
					<a href="#" ng-click="vm.loadPage(vm.ctx.crtPage+1)" aria-label="Next"><span aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
				</li>
			</ul>
		</div>

	</div>

</div>





