[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
<div class="container-fluid">
	<div id="editPartnerModel" class="modal editing-dialog" ng-class="{'show': vm.ctx.panel==='edit'}" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="vm.cancelEdit()"><span aria-hidden="true">&times;</span></button>
					<h3>Change the details of the Partner <em>{{vm.ctx.crtPartner.name}}</em></h3>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group">
							<label for="partnerEmail" class="col-sm-4 control-label">Email address</label>
							<div class="col-sm-8">
								<input type="email" class="form-control" id="partnerEmail" placeholder="Enter email" ng-model="vm.ctx.crtPartner.email" required>
							</div>
						</div>
						<div class="form-group">
							<label for="partnerName" class="col-sm-4 control-label">Name</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="partnerName" placeholder="Optional name of person or organization" ng-model="vm.ctx.crtPartner.name">
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Represents an Organization</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerIsOrg" ng-model="vm.ctx.crtPartner.is_organization"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerVerified" class="col-sm-4 control-label">Account Verified</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" class="disabled" id="partnerVerified" ng-model="vm.ctx.crtPartner.verified" disabled> 
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">Joined as Partner on</label>
							<div class="col-sm-8">
								<p class="form-control-static">{{vm.ctx.crtPartner.first_login|date:'medium'}}</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">Last seen on</label>
							<div class="col-sm-8">
								<p class="form-control-static">{{vm.ctx.crtPartner.last_login|date:'medium'}}</p>
							</div>
						</div>
						<hr/>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Account Enabled</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerEnabled" ng-model="vm.ctx.crtPartner.enabled"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Add Question</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanAddQuestion" ng-model="vm.ctx.crtPartner.can_add_question"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Edit other Partners' Questions</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanEditOthQuestion" ng-model="vm.ctx.crtPartner.can_edit_any_question"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Change Translations</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanChangeTranslation" ng-model="vm.ctx.crtPartner.can_change_translation"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Approve/Publish any Question</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerApproveQuestion" ng-model="vm.ctx.crtPartner.can_approve_question"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Archive any Question</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanArchiveQuestion" ng-model="vm.ctx.crtPartner.can_archive_any_question"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Delete any Question</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanDeleteAnyQuestion" ng-model="vm.ctx.crtPartner.can_delete_any_question"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Has Access to the Statistics</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanAccessStats" ng-model="vm.ctx.crtPartner.can_view_statistics"> 
							</div>
						</div>
						<div class="form-group">
							<label for="partnerEnabled" class="col-sm-4 control-label">Can Manage Partners (Admin)</label>
							<div class="col-sm-1">
								<input  class="form-control" type="checkbox" id="partnerCanManagePartners" ng-model="vm.ctx.crtPartner.can_manage_partners"> 
							</div>
						</div>


					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" ng-click="vm.cancelEdit()">Close</button>
					<button type="button" class="btn btn-primary" ng-click="vm.savePartner()">Save changes</button>
				</div>
			</div>
		</div>
	</div>
</div>

