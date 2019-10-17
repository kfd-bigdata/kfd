var dataTime = [ "00:00 - 01:00", "01:00 - 02:00", "02:00 - 03:00",
		"03:00 - 04:00", "04:00 - 05:00", "05:00 - 06:00", "06:00 - 07:00",
		"07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00",
		"11:00 - 12:00", "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
		"15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "19:00 - 20:00",
		"20:00 - 21:00", "21:00 - 22:00", "22:00 - 23:00", "23:00 - 00:00" ];

layui.config({
	base : 'static/modules/'
}).use([ 'jquery', 'tool' ], function() {
	var $ = layui.jquery, tool = layui.tool;

	var charts = [];

	tool.sendAjax('home/todayYesterdayContrast', 'GET', {}, function(d) {
		var chart = echarts.init(document.getElementById('peopleCount'));
		var option = lineChart(d.data);
		chart.setOption(option);
		charts.push(chart);
	});

	tool.sendAjax('home/projectCount?type=1', 'GET', {}, function(d) {
		var chart = echarts.init(document.getElementById('projectPv'));
		var option = pieChart(d.data);
		chart.on('updateAxisPointer', function(event) {
			var xAxisInfo = event.axesInfo[0];
			if (xAxisInfo) {
				var dimension = xAxisInfo.value + 1;
				chart.setOption({
					series : {
						id : 'pie',
						label : {
							formatter : '{b}: {@[' + dimension + ']} ({d}%)'
						},
						encode : {
							value : dimension,
							tooltip : dimension
						}
					}
				});
			}
		});
		chart.setOption(option);
		charts.push(chart);
	});

	tool.sendAjax('home/projectCount?type=2', 'GET', {}, function(d) {
		var chart = echarts.init(document.getElementById('projectUv'));
		var option = pieChart(d.data);
		chart.on('updateAxisPointer', function(event) {
			var xAxisInfo = event.axesInfo[0];
			if (xAxisInfo) {
				var dimension = xAxisInfo.value + 1;
				chart.setOption({
					series : {
						id : 'pie',
						label : {
							formatter : '{b}: {@[' + dimension + ']} ({d}%)'
						},
						encode : {
							value : dimension,
							tooltip : dimension
						}
					}
				});
			}
		});
		chart.setOption(option);
		charts.push(chart);
	});

	tool.sendAjax('home/mapCount', 'GET', {}, function(d) {
		var chart = echarts.init(document.getElementById('regionalDistribution'));
		var option = mapChart(d.data);
		chart.setOption(option);
		charts.push(chart);
	});

	function lineChart(data) {
		return {
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : data.time,
				x : 'right'
			},
			xAxis : {
				type : 'category',
				boundaryGap : false,
				data : dataTime
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			yAxis : {
				type : 'value',
				name : '人',
			},
			series : [ {
				type : 'line',
				name : data.time[0],
				data : data.todayData,
				areaStyle : {},
				color : '#417DF5'
			}, {
				type : 'line',
				name : data.time[1],
				data : data.yesterdayData,
				smooth : false,
				itemStyle : {
					normal : {
						lineStyle : {
							width : 3,
							type : 'dotted'
						}
					}
				},
				color : '#9CB7FF'
			} ]
		};
	}

	function pieChart(data) {
		return {
			legend : {},
			tooltip : {
				trigger : 'axis',
				showContent : false
			},
			dataset : {
				source : data
			},
			xAxis : {
				type : 'category',
				boundaryGap : false,
			},
			yAxis : {
				gridIndex : 0
			},
			grid : {
				top : '55%'
			},
			series : [ {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'pie',
				id : 'pie',
				radius : '30%',
				center : [ '50%', '25%' ],
				label : {
					formatter : '{b}: {@' + data[0][1] + '} ({d}%)'
				},
				encode : {
					itemName : 'product',
					value : data[0][1],
					tooltip : data[0][1]
				}
			} ]
		};
	}

	function mapChart(data) {
		return {
			tooltip : {
				show : true,
				formatter : function(params) {
					return params.name + '：' + params.data['value']
				},
			},
			visualMap : {
				type : 'continuous',
				text : [ '', '' ],
				showLabel : true,
				seriesIndex : [ 0 ],
				min : 0,
				max : 7,
				inRange : {
					color : [ '#FFFFFF', '#4979FF' ]
				},
				textStyle : {
					color : '#000'
				},
				bottom : 30,
				left : 'left',
			},
			grid : {
				right : 10,
				top : 80,
				bottom : 30,
				width : '20%'
			},
			yAxis : {
				type : 'category',
				nameGap : 16,
				axisLine : {
					show : false,
					lineStyle : {
						color : '#ddd'
					}
				},
			},
			xAxis : {
				type : 'value',
				scale : true,
				position : 'top',
				splitNumber : 1,
				boundaryGap : false,
				splitLine : {
					show : false
				},
				axisLine : {
					show : false
				},
				axisTick : {
					show : false
				},
				axisLabel : {
					margin : 2,
					textStyle : {
						color : '#aaa'
					}
				}
			},
			geo : {
				roam : false,
				map : 'china',
				left : 'center',
				label : {
					emphasis : {
						show : false
					}
				},
				itemStyle : {
					emphasis : {
						areaColor : '#886FF3'
					}
				}
			},
			series : [ {
				name : 'mapSer',
				type : 'map',
				roam : false,
				geoIndex : 0,
				label : {
					show : false,
				},
				itemStyle : {
					borderColor : '#FFFFFF'
				},
				data : data
			} ]
		};
	}

	window.onresize = function() {
		for ( var i in charts) {
			charts[i].resize();
		}
	};

});