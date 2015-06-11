[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container-fluid" ng-show="vm.activePanel==='archQ'">

	<div class="row q-row bottom-border"  ng-repeat="q in vm.archQ.results">
		<div class="col-sm-2 q-info-cell ">
			<div class="vcenter">
				<p class="text-nowrap" style="padding-top: 8px;">
					<span class="label label-success text-left">Yes: {{q.popular_vote_tally*100}} %</span>&nbsp;
					<span class="label label-danger text-right">No: {{(1-q.popular_vote_tally)*100}} %</span>
				</p>
				<span class="label label-default">Archived: {{q.archived_at|date:'medium'}}</span>
			</div>
		</div>
		<div class="col-sm-10 q-text-cell" ng-click="vm.editQuestion(q.id)">
			<div class="vcenter">
				<div class="">
					<span class="glyphicon glyphicon-inbox text-primary"></span> 
					&nbsp;
					<strong class="leading">{{q.title}}</strong>
				</div>
				<div class="">
					{{q.description}}
				</div>
			</div>
		</div>
	</div>


	<!-- pagination -->
	<div class="text-center">
		<ul class="pagination">
			<li ng-class="{'disabled': (vm.archQ.crtPage<=1)} ">
				<a href="#" ng-click="vm.loadPage(vm.archQ.crtPage-1)" aria-label="Previous"><span aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
			</li>
			<li ng-repeat="pg in vm.archQ.pagesRange track by $index" ng-class="{'active': (vm.archQ.crtPage==($index+1))} ">
				<a href="#" aria-label="{{$index+1}}" ng-click="vm.loadPage($index+1)">{{$index+1}}</a>
			</li>
			<li ng-class="{'disabled': (vm.archQ.crtPage>=vm.archQ.totalPages)} ">
				<a href="#" ng-click="vm.loadPage(vm.archQ.crtPage+1)" aria-label="Next"><span aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
			</li>
		</ul>
	</div>

	
</div>
