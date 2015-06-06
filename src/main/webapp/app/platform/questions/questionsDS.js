(function() {
	angular.module('app.questions')
		.service('questionsDS',['$q','$http',QuestionsDS]);//we want a new instance per client (so we use service instead of factory) so we can maintain state inside it...
		
	function QuestionsDS($q,$http) {
		return {
			getQuestionById: getQuestionById,
			saveQuestion: saveQuestion
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