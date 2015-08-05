(function() {
	angular.module('app.questions')
		.controller('QuestionsCtrl',['questionsDS','refDS',QuestionsCtrl]);

	function QuestionsCtrl(questionsDS,refDS) {

		//setup view model
		var vm = this;
		vm.crtQActivePanel = null;
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
		vm.crtComment = null;

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
		vm.canEditTranslation = canEditTranslation;
		vm.toggleTranslationLock = toggleTranslationLock;
		vm.closeTranslationsTab = closeTranslationsTab;
		vm.englishForLangCode = englishForLangCode;
		vm.publishQuestion = publishQuestion;
		vm.archiveQuestion = archiveQuestion;
		vm.deleteQuestion = deleteQuestion;
		vm.canUpvote = canUpvote;
		vm.upvote = upvote;
		vm.addComment = addComment;
		vm.deleteComment = deleteComment;
		vm.togglePubAgenda = togglePubAgenda;

		//init
		refDS.preload(true);
		switchPanel('pubQ');
		loadPage(1);
		initTags();
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
						panel.totalPages = Math.max(1,Math.ceil(panel.totalRecords/questionsDS.PAGE_SIZE));
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
			vm.crtComment = null;
			vm.crtTranslation = {lang:'en'};
			$('#officialVoteResults').slider('setValue',0);
			$('#parliamentVoteResults').slider('setValue',0);
			$('#councilVoteResults').slider('setValue',0);

			$('#archivedAtDP').data("DateTimePicker").clear();
			$('#openAtDP').data("DateTimePicker").clear();
			$('#closedAtDP').data("DateTimePicker").clear();
			$('#parliamentVoteDP').data("DateTimePicker").clear();
			$('#councilVoteDP').data("DateTimePicker").clear();
			$('#commissionDecisionDP').data("DateTimePicker").clear();

			$('#tagsSelector').val([]).trigger("change");

			togglePubAgenda();

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
			$('#questionContent').code('');
			vm.crtQActivePanel = 'main';
		}

		function editQuestion(questionId) {
			questionsDS.getQuestionById(questionId,function (question) {

				//set the date pickers
				if (question.archived_at) {$('#archivedAtDP').data("DateTimePicker").date(new Date(question.archived_at))} else {$('#archivedAtDP').data("DateTimePicker").clear()};
				if (question.open_at) {$('#openAtDP').data("DateTimePicker").date(new Date(question.open_at))} else {$('#openAtDP').data("DateTimePicker").clear()};
				if (question.closed_at) {$('#closedAtDP').data("DateTimePicker").date(new Date(question.closed_at))} else {$('#closedAtDP').data("DateTimePicker").clear()};
				if (question.parliament_voted_on) {$('#parliamentVoteDP').data("DateTimePicker").date(new Date(question.parliament_voted_on))} else {$('#parliamentVoteDP').data("DateTimePicker").clear()};
				if (question.council_voted_on) {$('#councilVoteDP').data("DateTimePicker").date(new Date(question.council_voted_on))} else {$('#councilVoteDP').data("DateTimePicker").clear()};
				if (question.commission_decided_on) {$('#commissionDecisionDP').data("DateTimePicker").date(new Date(question.commission_decided_on))} else {$('#commissionDecisionDP').data("DateTimePicker").clear()};

				vm.crtQuestion = question;
				if (vm.crtQuestion.children==null) vm.crtQuestion.children={translations:[],tags:[],comments:[]};
				if (vm.crtQuestion.children.translations==null) vm.crtQuestion.children.translations=[];
				if (vm.crtQuestion.children.tags==null) vm.crtQuestion.children.tags=[];
				if (vm.crtQuestion.children.comments==null) vm.crtQuestion.children.comments=[];

				var selectedTags = [];
				for (var i = vm.crtQuestion.children.tags.length - 1; i >= 0; i--) {
					selectedTags.push(vm.crtQuestion.children.tags[i].id);
				}
				$('#tagsSelector').val(selectedTags).trigger("change");

				vm.crtQuestion.children.comments.sort(function (a,b) {
					return b.id-a.id;
				});

				togglePubAgenda();
				//set the voting sliders
				updateSlider('officialVote','official_vote_tally');
				updateSlider('parliamentVote','parliament_vote_tally');
				updateSlider('councilVote','council_vote_tally');

				//load default translation
				vm.crtTranslation = {
					lang:'en',
					title: question.title,
					description: question.description,
					html_content: question.html_content,
					verified: false
				};
				$('#questionContent').code(vm.crtTranslation.html_content);
				$('.note-editable').attr('contenteditable',vm.canEditTranslation());

				vm.crtComment = null;

				//update dropdown
				if (question.children && question.children.translations) {
					refDS.languages(false,function (langs) {
						vm.translationsDropdown = [];
						for (var code in langs) {
							if (code==='en') {
								vm.translationsDropdown.unshift({
									code: 'en',
									label: 'English',
									verified: vm.crtQuestion.is_archived,
									hasContent: question.title && question.description && question.html_content
								});
							} else if (langs.hasOwnProperty(code)) {
								var isCompleted = false;
								var isVerified = false;
								for (var i = question.children.translations.length - 1; i >= 0; i--) {
									var tr = question.children.translations[i];
									if (tr.lang===code) {
										isCompleted = true;
										isVerified = isVerified || (tr.verified==true)
									}
								}

								vm.translationsDropdown.unshift({
									code: code,
									label: langs[code].label_en,
									verified: vm.crtQuestion.is_archived || isVerified,
									hasContent: isCompleted
								});
							}
						}
					});
				}
				//show the question
				vm.crtQActivePanel='main';
			}, function (err) {
				console.log('Cannot load question with id='+questionId+' :'+err);
			});

		}

		function cancelEdit() {
			vm.crtComment = null;
			vm.crtQActivePanel = null;
			$('#officialVoteResults').slider('setValue',0);
			vm.crtTranslation = {lang:'en'};
			$('#officialVoteResults').slider('setValue',0);
			$('#parliamentVoteResults').slider('setValue',0);
			$('#councilVoteResults').slider('setValue',0);
			$('#archivedAtDP').data("DateTimePicker").clear();
			$('#openAtDP').data("DateTimePicker").clear();
			$('#closedAtDP').data("DateTimePicker").clear();
			$('#parliamentVoteDP').data("DateTimePicker").clear();
			$('#councilVoteDP').data("DateTimePicker").clear();
			$('#commissionDecisionDP').data("DateTimePicker").clear();
			$('#tagsSelector').val([]).trigger("change");
			togglePubAgenda();
			vm.crtQuestion = null;
		}

		function saveQuestion() {
			if (vm.crtQuestion) {
				var q = vm.crtQuestion;
				if (vm.crtTranslation!=null && vm.crtTranslation.lang!==null) {
					saveCrtTranslation(q);
				}
				//sanitize dates
				var m = null;
				m=$('#archivedAtDP').data("DateTimePicker").date();
				q.archived_at=m?m.valueOf():q.archived_at;
				m=$('#closedAtDP').data("DateTimePicker").date();
				q.closed_at=m?m.valueOf():q.closed_at;
				m=$('#openAtDP').data("DateTimePicker").date();
				q.open_at=m?m.valueOf():q.open_at;
				m=$('#parliamentVoteDP').data("DateTimePicker").date();
				q.parliament_voted_on=m?m.valueOf():null;
				m=$('#councilVoteDP').data("DateTimePicker").date();
				q.council_voted_on=m?m.valueOf():null;
				m=$('#commissionDecisionDP').data("DateTimePicker").date();
				q.commission_decided_on=m?m.valueOf():null;

				//update tags
				var selectedTags = $('#tagsSelector').select2('data');
				q.children.tags=[];
				for (var i = selectedTags.length - 1; i >= 0; i--) {
					var tag = selectedTags[i];
					var tagId = parseInt(tag.id);
					if (tagId && tagId>=0) {
						q.children.tags.push({
							id:  tagId,
							text: tag.text
						});
					}
				}

				if (q.is_public_agenda) {
					q.official_vote_tally = saveSlider('officialVote');
					q.parliament_vote_tally = saveSlider('parliamentVote');
					q.council_vote_tally = saveSlider('councilVote');
				}

				questionsDS.saveQuestion(q,onQuestionSaved,onQuestionCannotSave);
			}
		}

		function onQuestionSaved() {
			vm.loadPage();
			resetCrtQuestion();
			vm.crtQActivePanel = null;
			vm.crtComment = null;
		}

		function resetCrtQuestion() {
			vm.crtQuestion = null;
			vm.crtTranslation = {lang:'en'};
			vm.translationsTab = [];
			vm.translationsDropdown = [];
			vm.crtComment = null;
			$('#officialVoteResults').slider('setValue',0);
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
						dd.hasContent = true;
					} else {
						dd.hasContent = false;
					}
					dd.verified = (vm.crtTranslation.verified==true);
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
						t.verified = trans.verified;
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
						verified: trans.verified,
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

		function getTranslationVerified(q,lng) {
			var isTransVerified = true;
			if (q!=null&&lng!=null) {
				if (lng==='en') {
					return false;
				} else {
					for (var i = q.children.translations.length - 1; i >= 0; i--) {
						var t = q.children.translations[i];
						if (t.lang===lng ) {
							isTransVerified = isTransVerified && (t.verified==true);
						}
					}
				}
			}
			return isTransVerified;
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
					verified: getTranslationVerified(vm.crtQuestion,lng)
				};
				$('#questionContent').code(vm.crtTranslation.html_content);
				$('.note-editable').attr('contenteditable',vm.canEditTranslation());

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

		function closeTranslationsTab(lng) {
			if (vm.translationsTab!=null) {
				var pos = -1;
				for (var i = vm.translationsTab.length - 1; i >= 0; i--) {
					var tab = vm.translationsTab[i];
					if (tab.lang===lng) {
						pos = i;
						break;
					}
				}
				if(pos>=0) {
					vm.translationsTab.splice(pos,1);
				}
			}
		}

		function toggleTranslationLock() {
			vm.crtTranslation.verified = !(vm.crtTranslation.verified);
			$('.note-editable').attr('contenteditable',vm.canEditTranslation());
		}

		function canEditTranslation() {
			if (vm.crtQuestion) {
				if (vm.crtQuestion.is_archived) return false;
				if (vm.crtTranslation.verified==true) return false;
			}
			return true;
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

		function initTags() {
			refDS.tags(false,function (data) {
				if (data) {
					var tagSelector = $('#tagsSelector');
					tagSelector.select2({
						placeholder: "Tag question",
						data: data,
						closeOnSelect: false,
						clearOnSelect: true,
						tags: true
					});
					tagSelector.hover(function (e) {
						tagSelector.select2('open');
					},function (e) {
						tagSelector.select2('close');
					})
				}
			});
		}

		function addComment(commType) {
			vm.crtComment = {
				id:null,//new comment
				comment_type: commType,
				text: ''
			};
			vm.crtQuestion.children.comments.unshift(vm.crtComment);
			$('#commentText').delay(500).focus();
		}

		function deleteComment(idx) {
			vm.crtQuestion.children.comments.splice(idx,1);
		}

		function togglePubAgenda() {
			if (vm.crtQuestion.is_public_agenda) {
				$('#officialVoteResults').slider('enable');
				$('#parliamentVoteResults').slider('enable');
				$('#councilVoteResults').slider('enable');
			} else {
				$('#officialVoteResults').slider('disable');
				$('#parliamentVoteResults').slider('disable');
				$('#councilVoteResults').slider('disable');
			}
		}

		//----------------------------------------------//

		function updateSlider(sel,fld) {
			var tally = vm.crtQuestion[fld]>0?Math.floor(vm.crtQuestion[fld] * 100):0;
			$(('#'+sel+'Results')).slider('setValue',tally);
			if (tally>0) {
				$(('#'+sel+'Label')).text(' Yes/No: ' + tally+'/'+ (100-tally)+' %');
			} else {
				$(('#'+sel+'Label')).text('No voting registered');
			}
		}

		function saveSlider(sel) {
			var tally = $(('#'+sel+'Results')).slider('getValue');
			if (tally>0) {
				return new Number((tally / 100).toPrecision(3));
			}
			return null;
		}

	}

}());
