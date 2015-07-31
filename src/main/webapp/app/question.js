(function() {
	var root = this;

	$(document).ready(function() {
		$('.logo').click(function() {
			window.location = '/';
		});

		$(window).resize(function() {
			onResize();
		});
		
		$('#votesDistributionEU').bind('mousewheel', function(e) {
			e.preventDefault();
		});

		init();
	});

	function init() {
		onResize();

		$.getJSON("/q/votes/by/country/"+App.qId, function(data) {
			$('#votesDistributionEU').highcharts('Map', {
				title: {
					text: 'Votes distribution'
				},
				mapNavigation: {
					enabled: true,
					buttonOptions: {
						verticalAlign: 'bottom'
					}
				},
				colorAxis: {
					min: 0,
					minColor: '#F0DDDD',
					maxColor: '#B39091'
				},
				series: [{
					data: data,
					mapData: Highcharts.maps['custom/european-union'],
					joinBy: 'hc-key',
					name: 'votes count',
					states: {
						hover: {
							color: '#6D000D'
						}
					},
					dataLabels: {
						enabled: true,
						format: '{point.name}'
					}
				}]
			});
		});

	}

	function onResize() {
		var navBarH = $('.navbar').height();
		$('.nav-buffer').css('height', '' + (navBarH + 20) + 'px');
	}

}());
