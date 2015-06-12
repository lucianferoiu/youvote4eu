(function() {
	angular.module('app.questions')
		.service('questionsDS',['$q','$http',QuestionsDS]);//we want a new instance per client (so we use service instead of factory) so we can maintain state inside it...
		
	function QuestionsDS($q,$http) {
		
		var ds = {
			PAGE_SIZE: 10,
			questionListURLs: {
				'archQ': '/platform/questions/archived',
				'pubQ': '/platform/questions/published',
				'propQ': '/platform/questions/proposed',
				'myQ': '/platform/questions/mine'
			}
		};
		
		return {
			PAGE_SIZE: ds.PAGE_SIZE,
			getQuestions: getQuestions,
			getQuestionById: getQuestionById,
			saveQuestion: saveQuestion,
			getUpvotableQuestions: getUpvotableQuestions,
			upvote: upvote
		};

		function getQuestionById(questionId,onSuccess,onError) {
			var cfg = {
				params: {
					id: questionId
				}
			};
			$http.get('/platform/questions/edit',cfg)
				.success(function(data, status, headers, config) {
					onSuccess(data);
				}) 
				.error(function(data, status, headers, config) {
					onError(data);
				});
		}
		
		
		function saveQuestion(question,onSuccess,onError) {
			var cfg = {};
			$http.post('/platform/questions/save',question,cfg)
				.success(function(data, status, headers, config) {
					onSuccess(data);
				}) 
				.error(function(data, status, headers, config) {
					onError(data);
				});
		}
		
		function getQuestions(page,questionsType,additionalParams,onSuccess,onError) {
			var cfg = {
				params: {
					from: (page-1)*ds.PAGE_SIZE,
					to: (page*ds.PAGE_SIZE)-1
				}
			};
			if (additionalParams) {
				cfg.params = angular.extend(cfg.params,additionalParams);
			}
			
			var url = ds.questionListURLs[questionsType];
			if (url) {
				$http.get(url,cfg)
					.success(function(data, status, headers, config) {
						onSuccess(data);
					}) 
					.error(function(data, status, headers, config) {
						onError(data);
					});
			} 
		}
		
		function getUpvotableQuestions(qIDs,onSuccess,onError) {
			var cfg = {
				params: {
					questionIDs: qIDs
				}
			};
			$http.get('/platform/questions/can_upvote',cfg)
				.success(function(data, status, headers, config) {
					onSuccess(data);
				}) 
				.error(function(data, status, headers, config) {
					onError(data);
				});
		}
		
		function upvote(questionID,onSuccess,onError) {
			var cfg = {};
			$http.put('/platform/questions/upvote',questionID,cfg)
				.success(function(data, status, headers, config) {
					onSuccess(data);
				}) 
				.error(function(data, status, headers, config) {
					onError(data);
				});
		}
		

/*************************************************************************************			
			var ds = {
				PAGE_SIZE: 10,
				total:0
			};
			return {
				countPages:countPages,
				countQuestions:countQuestions,
				getQuestions: getQuestions,
				getQuestionById: getQuestionById,
				saveQuestion: saveQuestion
				
			};
		//----------------------------------------------//
			
			function getQuestions(page,sort,onSuccess,onError) {
				var cfg = {
					params: {
						from: (page-1)*ds.PAGE_SIZE,
						to: (page*ds.PAGE_SIZE)-1,
						sort: sort
					}
				};
				$http.get('/platform/questions/list',cfg)
					.success(function(data, status, headers, config) {
						ds.total = data.total;
						onSuccess(data.results);
					}) 
					.error(function(data, status, headers, config) {
						onError(data);
					}); 
			}

			function countPages() {
				return Math.ceil(ds.total/ds.PAGE_SIZE);
			}
			
			function countQuestions() {
				return Math.max(ds.total,0);
			}
			
			//----------------------------------------------//

			function getQuestionById(questionId,onSuccess,onError) {
				var cfg = {
					params: {
						id: questionId
					}
				};
				$http.get('/platform/questions/edit',cfg)
					.success(function(data, status, headers, config) {
						onSuccess(data);
					}) 
					.error(function(data, status, headers, config) {
						onError(data);
					});
			}
			
			
			function saveQuestion(question,onSuccess,onError) {
				var cfg = {};
				$http.post('/platform/questions/save',question,cfg)
					.success(function(data, status, headers, config) {
						onSuccess(data);
					}) 
					.error(function(data, status, headers, config) {
						onError(data);
					});
				}
			
			
*************************************************************************************/

	}

}());