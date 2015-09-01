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

		$('#tallyPieChart').highcharts({
			chart: {
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false,
				type: 'pie'
			},
			colors: ['#7AA2A2','#B39091'],
			title: {
				text: 'Tally of the citizens vote'
			},
			plotOptions: {
				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					dataLabels: {
						enabled: false
					},
					showInLegend: true
				}
			},
			series: [{
				name: "Votes %",
				colorByPoint: true,
				data: [{
					name: "YES",
					y: Math.floor(App.voteTally * 100),
					sliced: true,
					selected: true
				}, {
					name: "NO",
					y: Math.floor((1 - App.voteTally) * 100)
				}]
			}]
		});

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
