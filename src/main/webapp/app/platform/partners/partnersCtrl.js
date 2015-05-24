(function() {
	angular.module('app.partners')
		.controller('PartnersCtrl',['partnersDS',PartnersCtrl]);
		
	function PartnersCtrl(partnersDS) {
		var vm = this;
		//----------------------------------------------//

		//----------------------------------------------//
		vm.partnersList = partnersDS.getPartners();
	}

}());