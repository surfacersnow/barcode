/*
ʹ�ýṹdemo��
<div class="autoBox">
<input type="text" class="autoInput" id="uname" />
<div class="autoList">
</div>
<input type="hidden" id="rname" name="rname" />
</div>

ҳ�����JS���룺
<script type="text/javascript">
InitAutoInput("uname", '/User/Gettestlist', { key: "ID_int", val: "name_nvarchar", format: "ID_int-name_nvarchar" });//ajax-URL����
InitAutoInput("uname", data, { key: "ID_int", val: "name_nvarchar", format: "ID_int-name_nvarchar" }, 'json');//��̬JSON�ļ�����
</script>

ע��
������JQ��
������hidden��ֵ��������Ҫ�ύ��ֵ
������ID ���⡣������ID������������������
�ı����ύ����̨�Ĳ�����Ϊ��searchKey
���߿��������޸�����Ĳ�����
*/

/*
��װajax�ύ
*/
//ajax�ύ
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
            //window.location.replace("/User/Login");������ת
        }
    });
}





/*
=====�Զ����=====
tid:�����ID
url:���������ַ���߾�̬JSON�ļ�����
showText:����key����Ҫ�����ύ����ʵֵ��val��ѡ����ı�����ʾ��ֵ��format��������ʾ�ֶΣ����֧��2��
dataType:��ѡ�������Ƿ��Ǿ�̬����
*/
var continueKeyupTime = 0;
var timerInterval;
//�ӳٲ���������
var timer = function(value) {
    timerInterval = setInterval(function() {
        continueKeyupTime++;
    }, value);
};
var zindex = 50; //����һ��ҳ�����Զ���ɻ����ڸǵ�����
function InitAutoInput(tid, url, showText, dataType) {
    //Ԫ�ض���===============================
    var inpitObj = $("#" + tid); //�����ı���
    var listDiv = $("#" + tid + "+ div"); //�����б��DIV
    var hiddenObj = $("#" + tid).parent().find("input[type=hidden]"); //ʵ���ύ��������ؼ�
    var queryInterval;

    //�����¼�===============================
    inpitObj.keyup(function(e) {
        var text = $.trim(inpitObj.val());
        var keyCode = e.keyCode;

        //û�����벻��ѯ
        if (text.length == 0) {
            //���������
            hiddenObj.val("");
            listDiv.hide("1000");
            inpitObj.prev("span").css({"display":"none"});
    		inpitObj.prev("span").html("");
    		inpitObj.css("padding-left","");
            return;
        }
        //���˲���ѯ�İ���
        if (keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || //��������
            keyCode == 13 || // enter
            keyCode == 36 || keyCode == 35 || // home end
            keyCode == 16 || keyCode == 17 || keyCode == 18// ctrl shift alt
            ) {
            return;
        }

        //���ø���z-index
        inpitObj.parent().attr("style", "z-index: " + parseInt(zindex) + parseInt(1));
        zindex = parseInt(zindex) + parseInt(1);

        //����div��ȸ߶Ⱥ���ʽ
        listDiv.width(inpitObj.outerWidth());//�޸Ľ�width()+2�滻ΪouterWidth();
        //listDiv.height(inpitObj.outerWidth() * 1.5);//�޸Ľ�width()�滻ΪouterWidth();
        //�����ӿ��
        var box = inpitObj.parent().width();
        //ƫ����
        //var left = box - inpitObj.width() - 1;
        //var style = listDiv.attr("style") + "left:" + left + "px;";
        //listDiv.attr("style", style);

        clearInterval(queryInterval);
        clearInterval(timerInterval);
        continueKeyupTime = 0;
        timer(300);

        var specialKeyCount = 0; //�������ִ�в�ѯ����
        //�ӳ�300����ִ�в�ѯ
        queryInterval = setInterval(function() {
            //��������ж�
            if (text.length == 0 || continueKeyupTime < 1) {
                return;
            }
            clearInterval(timerInterval);
            if (keyCode == 8 && (specialKeyCount < 2)) {
                specialKeyCount++;
                return;
            }
            clearInterval(queryInterval); //���һ�μ�ʱ��

            //�ж�����Դ
            if (dataType == "json") {
                var tempData = GetTempData1(url, showText, inpitObj.val()); //JSON��̬���ݲ�ѯ����
                ChangeHeigtAndSetText1(tid, tempData, showText);
            } else {
                //��ѯ����URL-ajax��JSON ���ظ�ʽ{Rows:[{}],Total:0}��
                AjaxSubmit(url, { searchKey: text }, function(data) {
                    ChangeHeigtAndSetText(tid, data.Rows, showText);
                })
            }
        }, 300);
    });

    //֧�ּ������¼��¼�=====================
    inpitObj.keydown(function(event) {
        //ֻ�����¼��س���ִ�����´���
        if (event.keyCode == 40 || event.keyCode == 38 || event.keyCode == 13) {
            //Ԫ�ض���=======================
            var cur = listDiv.find("ul").find("li.liover"); //��ǰѡ����
            var length = listDiv.find("ul").find("li").length; //�б����
            var first = listDiv.find("ul").find("li").eq(0); //��һ��
            var last = listDiv.find("ul").find("li").eq(length - 1); //����

            var firstTV = first.attr("textV"); //��һ��ѡ����ı�ֵ
            var firstHV = first.attr("hidenV"); //��һ��ѡ���������ֵ
            var lastTV = last.attr("textV"); //���һ��ѡ����ı�ֵ
            var lastHV = last.attr("hidenV"); //���һ��ѡ���������ֵ

            //�ж��Ƿ���ѡ�е���
            if (cur.length == 0) {
                //��
                if (event.keyCode == 40) {
                    first.addClass("liover");
                    inpitObj.val(firstTV);
                    //������ֵ
                    hiddenObj.val(firstHV);
                }
                //��
                if (event.keyCode == 38) {
                    last.addClass("liover");
                    inpitObj.val(lastTV);
                    //������ֵ
                    hiddenObj.val(lastHV);
                }
            } else {
                //��
                if (event.keyCode == 40) {
                    if (cur.html() != last.html()) {
                        cur.removeClass("liover");
                        cur.next().addClass("liover");
                        inpitObj.val(cur.next().attr("textV"));
                        //������ֵ
                        hiddenObj.val(cur.next().attr("hidenV"));
                    }
                }
                //��
                if (event.keyCode == 38) {
                    if (cur.html() != first.html()) {
                        cur.removeClass("liover");
                        cur.prev().addClass("liover");
                        inpitObj.val(cur.prev().attr("textV"));
                        //������ֵ
                        hiddenObj.val(cur.prev().attr("hidenV"));
                    }
                }

                //�س���ѡ��
                if (event.keyCode == 13) {
                    var CV = cur.attr("textV"); //��ǰѡ���ֵ
                    var CH = cur.attr("hidenV"); //��ǰѡ�������ڵ�ֵ
                    var SV = cur.attr("spanV");//ѡ�е�searchTitle
                    inpitObj.val(CV).attr('title', CH + '=' + CV);
                    //������ֵ
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
	�Զ�����ʾ��
*/
var ChangeHeigtAndSetText1 = function(tid, dataSoure, showText) {
    //Ԫ�ض���===============================
    var inpitObj = $("#" + tid); //�����ı���
    var listDiv = $("#" + tid + "+ div"); //�����б��DIV
    var hiddenObj = $("#" + tid).parent().find("input[type=hidden]"); //ʵ���ύ��������ؼ�
    if (dataSoure == "\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021") {
        //û������
        listDiv.show("1000");
        listDiv.html("<ul><li style='color:red'>" + dataSoure + "</li></ul>"); //����תunicode����ʾ�ֶ��������
        listDiv.height("");
        $("#" + tid).parent().find("input[type=hidden]").val("");
        return;
    }
    if (dataSoure.length > 0) {
        //��ȷƥ����Զ������ı���
        if (dataSoure.length == 1) {
            listDiv.hide();
            var item = dataSoure[0];
            inpitObj.prev("span").css({"display":"block"});
    		inpitObj.prev("span").html(item[showText.val]);
    		var inputObjPrevPadding = inpitObj.prev("span").outerWidth(true);
    		inpitObj.css("padding-left",""+(inputObjPrevPadding+5)+"px");
            inpitObj.val(item[currInputValue]).attr('title', item[showText.key] + '=' + item[currInputValue]); //����title����
            hiddenObj.val(item[showText.key]);
        } else {
            //��Ⱦ����
            GetHTML1(tid, dataSoure, showText);
            //����߶�
            var liheight = listDiv.find("ul").find("li").height();
            if (inpitObj.width() * 1.5 > liheight * dataSoure.length) {
                listDiv.height(liheight * dataSoure.length);
            }
        }
    } else {
        //û������
        //listDiv.show("1000");
        //listDiv.html("<ul><li style='color:red'>\u6ca1\u6709\u7ed3\u679c !</li></ul>"); //����תunicode��û�н����
        //listDiv.height("");
        //$("#" + tid).parent().find("input[type=hidden]").val("");
    }
}
/*
 * �Զ������������������Դ
 */
var GetTempData1 = function(data, showText, searchKey) {
    var returnData = []; //��������Դ
    //���ҷ�������Դ
    for (var i = 0; i < data.length; i++) {
        //tempStr = data[i][showText.key].toString()+"@"+data[i][showText.val].toString()+"@"+searchKey;
    	data[i].currInputValue = searchKey;
        returnData.push(data[i]);
    }
    return returnData;
}
/*
 * �Զ�����������
 * */
var GetHTML1 = function(tid, obj, showText) {
    //������ʾ�ı���ʽ��������ʾһ������2���ֶΣ�
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
            filed1 = obj[i][sarray[0]]; //format��һ������
            filed2 = obj[i][sarray[1]]; //format�ڶ�������
            filed3 = obj[i][showText.val]; //�ı���Ļ���ֵ
            filed4 = obj[i][showText.key]; //��Ҫ�ύ����ʵֵ
            filed5 = obj[i][currInputValue];//��ǰ�ı����������Ϣ
            html += "<li onclick='select1($(this),\"" + tid + "\",\"" + filed3 + "\");' textV='" + filed5 + "' hidenV='" + filed4 + "' spanV='" + filed3 + "'>" + filed1 + " " + filed2 + " : "+filed5+"</li>";//
        } else if (sarray.length == 1 && obj[i][sarray[0]] != undefined) {
            filed1 = obj[i][sarray[0]]; //format��һ������
            filed3 = obj[i][showText.val]; //�ı���Ļ���ֵ
            filed4 = obj[i][showText.key]; //��Ҫ�ύ����ʵֵ
            filed5 = obj[i]['currInputValue'];//��ǰ�ı����������Ϣ
            html += "<li onclick='select1($(this),\"" + tid + "\",\"" + filed3 + "\");' textV='" + filed5 + "' hidenV='" + filed4 + "' spanV='" + filed3 + "'>" + filed1 + " : "+filed5+ "</li>";
        } else {
            html += "<li>\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021</li>"; //����תunicode����ʾ�ֶ���������
        }
    }
    html += "</ul>";
    listDiv.html(html);
    listDiv.show("1000");

    //�����б���Ч��
    listDiv.find("ul").find("li").hover(function() {
        $(this).addClass("liover");
    }, function() {
        $(this).removeClass("liover");
    });
}
/**
 * 
 * �Զ���select����
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
    //������ֵ
    $("#" + tid).parent().find("input[type=hidden]").val(hidenV);
    listDiv.hide("1000");
    $("#" + tid + "_span").removeClass("Validform_wrong");
    $("#" + tid + "_span").html("");
}


/*
=====��̬���������߶��Լ�������Դֻ��1����ʱ���Զ������ı���=====
tid:�����ID
dataSoure:��ѯ����Դ
showText:����key����Ҫ�����ύ����ʵֵ��val��ѡ����ı�����ʾ��ֵ��format��������ʾ�ֶΣ����֧��2��
*/
var ChangeHeigtAndSetText = function(tid, dataSoure, showText) {
    //Ԫ�ض���===============================
    var inpitObj = $("#" + tid); //�����ı���
    var listDiv = $("#" + tid + "+ div"); //�����б��DIV
    var hiddenObj = $("#" + tid).parent().find("input[type=hidden]"); //ʵ���ύ��������ؼ�
    if (dataSoure == "\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021") {
        //û������
        listDiv.show("1000");
        listDiv.html("<ul><li style='color:red'>" + dataSoure + "</li></ul>"); //����תunicode����ʾ�ֶ��������
        listDiv.height("");
        $("#" + tid).parent().find("input[type=hidden]").val("");
        return;
    }
    if (dataSoure.length > 0) {
        //��ȷƥ����Զ������ı���
        if (dataSoure.length == 1) {
            listDiv.hide();
            var item = dataSoure[0];
            inpitObj.val(item[showText.val]).attr('title', item[showText.key] + ' ' + item[showText.val]); //����title����
            hiddenObj.val(item[showText.key]);
        } else {
            //��Ⱦ����
            GetHTML(tid, dataSoure, showText);
            //����߶�
            var liheight = listDiv.find("ul").find("li").height();
            if (inpitObj.width() * 1.5 > liheight * dataSoure.length) {
                listDiv.height(liheight * dataSoure.length);
            }
        }
    } else {
        //û������
        listDiv.show("1000");
        listDiv.html("<ul><li style='color:red'>\u6ca1\u6709\u7ed3\u679c !</li></ul>"); //����תunicode��û�н����
        listDiv.height("");
        $("#" + tid).parent().find("input[type=hidden]").val("");
    }
}

/*
=====���������ѯ�µ�JSON����Դ=====
data:����Դ
showText:JSON����
searchKey:��ѯ�ؼ���
*/
var GetTempData = function(data, showText, searchKey) {
    var returnData = []; //��������Դ
    //����������ʾ�ı���ʽ
    //    var sarray = showText.format.split('-');
    var tempStr;
    //���ҷ�������Դ
    for (var i = 0; i < data.length; i++) {
        //����������ʾ�ı���ʽ
        //        if (sarray.length == 2 && data[i][showText.key] != undefined && data[i][showText.val] != undefined) {
        //            tempStr = data[i][showText.key].toString() + data[i][showText.val].toString();
        //        } else if (sarray.length == 1 && data[i][sarray[0]] != undefined) {
        //            tempStr = data[i][sarray[0]].toString();
        //        } else {
        //            return "\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021"; //����תunicode����ʾ�ֶ���������
        //        }
        tempStr = data[i][showText.key].toString() + data[i][showText.val].toString();
        //��ѯ����
        if (tempStr.indexOf(searchKey) > -1) {
            returnData.push(data[i]);
        }
    }
    return returnData;
}

/*
=====��Ⱦ�����б�=====
tid�������ID
obj:����Դ
showText:����key����Ҫ�����ύ����ʵֵ��val��ѡ����ı�����ʾ��ֵ��format��������ʾ�ֶΣ����֧��2��
*/
var GetHTML = function(tid, obj, showText) {
    //������ʾ�ı���ʽ��������ʾһ������2���ֶΣ�
    var sarray = showText.format.split('-');
    var listDiv = $("#" + tid + "+ div");
    var html = "<ul>";
    var filed1;
    var filed2;
    var filed3;
    var filed4;
    for (var i = 0; i < obj.length; i++) {
        if (sarray.length == 2 && obj[i][showText.key] != undefined && obj[i][showText.val] != undefined) {
            filed1 = obj[i][sarray[0]]; //format��һ������
            filed2 = obj[i][sarray[1]]; //format�ڶ�������
            filed3 = obj[i][showText.val]; //�ı���Ļ���ֵ
            filed4 = obj[i][showText.key]; //��Ҫ�ύ����ʵֵ
            html += "<li onclick='select($(this),\"" + tid + "\",\"" + filed4 + "\");' textV='" + filed3 + "' hidenV='" + filed4 + "' >" + filed1 + " " + filed2 + "</li>";//
        } else if (sarray.length == 1 && obj[i][sarray[0]] != undefined) {
            filed1 = obj[i][sarray[0]]; //format��һ������
            filed3 = obj[i][showText.val]; //�ı���Ļ���ֵ
            filed4 = obj[i][showText.key]; //��Ҫ�ύ����ʵֵ
            html += "<li onclick='select($(this),\"" + tid + "\",\"" + filed4 + "\");' textV='" + filed3 + "' hidenV='" + filed4 + "'>" + filed1 + "</li>";
        } else {
            html += "<li>\u663e\u793a\u5b57\u6bb5\u8f93\u5165\u6709\u8bef\u0021</li>"; //����תunicode����ʾ�ֶ���������
        }
    }
    html += "</ul>";
    listDiv.html(html);
    listDiv.show("1000");

    //�����б���Ч��
    listDiv.find("ul").find("li").hover(function() {
        $(this).addClass("liover");
    }, function() {
        $(this).removeClass("liover");
    });
}

/*
=====�����ѡ������=====
obj:����Ķ���
tid�������ID
value����ʵ��Ҫ�ύ��ֵ
*/
var select = function(obj, tid, value) {
    var listDiv = $("#" + tid + "+ div");
    var text = obj.attr("textV");
    $("#" + tid).val(text).attr('title', value + ' ' + text); ;
    //������ֵ
    $("#" + tid).parent().find("input[type=hidden]").val(value);
    listDiv.hide("1000");
    $("#" + tid + "_span").removeClass("Validform_wrong");
    $("#" + tid + "_span").html("");
}
/*
=====���ð�ť����Զ��������ֵ������ڱ������ð�ť�޷����������ֵ��=====
formid:��ID
*/
var clearReset = function(formid) {
    $("#" + formid).find("input[type=reset]").click(function() {
        $("#" + formid).find(".autoBox").find("input[type=hidden]").val("");
    });
}
/*
=================�Զ���ɽ���
*/ 