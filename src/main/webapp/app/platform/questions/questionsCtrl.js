(function() {
	angular.module('app.questions')
		.controller('QuestionsCtrl',['questionsDS',QuestionsCtrl]);
		
	function QuestionsCtrl(questionsDS) {
		
		//setup view model
		var vm = this;
		vm.editingQuestion=false;
		vm.crtQuestion = {};
		vm.crtTranslation = {lang:'en'};
		vm.translationsTab = [];
		
		//vm API
		vm.switchPanel = switchPanel;
		vm.addQuestion = addQuestion;
		vm.cancelEdit = cancelEdit;
		vm.saveQuestion = saveQuestion;
		vm.switchTranslation = switchTranslation;
		vm.publishQuestion = publishQuestion;
		vm.archiveQuestion = archiveQuestion;
		vm.deleteQuestion = deleteQuestion;
		
		//init
		switchPanel('pubQ');
		//----------------------------------------------//
		function addQuestion() {
			vm.crtQuestion = {
				translations:[],
				tags:[],
				comments:[]
			};
			vm.editingQuestion = true;
		}
		
		function cancelEdit() {
			vm.crtQuestion = null;
			vm.editingQuestion=false;
		}
		
		function saveQuestion() {
			if (vm.crtQuestion) {
				var q = vm.crtQuestion;
				if (vm.crtTranslation!=null && vm.crtTranslation.lang!==null) {
					saveCrtTranslation(q);
				}
				questionsDS.saveQuestion(q,onQuestionSaved,onQuestionCannotSave);
			}
		}
		
		function onQuestionSaved() {
			vm.crtQuestion = null;
			vm.editingQuestion=false;
		}
		
		function onQuestionCannotSave(msg) {
			cancelEdit();
			console.log('cannot save question: '+msg);
		}
		
		function saveCrtTranslation(q) {
			vm.crtTranslation.html_content = $('#questionContent').code();
			if (vm.crtTranslation.lang==='en') {
				q.title = vm.crtTranslation.title;
				q.description = vm.crtTranslation.description;
				q.html_content = vm.crtTranslation.html_content;
			} else {
				setTranslation(q,vm.crtTranslation,'title');
				setTranslation(q,vm.crtTranslation,'description');
				setTranslation(q,vm.crtTranslation,'html_content');
			}
		}
		
		function setTranslation(q,trans,fld) {
			if (q!=null&&trans!=null) {
				if (q.translations==null) q.translations=[];
				var isNewTrans = true;
				for (var i = q.translations.length - 1; i >= 0; i--) {
					var t = q.translations[i];
					if (t.lang===trans.lang && t.field_type===fld) {
						t.text = trans[fld];
						isNewTrans = false;
						break;
					}
				}
				if (isNewTrans) {
					var newTrans = {
						parent_id:q.id,
						parent_type:'question',
						field_type:fld,
						lang: trans.lang,
						text: trans[fld]
					};
					q.translations.push(newTrans);
				}
				
			}
		}
		
		function getTranslation(q,lng,fld) {
			var isNewTrans = true;
			if (q!=null&&lng!=null) {
				if (lng==='en') {
					return q[fld];
				} else {
					for (var i = q.translations.length - 1; i >= 0; i--) {
						var t = q.translations[i];
						if (t.lang===lng && t.field_type===fld) {
							return t.text;
						}
					}
				}
			}
			return null;
		}
		
		function switchTranslation(lng) {
			if (vm.crtQuestion!=null&&vm.crtTranslation.lang!==lng) {
				saveCrtTranslation(vm.crtQuestion);
				//
				vm.crtTranslation = {
					lang: lng,
					title: getTranslation(vm.crtQuestion,lng,'title')||'',
					description: getTranslation(vm.crtQuestion,lng,'description')||'',
					html_content: getTranslation(vm.crtQuestion,lng,'html_content')||'',
				};
				$('#questionContent').code(vm.crtTranslation.html_content);
				
				if (vm.translationsTab!=null) {
					var tabExists = false;
					for (var i = vm.translationsTab.length - 1; i >= 0; i--) {
						var tab = vm.translationsTab[i];
						if (tab.lang===lng) {
							tabExists = true;
						}
					}
					if (!tabExists) {
						if (vm.translationsTab.length>5) {
							vm.translationsTab.shift();
						}
						vm.translationsTab.push({
							lang: lng,
							label: lng
						});
					}
				}
				
			}
		}
		
		
		function publishQuestion() {
			vm.editingQuestion=false;
		}
		
		function archiveQuestion() {
			vm.editingQuestion=false;
		}
		
		function deleteQuestion() {
			vm.editingQuestion=false;
		}

		//----------------------------------------------//

		function switchPanel(panel) {
			vm.activePanel = panel;
		}

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