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
	
	// 获取数据
	var renderTable = function() {
		treetable.render({
			treeColIndex : 1,
			treeSpid : 0,
			treeIdName : 'menuId',
			treePidName : 'parentId',
			elem : '#data-table',
			url : 'menu/list',
			toolbar : tool.createHeadButton(headButton),
			treeDefaultClose : false,
			treeLinkage : false,
			defaultToolbar : [],
			response : {
				statusCode : 200
			},
			cols : [ [ {
				field : 'menuId',
				title : 'ID',
				width : 80
			}, {
				field : 'name',
				title : '名称'
			}, {
				field : 'url',
				title : '访问路径'
			}, {
				field : 'sort',
				title : '排序',
				width : 80
			}, {
				field : 'type',
				title : '页面类型',
				width : 100,
				templet : '#tools-type'
			}, {
				title : '操作',
				width : 300,
				templet : function(d){
					var tempButton = [];
					if(d.type == 0){
						var temp={};
						for(let i=0;i<lineButton.length;i++){
							tempButton.push(lineButton[i]);
						}
						temp.name='添加菜单';
						temp.evenet='add';
						temp.colour='layui-bg-cyan';
						tempButton.push(temp);
						return tool.createHeadButton(tempButton);
					}
					else{
						return tool.createHeadButton(lineButton);
					}
				}
			} ] ]
		});
	};

	// 加载数据
	renderTable();

	// 弹出框
	/**
	 * 弹出框
	 * title : 标题
	 * flag	 : 
	 */
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

	// 行监听按钮
	table.on('tool(data-table)', function(obj) {
		var data = obj.data;
		switch (obj.event) {
		case 'add':
			if(data.type != 0){
				layer.msg("当前页面类型不允许添加菜单");
				return false;
			}
			openAddWindow("添加");
			tool.sendAjax('menu/getAllButton', 'POST', {
			}, function(d) {
				var htmlstr = '' 
				for (i=0; i<d.length ; i++) {
					var buttonId =d[i].buttonId;
					htmlstr += '<input type="checkbox" name="checkboxBtn" title="'+d[i].buttonName+'" id="button'+d[i].buttonId+'" value="'+d[i].buttonId+'" />';
				}
				$('#checkboxall').html(htmlstr);
				form.render("checkbox");
			});
			form.val('add-from', {
				parentId : data.menuId,
				type : 1,
				buttonType : 'add'
			});
			break;
		case 'del':
			layer.confirm('真的删除吗？', {
				title : '提示'
			}, function(index) {
				tool.sendAjax('menu/del', 'POST', {
					menuId : data.menuId
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
			if (data.type == 1) {
				tool.sendAjax('menu/getAllButton', 'POST', {
				}, function(d) {
					var htmlstr = '' 
					for (i=0; i<d.length ; i++) {
						var buttonId =d[i].buttonId;
						htmlstr += '<input type="checkbox" name="checkboxBtn" title="'+d[i].buttonName+'" id="button'+d[i].buttonId+'" value="'+d[i].buttonId+'" />';
					}
					$('#checkboxall').html(htmlstr);
					// 回显按钮被选中
					var btnArr = new Array();
					btnArr = data.buttons.split(',');
					for(var i = 0; i < btnArr.length;i++){
					    $('#button'+btnArr[i]).attr("checked",true)
					}
					form.render("checkbox");
				});
			} else {
				$("#aaa").html('');
			}
			form.val("add-from", {
				menuId : data.menuId,
				name : data.name,
				url : data.url,
				sort : data.sort,
				type : data.type,
				parentId : data.parentId,
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
			$('#aaa').attr("class","layui-hide");
			tool.addDivHideByNames([ 'url' ]);
			form.val('add-from', {
				parentId : 0,
				type : 0,
				buttonType : 'add'
			});
		}
	});

	// 提交按钮监听
	form.on('submit(button-submit)', function(data) {
		//获取checkbox[name='checkboxBtn']的值
        var arr = new Array();
        $("input:checkbox[name='checkboxBtn']:checked").each(function(i){
                arr[i] = $(this).val();
        });
        data.field.buttons = arr.join(",");//将数组合并成字符串
		switch (data.field.buttonType) {
		case 'add':
			tool.sendAjax('menu/add', 'POST', data.field, function(d) {
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
			tool.sendAjax('menu/update', 'POST', data.field, function(d) {
				//console.log(d)
				if (d.code == "code-10000") {
					renderTable();
					layer.msg("修改成功");
				} else if (d.code == "code-50014") {
					layer.msg(d.message);
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