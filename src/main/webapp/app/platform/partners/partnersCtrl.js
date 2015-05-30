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
		vm.cancelEdit = cancelEdit;
		vm.savePartner = savePartner;
		//----------------------------------------------//
		
		loadPage(1);
				
		//----------------------------------------------//
		
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
		
		function sortPartners(by) {
			vm.ctx.sortBy = by;
			vm.ctx.sortDir = !vm.ctx.sortDir;
			vm.loadPage(1);//always reset the current page when re-sorting
		}
		
		//----------------------------------------------//
		
		function editPartner(partnerId) {
			partnersDS.getPartnerById(partnerId,onPartnerEdit,onPartnerEditError);
			swapPanel('edit');
		}
		
		function onPartnerEdit(partner) {
			vm.ctx.crtPartner = partner;
		}
		
		function onPartnerEditError(data) {
			cancelEdit();
		}

		function cancelEdit() {
			vm.ctx.crtPartner = null;
			swapPanel('list');
		}
		
		function savePartner() {
			if (vm.ctx.crtPartner) {
				partnersDS.savePartner(vm.ctx.crtPartner,onPartnerSaved,onPartnerCannotSave);
			}
		}
		
		function onPartnerSaved() {
			vm.ctx.crtPartner = null;
			vm.loadPage(vm.ctx.crtPage);
			swapPanel('list');
		}
		
		function onPartnerCannotSave(msg) {
			cancelEdit();
			console.log('cannot save partner: '+msg);
		}

		//----------------------------------------------//

		function addPartner() {
			swapPanel('add');
		}

		//----------------------------------------------//

		function swapPanel(panel) {
			vm.ctx.panel = panel;
		}
		
		function resetCtx() {
			vm.ctx = angular.copy(vm.ctxCopy);
		}

		function range(n) {
			return new Array(n);
		}
		
	}

}());