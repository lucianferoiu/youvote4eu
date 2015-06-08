(function() {
	angular.module('util.ref',[])
		.service('refDS',['$q','$http',refDS]);//we want a new instance per client (so we use service instead of factory) so we can maintain state inside it...
		
	function refDS($q,$http) {
		var ds = this;
		ds.cache = {};
		
		return {
			preload: preload,
			languages: getLanguages,
			user: getCurrentUser,
			range: newRange
		};

		function getLanguages(force,fn) {
			if (ds.cache.langs && !force) {
				if (fn) {
					fn(ds.cache.langs);
				}
				return ds.cache.langs;
			} else {
				var cfg = {};
				$http.get('/platform/ref/langs',cfg)
					.success(function(data, status, headers, config) {
						ds.cache.langs = {};
						for (var i = data.length - 1; i >= 0; i--) {
							var lang = data[i];
							ds.cache.langs[lang.code] = lang;
						}
					
						if (fn) {
							fn(data);
						}
					}) 
					.error(function(data, status, headers, config) {
						delete ds.cache.langs;
						console.log('cannot retrieve the languages list..' + data);
					});
			}
		}
		
		function getCurrentUser(force,fn) {
			if (ds.cache.crtUser && !force) {
				if (fn) {
					fn(ds.cache.crtUser);
				}
				return ds.cache.crtUser;
			} else {
				var cfg = {};
				$http.get('/platform/ref/user',cfg)
					.success(function(data, status, headers, config) {
						ds.cache.crtUser = data;
						if (fn) {
							fn(data);
						}
					}) 
					.error(function(data, status, headers, config) {
						delete ds.cache.crtUser;
						console.log('cannot retrieve the current user..' + data);
					});
			}
			
		}
		
		function preload(force) {
			getLanguages(force);
			getCurrentUser(force);
		}
		
		function newRange(n) {
			return new Array(n);
		}

	}

}());