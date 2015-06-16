[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container-fluid" ng-show="vm.activePanel==='pubQ'">

	<div class="row q-row bottom-border" ng-repeat="q in vm.pubQ.results">
		<div class="col-sm-2 q-info-cell ">
			<div class="text-right" style="margin-top:18px;">
				<span class="q-votes" >&nbsp;{{q.popular_votes|pad:5:'0'}} votes</span>
			</div>
		</div>
		<div class="col-sm-10 q-text-cell" ng-click="vm.editQuestion(q.id)">
			<div class="">
				<div class="">
					<span class="text-primary">[ {{q.id|pad:3}} ] </span> 
					&nbsp;&nbsp;
					<span class="glyphicon glyphicon-star-empty text-primary"></span> 
					&nbsp;&nbsp;
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
			<li ng-class="{'disabled': (vm.pubQ.crtPage<=1)} ">
				<a href="#" ng-click="vm.loadPage(vm.pubQ.crtPage-1)" aria-label="Previous"><span aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
			</li>
			<li ng-repeat="pg in vm.pubQ.pagesRange track by $index" ng-class="{'active': (vm.pubQ.crtPage==($index+1))} ">
				<a href="#" aria-label="{{$index+1}}" ng-click="vm.loadPage($index+1)">{{$index+1}}</a>
			</li>
			<li ng-class="{'disabled': (vm.pubQ.crtPage>=vm.pubQ.totalPages)} ">
				<a href="#" ng-click="vm.loadPage(vm.pubQ.crtPage+1)" aria-label="Next"><span aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
			</li>
		</ul>
	</div>
	
</div>
