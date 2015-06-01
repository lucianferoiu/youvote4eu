[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container-fluid">
	<hr/>
	
	<div class="row q-row bottom-border">
		<div class="col-sm-2 q-info-cell ">
			<div class="vcenter text-right">
				<span class="label label-default">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11.234 votes&nbsp;&nbsp;</span>
			</div>
		</div>
		<div class="col-sm-10 q-text-cell">
			<div class="vcenter">
				<div class="">
					<strong class="leading">Question Title: Concise and rallying question ?</strong>
				</div>
				<div class="">
					Longer body and description of the question. This could go on for several lines -- one, maybe even two lines but it must be broken down 
					to about 250 characters... Some other words here until we reach 250 words in total for this question description.
				</div>
			</div>
		</div>
	</div>

	<div class="row q-row bottom-border">
		<div class="col-sm-2 q-info-cell ">
			<div class="vcenter">
				<p class="text-nowrap" style="padding-top: 8px;">
					<span class="label label-success text-left">Yes: 15%</span>&nbsp;
					<span class="label label-danger text-right">No: 85%</span>
				</p>
				<span class="label label-default">Archived: Aug 31, 2015</span>
			</div>
		</div>
		<div class="col-sm-10 q-text-cell">
			<div class="vcenter">
				<div class="">
					<strong class="leading">Question Title: Concise and rallying question ?</strong>
				</div>
				<div class="">
					Longer body and description of the question. This could go on for several lines -- one, maybe even two lines but it must be broken down 
					to about 250 characters... Some other words here until we reach 250 words in total for this question description.
				</div>
			</div>
		</div>
	</div>

	<div class="row q-row bottom-border">
		<div class="col-sm-2 q-info-cell">
			<button class="btn btn-default vcenter disabled" type="button">
				<span class="glyphicon glyphicon-thumbs-up huge"></span> &nbsp;&nbsp;&nbsp;<span class="badge q-badge vcenter" style="top:4px;">&nbsp;&nbsp;&nbsp;31</span>
			</button>
		</div>
		<div class="col-sm-10 q-text-cell">
			<div class="vcenter">
				<div class="">
					<strong class="leading">Question Title: Concise and rallying question ?</strong>
				</div>
				<div class="">
					Longer body and description of the question. This could go on for several lines -- one, maybe even two lines but it must be broken down 
					to about 250 characters... Some other words here until we reach 250 words in total for this question description.
				</div>
			</div>
		</div>
	</div>

	<div class="row q-row bottom-border">
		<div class="col-sm-2 q-info-cell">
			<button class="btn btn-default vcenter" type="button">
				<span class="glyphicon glyphicon-thumbs-up huge"></span> &nbsp;&nbsp;&nbsp;<span class="badge q-badge vcenter" style="top:4px;">&nbsp;&nbsp;&nbsp;21</span>
			</button>
		</div>
		<div class="col-sm-10 q-text-cell">
			<div class="vcenter">
				<div class="">
					<strong class="leading">Question Title: Concise and rallying question ?</strong>
				</div>
				<div class="">
					Longer body and description of the question. This could go on for several lines -- one, maybe even two lines but it must be broken down 
					to about 250 characters... Some other words here until we reach 250 words in total for this question description.
				</div>
			</div>
		</div>
	</div>


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
