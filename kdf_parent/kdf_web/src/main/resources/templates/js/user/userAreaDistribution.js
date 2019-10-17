layui.config({
	base : 'static/modules/'
}).use([ 'jquery', 'tool' ], function() {
	var $ = layui.jquery, tool = layui.tool;
	var charts = [];
	
	tool.sendAjax('home/mapCount', 'GET', {}, function(d) {
		var chart = echarts.init(document.getElementById('areaDistribution'));
		var option = mapChart(d.data);
		chart.setOption(option);
		charts.push(chart);
	});
	
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