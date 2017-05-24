/*
使用结构demo：
<div class="autoBox">
<input type="text" class="autoInput" id="uname" />
<div class="autoList">
</div>
<input type="hidden" id="rname" name="rname" />
</div>

页面调用JS代码：
<script type="text/javascript">
InitAutoInput("uname", '/User/Gettestlist', { key: "ID_int", val: "name_nvarchar", format: "ID_int-name_nvarchar" });//ajax-URL请求
InitAutoInput("uname", data, { key: "ID_int", val: "name_nvarchar", format: "ID_int-name_nvarchar" }, 'json');//静态JSON文件请求
</script>

注：
需引用JQ库
隐藏域hidden的值才是你需要提交的值
输入框的ID 随意。隐藏域ID根据你自身需求设置
文本框提交到后台的参数名为：searchKey
或者可以自行修改下面的参数名
*/

/*
封装ajax提交
*/
//ajax提交
function AjaxSubmit(url, parms, callback) {
    $.ajax({
        type: 'get',
        url: url,
        data: parms,
        cache: false,
        dataType: 'json',
        async: true,
        success: function(data) {
            if (callback != null && callback.toString() != "undefined") {
                callback(data);
            }
        },
        error: function(jqXHR, textStatus, responseText) {
            //window.location.replace("/User/Login");错误跳转
        }
    });
}





/*
=====自动完成=====
tid:输入框ID
url:数据请求地址或者静态JSON文件数据
showText:参数key：需要保存提交的真实值；val：选择后文本框显示的值；format：下拉显示字段，最多支持2个
dataType:可选参数，是否是静态数据
*/
var continueKeyupTime = 0;
var timerInterval;
//延迟操作计数器
var timer = function(value) {
    timerInterval = setInterval(function() {
        continueKeyupTime++;
    }, value);
};
var zindex = 50; //控制一个页面多个自动完成互相遮盖的问题
function InitAutoInput(tid, url, showText, dataType) {
    //元素定义===============================
    var inpitObj = $("#" + tid); //输入文本框
    var listDiv = $("#" + tid + "+ div"); //下拉列表的DIV
    var hiddenObj = $("#" + tid).parent().find("input[type=hidden]"); //实际提交的隐藏域控件
    var queryInterval;

    //输入事件===============================
    inpitObj.keyup(function(e) {
        var text = $.trim(inpitObj.val());
        var keyCode = e.keyCode;

        //没有输入不查询
        if (text.length == 0) {
            //清空隐藏域
            hiddenObj.val("");
            listDiv.hide("1000");
            inpitObj.prev("span").css({"display":"none"});
    		inpitObj.prev("span").html("");
    		inpitObj.css("padding-left","");
            return;
        }
        //过滤不查询的按键
        if (keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || //上下左右
            keyCode == 13 || // enter
            keyCode == 36 || keyCode == 35 || // home end
            keyCode == 16 || keyCode == 17 || keyCode == 18// ctrl shift alt
            ) {
            return;
        }

        //设置父级z-index
        inpitObj.parent().attr("style", "z-index: " + parseInt(zindex) + parseInt(1));
        zindex = parseInt(zindex) + parseInt(1);

        //设置div宽度高度和样式
        listDiv.width(inpitObj.outerWidth());//修改将width()+2替换为outerWidth();
        //listDiv.height(inpitObj.outerWidth() * 1.5);//修改将width()替换为outerWidth();
        //外框盒子宽度
        var box = inpitObj.parent().width();
        //偏移量
        //var left = box - inpitObj.width() - 1;
        //var style = listDiv.attr("style") + "left:" + left + "px;";
        //listDiv.attr("style", style);

        clearInterval(queryInterval);
        clearInterval(timerInterval);
        continueKeyupTime = 0;
        timer(300);

        var specialKeyCount = 0; //特殊键不执行查询操作
        //延迟300毫秒执行查询
        queryInterval = setInterval(function() {
            //按键间隔判断
            if (text.length == 0 || continueKeyupTime < 1) {
                return;
            }
            clearInterval(timerInterval);
            if (keyCode == 8 && (specialKeyCount < 2)) {
                specialKeyCount++;
                return;
            }
            clearInterval(queryInterval); //清除一次计时器

            //判断数据源
            if (dataType == "json") {
                var tempData = GetTempData1(url, showText, inpitObj.val()); //JSON静态数据查询方法
                ChangeHeigtAndSetText1(tid, tempData, showText);
            } else {
                //查询数据URL-ajax（JSON 返回格式{Rows:[{}],Total:0}）
                AjaxSubmit(url, { searchKey: text }, function(data) {
                    ChangeHeigtAndSetText(tid, data.Rows, showText);
                })
            }
        }, 300);
    });

    //支持键盘上下键事件=====================
    inpitObj.keydown(function(event) {
        //只有上下键回车才执行以下代码
        if (event.keyCode == 40 || event.keyCode == 38 || event.keyCode == 13) {
            //元素定义=======================
            var cur = listDiv.find("ul").find("li.liover"); //当前选中项
            var length = listDiv.find("ul").find("li").length; //列表个数
            var first = listDiv.find("ul").find("li").eq(0); //第一个
            var last = listDiv.find("ul").find("li").eq(length - 1); //最后个

            var firstTV = first.attr("textV"); //第一个选项的文本值
            var firstHV = first.attr("hidenV"); //第一个选项的隐藏于值
            var lastTV = last.attr("textV"); //最后一个选项的文本值
            var lastHV = last.attr("hidenV"); //最后一个选项的隐藏于值

            //判断是否有选中的项
            if (cur.length == 0) {
                //下
                if (event.keyCode == 40) {
                    first.addClass("liover");
                    inpitObj.val(firstTV);
                    //隐藏域赋值
                    hiddenObj.val(firstHV);
                }
                //上
                if (event.keyCode == 38) {
                    last.addClass("liover");
                    inpitObj.val(lastTV);
                    //隐藏域赋值
                    hiddenObj.val(lastHV);
                }
            } else {
                //下
                if (event.keyCode == 40) {
                    if (cur.html() != last.html()) {
                        cur.removeClass("liover");
                        cur.next().addClass("liover");
                        inpitObj.val(cur.next().attr("textV"));
                        //隐藏域赋值
                        hiddenObj.val(cur.next().attr("hidenV"));
                    }
                }
                //上
                if (event.keyCode == 38) {
                    if (cur.html() != first.html()) {
                        cur.removeClass("liover");
                        cur.prev().addClass("liover");
                        inpitObj.val(cur.prev().attr("textV"));
                        //隐藏域赋值
                        hiddenObj.val(cur.prev().attr("hidenV"));
                    }
                }

                //回车键选择
                if (event.keyCode == 13) {
                    var CV = cur.attr("textV"); //当前选项的值
                    var CH = cur.attr("hidenV"); //当前选项隐藏于的值
                    var SV = cur.attr("spanV");//选中的searchTitle
                    inpitObj.val(CV).attr('title', CH + '=' + CV);
                    //隐藏域赋值
                    hiddenObj.val(CH);
                    inpitObj.prev("span").css({"display":"block"});
    				inpitObj.prev("span").html(SV);
    				var inputObjPrevPadding = inpitObj.prev("span").outerWidth(true);
    				inpitObj.css("padding-left",""+(inputObjPrevPadding+5)+"px");
                    listDiv.hide("1000");
                }
            }
        }
    });
}

