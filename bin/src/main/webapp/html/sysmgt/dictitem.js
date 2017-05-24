/**
 * Created by lenovo on 2015/9/20.
 */
var defGrid;
var refid;
var parm = new Object();
$(function(){
    refid=window.location.toString().replace(/^[^?]+\?defid=/,'');
    $("#layout_main").ligerLayout({
    });
    parm.dict_id = refid;
    defGrid = $("#query_list").ligerGrid({
    columns:[
    {display:'操作',isSort:false,width:60,render:function(rowdata,rowindex,value) {
    var h = "";
    if (!rowdata._editing) {
         h += "<a href='javascript:void(0);' class='opt-link' onclick='beginEdit(" + rowindex + ")'>修改</a> ";
         h += "<a href='javascript:void(0);' class='opt-link' onclick='deleteRow(" + rowindex + ")'>删除</a> ";
    } else {
         h += "<a href='javascript:void(0);' class='opt-link' onclick='endEdit(" + rowindex + ")'>提交</a> ";
         h += "<a href='javascript:void(0);' class='opt-link' onclick='cancelEdit(" + rowindex + ")'>取消</a> ";
    }
         return h;
    } },
    {display:'数据项编号',name:'code',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'所属参数字典',name:'dict_id',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'上层数据项',name:'dict_line_id',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'数据项名称',name:'name',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'数据项值',name:'value',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'显示顺序',name:'seq',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'分级码',name:'path',width:80,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'级数',name:'layer',width:100,minWidth:60,align:'left',editor:{type:'text'}},
    {display:'明细否',name:'detail',width:70,data:getyesOrno,render:switcher_yesOrNo,editor:{type:'select',data:getyesOrno,textField:'name',valueField:'value',selectBoxHeight:50}},
    {display:'状态',name:'status',width:70,data:status,render:switcher,editor:{type:'select',data:getStatus,textField:'name',valueField:'value',selectBoxHeight:50}},
    {display:'说明',name:'note',width:200,align:'left',editor:{type:'text'}}
    ],
    pageSize:10,
    usePager: true,
    sortName:'id',
    width:'100%',
    height:'100%',
    postData:parm,
    dataAction:'server',
    checkbox:false,
    rownumbers:false,
    fixedCellHeight:false,
    colDraggable:true,
    enabledEdit:true,
    clickToEdit:false,
    onSelectRow:function(rowdata, rowindex){$("#txtrowindex").val(rowindex);},
    url: '../../dict/queryDictItem',
    contentType : "application/json; charset=utf-8",
    param_status:'rbac.status',
    param_yesOrNo:'yesOrNo'
    });
    $("#pageloading").hide();
    });
function beginEdit(rowid) {
    defGrid.beginEdit(rowid);
    }
function cancelEdit(rowid) {
    defGrid.cancelEdit(rowid);
    }
function endEdit(rowid) {
    defGrid.endEdit(rowid);
    var row = defGrid.getRow(rowid);
    toModify(row);
    }
function deleteRow(rowid) {
    $.ligerDialog.confirm('您确定要删除该项数据吗？', function(r) {
        if (!r)
            return;
        var row = defGrid.getRow(rowid);
        if (row['__status'] !== 'add')
            toDel(row);
        defGrid.deleteRow(rowid);
    })
    }
function switcher(item) {
    return paramRender(item, defGrid.options.param_status, item.status);
    }
var getStatus = new function () {
    return getData('rbac.status');
    }
function switcher_yesOrNo(item) {
    return paramRender(item, defGrid.options.param_yesOrNo, item.detail);
    }
var getyesOrno = new function () {
    return getData('yesOrNo');
    }
function toQuery() {
    var param = new Object();
    param.code=$("#defCode").val();
    param.name = $("#defName").val();
    param.status = $("#dictStatus").val();
    param.dict_id=refid;
    // l-list 线性结构，默认；t-tree 树型结构
    param.rt='l';
    //重置页码
    defGrid.set({
        page : 1,
        newPage : 1,
        postData : param
    });
    // grid 重新加载数据
    defGrid.loadData();
}

var addDialog;
function openDictDef() {
    toReset('#new_user_def input,select,textarea');
    addDialog = $.ligerDialog.open({
    target:$("#new_user_def"),
    title:"新增字典数据",
    width:450,
    height:450
    });
    }
function validate() {
    var valid = true;

    $('*[id$="S"],*[id$="S_txt"]').each(function(index, element) {

    if (!$(this).rules()) return;
    if (!$(element).valid()) {
    valid = false;
    return;
    }
    });
    if (!valid) return false;

    return true
    }
// 新增保存处理
function toAdd() {
    if (!validate())
    {
        return;
    }
    var param = new Object();
    param.code=$("#codeS").val();
    param.dict_id=refid;
    param.dict_line_id=$("#codeS").val();
    param.name = $("#newNameS").val();
    param.value = $("#newVALUES").val();
    param.seq=$("#newSEQ").val();
    param.detail=$("#newDetail").val();
    param.layer=1;
    param.path='';
    param.status = $("#newStatus").val();
    param.note=$("#newDesc").val();
    var data = JSON.stringify(param);
    ajaxSubmit("/dict/addItem", data, function(data) {
        if (data) {
            if (parseInt(data['status']) > 0) {
                $.ligerDialog.success('保存成功', '操作完成', function() {
                    addDialog.hide();
                    toQuery();
                });
            } else if (parseInt(data['status']) == -1) {
                $.ligerDialog.error('用户[' + $("#newName").val() + "]已存在,不能重复添加",'错误');
            } else {
                $.ligerDialog.error('保存失败');
            }
        }
    });
}

function toModify(row) {

    ajaxSubmit("/dict/updateItem", JSON.stringify(row), function(data) {
        if (data) {
            if (parseInt(data['status']) > 0) {
                $.ligerDialog.success('保存成功');
                toQuery();
            } else {
                $.ligerDialog.error('保存失败');
            }
        }
    });
}
//删除时，判断此用户下是否已设置角色信息，删除时提示，将删除用户的角色设置信息
//确认删除时，将用户下的角色设置信息一并删除。
function toDel(row) {
    ajaxSubmitGet("/dict/deleteDictItem/"+row.id, JSON.stringify(row), function(data) {
    if (data) {
    if (parseInt(data['status']) > 0) {
    $.ligerDialog.success('删除成功');
    } else {
    $.ligerDialog.error('删除失败');
    }
    }
    });
    }