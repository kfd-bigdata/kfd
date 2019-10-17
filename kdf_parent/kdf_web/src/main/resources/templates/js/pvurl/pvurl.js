var mod = [ 'jquery', 'layer', 'tool' ];

layui.config({
    base : 'static/modules/'
}).use(mod, function() {
    var $ = layui.jquery;
    var layer = layui.layer;
    var tool = layui.tool;
    var title = [];
    var data = [];
    tool.sendAjax('pvUrl/getPvUrlList', 'POST', {},function(d) {
        for (var i = 0; i < d.length; i++) {
			title.push(d[i].url);
			data.push(d[i].count);
		}
        var peopleCountChart = echarts.init(document.getElementById('peopleCount'));
        var option = {
        	    tooltip : {
        	        trigger: 'axis',
        	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
        	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        	        }
        	    },
        	    legend: {
        	        data: ['次数']
        	    },
        	    grid: {
        	        left: '3%',
        	        right: '4%',
        	        bottom: '3%',
        	        height:'auto',
        	        containLabel: true
        	    },
        	    xAxis:  {
        	        type: 'value'
        	    },
        	    yAxis: {
        	        type: 'category',
        	        data: title
        	    },
        	    series: [
        	        {
        	            name: '访问次数',
        	            type: 'bar',
        	            stack: '总量',
        	            label: {
        	                normal: {
        	                    show: true,
        	                    position: 'insideRight'
        	                }
        	            },
        	            barWidth:30,
        	            data: data
        	        }
        	    ]
        	};
        peopleCountChart.setOption(option);
    });

    window.onresize = function() {
        peopleCountChart.resize();
    }
});