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
				
				var zeros = '';
				for (var i = n-len; i >= 0; i--) {
					zeros = zeros+padder;
				}
				return (zeros + input);
			};
		})
		.directive('ngEnter', function () {
			return function (scope, element, attrs) {
				element.bind("keydown keypress", function (event) {
					if(event.which === 13) {
						scope.$apply(function (){
							scope.$eval(attrs.ngEnter);
						});
						event.preventDefault();
					}
				});
			};
		});
	
}());