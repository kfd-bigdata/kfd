var mod = [ 'jquery', 'table', 'form', 'layer','tree', 'util', 'tool'];

layui.config({
	base : 'static/modules/'
}).use(mod, function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var tool = layui.tool;
	var tree = layui.tree;
	var util = layui.util;

	var index = null;
	
	// 按钮
	var buttons = tool.createButton();
	var headButton = buttons.headButton;
	var lineButton = buttons.lineButton;
	var headButton = tool.createHeadButton(headButton);
	var lineButton = tool.createLineButton(lineButton);

	// 初始化数据
	var tableIns = table.render({
		url : 'user/list',
		elem : '#data-table',
		title : '用户列表',
		page : true,
		toolbar : headButton,
		defaultToolbar : [],
		response : {
			statusCode : 200
		},
		cols : [ [ {
			type : 'checkbox'
		}, {
			field : 'userId',
			title : 'ID',
			width : 80
		}, {
			field : 'userName',
			title : '用户名',
		}, {
			field : 'nickname',
			title : '用户昵称',
		}, {
			field : 'phone',
			title : '手机号',
		}, {
			field : 'email',
			title : '联系邮箱',
		}, {
			field : 'departmentNameArr',
			title : '岗位',
		}, {
			title : '操作',
			width : 200,
			toolbar : lineButton 
		} ] ]
	});

	// 行按钮监听
	table.on('tool(data-table)', function(obj) {
		var data = obj.data;
		if (obj.event == 'update') {
			// 打开弹出框
			openAddWindow('修改');
			$("#password").html('');
			// 追加禁用样式
			tool.addInputDisabledByNames([ 'userName','password' ]);
			// 表单赋值
			form.val("add-from", {
				"userId" : data.userId,
				"userName" : data.userName,
				"nickname" : data.nickname,
				"phone" : data.phone,
				"email" : data.email,
				"buttonType" : 'update'
			});
			loadTree(data.userId);
		}
	});

	// 头按钮监听
	table.on('toolbar(data-table)', function(obj) {
		var data = table.checkStatus(obj.config.id);
		switch (obj.event) {
		case 'add':
			openAddWindow('添加');
			form.val("add-from", {
				"buttonType" : 'add'
			});
			loadTree(0);
			break;
		case 'del':
			var ids = [];
			for (var i = 0; i < data.data.length; i++) {
				ids.push(data.data[i].userId);
			}
			// 删除请求
			tool.sendAjax('user/del', 'POST', {
				ids : ids
			}, function(d) {
				if (d) {
					layer.msg("删除成功");
					tableIns.reload();
				} else {
					layer.msg("删除失败");
				}
			});
			break;
		}
	});
	
	/**
	 * 加载树形菜单
	 */
	function loadTree(userId){
		// 树形组件
	  	$.ajax({
			type : 'GET',
			url : 'user/departmentInfo?userId='+userId+'',
			success : function(data) {
				console.log(data.department)
				//树形菜单
				tree.render({
				  elem: '#departmentAndPostName',
				  data: data.department,
				  showCheckbox: true,  //是否显示复选框
				  id: 'department_tree'
			   });
			}
	  	});
	}

	// 弹出框
	function openAddWindow(title) {
		index = layer.open({
			type : 1,
			title : title,
			area : ['80%','80%'],
			offset: '100px',
			content : $('#tools-window').html(),
			btn : [ '确定', '取消' ],
			btn1 : function(index, layero) {
				var checkData = tree.getChecked('department_tree');
				var arr = getIdAll(checkData, new Array());
				$('#departmentIds').val(arr);
				$('#button-submit').click();
			},
			end : function(index, layero) {
				$('#departmentIds').val('');
			}
		});
	}
	

	// 提交按钮监听
	form.on('submit(button-submit)', function(data) {
		switch (data.field.buttonType) {
		case 'add':
			tool.sendAjax('user/add', 'POST', data.field, function(d) {
				if (d) {
					layer.msg("添加成功");
					tableIns.reload();
				} else {
					layer.msg("添加失败");
				}
				layer.close(index);
			});
			break;
		case 'update':
			tool.sendAjax('user/update', 'POST', data.field, function(d) {
				if (d) {
					layer.msg("修改成功");
					tableIns.reload();
				} else {
					layer.msg("修改失败");
				}
				layer.close(index);
			});
			break;
		}
		return false;
	});
	
	// 表单正则
	form.verify({
		username : function(value, item) {
			if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
				return '用户名不能有特殊字符';
			}
			if (/(^\_)|(\__)|(\_+$)/.test(value)) {
				return '用户名首尾不能出现下划线\'_\'';
			}
			if (/^\d+\d+\d$/.test(value)) {
				return '用户名不能全为数字';
			}
		},
		password : function(value, item) {
			if (!new RegExp("^[a-zA-Z0-9_\\s·]+$").test(value)) {
				return '密码只能为字母和数字的组合';
			}
		}
	});
	
	// 获取全部选中ID
	function getIdAll(checkData, arr) {
		for (var i = 0; i < checkData.length; i++) {
			if(checkData[i].type == 0){
				arr.push(checkData[i].id);
			}
			var children = checkData[i].children;
			if (children != undefined) {
				getIdAll(children, arr);
			}
		}
		return arr;
	}
});