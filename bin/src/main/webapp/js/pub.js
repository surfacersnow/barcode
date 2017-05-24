//兼容ie js start

//Internet Explorer 10 和 Windows Phone 8
// Copyright 2014-2015 Twitter, Inc.
// Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
  var msViewportStyle = document.createElement('style')
  msViewportStyle.appendChild(
	document.createTextNode(
	  '@-ms-viewport{width:auto!important}'
	)
  )
  document.querySelector('head').appendChild(msViewportStyle)
}
//安卓侧边栏空间处理
$(function () {
  var nua = navigator.userAgent
  var isAndroid = (nua.indexOf('Mozilla/5.0') > -1 && nua.indexOf('Android ') > -1 && nua.indexOf('AppleWebKit') > -1 && nua.indexOf('Chrome') === -1)
  if (isAndroid) {
	$('select.form-control').removeClass('form-control').css('width', '100%')
  }
})

//兼容ie js start

//select 控件加载远程数据 start
function bindSelect(elementId,data,selectData,validateFlag,selFirst){
	$("#"+elementId).html('<option value="">&nbsp;</option>');
	var select_remote = $("#"+elementId).select2({
		width:'100%',
		placeholder: $('[for="'+elementId+'"]').html()||"",
		allowClear: true,
		data:function(){
			//ldz 修改向select2中传入data
			var select2_data = [];
			if(data){
				for(var i in data){
					var key = data[i].value;
					var text = data[i].name;
					select2_data.push({
						id:key,
						text:text
					});
				}
			}
			return select2_data;
		}(),
		initSelection: function (element, callback) {   // 初始化时设置默认值
	        var currdata = [];
	        if(selectData){
	        	for(var i in data){
					if(selectData.partner_id == data[i].value){
						currdata.push({id: data[i].value, text: data[i].name});
					}
				}
	        }else{
	        	currdata.push({id: "", text: ""});
	        }
	        callback(currdata);
	    }
	});
	//绑定验证 data-toggle="tooltip" data-placement="right" title=""
	if(validateFlag){
		select_remote.next().attr({
			"data-toggle":"tooltip",
			"data-placement":"right",
			"title":""
		});
		select_remote.on('change',function(){
	    	if(select_remote.val()){
	    		select_remote.parent().removeClass("has-error").addClass("has-success");
	    		select_remote.next().attr("data-original-title", "输入正确");
	    		select_remote.next().find(".select2-selection--single").attr("style","border-color: #3c763d");
	    	}else{
	    		select_remote.parent().removeClass("has-success").addClass("has-error");
	    		select_remote.next().attr("data-original-title", "该字段不能为空");
	    		select_remote.next().find(".select2-selection--single").attr("style","border-color:#a94442");
	    	}
	    });
	}
	if(selFirst){
		select_remote.val(data[0].value).trigger("change");
	}
	return select_remote;
}
//select 控件加载远程数据 end
//grid 内 select start
function bindSelectInGrid(elementId,data,selectData){
	$("#"+elementId).html('<option value="">&nbsp;</option>');
	var select_remote = $("#"+elementId).select2({
		width:'100%',
		placeholder: $('[for="'+elementId+'"]').html(),
		allowClear: true,
		data:function(){
			//ldz 修改向select2中传入data
			var select2_data = [];
			if(data){
				for(var i in data){
					var key = data[i].value;
					var text = data[i].name;
					select2_data.push({
						id:key,
						text:text
					});
				}
			}
			return select2_data;
		}()
	});
	return select_remote;
}
function GridBindSelect(obj,selectIdPre,selectParent,data){
	var element_select_id = selectIdPre+$(obj).attr("id").replace(/\|/g,"_");
	var selectParent = $(obj).find(selectParent)?$(obj).find(selectParent):$(obj);
	var element_select = $('<select id="'+element_select_id+'"></select>');
	var element_select_html = element_select.prop("outerHTML");
	selectParent.html(element_select_html);
	var select_grid = bindSelectInGrid(element_select_id,data,[]);
	if(select_grid){
		$(obj).unbind("click");
	}
	$("#"+element_select_id).change(GridSelected(selectParent[0],obj));
}
//function GridSelected(obj,parentObj){
//	var key = $(obj).val();
//	var text = $(obj).find("option:selected").text();
//	$(obj).parents(parentObj).html(""+text+"<span style='display:none'>"+key+"</span>");
//	
//	$(tdObj)
//}
//grid 内 select end

//将form表单中的字段转换成json格式对象
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {

            o[this.name] = this.value || '';
        }
    });
    return o;
};