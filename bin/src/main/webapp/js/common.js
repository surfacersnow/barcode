/**
 * 请求参数设置
 * @param type 数据操作类型， a
 * @param entityId 数据操作 DML实体标识
 * @param dataS   传递的数据参数
 * @param rtnType l-list 线性结构，默认；t-tree 树型结构
 * @param isPage  是否分页
 * @param currentPage 当前页
 * @param pageSize  分页大小
 * @returns {___anonymous117_119}
 */
function reqObj(type, entityId, dataS, rtnType, isPage, currentPage, pageSize) {
	var obj = new Object();
	
	obj.t = type;//操作类型
	
	obj.en = ''; // DML实体名称
	if (entityId)
		obj.en = entityId;
	
	obj.rt = 'l';  // l-list 线性结构，默认；t-tree 树型结构
	if (rtnType)
		obj.rt = rtnType;
	
	obj.p = false;   // 是否分页
	if (isPage)
		obj.p = isPage;
	
	obj.cp = 1;    // 当前页码
	if (currentPage)
		obj.cp = currentPage;
	
	ps = 20;   // 每页记录数
	if (pageSize)
		obj.ps = pageSize;
	
	obj.data = "{}";  // 传递的参数
	if (dataS) {
		obj.data = dataS;
	}
	
	return obj;
}

function reqJson(sqlid, dataS) {
    var obj = new Object();
    obj.sql = ''; // DML实体名称
    if (sqlid)
        obj.sql = sqlid;

    obj.rt = 'l';  // l-list 线性结构，默认；t-tree 树型结构
    obj.data = "{}";  // 传递的参数
    if (dataS) {
        obj.data = dataS;
    }

    return obj;
}
/**
 * 请求参数设置，为主从数据处理
 * @param masterData   主表数据
 * @param slaveData   从表数据
 * @returns {___anonymous633_635}
 */
function reqObjForMSData(masterData,slaveData) {
	var obj = new Object();
	
	obj.data = "{}";  // 传递的主表参数
	if (masterData) {
		obj.data = masterData;
	}
	
	obj.data_s = "{}";  // 传递的从表参数
	if (slaveData) {
		obj.data_s = slaveData;
	}	
	
	return obj;
}
/**
 * 请求参数处理，为流程处理
 * 
 * @param data_m 主表数据
 * @param data_s 从表数据
 * @returns {___anonymous1422_1424}
 */
function reqObjForFlow(data_m, data_s) {
	var obj = new Object();
	obj=reqObjForMSData(data_m,data_s);
	return obj;
}
/**
 * Ajax提交后台操作
 * 
 * @param url string 请求地址，必需
 * @param data string 向服务器提交的数据，json格式或key=value（多个条件“&”连接）格式
 * @param onSuccess Function 提交返回成功后执行
 * @param onError Function 提交发生异常执行
 * @param async boolean 异步标志，默认true
 * 
 * @returns onSuccess及onError函数的执行结果
 */
function ajaxSubmit(url, data, onSuccess, onError, async) {
	if (async === undefined || async === null)
		async = true;
	var r;
	$.ajax({
		url: url,
		type: "POST",
		async:async,
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: data,
		success: function(data) {
			if (data.status) {
				if (data.status != '1') {
					var msg = data['msg'];
					if (!msg)
						msg = "您的登录状态已失效，请重新登录。";
					alert(msg);
					if (data.status == '0')
						parent.location = "/";
					return;
				}
			}
			
			if (onSuccess)
				r = onSuccess(data);
		},
		error: function(data) {
			if (onError)
				r = onError(data);
			else
				alert("[严重错误] 向服务器提交请求失败！");
				alert(JSON.stringify(data));
		}
	});
	return r;
}
function ajaxSubmitGet(url, onSuccess, onError, async) {
	if (async === undefined || async === null)
		async = true;
	var r;
	$.ajax({
		url: url,
		type: "GET",
		async:async,
		dataType: "json",
		contentType: "application/json; charset=utf-8", 
		success: function(data) {
			if (data.status) {
				if (data.status != '1') {
					var msg = data['msg'];
					if (!msg)
						msg = "您的登录状态已失效，请重新登录。";
					alert(msg);
					
					if (data.status == '0')
						parent.location = "/";
					return;
				}
			}
			
			if (onSuccess)
				r = onSuccess(data);
		},
		error: function(data) {
			if (onError)
				r = onError(data);
			else
				alert("[严重错误] 向服务器提交请求失败！");
				alert(JSON.stringify(data));
		}
	});
	return r;
}
/**
 * 重写Get请求，同步操作
 * @param url
 * @returns {*}
 */
