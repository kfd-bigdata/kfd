layui.use([ 'jquery', 'element' ], function() {
		var $ = layui.jquery;
		var element = layui.element;

		// 页面加载获取echarts数据
		getBarData("browserBar");
		getBarData("osBar");
		getLineData();
		
		// 获取柱状图数据 分为两种 根据barType区分(browserBar为浏览器uv，osBar为操作系统uv)
		function getBarData(barType) {
			var chart = echarts.init(document.getElementById('browserCount'));
			if (barType == "osBar") {
				chart = echarts.init(document.getElementById('osCount'));
			}
			$.ajax({
				type : "POST",
				url : "/admin/deviceUv/getUvBarData",
				data : {
					"searchDate" : "",
					"barType" : barType
				},
				async : false,
				success : function(d) {
					var xArr = new Array();
					var dataArr = new Array();
					for ( var i in d) {
						// x轴数组
						xArr[i] = d[i].name;
						// 柱状图数据数组
						dataArr[i] = d[i].uvCount;
					}
					var barOption = getBarOption(xArr, dataArr);
					chart.setOption(barOption);
				}
			});
		}
		// 组装柱状图的option
		function getBarOption(xArr, dataArr) {
			var barOption = {
				color : [ '#3398DB' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				xAxis : [ {
					axisLabel : {
						interval : 0,
						rotate : 300
					},
					type : 'category',
					data : xArr,
					axisTick : {
						alignWithLabel : true
					}
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				series : [ {
					name : '直接访问',
					type : 'bar',
					barWidth : '60%',
					data : dataArr
				} ]
			};
			return barOption;
		}
		// 组装折线图的option
		function getLineOption(xArr, pcLineArr, mobileLineArr) {
			var lineOption = {
				title : {
					text : '客户端访问量变化'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : [ 'pc', 'mobile' ]
				},
				// 折线图右上角工具栏
// 				toolbox : {
// 					show : true,
// 					feature : {
// 						dataZoom : {
// 							yAxisIndex : 'none'
// 						},
// 						dataView : {
// 							readOnly : false
// 						},
// 						magicType : {
// 							type : [ 'line', 'bar' ]
// 						},
// 						restore : {},
// 						saveAsImage : {}
// 					}
// 				},
				grid : {
					left : '10%',
					bottom : '30%'
				},
				xAxis : {
					axisLabel : {
						interval : 0,
						rotate : 300
					},
					type : 'category',
					boundaryGap : false,
					data : xArr
				},
				yAxis : {
					type : 'value',
					axisLabel : {
						formatter : '{value}'
					}
				},
				series : [ {
					name : 'pc',
					type : 'line',
					data : pcLineArr,
					markPoint : {
						data : [ {
							type : 'max',
							name : '最大值'
						}, {
							type : 'min',
							name : '最小值'
						} ]
					}
				}, {
					name : 'mobile',
					type : 'line',
					data : mobileLineArr,
					markPoint : {
						data : [ {
							type : 'max',
							name : '最大值'
						}, {
							type : 'min',
							name : '最小值'
						}, [ {
							symbol : 'none',
							x : '90%',
							yAxis : 'max'
						}, {
							symbol : 'circle',
							type : 'max',
							name : '最高点'
						} ] ]
					}
				} ]
			};
			return lineOption;
		}
		// 获取折线图的数据
		function getLineData() {
			var clientTypeCountChart = echarts.init(document
					.getElementById('clientTypeCount'));
			$.ajax({
				type : "POST",
				url : "/admin/deviceUv/getUvLineData",
				data : {
					"searchDate" : ""
				},
				async : false,
				success : function(d) {
					console.log(d);
					var xArr = new Array();
					var pcLineArr = new Array();
					var mobileLineArr = new Array();
					for ( var i in d) {
						xArr[i] = d[i].requestTime;
						pcLineArr[i] = d[i].pcCount;
						mobileLineArr[i] = d[i].mobileCount;
					}
					var lineOption = getLineOption(xArr, pcLineArr, mobileLineArr);
					clientTypeCountChart.setOption(lineOption);
				}
			});
		}
	});