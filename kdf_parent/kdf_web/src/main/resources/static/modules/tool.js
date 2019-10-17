var mod = [ 'jquery' ];

layui.define(mod, function(exports) {
	var $ = layui.jquery;

	var obj = {
		// 追加禁用样式
		addInputDisabledByNames : function(names) {
			for ( var i in names) {
				var name = $('input[name="' + names[i] + '"]');
				name.attr('disabled', 'disabled').addClass('layui-disabled');
			}
		},
		// 追加隐藏
		addDivHideByNames : function(names) {
			for ( var i in names) {
				var name = $('div[name="' + names[i] + '"]');
				name.addClass('layui-hide');
			}
		},
		// 生成头按钮
		createHeadButton : function(json) {
			var body = $('<div></div>');
			body.addClass('layui-btn-container');
			for ( var i in json) {
				var data = json[i];

				var button = $('<button></button>');
				button.addClass('layui-btn layui-btn-sm');
				button.attr('lay-event', data.evenet);
				button.text(data.name);

				var colour = data.colour;
				if (null != data.colour) {
					button.addClass(colour);
				}
				button.appendTo(body);
			}
			return body.prop("outerHTML");
		},
		// 生成行按钮
		createLineButton : function(json) {
			var body = $('<div></div>');
			body.addClass('layui-table-cell laytable-cell-1-0-4');
			for ( var i in json) {
				var data = json[i];

				var a = $('<a></a>');
				a.addClass('layui-btn layui-btn-xs');
				a.attr('lay-event', data.evenet);
				a.text(data.name);

				var colour = data.colour;
				if (null != data.colour) {
					a.addClass(colour);
				}
				a.appendTo(body);
			}
			return body.prop("outerHTML");
		},
		// 发送请求
		sendAjax : function(url, type, data, successFunction, failFunction) {
			$.ajax({
				url : url,
				type : type,
				data : data,
				traditional : true,
				success : function(d) {
					//console.log(d);
					successFunction(d);
				},
				error : function(e) {
					//console.log(e);
					failFunction(e);
				}
			});
		},
		createButton : function (){
			var result={};
			var headButton = [];
			var lineButton = [];
			var menuId = $("#"+location.hash.split('#/')[1])[0].getAttribute('menu-id');
			$.ajax({
				url : "button/getMenuButtons",
				type : 'POST',
				async: false,
				data : {"menuId" : menuId},
				traditional : true,
				success : function(data) {
					for(var i=0; i<data.length ; i++){
						var temp={};
						temp.name=data[i].buttonName;
						temp.evenet=data[i].event;
						temp.colour=data[i].colour;
						if(data[i].type == 0){//头
							headButton.push(temp);
						}
						else if(data[i].type == 1){//行
							lineButton.push(temp);
						}
					}
					result.headButton=headButton;
					result.lineButton=lineButton;
				}
			});
			return result;
		}

		
	};
   
	
	exports('tool', obj);
});