function syncGetRequest(url,onSuccess) {
    var returnData;
    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        async: true,
        contentType: 'application/json; charset=utf-8',
        dataType: "json",
        success: function(data) {
			if (data.status) {
				if (data.status != '1') {
					var msg = data['msg'];
					if (!msg)
						msg = "您的登录状态已失效，请重新登录。";
					alert(msg);
					
					if (data.status == '0')
						parent.location = "/";
					return;
				}
			}
			
			if (onSuccess)
				returnData = onSuccess(data);
//            returnData=data;
        },
        error: function(data){
//			if (onError)
//				returnData = onError(data);
//			else
//				alert("[严重错误] 向服务器提交请求失败！");
//				alert(JSON.stringify(data));
            returnData = {
                status: 0,
                data: "服务器内部异常"
            };
        }
    });
    return returnData;
}
/**
 * 清空输入
 * 使用jQuery选择符指定要清空的页面组件
 * 
 * @param selector String jQuery选择符
 * 
 * @returns 无
 */
function toReset(selector) {
	if (!selector) {
		selector = "input,select,textarea";
	}
	$(selector).each(function(i){
		if ($(this).attr("type") !== "button")
			$(this).val("");
	});
}
/** 参数缓存 */
var _dicts = {};
/**
 * 由传入的参数代码获取参数配置信息
 * 
 * @param String paramName 参数代码
 * @param Object query 查询条件对象，可选
 * @param boolean needCache 是否缓存该参数，默认true
 * 
 * @returns obj 参数配置信息
 */
function getParam(paramName, query, needCache) {
	if (!paramName)
		return {};

	var ca = true;
	if (needCache !== undefined)
		ca = needCache;
	
	if (ca) {
		var pn = paramName;
		pn = pn.replace(/\./, "_");
		var cache = eval("_dicts['" + pn + "']");
		if (cache)
			return cache;
	}
	
	var uri = "../../dict/cachedict";

	if (!query) {
		query = new Object();
	}
	query.d = paramName;
	
	var param = ajaxSubmit(uri, JSON.stringify(query), function(data) {
		if (!data) return {};
		return toTree(data);
	}, null, false);
	
	if (ca)
		eval("_dicts." + pn + " = param");
	return param;
}
/**
 * 将参数值转换为对应的名称
 * 
 * @param item obj UI组件对象
 * @param paramName String 参数定义代码
 * @param v String 值
 * @param Object query 查询条件对象，可选
 * @param boolean needCache 是否缓存该参数，默认true
 * 
 * @returns String 名称
 */
function paramRender(item, paramName, v, query, needCache) {
	if (!paramName || !v)
		return "";
	
	var param = getParam(paramName, query, needCache);
	if (param[v])
		return param[v]["name"];
	else
		return "";
}
/**
 * 获取参数字典信息，提供给Select组件使用
 * @param paramName String 参数名称
 * @param Object query 查询条件对象，可选
 * @param boolean needCache 是否缓存该参数，默认true
 * 
 * @returns {Array} 字典数据项数组，根据参数的seq升序排列
 */
function getData(paramName, query, needCache) {
	if (paramName) {
		var param = getParam(paramName, query, needCache);
		if (param) {
			var p = new Array();
			var item;
			for (var prop in param) {
				item = param[prop];
				p.push(fillZero(item.seq) + ":" + JSON.stringify(param[prop]));
			}
			p.sort();
			for (var i = 0; i < p.length; i++) {
				p[i] = $.parseJSON(p[i].replace(/^[^:]+:/, ''));
			}
			return p;
		}
	}
	return [];
};
var zeros = '00000000';
/**
 * 将数字前补0
 * @param seq String 顺序号
 * @returns 
 */
function fillZero(seq) {
	if (!seq)
		seq = "1";
	if (typeof seq === 'Number')
		seq = seq.toString();
	return zeros.substring(1, zeros.length - seq.length) + seq;
}

