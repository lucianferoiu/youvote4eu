(function() {
	angular.module('app.questions')
		.controller('QuestionsCtrl',['questionsDS',QuestionsCtrl]);
		
	function QuestionsCtrl(questionDS) {
		var vm = this;
		


/*************************************************************************************
		//----------------------------------------------//
		vm.ctx = {
			panel: 'list',
			crtPage: 1,
			totalPages: 1,
			pagesRange: [],
			sortBy: 'last_login',
			sortDir: false,
			crtQuestion: null
		};
		vm.ctxCopy = angular.copy(vm.ctx);//preserve a reset point
		
		//api:
		vm.loadPage = loadPage;
		vm.sortQuestions = sortQuestions;
		vm.addQuestion = addQuestion;
		vm.editQuestion = editQuestion;
		vm.cancelEdit = cancelEdit;
		vm.saveQuestion = saveQuestion;
		//----------------------------------------------//
		
		loadPage(1);
				
		//----------------------------------------------//
		
		function loadPage(page) {
			if (page>=1 && page<=vm.ctx.totalPages) {
				vm.ctx.crtPage=page;
				var sort = (vm.ctx.sortDir?'':'-') + vm.ctx.sortBy;
				questionsDS.getQuestions(page,sort,onPageLoad,onPageError);
				swapPanel('list');
			}
		}
		
		function onPageLoad(results) {
			vm.questions = results;
			vm.ctx.totalPages = questionsDS.countPages();
			vm.ctx.pagesRange = range(vm.ctx.totalPages);
		}
		
		function onPageError(data) {
			resetCtx();
		}
		
		function sortQuestions(by) {
			vm.ctx.sortBy = by;
			vm.ctx.sortDir = !vm.ctx.sortDir;
			vm.loadPage(1);//always reset the current page when re-sorting
		}
		
		//----------------------------------------------//
		
		function editQuestion(questionId) {
			questionsDS.getQuestionById(questionId,onQuestionEdit,onQuestionEditError);
			swapPanel('edit');
		}
		
		function onQuestionEdit(question) {
			vm.ctx.crtQuestion = question;
		}
		
		function onQuestionEditError(data) {
			cancelEdit();
		}

		function cancelEdit() {
			vm.ctx.crtQuestion = null;
			swapPanel('list');
		}
		
		function saveQuestion() {
			if (vm.ctx.crtQuestion) {
				questionsDS.saveQuestion(vm.ctx.crtQuestion,onQuestionSaved,onQuestionCannotSave);
			}
		}
		
		function onQuestionSaved() {
			vm.ctx.crtQuestion = null;
			vm.loadPage(vm.ctx.crtPage);
			swapPanel('list');
		}
		
		function onQuestionCannotSave(msg) {
			cancelEdit();
			console.log('cannot save question: '+msg);
		}

		//----------------------------------------------//

		function addQuestion() {
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
		

**************************************************************************************/

	}

}());