[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="container-fluid" ng-show="vm.activePanel==='propQ'">

	<div class="row q-row bottom-border" ng-repeat="q in vm.propQ.results">
		<div class="col-sm-1 col-sm-offset-1 q-info-cell">
			<div class="text-center" data-toggle="tooltip" title="{{vm.canUpvote(q.id)?'Support the question by upvoting it!':''}}">
				<span class="glyphicon glyphicon-arrow-up huge" style="padding-top:10px;" 
					ng-class="{'upvotable':vm.canUpvote(q.id),'unvotable':(!vm.canUpvote(q.id))}"
					ng-click="vm.upvote(q.id)">
				</span>
					
			</div>
			<div class="text-center">
				<span class="label label-default">{{q.support|pad:5:'&nbsp;'}}</span>
			</div>
		</div>
		<div class="col-sm-10 q-text-cell" ng-click="vm.editQuestion(q.id)">
			<div class="vcenter">
				<div class="">
					<span class="text-primary">[ {{q.id|pad:3}} ] </span> 
					&nbsp;&nbsp;
					<span class="glyphicon glyphicon-circle-arrow-up text-primary"></span> 
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
			<li ng-class="{'disabled': (vm.propQ.crtPage<=1)} ">
				<a href="#" ng-click="vm.loadPage(vm.propQ.crtPage-1)" aria-label="Previous"><span aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
			</li>
			<li ng-repeat="pg in vm.propQ.pagesRange track by $index" ng-class="{'active': (vm.propQ.crtPage==($index+1))} ">
				<a href="#" aria-label="{{$index+1}}" ng-click="vm.loadPage($index+1)">{{$index+1}}</a>
			</li>
			<li ng-class="{'disabled': (vm.propQ.crtPage>=vm.propQ.totalPages)} ">
				<a href="#" ng-click="vm.loadPage(vm.propQ.crtPage+1)" aria-label="Next"><span aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
			</li>
		</ul>
	</div>

	
</div>

[@content for="footer_script"]
<script type="text/javascript">
$(function () {
	$('[data-toggle="tooltip"]').tooltip();
});
</script>
[/@content]
