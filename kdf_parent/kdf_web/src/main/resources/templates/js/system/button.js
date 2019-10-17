var mod = [ 'jquery', 'table', 'form', 'layer', 'tool' ];

layui.config({
	base : 'static/modules/'
}).use(mod, function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var tool = layui.tool;

	var index = null;

	// 按钮
	var buttons = tool.createButton();
	var headButton = buttons.headButton;
	var lineButton = buttons.lineButton;
	var headButton = tool.createHeadButton(headButton);
	var lineButton = tool.createLineButton(lineButton);
	
	
	// 获取数据
	var renderTable = function() {
		table.render({
			treeColIndex : 1,
			treeSpid : 0,
			elem : '#data-table',
			url : 'button/list',
			toolbar : headButton,
			page : true,
			defaultToolbar : [],
			response : {
				statusCode : 200
			},
			cols : [ [ 
			{
				field : 'buttonName',
				title : '名称'
			}, {
				field : 'event',
				title : '方法名'
			}, {
				field : 'colour',
				title : '按钮颜色',
			}, {
				field : 'type',
				title : '页面类型',
				width : 100,
				templet : '#tools-type'
			}, {
				title : '操作',
				width : 200,
				toolbar : lineButton
			} ] ]
		});
	};

	// 加载数据
	renderTable();

	// 弹出框
	function openAddWindow(title) {
		index = layer.open({
			type : 1,
			title : title,
			area : ['60%','80%'],
			maxHeight : 600,
			content : $('#tools-window').html(),
			btn : [ '确定', '取消' ],
			btn1 : function(index, layero) {
				$('#button-submit').click();
			}
		});
	}

	// 行监听按钮
	table.on('tool(data-table)', function(obj) {
		var data = obj.data;
		switch (obj.event) {
		case 'del':
			layer.confirm('真的删除吗？', {
				title : '提示'
			}, function(index) {
				tool.sendAjax('button/del', 'POST', {
					buttonId : data.buttonId
				}, function(d) {
					if (d) {
						layer.msg("删除成功");
						renderTable();
					} else {
						layer.msg("删除失败");
					}
				});
				layer.close(index);
			});
			break;
		case 'update':
			openAddWindow("修改");
			form.val("add-from", {
				buttonId : data.buttonId,
				buttonName : data.buttonName,
				event : data.event,
				colour : data.colour,
				type : data.type,
				buttonType : 'update'
			});
			break;
		}
	});

	// 头按钮监听
	table.on('toolbar(data-table)', function(obj) {
		var data = table.checkStatus(obj.config.id);
		if (obj.event == 'add') {
			openAddWindow("添加");
			tool.addDivHideByNames([ 'url' ]);
			form.val('add-from', {
				buttonType : 'add'
			});
		}
	});

	// 提交按钮监听
	form.on('submit(button-submit)', function(data) {
		console.log(data.field)
		switch (data.field.buttonType) {
		case 'add':
			tool.sendAjax('button/add', 'POST', data.field, function(d) {
				if (d) {
					renderTable();
					layer.msg("添加成功");
				} else {
					layer.msg("添加失败");
				}
				layer.close(index);
			});
			break;
		case 'update':
			tool.sendAjax('button/update', 'POST', data.field, function(d) {
				if (d) {
					renderTable();
					layer.msg("修改成功");
				} else {
					layer.msg("修改失败");
				}
				layer.close(index);
			});
			break;
		}
		return false;
	});

});