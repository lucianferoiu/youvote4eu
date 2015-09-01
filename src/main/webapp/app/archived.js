(function() {
	var root = this;

	$(document).ready(function() {
		$('.logo').click(function() {
			window.location = '/';
		});

		$(window).resize(function() {
			onResize();
		});

		$('.arch-q-summary').click(function() {
			var qid = $(this).attr('data-q-id');
			window.location = App.reqHostname + '/archived/' + qid;
		});

		$('#votesTallyMap').bind('mousewheel', function(e) {
			e.preventDefault();
		});
		$('#votesDistributionMap').bind('mousewheel', function(e) {
			e.preventDefault();
		});

		init();
	});

	function init() {

		onResize();

		$.getJSON("/aq/tally/by/country/" + App.qId, function(data) {
			$('#votesTallyMap').highcharts('Map', {
				title: {
					text: 'Votes tally'
				},
				mapNavigation: {
					enabled: true,
					buttonOptions: {
						verticalAlign: 'bottom'
					}
				},
				colorAxis: {
					min: 0,
					max: 100,
					minColor: '#B39091',
					maxColor: '#7AA2A2'
				},
				series: [{
					data: data,
					mapData: Highcharts.maps['custom/european-union'],
					joinBy: 'hc-key',
					name: '% YES',
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

		$.getJSON("/aq/votes/by/country/" + App.qId, function(data) {
			$('#votesDistributionMap').highcharts('Map', {
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
					minColor: '#FFFDFD',
					maxColor: '#777070'
				},
				series: [{
					data: data,
					mapData: Highcharts.maps['custom/european-union'],
					joinBy: 'hc-key',
					name: 'votes cast',
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

	}

	function onResize() {
		var navBarH = $('.navbar').height();
		$('.nav-buffer').css('height', '' + (navBarH + 20) + 'px');
	}


}());