/**
 * 对所有定义了param属性的select元素进行option填充<br/>
 * [param]属性定义了对应的字典名称，根据该名称可以获取其字典配置<br/>
 * [first]属性定义了select的第一行内容<br/>
 * [default]属性定义了默认选中的项
 */
function selectInjector() {
	var sel, fst, paramName, param, opt, selected;
	$("select").each(function(i) {
		sel = $(this);
		opt = "";
		paramName = sel.attr("param");
		if (paramName) {
			param = getData(paramName);
			if (param) {
				selected = sel.attr("default");
				fst = sel.attr("first");
				if (fst !== undefined) {
					opt = "<option value=''>" + fst + "</option>";
				}
				for (var i = 0; i < param.length; i++) {
					if (param[i].status !== "1")
						continue;
				
					opt += "<option value='" + param[i].value + "'";
					if (param[i].value == selected) {
						opt += " selected";
						
					}
					opt += ">" + param[i].name + "</option>";
				}
			}
			sel.html(opt);
		}
	});
}
/**
 * 将查询结果线性Array对象转换为Tree型
 * 
 * @param data array 线性列表
 */
function toTree(data) {
	if (data['tree']) {
		var t = data['tree'];
		
	}
	return data['param'];
}

/**
 * 向Grid组件中的Toolbar中添加按钮
 * 
 * @param divStyle String 按钮的上层Div元素的样式，该样式主要用于区别不同的按钮区域，可以不在style中定义
 * @param btnStyle String 添加的按钮的样式
 * @param action Function 按钮的onclick事件
 * @param title String 鼠标指向该按钮时的提示信息
 */
function appendBtnForGridToolbar(divStyle, btnStyle, action, title,btnName,containerId) {
	if (!divStyle && !btnStyle)
		return;
	var showBtNameStr='';
	if(btnName)
		showBtNameStr='<div style="color: #2C4D79;width:100px;height: 22px;line-height:22px;text-align:center;"><span>' + btnName	+ '</span></div>';
	
	var endTag = ".l-panel-bar .l-panel-bbar-inner .l-clear";
	if (containerId)
		endTag = containerId + " " + endTag;
	$(endTag).before('<div class="l-bar-group"><div class="l-bar-button '
			+ divStyle + '"><span class="' + btnStyle + '" title="' + title
			+ '"></span></div>'+showBtNameStr+'</div><div class="l-bar-separator"></div>');
	var s = "div.l-bar-button";
	if (divStyle)
		s = s.concat(".").concat(divStyle);
	$(s).hover(function() {
		$(this).addClass('l-bar-button-over');
	},function() {
		$(this).removeClass('l-bar-button-over');
	});
	
	if (btnStyle)
		s = s.concat(" span.").concat(btnStyle);
	$(s).click(action);
}
/**
 * 向Grid组件中的Toolbar中添加“新增”按钮
 * 
 * @param action Function 按钮的onclick事件
 * @param title String 鼠标指向该按钮时的提示信息
 */
function appendAddBtn(action, title,btnName,buttonId,containerId) {
	if (!title)
		title = "新增";
	if(!buttonId)
		buttonId = "l-bar-btnadd";
	else
		buttonId = "l-bar-btnadd-"+buttonId;
	appendBtnForGridToolbar(buttonId, "l-icon-add", action, title,btnName,containerId);
}
/**
 * 向Grid组件中的Toolbar中添加“修改”按钮
 * 
 * @param action Function 按钮的onclick事件
 * @param title String 鼠标指向该按钮时的提示信息
 */
function appendModifyBtn(action, title,containerId) {
	if (!title)
		title = "修改";
	appendBtnForGridToolbar("l-bar-btnedit", "l-icon-modify", action, title,null, containerId);
}
/**
 * 向Grid组件中的Toolbar中添加“查询”按钮
 * 
 * @param action Function 按钮的onclick事件
 * @param title String 鼠标指向该按钮时的提示信息
 */
function appendSearchBtn(action, title) {
	if (!title)
		title = "查询";
	appendBtnForGridToolbar("l-bar-btnsearch", "l-icon-search", action, title);
}
/**
 * 向Grid组件中的Toolbar中添加“删除”按钮
 * 
 * @param action Function 按钮的onclick事件
 * @param title String 鼠标指向该按钮时的提示信息
 */
