(function() {
	angular.module('app.partners',[])
		.controller('AppCtrl',AppCtrl);
	
	function AppCtrl() {
		var vm = this;
		//----------------------------------------------//
		vm.ctx = {
				title:'Platform Partners',
				panel: 'list'
		};
		vm.swapPanel = swapPanel;
		vm.addPartner = addPartner;
		vm.editPartner = editPartner;
		//----------------------------------------------//
		
		swapPanel('list');
				
		//----------------------------------------------//
		function swapPanel(panel) {
			var panels = {
				'list': {title:'List of Platform Partners'},
				'edit': {title:'Edit details of Platform Partner'},
				'add': {title:'Add a new Platform Partner'}
			};
			vm.ctx.panel = panel;
			vm.ctx.title = panels[panel].title;
		}
		
		function addPartner() {
			swapPanel('add');
		}
		
		function editPartner(partnerId) {
			swapPanel('edit');
		}
		
	}
	
}());