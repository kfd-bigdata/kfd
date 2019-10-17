var mod = [ 'jquery', 'table', 'form', 'layer', 'treetable', 'tool' ];

layui.config({
	base : 'static/modules/'
}).use(mod, function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var tool = layui.tool;
	var treetable = layui.treetable;

	var index = null;

	// 按钮
	var buttons = tool.createButton();
	var headButton = buttons.headButton;
	var lineButton = buttons.lineButton;
	var headButton = tool.createHeadButton(headButton);
//	var lineButton = tool.createLineButton(lineButton);
	
	// 获取数据
	var renderTable = function() {
		treetable.render({
			treeColIndex : 1,
			treeSpid : 0,
			treeIdName : 'departmentId',
			treePidName : 'departmentParentId',
			elem : '#data-table',
			url : 'department/list',
			toolbar : headButton,
			treeDefaultClose : false,
			treeLinkage : false,
			defaultToolbar : [],
			response : {
				statusCode : 200
			},
			cols : [ [{
				field : 'departmentId',
				title : 'ID'
			},  {
				field : 'departmentName',
				title : '部门/岗位名称'
			}, {
				field : 'departmentRemark',
				title : '部门/岗位描述'
			},  {
				field : 'type',
				title : '类型',
				align : 'center'
			}, {
				field : 'sort',
				title : '排序',
			},{
				title : '操作',
				templet : function(d){
					var tempButton = [];
					if(d.type == 0){
						// 岗位
						for(let i=0;i<lineButton.length;i++){
							tempButton.push(lineButton[i]);
						}
						var temp2={};
						temp2.name='关联角色';
						temp2.evenet='departRole';
						temp2.colour='layui-bg-blue';
						
						tempButton.push(temp2);
						return tool.createLineButton(tempButton);
					} else if (d.type == 1){
						// 部门
						for(let i=0;i<lineButton.length;i++){
							tempButton.push(lineButton[i]);
						}
						var temp={};
						temp.name='添加';
						temp.evenet='add';
						temp.colour='layui-bg-green';
						tempButton.push(temp);
						return tool.createLineButton(tempButton);
					}
				}
			} ] ],
			done: function (res, curr, count) {
                $("[data-field='type']").children().each(function () {
                    if ($(this).text() == '0') {
                        $(this).text("岗位");
                    } else if($(this).text()=='1'){
                    	$(this).text("部门");
                    }
                });
            }
			
		});
	};

	// 加载数据
	renderTable();

	// 弹出框
	function openAddWindow(title) {
		index = layer.open({
			type : 1,
			title : title,
			area : '500px',
			maxHeight : 450,
			content : $('#tools-window').html(),
			btn : [ '确定', '取消' ],
			btn1 : function(index, layero) {
				$('#button-submit').click();
			}
		});
	}
	// 弹出框
	function openRoleWindow(title) {
		index = layer.open({
			type : 1,
			title : title,
			area : '500px',
			maxHeight : 450,
			content : $('#tools-dialog').html(),
			btn : [ '确定', '取消' ],
			btn1 : function(index, layero) {
				$('#button-role-submit').click();
			}
		});
	}

	// 行监听按钮
	table.on('tool(data-table)', function(obj) {
		var data = obj.data;
		switch (obj.event) {
		case 'add':
			openAddWindow("添加");
			form.val('add-from', {
				departmentParentId : obj.data.departmentId,
				buttonType : 'add'
			});
			break;
		case 'del':
			layer.confirm('真的删除吗？', {
				title : '提示'
			}, function(index) {
				tool.sendAjax('department/del', 'POST', {
					departmentId : data.departmentId
				}, function(d) {
					if (d == 1) {
						renderTable();
						layer.msg("添加成功");
					} else if (d == 3) {
						layer.msg("该岗位下已绑定用户，不可删除！");
					} else {
						layer.msg("添加失败");
					}
					layer.close(index);
				});
				layer.close(index);
			});
			break;
		case 'update':
			openAddWindow("修改");
			form.val('add-from', {
				departmentId : data.departmentId,
				departmentName : data.departmentName,
				departmentRemark : data.departmentRemark,
				sort : data.sort,
				departmentParentId : data.departmentParentId,
				type : data.type,
				buttonType : 'update'
			});
			break;
		case 'departRole':
			tool.sendAjax('department/getRoleList', 'POST', {
				departmentId : data.departmentId
			}, function(d) {
				openRoleWindow("关联角色");
				var str ='';
				var roleList = d.roleList;
				for (var i=0; i < roleList.length; i++) {
					var role = roleList[i];
					if (role.checked) {
						str += '<input type="checkbox" name="'+ role.roleName +'" title="'+ role.roleName +'" checked id="'+ role.roleId +'">';
					} else {
						str += '<input type="checkbox" name="'+ role.roleName +'" title="'+ role.roleName +'" id="'+ role.roleId +'">';
					}
				}
				$("#chexkboxdiv22").html(str);
				$("#departId").val(data.departmentId);
				form.render("checkbox");
			});
			break;
		}
	});

	// 头按钮监听
	table.on('toolbar(data-table)', function(obj) {
		var data = table.checkStatus(obj.config.id);
		if (obj.event == 'add') {
			openAddWindow("添加");
			form.val('add-from', {
				departmentParentId : 0,
				buttonType : 'add'
			});
		}
	});

	// 提交按钮监听
	form.on('submit(button-submit)', function(data) {
		switch (data.field.buttonType) {
		case 'add':
			tool.sendAjax('department/add', 'POST', data.field, function(d) {
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
			tool.sendAjax('department/update', 'POST', data.field, function(d) {
				if (d == 1) {
					renderTable();
					layer.msg("修改成功");
				} else if (d == 2){
					layer.msg("部门下包含子部门，不能修改为岗位！");
				} else if (d == 3){
					layer.msg("该岗位下已绑定用户，不可更改！");
				} else {
					layer.msg("修改失败");
				}
				layer.close(index);
			});
			break;
		}
		return false;
	});
	
	// 角色框提交按钮监听
	form.on('submit(button-role-submit)', function(data) {
		var checkboxArray = $('input[type=checkbox]:checked')
		var idStrs = new Array();
		for (var i=0; i < checkboxArray.length; i++) {
			idStrs.push(checkboxArray[i].id);
		}
		tool.sendAjax('department/updateDepartRole', 'POST', {
			departmentId : data.field.departId,
			roleIdStrs : idStrs
		}, function(d) {
			if (d) {
				renderTable();
				layer.msg("修改成功");
			} else {
				layer.msg("修改失败");
			}
			layer.close(index);
		});
		return false;
	});

});