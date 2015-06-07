(function() {
	angular.module('util.ref',[])
		.service('refDS',['$q','$http',refDS]);//we want a new instance per client (so we use service instead of factory) so we can maintain state inside it...
		
	function refDS($q,$http) {
		var ds = this;
		ds.cache = {};
		
		return {
			preload: preload,
			languages: getLanguages,
			user: getCurrentUser
		};

		function getLanguages(force) {
			if (ds.cache.langs && !force) {
				return ds.cache.langs;
			}
			var cfg = {};
			$http.get('/platform/ref/langs',cfg)
				.success(function(data, status, headers, config) {
					ds.cache.langs = {};
					for (var i = data.length - 1; i >= 0; i--) {
						var lang = data[i];
						ds.cache.langs[lang.code] = lang;
					}
				}) 
				.error(function(data, status, headers, config) {
					delete ds.cache.langs;
					console.log('cannot retrieve the languages list..' + data);
				});
		}
		
		function getCurrentUser(force) {
			if (ds.cache.crtUser && !force) {
				return ds.cache.crtUser;
			}
			var cfg = {};
			$http.get('/platform/ref/user',cfg)
				.success(function(data, status, headers, config) {
					ds.cache.crtUser = data;
				}) 
				.error(function(data, status, headers, config) {
					delete ds.cache.crtUser;
					console.log('cannot retrieve the current user..' + data);
				});
			
		}
		
		function preload(force) {
			getLanguages(force);
			getCurrentUser(force);
		}

	}

}());