/*
	自定义提示框
*/
var ChangeHeigtAndSetText1 = function(tid, dataSoure, showText) {
    //元素定义===============================
    var inpitObj = $("#" + tid); //输入文本框
    var listDiv = $("#" + tid + "+ div"); //下拉列表的DIV
    var hiddenObj = $("#" + tid).parent().find("input[type=hidden]"); //实际提交的隐藏域控件
    if (dataSoure == "\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021") {
        //没有数据
        listDiv.show("1000");
        listDiv.html("<ul><li style='color:red'>" + dataSoure + "</li></ul>"); //中文转unicode：显示字段输入错误！
        listDiv.height("");
        $("#" + tid).parent().find("input[type=hidden]").val("");
        return;
    }
    if (dataSoure.length > 0) {
        //精确匹配后自动回填文本框
        if (dataSoure.length == 1) {
            listDiv.hide();
            var item = dataSoure[0];
            inpitObj.prev("span").css({"display":"block"});
    		inpitObj.prev("span").html(item[showText.val]);
    		var inputObjPrevPadding = inpitObj.prev("span").outerWidth(true);
    		inpitObj.css("padding-left",""+(inputObjPrevPadding+5)+"px");
            inpitObj.val(item[currInputValue]).attr('title', item[showText.key] + '=' + item[currInputValue]); //增加title属性
            hiddenObj.val(item[showText.key]);
        } else {
            //渲染下拉
            GetHTML1(tid, dataSoure, showText);
            //计算高度
            var liheight = listDiv.find("ul").find("li").height();
            if (inpitObj.width() * 1.5 > liheight * dataSoure.length) {
                listDiv.height(liheight * dataSoure.length);
            }
        }
    } else {
        //没有数据
        //listDiv.show("1000");
        //listDiv.html("<ul><li style='color:red'>\u6ca1\u6709\u7ed3\u679c !</li></ul>"); //中文转unicode：没有结果！
        //listDiv.height("");
        //$("#" + tid).parent().find("input[type=hidden]").val("");
    }
}
/*
 * 自定义根据输入整合数据源
 */
