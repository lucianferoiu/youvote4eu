(function() {
	angular.module('app.questions',['util.ref'])
		.filter('pad', function() {
			return function(input, n, padder) {
				if(input === undefined) {
					input = "";
				}
				if(input.length >= n) {
					return input
				}
				if (!padder) {
					padder = '0';
				}
				var inputAsStr = ''+input;
				var len = inputAsStr.length;
				var zeros = padder.repeat(n - len);
				return (zeros + input);
			};
		});
	
}());