var mod = [ 'jquery', 'layer', 'tool' ];

layui.config({
    base : 'static/modules/'
}).use(mod, function() {
    var $ = layui.jquery;
    var layer = layui.layer;
    var tool = layui.tool;
    var title = [];
    var data = [];
    tool.sendAjax('pvUrl/getPvUrlTime', 'POST', {},function(d) {
        console.log(d);
        for (var i = 0; i < d.length; i++) {
			title.push(d[i].time);
			data.push(d[i].count);
		}
        var peopleCountChart = echarts.init(document.getElementById('peopleCount'));
        var option = {
        	    xAxis: {
        	        type: 'category',
        	        data: title
        	    },
        	    yAxis: {
        	        type: 'value'
        	    },
        	    series: [{
        	        data: data,
        	        type: 'line'
        	    }]
        	};

        peopleCountChart.setOption(option);
    });

    window.onresize = function() {
        peopleCountChart.resize();
    }
});