var GetTempData1 = function(data, showText, searchKey) {
    var returnData = []; //返回数据源
    //查找返回数据源
    for (var i = 0; i < data.length; i++) {
        //tempStr = data[i][showText.key].toString()+"@"+data[i][showText.val].toString()+"@"+searchKey;
    	data[i].currInputValue = searchKey;
        returnData.push(data[i]);
    }
    return returnData;
}
/*
 * 自定义下拉内容
 * */
var GetHTML1 = function(tid, obj, showText) {
    //分析显示文本格式（下拉显示一个还是2个字段）
    var sarray = showText.format.split('-');
    var listDiv = $("#" + tid + "+ div");
    var html = "<ul>";
    var filed1;
    var filed2;
    var filed3;
    var filed4;
    var filed5;
    for (var i = 0; i < obj.length; i++) {
        if (sarray.length == 2 && obj[i][showText.key] != undefined && obj[i][showText.val] != undefined) {
            filed1 = obj[i][sarray[0]]; //format第一个参数
            filed2 = obj[i][sarray[1]]; //format第二个参数
            filed3 = obj[i][showText.val]; //文本框的回填值
            filed4 = obj[i][showText.key]; //需要提交的真实值
            filed5 = obj[i][currInputValue];//当前文本框输入的信息
            html += "<li onclick='select1($(this),\"" + tid + "\",\"" + filed3 + "\");' textV='" + filed5 + "' hidenV='" + filed4 + "' spanV='" + filed3 + "'>" + filed1 + " " + filed2 + " : "+filed5+"</li>";//
        } else if (sarray.length == 1 && obj[i][sarray[0]] != undefined) {
            filed1 = obj[i][sarray[0]]; //format第一个参数
            filed3 = obj[i][showText.val]; //文本框的回填值
            filed4 = obj[i][showText.key]; //需要提交的真实值
            filed5 = obj[i]['currInputValue'];//当前文本框输入的信息
            html += "<li onclick='select1($(this),\"" + tid + "\",\"" + filed3 + "\");' textV='" + filed5 + "' hidenV='" + filed4 + "' spanV='" + filed3 + "'>" + filed1 + " : "+filed5+ "</li>";
        } else {
            html += "<li>\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021</li>"; //中文转unicode：显示字段输入有误！
        }
    }
    html += "</ul>";
    listDiv.html(html);
    listDiv.show("1000");

    //下拉列表光棒效果
    listDiv.find("ul").find("li").hover(function() {
        $(this).addClass("liover");
    }, function() {
        $(this).removeClass("liover");
    });
}
/**
 * 
 * 自定义select方法
 */
var select1 = function(obj, tid, value) {
    var listDiv = $("#" + tid + "+ div");
    var text = obj.attr("textV");
    var hidenV = obj.attr("hidenV");
    $("#" + tid).prev("span").css({"display":"block"});
    $("#" + tid).prev("span").html(value);
    var inputObjPrevPadding = $("#" + tid).prev("span").outerWidth(true);
    $("#" + tid).css("padding-left",""+(inputObjPrevPadding+5)+"px");
    $("#" + tid).val(text).attr('title', hidenV + '=' + text); ;
    //隐藏域赋值
    $("#" + tid).parent().find("input[type=hidden]").val(hidenV);
    listDiv.hide("1000");
    $("#" + tid + "_span").removeClass("Validform_wrong");
    $("#" + tid + "_span").html("");
}


/*
=====动态更改下拉高度以及当数据源只有1个的时候自动回填文本框=====
tid:输入框ID
dataSoure:查询数据源
showText:参数key：需要保存提交的真实值；val：选择后文本框显示的值；format：下拉显示字段，最多支持2个
*/
var ChangeHeigtAndSetText = function(tid, dataSoure, showText) {
    //元素定义===============================
    var inpitObj = $("#" + tid); //输入文本框
    var listDiv = $("#" + tid + "+ div"); //下拉列表的DIV
    var hiddenObj = $("#" + tid).parent().find("input[type=hidden]"); //实际提交的隐藏域控件
    if (dataSoure == "\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021") {
        //没有数据
        listDiv.show("1000");
        listDiv.html("<ul><li style='color:red'>" + dataSoure + "</li></ul>"); //中文转unicode：显示字段输入错误！
        listDiv.height("");
        $("#" + tid).parent().find("input[type=hidden]").val("");
        return;
    }
    if (dataSoure.length > 0) {
        //精确匹配后自动回填文本框
        if (dataSoure.length == 1) {
            listDiv.hide();
            var item = dataSoure[0];
            inpitObj.val(item[showText.val]).attr('title', item[showText.key] + ' ' + item[showText.val]); //增加title属性
            hiddenObj.val(item[showText.key]);
        } else {
            //渲染下拉
            GetHTML(tid, dataSoure, showText);
            //计算高度
            var liheight = listDiv.find("ul").find("li").height();
            if (inpitObj.width() * 1.5 > liheight * dataSoure.length) {
                listDiv.height(liheight * dataSoure.length);
            }
        }
    } else {
        //没有数据
        listDiv.show("1000");
        listDiv.html("<ul><li style='color:red'>\u6ca1\u6709\u7ed3\u679c !</li></ul>"); //中文转unicode：没有结果！
        listDiv.height("");
        $("#" + tid).parent().find("input[type=hidden]").val("");
    }
}

