(function() {
	angular.module('app.questions')
		.controller('QuestionsCtrl',['questionsDS','refDS',QuestionsCtrl]);
		
	function QuestionsCtrl(questionsDS,refDS) {
		
		//setup view model
		var vm = this;
		vm.editingQuestion=false;
		vm.pubQ = {
			crtPage: 1,
			searchWord: '',
			totalPages: 1,
			pagesRange: [],
			totalResults: 0,
			results: []
		};
		vm.archQ = angular.copy(vm.pubQ);
		vm.propQ = angular.copy(vm.pubQ);
		vm.myQ = angular.copy(vm.pubQ);
		vm.propQ.canVoteOnQuestions = [];
		//
		vm.crtQuestion = {};
		vm.crtTranslation = {lang:'en'};
		vm.translationsTab = [];
		vm.translationsDropdown = [];
		
		//vm API
		vm.switchPanel = switchPanel;
		vm.loadPage = loadPage;
		vm.searchByWord = searchByWord;
		vm.clearSearch = clearSearch;
		vm.addQuestion = addQuestion;
		vm.editQuestion = editQuestion;
		vm.cancelEdit = cancelEdit;
		vm.crtQuestionInalid = crtQuestionInalid;
		vm.saveQuestion = saveQuestion;
		vm.switchTranslation = switchTranslation;
		vm.englishForLangCode = englishForLangCode;
		vm.publishQuestion = publishQuestion;
		vm.archiveQuestion = archiveQuestion;
		vm.deleteQuestion = deleteQuestion;
		vm.canUpvote = canUpvote;
		vm.upvote = upvote;
		
		//init
		refDS.preload(true);
		switchPanel('pubQ');
		loadPage(1);
		//----------------------------------------------//
		
		function loadPage(page) {
			var panel = vm[vm.activePanel];
			if (panel) {
				if (!page) {
					page = panel.crtPage;//simply reload
				}
				if ( (panel.searchWord && panel.searchWord.length>1) || (page>=1 && page<=panel.totalPages)) {

					var params = {};
					if (panel.searchWord && panel.searchWord.length>1) {
						params.search = panel.searchWord;
					}
					
					questionsDS.getQuestions(page,vm.activePanel,params,function (data) {
						panel.totalRecords = data.total;
						panel.totalPages = Math.ceil(panel.totalRecords/questionsDS.PAGE_SIZE);
						panel.pagesRange = refDS.range(panel.totalPages);
						panel.results = data.results;
						panel.crtPage=page;
						
						if (panel.canVoteOnQuestions) {//proposed questions... ask what the partner can upvote
							var qIDs = [];
							for (var i = panel.results.length - 1; i >= 0; i--) {
								var q = panel.results[i];
								qIDs.push(q.id);
							}
							questionsDS.getUpvotableQuestions(qIDs, function (data) {
								 panel.canVoteOnQuestions = panel.canVoteOnQuestions.concat(data);
								
							},function (message) {
								console.log('Cannot retrieve upvotabe questions : '+message);
							})
						}
						
					},function (message) {
						console.log('Cannot load page '+page+' of '+vm.activePanel+'questions: '+message);
					});
				}
			}
		}
		
		function searchByWord() {
			loadPage();
		}
		
		function clearSearch() {
			var panel = vm[vm.activePanel];
			if (panel) {
				panel.searchWord = '';
				loadPage();
			}
		}
		
		function canUpvote(qID) {
			if (qID && vm.propQ.canVoteOnQuestions) {
				return vm.propQ.canVoteOnQuestions.indexOf(qID)>=0;
			}
			return false;
		}
		
		function upvote(questionID) {
			if (questionID && vm.propQ.canVoteOnQuestions) {
				var qIdx = vm.propQ.canVoteOnQuestions.indexOf(questionID);
				if (qIdx>=0) {
					vm.propQ.canVoteOnQuestions.splice(qIdx,1);
				} else {
					return;//ignore clicks on un-upvotable questions
				}
				questionsDS.upvote(questionID, function (data) {
					vm.propQ.canVoteOnQuestions = [];
					vm.loadPage();
				},function (message) {
					console.log('Cannot upvote question '+questionID+' :'+message);
				});
			}
		}
		
		//----------------------------------------------//
		
		function addQuestion() {
			vm.crtQuestion = {
				children: {
					translations:[],
					tags:[],
					comments:[]
				}
			};
			refDS.languages(false,function (langs) {
				vm.translationsDropdown = [];
				for (var code in langs) {
					if (langs.hasOwnProperty(code)) {
						vm.translationsDropdown.unshift({
							code: code,
							label: langs[code].label_en,
							completed: false
						});
					}
				}
			});
			
			vm.editingQuestion = true;
		}
		
		function editQuestion(questionId) {
			questionsDS.getQuestionById(questionId,function (question) {
				vm.crtQuestion = question;
				if (vm.crtQuestion.children==null) vm.crtQuestion.children={translations:[],tags:[],comments:[]};
				if (vm.crtQuestion.children.translations==null) vm.crtQuestion.children.translations=[];
				if (vm.crtQuestion.children.tags==null) vm.crtQuestion.children.tags=[];
				if (vm.crtQuestion.children.comments==null) vm.crtQuestion.children.comments=[];
				
				//load default translation
				vm.crtTranslation = {
					lang:'en',
					title: question.title,
					description: question.description,
					html_content: question.html_content,
				};
				$('#questionContent').code(vm.crtTranslation.html_content);
				
				//update dropdown
				if (question.children && question.children.translations) {
					refDS.languages(false,function (langs) {
						vm.translationsDropdown = [];
						for (var code in langs) {
							if (code==='en') {
								vm.translationsDropdown.unshift({
									code: 'en',
									label: 'English',
									completed: question.title && question.description && question.html_content
								});
							} else if (langs.hasOwnProperty(code)) {
								var isCompleted = false;
								for (var i = question.children.translations.length - 1; i >= 0; i--) {
									var tr = question.children.translations[i];
									if (tr.lang===code) {
										isCompleted = true;
									}
								}
							
								vm.translationsDropdown.unshift({
									code: code,
									label: langs[code].label_en,
									completed: isCompleted
								});
							}
						}
					});
				}
				//show the question
				vm.editingQuestion = true;
			}, function (err) {
				console.log('Cannot load question with id='+questionId+' :'+err);
			});
			
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
			vm.loadPage();
			resetCrtQuestion();
			vm.editingQuestion=false;
		}
		
		function resetCrtQuestion() {
			vm.crtQuestion = null;
			vm.crtTranslation = {lang:'en'};
			vm.translationsTab = [];
			vm.translationsDropdown = [];
		}
		
		function onQuestionCannotSave(msg) {
			// cancelEdit();
			console.log('cannot save question: '+msg);
		}
		
		function crtQuestionInalid() {
			if (!vm.crtQuestion) return true;
			if (vm.crtTranslation.lang==='en') {
				if ( (!vm.crtTranslation.title) || (vm.crtTranslation.title.length<5) || 
					(!vm.crtTranslation.description) || (vm.crtTranslation.description.length<20)) return true;
			} else {
				if ( (!vm.crtQuestion.title) || (vm.crtQuestion.title.length<5) || 
					(!vm.crtQuestion.description) || (vm.crtQuestion.description.length<20)) return true;
			}
			return false;
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
			
			//update the dropdown
			for (var i = vm.translationsDropdown.length - 1; i >= 0; i--) {
				var dd = vm.translationsDropdown[i];
				if (dd.code===vm.crtTranslation.lang) {
					if (vm.crtTranslation.title && vm.crtTranslation.title.length>5 && vm.crtTranslation.description && vm.crtTranslation.description.length>10 && vm.crtTranslation.html_content) {
						dd.completed = true;
					} else {
						dd.completed = false;
					}
				}
			}	
				
			
		}
		
		function setTranslation(q,trans,fld) {
			if (q!=null&&trans!=null) {
				
				var isNewTrans = true;
				for (var i = q.children.translations.length - 1; i >= 0; i--) {
					var t = q.children.translations[i];
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
					q.children.translations.push(newTrans);
				}
				
			}
		}
		
		function getTranslation(q,lng,fld) {
			var isNewTrans = true;
			if (q!=null&&lng!=null) {
				if (lng==='en') {
					return q[fld];
				} else {
					for (var i = q.children.translations.length - 1; i >= 0; i--) {
						var t = q.children.translations[i];
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
				//save what we have now in the input fields
				saveCrtTranslation(vm.crtQuestion);
				
				//load the values from the temp. storage
				vm.crtTranslation = {
					lang: lng,
					title: getTranslation(vm.crtQuestion,lng,'title')||'',
					description: getTranslation(vm.crtQuestion,lng,'description')||'',
					html_content: getTranslation(vm.crtQuestion,lng,'html_content')||'',
				};
				$('#questionContent').code(vm.crtTranslation.html_content);
				
				//update the tabs
				if (vm.translationsTab!=null) {
					var tabExists = false;
					for (var i = vm.translationsTab.length - 1; i >= 0; i--) {
						var tab = vm.translationsTab[i];
						if (tab.lang===lng) {
							tabExists = true;
						}
					}
					if ( (!tabExists) && (lng !== 'en') ) {
						if (vm.translationsTab.length>5) {
							vm.translationsTab.shift();
						}
						vm.translationsTab.push({
							lang: lng,
							label: englishForLangCode(lng)
						});
					}
				}
				
			}
		}
		
		
		function publishQuestion() {
			if (vm.crtQuestion) {
				vm.crtQuestion.is_published=true;
				if (!vm.crtQuestion.open_at) {
					vm.crtQuestion.open_at = Date.now();
				}
				saveQuestion();
			}
		}
		
		function archiveQuestion() {
			if (vm.crtQuestion) {
				vm.crtQuestion.is_archived=true;
				vm.crtQuestion.archived_at = Date.now();
				saveQuestion();
			}
		}
		
		function deleteQuestion() {
			if (vm.crtQuestion) {
				vm.crtQuestion.is_deleted=true;
				saveQuestion();
			}
		}

		//----------------------------------------------//

		function switchPanel(panelName) {
			var panel = vm[vm.activePanel];
			vm.activePanel = panelName;
			if (panel) {
				loadPage(panel.crtPage);
			}
		}
		
		function englishForLangCode(code) {
			var langs = refDS.languages();
			if (langs!=null) {
				var lang = langs[code];
				if (lang!=null) {
					return lang.label_en;
				}
			}
			return code;
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