function appendDeleteBtn(action, title,containerId) {
	if (!title)
		title = "删除";
	appendBtnForGridToolbar("l-bar-btndel", "l-icon-delete", action, title, null, containerId);
}
/**
 * 向Grid组件中的Toolbar中添加“管理”按钮
 * 
 * @param action Function 按钮的onclick事件
 * @param title String 鼠标指向该按钮时的提示信息
 */
function appendManageBtn(action, title) {
	if (!title)
		title = "维护";
	appendBtnForGridToolbar("l-bar-btnman", "l-icon-config", action, title);
}
/**
 * 解析URL中的Query String部分传递的参数
 * 
 * @param url 请求url
 * @returns {Object} 将query string中的参数封装为对象
 */
function getQueryString(url) {
	var qs = url.replace(/^[^?]+\??/, '');
	var queryObj = new Object();
	if (qs.length === 0)
		return queryObj;
	
	var qsa = qs.split('&');
	for (var i = 0; i < qsa.length; i++) {
		if (qsa[i].match(/[^=]+=[^=]+/))
			eval('queryObj.'.concat(qsa[i].replace('=', '="')).concat('"'));
		else if (qsa[i].match(/[^=]+=/))
			eval('queryObj.'.concat(qsa[i]).concat('""'));
	}
	return queryObj;
}
$(function() {
	selectInjector();
});
function Contrast(rowid)
{
	if(rowid.state.substr(0,1)=='0' && rowid.flow_state.substr(0,1)=='0')
		{
	return "ok";
		}
	else if(rowid.state.substr(0,1)==rowid.flow_state.substr(0,1))
		{
		return "false";
		
		}
	
}
//必输数据项校验控制
function validateCon()
{
	// 必输数据项校验控制
	$('*[id$="S"],*[id$="S_txt"]').each(function(index, element) {
		if (!$(this).rules()) return;
		if (!$(element).valid()) {
			valid = false;
			return;
		}
	});
	
	if (!valid) return;
}
//获取今天日期，输出格式为YYYY-MM-DD
function getToDay(){    
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = now.getMonth();
    var nowDate = now.getDate();
    nowMonth = doHandleMonth(nowMonth + 1);
    nowDate = doHandleMonth(nowDate);
    return nowYear+"-"+nowMonth+"-"+nowDate;
}

//修改日期格式填充零
function doHandleMonth(month){
    if(month.toString().length == 1){
     month = "0" + month;
    }
    return month;
}

/**
 * 修改日期格式填充零
 * @param date  被修改的时间数据
 * @param dateformat  修改的格式
 * @returns   修改后的时间格式
 */
function getFormatDate(date, dateformat)
{
    var g = this, p = this.options;
    if (isNaN(date)) return null;
    var format = dateformat;
    var o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
        "q+": Math.floor((date.getMonth() + 3) / 3),
        "S": date.getMilliseconds()
    };
    if (/(y+)/.test(format))
    {
        format = format.replace(RegExp.$1, (date.getFullYear() + "")
    .substr(4 - RegExp.$1.length));
    }
    for (var k in o)
    {
        if (new RegExp("(" + k + ")").test(format))
        {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
        : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
/**
 * 计算两个日期之间的天数之差
 * @param beginDate  开始日期 YYYY-MM-DD
 * @param endDate  结束日期 YYYY-MM-DD
 * @returns {Number}  日期之差
 */
function getdiffDate(beginDate,endDate){
	var strDateArrayStart = beginDate.split("-");
	var strDateArrayEnd = endDate.split("-");
	var strDateS = new Date(strDateArrayStart[0] + "/" + strDateArrayStart[1] + "/" + strDateArrayStart[2]);
	var strDateE = new Date(strDateArrayEnd[0] + "/" + strDateArrayEnd[1] + "/" + strDateArrayEnd[2]);
	var intDay = Math.floor((strDateE.getTime()-strDateS.getTime())/(1000*3600*24));
	return intDay;
}

/**
 * 根据孩子节点code，获取父节点code
 * @param childCode 孩子节点code
 * @returns 父节点code
 */
function getPid(childCode){
	var pos=childCode.lastIndexOf("-");
	var pidCode = childCode.substring(0,pos);
	return pidCode;
}

/**
 * 判断字符串中是否包含组织分隔符"-"
 * @param str 字符串
 * @returns {Boolean}
 */
function isContain(str){
	if(str.indexOf("-") > 0){
		return true;
	}
	return false;
}

/** 
 * 时间对象的格式化; 
 */  
Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()  
        // millisecond  
    };  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
}


