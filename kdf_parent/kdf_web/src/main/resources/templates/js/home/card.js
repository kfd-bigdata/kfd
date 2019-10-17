layui.config({
	base : 'static/modules/'
}).use([ 'jquery', 'tool' ], function() {
	var $ = layui.jquery;
	var tool = layui.tool;

	tool.sendAjax('home/count', 'GET', {}, function(d) {
		$('#pvCount').html(d.data.pvCount);
		$('#pvAvg').html(d.data.pvAvg);
		$('#pvToday').html(d.data.pvToday);
		$('#pvYesterday').html(d.data.pvYesterday);
		$('#uvCount').html(d.data.uvCount);
		$('#uvAvg').html(d.data.uvAvg);
		$('#uvToday').html(d.data.uvToday);
		$('#uvYesterday').html(d.data.uvYesterday);
	});

});
