var mod = [ 'jquery', 'table', 'form', 'layer', 'tree', 'tool' ];

layui.config({
	base : 'static/modules/'
}).use(mod, function() {
	var $ = layui.jquery;
	var tree = layui.tree;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var tool = layui.tool;
	var index = null;
	var menuId;
	// 按钮
	var buttons = tool.createButton();
	var headButton = buttons.headButton;
	var lineButton = buttons.lineButton;
	var headButton = tool.createHeadButton(headButton);
	var lineButton = tool.createLineButton(lineButton);
	

	/**
	 * 初始化数据
	 */
	var tableIns = table.render({
		url : 'role/list',
		elem : '#data-table',
		title : '角色列表',
		page : true,
		toolbar : headButton,
		defaultToolbar : [],
		response : {
			statusCode : 200
		},
		cols : [ [ {
			type : 'checkbox'
		}, {
			field : 'roleId',
			title : 'ID',
			width : 80
		}, {
			field : 'roleName',
			title : '角色名称',
		}, {
			field : 'roleDescribe',
			title : '描述',
		}, {
			title : '操作',
			width : 240,
			toolbar : lineButton
		} ] ]
	});

	// 弹出框
	function openAddWindow(title) {
		index = layer.open({
			type : 1,
			area : '500px',
			title : title,
			content : $('#tools-window').html(),
			btn : [ '确定', '取消' ],
			btn1 : function(index, layero) {
				$('#button-submit').click();
			},
		});
	}

	// 行按钮监听
	table.on('tool(data-table)', function(obj) {
		var data = obj.data;
		switch (obj.event) {
		case 'del': 
			layer.confirm('真的删除吗？', {
				title : '提示'
			}, function(index) {
				tool.sendAjax('role/del', 'POST', {
					roleId : data.roleId
				}, function(d) {
					if (d) {
						layer.msg("删除成功");
					} else {
						layer.msg("删除失败");
					}
					tableIns.reload();
				});
				layer.close(index);
			});
			break;
		case 'associated-menu':
			tool.sendAjax('menu/tree', 'POST', {
				roleId : data.roleId
			}, function(d) {
				openTreeWindow(d.roleId);
				tree.render({
					id : 'menu-tree',
					elem : '#menu-tree',
					showCheckbox : true,
					data : d.menu
				});
			});
			break;
		case 'associated-button': 
			tool.sendAjax('menu/buttonTree', 'POST', {
				roleId : data.roleId
			}, function(d) {
				openButtonTreeWindow(d.roleId);
				tree.render({
					id : 'menu-tree',
					elem : '#menu-tree',
					showCheckbox : true,
					data : d.menu,
					click: function(obj){
						$("#buttons").html("");
					      var temp = obj.data;  //获取当前点击的节点数据
					      menuId=temp.id;
					      tool.sendAjax('role/getButtonsByMenuId', 'POST', {menuId : menuId, roleId : data.roleId}, function(d) {
					    	  var str="";
					    	  for(var i=0; i< d.length ; i++){
					    		if(d[i].checkedStatus){
					    			str+='<input type="checkbox" name="button" value="'+d[i].buttonId+'" lay-skin="primary" title="'+d[i].buttonName+'" checked="">';
					    		}
					    		else{
					    			str+='<input type="checkbox" name="button" value="'+d[i].buttonId+'" lay-skin="primary" title="'+d[i].buttonName+'">';
					    		}
					    	  }
					    	  $("#buttons").html(str);
						      form.render("checkbox");
						});
					     
					    }
				});
			});
		}
	});

	// 头按钮监听
	table.on('toolbar(data-table)', function(obj) {
		var data = table.checkStatus(obj.config.id);
		switch (obj.event) {
		case 'add':
			openAddWindow('添加');
			break;
		}
	});

	// 提交按钮监听
	form.on('submit(button-submit)', function(data) {
		tool.sendAjax('role/add', 'POST', data.field, function(d) {
			if (d) {
				layer.msg("添加成功");
				tableIns.reload();
			} else {
				layer.msg("添加失败");
			}
			layer.close(index);
		});
		return false;
	});

	// 树形弹出框
	function openTreeWindow(roleId) {
		index = layer.open({
			type : 1,
			content : $('#tools-tree-window').html(),
			area : [ '500px', '500px' ],
			title : '关联菜单',
			btn : [ '确定', '取消' ],
			btn1 : function(index, layero) {
				var checkData = tree.getChecked('menu-tree');
				var arr = getIdAll(checkData, new Array());
				console.log(arr);
				tool.sendAjax('menu/addRoleMenu', 'POST', {
					ids : arr,
					roleId : roleId
				}, function(d) {
					if (d) {
						layer.msg("修改成功");
					} else {
						layer.msg("修改失败");
					}
					layer.close(index);
				});
			}
		});
	}
	
	// 按钮树形弹出框
	function openButtonTreeWindow(roleId) {
		index = layer.open({
			type : 1,
			content : $('#tools-tree-button-window').html(),
			area : [ '500px', '500px' ],
			title : '关联按钮',
			btn : [ '保存当前菜单按钮权限', '取消' ],
			btn1 : function(index, layero) {
				var arr = new Array();
		        $("input:checkbox[name='button']:checked").each(function(i){
		                arr[i] = $(this).val();
		        });
				tool.sendAjax('menu/addRoleMenuButtons', 'POST', {
					ids : arr,
					roleId : roleId,
					menuId : menuId
				}, function(d) {
					if (d) {
						layer.msg("修改成功");
					} else {
						layer.msg("修改失败");
					}
				});
			}
		});
		form.render("checkbox");
	}

	// 获取全部选中ID
	function getIdAll(checkData, arr) {
		for (var i = 0; i < checkData.length; i++) {
			arr.push(checkData[i].id);
			var children = checkData[i].children;
			if (children != undefined) {
				getIdAll(children, arr);
			}
		}
		return arr;
	}

});