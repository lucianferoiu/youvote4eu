(function() {
	angular.module('app.partners')
	.service('partnersDS',['$q','$http',PartnersDS]);//we want a new instance per client (so we use service instead of factory) so we can maintain state inside it...

		function PartnersDS($q,$http) {
			var ds = {
				PAGE_SIZE: 10,
				total:0
			};
			return {
				countPages:countPages,
				countPartners:countPartners,
				getPartners: getPartners,
				getPartnerById: getPartnerById,
				savePartner: savePartner,
				messagePartner: messagePartner,
				banPartner: banPartner
			};
		//----------------------------------------------//


			function getPartners(page,sort,onSuccess,onError) {
				var cfg = {
					params: {
						from: (page-1)*ds.PAGE_SIZE,
						to: (page*ds.PAGE_SIZE)-1,
						sort: sort
					}
				};
				$http.get('/platform/partners/list',cfg)
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


			function countPartners() {
				return Math.max(ds.total,0);
			}

			//----------------------------------------------//

			function getPartnerById(partnerId,onSuccess,onError) {
				var cfg = {
					params: {
						id: partnerId
					}
				};
				$http.get('/platform/partners/edit',cfg)
					.success(function(data, status, headers, config) {
						onSuccess(data);
					})
					.error(function(data, status, headers, config) {
						onError(data);
					});
			}

			function savePartner(partner,onSuccess,onError) {
				var cfg = {};
				$http.post('/platform/partners/save',partner,cfg)
					.success(function(data, status, headers, config) {
						onSuccess(data);
					})
					.error(function(data, status, headers, config) {
						onError(data);
					});
				}

					//----------------------------------------------//

				function messagePartner(partnerId,message,onSuccess,onError) {
					var cfg = {
						params: {
							id: partnerId,
							message: message
						}
					};
					$http.post('/platform/partners/message',partnerId,cfg)
						.success(function(data, status, headers, config) {
							onSuccess(data);
						})
						.error(function(data, status, headers, config) {
							onError(data);
						});
				}

				function banPartner(partnerId,message,onSuccess,onError) {
					var cfg = {
						params: {
							id: partnerId,
							message: message
						}
					};
					$http.post('/platform/partners/ban',partnerId,cfg)
						.success(function(data, status, headers, config) {
							onSuccess(data);
						})
						.error(function(data, status, headers, config) {
							onError(data);
						});
				}

		}
}());
