(function() {
	angular.module('app.questions',['util.ref'])
		.filter('nbsppad', function() {
			return function(input, n) {
				if(input === undefined)
					input = ""
				if(input.length >= n)
					return input
				var spaces = "nbsp;".repeat(n);
				return (spaces + input).slice(-5 * n)
			};
		});
	
}());