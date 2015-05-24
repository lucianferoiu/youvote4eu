(function() {
	angular.module('app.partners')
		.factory('partnersDS',['$q','$http',PartnersDS]);
		
		function PartnersDS($q,$http) {
			return {
				getPartners: getPartners,
				getPartnerById: getPartnerById
			};
		//----------------------------------------------//
			
			function getPartners() {
				 
				var p = [
				  {  
				    "can_add_question":true,
				    "can_approve_question":false,
				    "can_archive_any_question":false,
				    "can_change_translation":false,
				    "can_delete_any_question":false,
				    "can_edit_any_question":false,
				    "can_edit_own_question":true,
				    "can_manage_partners":true,
				    "can_view_statistics":true,
				    "email":"gogu@example.com",
				    "enabled":true,
				    "first_login":"2015-04-22T12:50Z",
				    "id":1,
				    "is_organization":false,
				    "last_login":"2015-05-24T12:43Z",
				    "name":null,
				    "verified":true
				  },
				  {  
				    "can_add_question":true,
				    "can_approve_question":false,
				    "can_archive_any_question":false,
				    "can_change_translation":false,
				    "can_delete_any_question":false,
				    "can_edit_any_question":false,
				    "can_edit_own_question":true,
				    "can_manage_partners":false,
				    "can_view_statistics":true,
				    "email":"gogu4@example.com",
				    "enabled":true,
				    "first_login":"2015-04-23T10:54Z",
				    "id":3,
				    "is_organization":false,
				    "last_login":"2015-04-23T10:54Z",
				    "name":null,
				    "verified":true
				  },
				  {  
				    "can_add_question":true,
				    "can_approve_question":false,
				    "can_archive_any_question":false,
				    "can_change_translation":false,
				    "can_delete_any_question":false,
				    "can_edit_any_question":false,
				    "can_edit_own_question":true,
				    "can_manage_partners":false,
				    "can_view_statistics":true,
				    "email":"gogu3@example.com",
				    "enabled":true,
				    "first_login":null,
				    "id":2,
				    "is_organization":false,
				    "last_login":null,
				    "name":null,
				    "verified":false
				  },
				  {  
				    "can_add_question":true,
				    "can_approve_question":false,
				    "can_archive_any_question":false,
				    "can_change_translation":false,
				    "can_delete_any_question":false,
				    "can_edit_any_question":false,
				    "can_edit_own_question":true,
				    "can_manage_partners":false,
				    "can_view_statistics":true,
				    "email":"gogu5@example.com",
				    "enabled":true,
				    "first_login":null,
				    "id":4,
				    "is_organization":false,
				    "last_login":null,
				    "name":null,
				    "verified":false
				  }
				];
				return p;
			}
			
			function getPartnerById(partnerId) {
			}
			
		}
}());