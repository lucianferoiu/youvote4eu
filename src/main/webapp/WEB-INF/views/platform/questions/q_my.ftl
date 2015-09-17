[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container-fluid" ng-show="vm.activePanel==='myQ'">

	<div class="row q-row bottom-border" ng-repeat="q in vm.myQ.results">

		<div class="col-sm-2 q-info-cell" ng-show="q.is_archived">
			<div class="">
				<p class="text-nowrap" style="padding-top: 8px;">
					<span class="label label-success text-left">Yes: {{(q.popular_vote_tally*100).toFixed(0)}} %</span>&nbsp;
					<span class="label label-danger text-right">No: {{((1-q.popular_vote_tally)*100).toFixed(0)}} %</span>
				</p>
				<span class="label label-default">Archived: {{q.archived_at|date:'MMM d, y h:mm'}}</span>
			</div>
		</div>

		<div class="col-sm-1 col-sm-offset-1 q-info-cell " ng-show="q.is_published && (!q.is_archived)">
			<div class="">
				<span class="label label-default text-right"><strong>{{q.popular_votes|pad:11:'&nbsp;'}}</strong> votes</span>
			</div>
		</div>

		<div class="col-sm-2 q-info-cell" ng-show="(!q.is_published) && (!q.is_archived)">
			<div class="text-center pull-right" data-toggle="tooltip" title="{{vm.canUpvote(q.id)?'Support the question by upvoting it!':''}}">
				&nbsp;&nbsp;<span class="glyphicon glyphicon-arrow-up huge unvotable" style="padding-top:14px;">
				</span>
			</div>
			<div class=" q-support text-right pull-right">&nbsp;{{q.support}}</div>
		</div>



		<div class="col-sm-10 q-text-cell" ng-click="vm.editQuestion(q.id)">
			<div class="">
				<div class="">
					<span class="text-primary">[ {{q.id|pad:3}} ] </span>
					&nbsp;&nbsp;
					<span class="glyphicon glyphicon-inbox text-primary" ng-show="q.is_archived"></span>
					<span class="glyphicon glyphicon-star-empty text-primary" ng-show="q.is_published && (!q.is_archived)"></span>
					<span class="glyphicon glyphicon-circle-arrow-up text-primary" ng-show="(!q.is_published) && (!q.is_archived)"></span>
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
			<li ng-class="{'disabled': (vm.myQ.crtPage<=1)} ">
				<a href="#" ng-click="vm.loadPage(vm.myQ.crtPage-1)" aria-label="Previous"><span aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
			</li>
			<li ng-repeat="pg in vm.myQ.pagesRange track by $index" ng-class="{'active': (vm.myQ.crtPage==($index+1))} ">
				<a href="#" aria-label="{{$index+1}}" ng-click="vm.loadPage($index+1)">{{$index+1}}</a>
			</li>
			<li ng-class="{'disabled': (vm.myQ.crtPage>=vm.myQ.totalPages)} ">
				<a href="#" ng-click="vm.loadPage(vm.myQ.crtPage+1)" aria-label="Next"><span aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
			</li>
		</ul>
	</div>


</div>
