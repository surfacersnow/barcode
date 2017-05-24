var defGrid;
var defid;
$(function() {
    defGrid = $("#query_list").ligerGrid({
        columns : [ {
            display : '操作',isSort : false,width : 60,
            render : function(rowdata, rowindex, value) {
                var h = "";
                if (!rowdata._editing) {
                    h += "<a href='javascript:void(0);' class='opt-link' onclick='beginEdit(" + rowindex + ")'>修改</a> ";
                    h += "<a href='javascript:void(0);' class='opt-link' onclick='deleteRow(" + rowindex + ")'>删除</a> ";
                } else {
                    h += "<a href='javascript:void(0);' class='opt-link' onclick='endEdit(" + rowindex + ")'>提交</a> ";
                    h += "<a href='javascript:void(0);' class='opt-link' onclick='cancelEdit(" + rowindex + ")'>取消</a> ";
                }
                return h;
            }
        }, {
            display : '名称',name : 'name',width : 180,minWidth : 60,align : 'left',
            editor : {type : 'text'}
        }, {
            display : '代码',name : 'code',width : 180,align : 'left',
            editor : {type : 'text'}
        }, {
            display : '状态',name : 'status',width : 70,data : status,render : switcher,
            editor : {type : 'select', data : getStatus,textField : 'name',valueField : 'value',selectBoxHeight : 50}
        }, {
            display : '字典数据项', isSort : false,width : 100,
            render : function(rowdata, rowindex, value) {
                return "<a href='javascript:void(0);' class='opt-link' onclick='openDictItem(" + rowindex + ")'>管理字典数据项</a>";
            }
        }, {
            display : '说明', name : 'note', width : 200, align : 'left',
            editor : {type : 'text'}
        }
        ],
        usePager:true,
        pageSize : 10,
        sortName : 'id',
        height : '100%',
        dataAction:'server',
        url : '../../dict/queryDict',
        contentType : "application/json; charset=utf-8",
        fixedCellHeight : false,
        colDraggable : true,
        enabledEdit : true,
        clickToEdit : false,
        onSelectRow : function(rowdata, rowindex) {
            $("#txtrowindex").val(rowindex);
        },
        param_status : 'rbac.status'
    });
    $("#pageloading").hide();

    var moveInBox = $("#newStatuNameS").ligerComboBox({
        autocomplete : true,
        valueField : 'value',
        textField : 'name',
        data : getStatus,
        onSelected : function(v, t) {
            $("#newStatuS").val(v);
        }
    });
    var tyoeInBox = $("#newTypeNameS").ligerComboBox({
        autocomplete : true,
        valueField : 'value',
        textField : 'name',
        data : getType,
        onSelected : function(v, t) {
            $("#newTypeS").val(v);
        }
    });
});

// 查询方法
function toQuery() {
    //设置查询参数
    param = new Object();
    //设置查询条件参数
    param.name = $("#dictName").val();
    param.code = $("#dictCode").val();
    param.status = $("#dictStatus").val();

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

function beginEdit(rowid) {
    defGrid.beginEdit(rowid);
}
function cancelEdit(rowid) {
    defGrid.cancelEdit(rowid);
}
function endEdit(rowid) {
    defGrid.endEdit(rowid);
    var row = defGrid.getRow(rowid);
    toModify(JSON.stringify(row));
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
var getStatus = new function() {
    return getData('rbac.status');
}
var getType = new function() {
    return getData('rbac.difType');
}

var addDialog;

function openDictDef() {
    toReset('#new_dict_def input,select,textarea');
    addDialog = $.ligerDialog.open({
        target : $("#new_dict_def"),
        title : "新增字典定义",
        width : 350,
        height : 400
    });
}

function openDictItem(index) {
    defid = defGrid.getRow(index)['id'];
    var defName
    defName = defGrid.getRow(index)['name'];
    parent.f_addTab('dictitem' + defid + '', '(' + defName + ')字典数据项维护', './html/sysmgt/dictitem.html?defid=' + defid + '');

}
function validate() {
    var valid = true;

    $('*[id$="S"],*[id$="S_txt"]').each(function(index, element) {

        if (!$(this).rules())
            return;
        if (!$(element).valid()) {
            valid = false;
            return;
        }
    });
    if (!valid)
        return false;

    return true
}
function toAdd() {
    if (!validate()) {
        return;
    }
    var param = new Object();
    param.name = $("#newNameS").val();
    param.code = $("#newCodeS").val();
    param.status = $("#newStatuS").val();
    param.note = $("#newDescS").val();
    param.type = $("#newTypeS").val();
    var data = JSON.stringify(param);
    ajaxSubmit("/dict/addDict", data, function(data) {
        if (data) {
            if (parseInt(data['status']) > 0) {
                $.ligerDialog.success('保存成功', '操作完成', function() {
                    addDialog.hide();
                    toQuery();
                });
            } else if (parseInt(data['status']) == -1) {
                $.ligerDialog.error('用户[' + $("#newName").val() + ']已存在<br>不能重复添加', '错误');
            } else {
                $.ligerDialog.error('保存失败');
            }
        }
    });
}
function toModify(row) {
    var url = "/dict/updateDict";
    ajaxSubmit(url, row, function(data) {
        if (data) {
            if (parseInt(data['status']) > 0) {
                $.ligerDialog.success('保存成功');
            } else {
                $.ligerDialog.error('保存失败');
            }
        }
    });
}
function toDel(row) {
    var url = "/dict/deleteDictDef/"+row.id;
    ajaxSubmitGet(url, JSON.stringify(row), function(data) {
        if (data) {
            if (parseInt(data['result']) > 0) {
                $.ligerDialog.success('删除成功');
            } else {
                $.ligerDialog.error('删除失败');
            }
        }
    });
}