/*
=====根据输入查询新的JSON数据源=====
data:数据源
showText:JSON属性
searchKey:查询关键字
*/
var GetTempData = function(data, showText, searchKey) {
    var returnData = []; //返回数据源
    //分析下拉显示文本格式
    //    var sarray = showText.format.split('-');
    var tempStr;
    //查找返回数据源
    for (var i = 0; i < data.length; i++) {
        //分析下拉显示文本格式
        //        if (sarray.length == 2 && data[i][showText.key] != undefined && data[i][showText.val] != undefined) {
        //            tempStr = data[i][showText.key].toString() + data[i][showText.val].toString();
        //        } else if (sarray.length == 1 && data[i][sarray[0]] != undefined) {
        //            tempStr = data[i][sarray[0]].toString();
        //        } else {
        //            return "\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021"; //中文转unicode：显示字段输入有误！
        //        }
        tempStr = data[i][showText.key].toString() + data[i][showText.val].toString();
        //查询数据
        if (tempStr.indexOf(searchKey) > -1) {
            returnData.push(data[i]);
        }
    }
    return returnData;
}

/*
=====渲染下拉列表=====
tid：输入框ID
obj:数据源
showText:参数key：需要保存提交的真实值；val：选择后文本框显示的值；format：下拉显示字段，最多支持2个
*/
var GetHTML = function(tid, obj, showText) {
    //分析显示文本格式（下拉显示一个还是2个字段）
    var sarray = showText.format.split('-');
    var listDiv = $("#" + tid + "+ div");
    var html = "<ul>";
    var filed1;
    var filed2;
    var filed3;
    var filed4;
    for (var i = 0; i < obj.length; i++) {
        if (sarray.length == 2 && obj[i][showText.key] != undefined && obj[i][showText.val] != undefined) {
            filed1 = obj[i][sarray[0]]; //format第一个参数
            filed2 = obj[i][sarray[1]]; //format第二个参数
            filed3 = obj[i][showText.val]; //文本框的回填值
            filed4 = obj[i][showText.key]; //需要提交的真实值
            html += "<li onclick='select($(this),\"" + tid + "\",\"" + filed4 + "\");' textV='" + filed3 + "' hidenV='" + filed4 + "' >" + filed1 + " " + filed2 + "</li>";//
        } else if (sarray.length == 1 && obj[i][sarray[0]] != undefined) {
            filed1 = obj[i][sarray[0]]; //format第一个参数
            filed3 = obj[i][showText.val]; //文本框的回填值
            filed4 = obj[i][showText.key]; //需要提交的真实值
            html += "<li onclick='select($(this),\"" + tid + "\",\"" + filed4 + "\");' textV='" + filed3 + "' hidenV='" + filed4 + "'>" + filed1 + "</li>";
        } else {
            html += "<li>\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021</li>"; //中文转unicode：显示字段输入有误！
        }
    }
    html += "</ul>";
    listDiv.html(html);
    listDiv.show("1000");

    //下拉列表光棒效果
    listDiv.find("ul").find("li").hover(function() {
        $(this).addClass("liover");
    }, function() {
        $(this).removeClass("liover");
    });
}

/*
=====鼠标点击选择下拉=====
obj:点击的对象
tid：输入框ID
value：真实需要提交的值
*/
var select = function(obj, tid, value) {
    var listDiv = $("#" + tid + "+ div");
    var text = obj.attr("textV");
    $("#" + tid).val(text).attr('title', value + ' ' + text); ;
    //隐藏域赋值
    $("#" + tid).parent().find("input[type=hidden]").val(value);
    listDiv.hide("1000");
    $("#" + tid + "_span").removeClass("Validform_wrong");
    $("#" + tid + "_span").html("");
}
/*
=====重置按钮清除自动完成隐藏值（解决在表单中重置按钮无法清除隐藏域值）=====
formid:表单ID
*/
var clearReset = function(formid) {
    $("#" + formid).find("input[type=reset]").click(function() {
        $("#" + formid).find(".autoBox").find("input[type=hidden]").val("");
    });
}
/*
=================自动完成结束
*/ 