/**
 * 查询条件展示隐藏
 * @param obj
 */
function selectToggle(obj){
    if($(".searchForm").get(0).style.display=="none"){
        var searchHeight = $(".commenSearch").outerHeight(true)+$(".searchForm").outerHeight(true);
        $(".searchForm").attr("style","display:block");
        $(".searchContent").height(searchHeight);
        $(".listTable").height($(window).height()-searchHeight);
        $("#searchMark").removeClass("glyphicon glyphicon-menu-down");
        $("#searchMark").addClass("glyphicon glyphicon-menu-up");
    }else{
        var searchHeight = $(".commenSearch").outerHeight();
        $(".searchForm").attr("style","display:none");
        $(".searchContent").height(searchHeight);
        $(".listTable").height($(window).height()-searchHeight);
        $("#searchMark").removeClass("glyphicon glyphicon-menu-up");
        $("#searchMark").addClass("glyphicon glyphicon-menu-down");
    }
}

/**
 * 获取考勤设备公共方法
 * @param machuse 考勤设备用途，0：采集设备 1：考勤设备
 */
function get_machine(machuse) {
	var machine = new Object();
	machine.machuse = machuse;
	return getData('machine_list',machine,false);
};

/**
 * 获取公司部门公共方法
 * @param type 组织类型，0：公司 1：部门 
 */
function get_org(type) {
	var org = new Object();
	org.org_type =type;
	return getData('org_list',org,false);
};

/**
 * 获取是否
 * @param type 是否，0：否 1：是 
 */
function get_yesOrNo() {
    return getData('yesOrNo');
};

/**
 * 获取当前日期MM/dd/YY--12/23/16
 * @returns {String}
 */
function getnowtime() {  
    var nowtime = new Date();  
    var year = nowtime.getFullYear(); 
    year = year.toString().substr(2, 2);
    var month = padleft0(nowtime.getMonth() + 1);  
    var day = padleft0(nowtime.getDate());  
    return month + "/" + day + "/" + year;  
} 
/**
 * 月份补齐2位数
 * @returns {String}
 */
function padleft0(obj) {  
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);  
}

//除法函数，用来得到精确的除法结果
//说明：JavaScript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
//调用：accDiv(arg1,arg2)
//返回值：arg1除以arg2的精确结果
function accDiv(arg1,arg2){
var t1=0,t2=0,r1,r2;
try{t1=arg1.toString().split(".")[1].length}catch(e){}
try{t2=arg2.toString().split(".")[1].length}catch(e){}
with(Math){
r1=Number(arg1.toString().replace(".",""))
r2=Number(arg2.toString().replace(".",""))
return (r1/r2)*pow(10,t2-t1);
}
}


//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2)
{
var m=0,s1=arg1.toString(),s2=arg2.toString();
try{m+=s1.split(".")[1].length}catch(e){}
try{m+=s2.split(".")[1].length}catch(e){}
returnNumber(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}

//加法函数，用来得到精确的加法结果
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
//调用：accAdd(arg1,arg2)
//返回值：arg1加上arg2的精确结果
function accAdd(arg1,arg2){
var r1,r2,m;
try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
m=Math.pow(10,Math.max(r1,r2))
return (arg1*m+arg2*m)/m
}

//减法函数

function accSub(arg1, arg2) {
var r1, r2, m, n;
try { r1 = arg1.toString().split(".")[1].length } catch (e) { r1 = 0}
try { r2 = arg2.toString().split(".")[1].length } catch (e) { r2 = 0}
m = Math.pow(10, Math.max(r1, r2));
//last modify by deeka
//动态控制精度长度
n = (r1 >= r2) ? r1 : r2;
return ((arg1 * m - arg2 * m) / m).toFixed(n);
}
function getHtmlStringByXmlhttp(urlstr){
	var s="";
	$.ajax({
   	   	url:urlstr,
   	   	type:"POST",
   	   	dataType:"xml",
	   	cache: false,
	    async: false,
   	   	error: function(xml){  
   	       	alert('数据加载失败！');  
   	   	},
   		success: function(xml){ 
   	   		$(xml).find("item").each(function(i){ 
   	   			s= $(this).attr("str")+"";
   	    	});
   	   		
   	  	}
   	});
	return s;
};
