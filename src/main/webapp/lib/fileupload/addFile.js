/**
 * Created by lenovo on 2015/12/22.
 */

//var k=1;
//function add()
//{
//    var j=k-1;
//    var tagname="upload"+j;
//    var upload=document.getElementById(tagname).value;
//    if(upload=="")
//    {
//        alert("上传文件未满,不需要增加上传栏");
//        return false;
//    }
//    var name="upload"+k;
//    var file=createfile("input","file",name);
//    var br = document.createElement("br");
//    var files = document.getElementById("files");
//    files.appendChild(br);
//    files.appendChild(file);
//    k++;
//}
//function createfile(tagName,type,name)
//{
//    var element = null;
//    try{
//        element=document.createElement(tagName);
//        element.type=type;
//        element.name=name;
//        element.id=name;
////        element.class="fileupload";
//        element.style.cssText="BORDER: #91c0e3 1px solid; width:50%;HEIGHT: auto; BACKGROUND-COLOR: #FFFFFF;color: #004779;";
////        element.style.border="#91c0e3 1px solid";
////        element.style.width="77%";
////        element.style.height="auto";
////        //element.style.background-color="#FFFFFF";
////        element.style.color="#004779";
//    }catch(e)
//    {
//    }
//    return element;
//}

/*
 * 上传附件
 */
function upload(){
//    var uplist = $("input[name^=uploadFile]");
//    var arrId = [];
//    for (var i=0; i< uplist.length; i++){
//        if(uplist[i].value){
//            arrId[i] = uplist[i].id;
//        }
//    }
    
    var arrId = [];
    arrId[0]="image";
    $.ajaxFileUpload({
        type: "POST",  //提交方式
        url:'/employee/manyFileUpload',
//        url:'/mesginfo/manyFileInput',
        secureuri:false,
        fileElementId:arrId,
        dataType: 'text/html',
        success:function(data,status){
            var json=eval(data);
            var messageDiv=document.getElementById("project_document");
            for(var i=0;i<json.length;i++){
                messageDiv.innerHTML+="<input type='hidden' name='project_document' value="+json[i].customer_descr+"/>";
            }
            messageDiv.innerHTML+="<TD>&nbsp;<FONT COLOR=#ff0000>上传成功</FONT></TD>";
        },
        error : function(data, status, e) {
            alert("系统报错，稍后重试！");
        }
    });
}

/*
 * 下载附件
 */
function download(){
    var url = "/mesginfo/downloadFile";
    window.open(url);
}

/*
 * 动态增加附件
 */
function addFiles(){
    var table=document.getElementById("fileUpload");
    var tdcount=table.rows.length;
    if(tdcount*1<5){
        var new_tr = document.createElement("tr");
        var new_td1 = document.createElement("td");
        var new_td2 = document.createElement("td");
//        new_td1.className = "tdTitle";
//        new_td1.style.width="9%";
//        new_td2.className = "tdRight";
//        new_td2.style.width="91%";
        var i=1;
        for(i=1;i<=5;i++){
            if(document.getElementsByName("uploadFile"+i).length==0){
                break;
            }
        }
        new_td1.innerHTML = "附件名称：";
        new_td2.innerHTML = "<input type=\"file\" name=\"uploadFile"+i+"\" id=\"uploadFile"+i+"\"><input type=\"button\" value=\"移除\" onclick=\"deleteFile(this.parentNode.parentNode)\"><font color=\"red\">(附件不超过2兆)</font>";
    }else{
        alert("最多允许一次性上传5个文件！");
        return false;
    }
    new_tr.appendChild(new_td1);
    new_tr.appendChild(new_td2);
    table.tBodies[0].appendChild(new_tr);
}

/*
 * 动态删除附件
 */
function deleteFile(tr){
    var root=tr.parentNode;
    root.deleteRow(tr.rowIndex);
}
