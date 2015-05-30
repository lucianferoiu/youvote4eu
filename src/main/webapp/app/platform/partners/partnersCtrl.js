(function() {
	angular.module('app.partners')
		.controller('PartnersCtrl',['partnersDS',PartnersCtrl]);
		
	function PartnersCtrl(partnersDS) {
		var vm = this;
		//----------------------------------------------//
		vm.ctx = {
			panel: 'list',
			crtPage: 1,
			totalPages: 1,
			pagesRange: [],
			sortBy: 'last_login',
			sortDir: false,
			crtPartner: null
		};
		vm.ctxCopy = angular.copy(vm.ctx);//preserve a reset point
		
		//api:
		vm.loadPage = loadPage;
		vm.sortPartners = sortPartners;
		vm.addPartner = addPartner;
		vm.editPartner = editPartner;
		//----------------------------------------------//
		
		loadPage(1);
				
		//----------------------------------------------//
		function swapPanel(panel) {
			vm.ctx.panel = panel;
		}
		
		function loadPage(page) {
			if (page>=1 && page<=vm.ctx.totalPages) {
				vm.ctx.crtPage=page;
				var sort = (vm.ctx.sortDir?'':'-') + vm.ctx.sortBy;
				partnersDS.getPartners(page,sort,onPageLoad,onPageError);
				swapPanel('list');
			}
		}
		
		function onPageLoad(results) {
			vm.partners = results;
			vm.ctx.totalPages = partnersDS.countPages();
			vm.ctx.pagesRange = range(vm.ctx.totalPages);
		}
		
		function onPageError(data) {
			resetCtx();
			
		}
		
		function resetCtx() {
			vm.ctx = angular.copy(vm.ctxCopy);
		}
		
		function sortPartners(by) {
			vm.ctx.sortBy = by;
			vm.ctx.sortDir = !vm.ctx.sortDir;
			vm.loadPage(1);//always reset the current page when re-sorting
		}
		
		function addPartner() {
			swapPanel('add');
		}
		
		function editPartner(partnerId) {
			swapPanel('edit');
		}
		
		function range(n) {
			return new Array(n);
		}
		
	}

}());