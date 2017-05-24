var paramS = new Object();
var opt;
    $(function () {
        //1.初始化Table
        var oTable = new TableInit();
        oTable.Init();
        //2.初始化Button的点击事件
        var oButtonInit = new ButtonInit();
        oButtonInit.Init();
    	$('#barcodegrid').on('click-row.bs.table', function (e, row, $element) {
              $('.success').removeClass('success');
              $($element).addClass('success');
          });
    });
    var TableInit = function () {
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#barcodegrid').bootstrapTable({
                url: '../../tmwh/query',         //请求后台的URL（*）
                method: 'post',                      //请求方式（*）
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                showHeader:true,
                queryParamsType:'page',
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber:1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [10, 20, 50, 100],        //可供选择的每页的行数（*）
                search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: false,
                height:460,
                showColumns: false,                  //是否显示所有的列
                showRefresh: false,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                singleSelect: true,
                uniqueId: "barcode_khbh",           //每一行的唯一标识，一般为主键列
                showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
                cardView:  false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                showExport: false,   
                exportDataType: "basic",              //basic', 'all', 'selected'.
                exportTypes: ['excel'],
                columns: [{radio: true},
                          {field: 'Number',title: '序号',formatter: function (value, row, index) {return index+1;}},
                          {field: 'barcode_khmc', title: '客户名称',halign:'center',valign:'middle',width:'40px'},
                	      {field: 'barcode_khbh', title: '客户编号',halign:'center',valign:'middle',width:'100px'},
                	      {field: 'barcode_sc', title: '生产编号',halign:'center',valign:'middle',width:'100px'},
                          {field: 'barcode_upc', title: '客户UPC码',halign:'center',valign:'middle',width:'100px'},
                          {field: 'barcode_gg',title: '规格',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_ks',title: '款式',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_pj',title: '偏距',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_pcd',title: 'PCD',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_zxk',title: '中心孔',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_djz',title: '单净重(kg)',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_mjz',title: '单毛重(kg)',halign:'center',valign:'middle',width:'100px'},
                          {field: 'barcode_djt',title: '单体积',halign:'center',valign:'middle',width:'100px'},
                          {field: 'barcode_bmzt',title: '表面状态',halign:'center',valign:'middle',width:'180px'}, 
                          {field: 'barcode_bmztyw',title: '状态英文',halign:'center',valign:'middle',width:'200px'}, 
                          {field: 'barcode_bzfs',title: '包装方式',halign:'center',valign:'middle',width:'100px'}, 
                          {field: 'barcode_bqbh',title: '标签类型',halign:'center',valign:'middle',width:'80px'},
                ]
            });
        };

        //得到查询的参数
        oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                ps: params.pageSize,   //页面大小
                cp: params.pageNumber,  //页码
                data:paramS,
                sql:'tmwh.select.barcode.query',
            };
            return temp;
        };
        return oTableInit;
    };
    var ButtonInit = function () {
        var oInit = new Object();
        var postdata = {};
        oInit.Init = function () {
            //初始化页面上面的按钮事件
        	$('#pop_add_button').bind('click', function() {
    			$.ajaxFileUpload({
    				type : "POST", //提交方式
    				url : '../../tmwh/excelUpload', //用于文件上传的服务器端请求地址
    				secureuri : false, //一般设置为false
    				fileElementId : $("input#file").attr("id"), //文件上传控件的id属性  <input type="file" id="file" name="file" /> 注意，这里一定要有name值   
    				dataType : 'json',//返回值类型 一般设置为json
    				success : function(data) { //服务器成功响应处理函数
    					 toastr.success(data["msg"]);
    				},
    				error : function(data)//服务器响应失败处理函数
    				{
    					toastr.error("导入excel发生错误");
    				}
    			})
    		});
            //导入
        	$('#btn_import').bind('click', function() {
        		$('#addMx_Modal').modal('show');
    		});
        	  //导入
        	$('#btn_download').bind('click', function() {
        		var url = "../../tmwh/downloadExcel";
        		window.open(url);
    		});
            //新增
        	$('#btn_add').bind('click', function() {
        		//parent.f_addTab('add_heatflow', '新增','./html/heatflow/add_heatflow.html');
        		 $("#modaltitle").text("新增");
        		 opt = 'add';
        		 $("#barcode_khbh").removeAttr("readonly","readonly");
        		 $("#addbarcode_modal").find(".form-control").val("");
        		$('#addbarcode_modal').modal('show');
    		});
            //修改
        	$('#btn_edit').bind('click', function() {
        		var arrselections = $("#barcodegrid").bootstrapTable('getSelections');
        		 if (arrselections.length > 1) {
        			 toastr.warning("只能选择一行进行编辑!");
        			return;
        		 }
        		 if (arrselections.length <= 0) {
        			 toastr.warning("请选择有效数据!");
         			return;
         		 }
        		 $("#modaltitle").text("编辑");
        		 opt = 'update';
        		 $("#barcode_khbh").attr("readonly","readonly");
        		 $("#barcode_khbh").val(arrselections[0].barcode_khbh);
        		 $("#barcode_ks").val(arrselections[0].barcode_ks);
        	     $("#barcode_sc").val(arrselections[0].barcode_sc);
      		     $("#barcode_khmc").val(arrselections[0].barcode_khmc);
      		     $("#barcode_upc").val(arrselections[0].barcode_upc);
      		     $("#barcode_gg").val(arrselections[0].barcode_gg);
      		     $("#barcode_pcd").val(arrselections[0].barcode_pcd);
      		     $("#barcode_zxk").val(arrselections[0].barcode_zxk);
      		     $("#barcode_pj").val(arrselections[0].barcode_pj);
      		     $("#barcode_djz").val(arrselections[0].barcode_djz);
      		     $("#barcode_mjz").val( arrselections[0].barcode_mjz);
      		     $("#barcode_djt").val(arrselections[0].barcode_djt);
      		     $("#barcode_bmzt").val(arrselections[0].barcode_bmzt);
      		     $("#barcode_bmztyw").val(arrselections[0].barcode_bmztyw);
      		     $("#barcode_bzfs").val(arrselections[0].barcode_bzfs);
      		     $("#barcode_bqbh").val(arrselections[0].barcode_bqbh);
      		     $('#addbarcode_modal').modal();
    		});
        	//删除
        	$('#btn_delete').bind('click', function() {
        		var arrselections = $("#barcodegrid").bootstrapTable('getSelections');
       			 if (arrselections.length <= 0) {
       				 toastr.warning("请选择有效数据!");
        			return;
        		 }
       			Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
       				if (!e) {
       				 return;
       				}
       				var barcode_khbh = arrselections[0].barcode_khbh;
       				toDel(barcode_khbh);
       			})
    		});
        	$("#btn_submit").click(function () {
        		var bh = $("#barcode_khbh").val();
                 postdata.barcode_khbh=$("#barcode_khbh").val();
                 postdata.barcode_sc=$("#barcode_sc").val();
                 postdata.barcode_khmc=$("#barcode_khmc").val();
                 postdata.barcode_upc=$("#barcode_upc").val();
    		     postdata.barcode_ks=$("#barcode_ks").val();
    		     postdata.barcode_pj=$("#barcode_pj").val();
    		     postdata.barcode_gg=$("#barcode_gg").val();
    		     postdata.barcode_zxk=$("#barcode_zxk").val();
    		     postdata.barcode_pcd=$("#barcode_pcd").val();
    		     postdata.barcode_mjz=$("#barcode_mjz").val();
    		     postdata.barcode_djz=$("#barcode_djz").val( );
    		     postdata.barcode_bmzt=$("#barcode_bmzt").val();
    		     postdata.barcode_djt=$("#barcode_djt").val();
    		     postdata.barcode_bzfs=$("#barcode_bzfs").val();
    		     postdata.barcode_bmztyw=$("#barcode_bmztyw").val();
    		     postdata.barcode_bqbh=$("#barcode_bqbh").val();
     			var url;
     			if(bh==''||bh==null){
     				toastr.error('编号不能为空！');
     				return;
     			}
     			if (opt == 'update') {
     				url = "../../tmwh/updateBarcode";
     			}else{
     				url = "../../tmwh/saveBarcode";
     			}
     			// 提交后台保存数据转换为JSON 格式
     			var data = JSON.stringify(postdata);
     			//保存提交后台
     			if (url){
     				ajaxSubmit(url, data, function(data) {
   					if (parseInt(data['status']) > 0) {
   						 toastr.success('信息保存成功!');
   						$("#addbarcode_modal").modal("hide")
   						toQuery();
   					} else {
   						 toastr.error('保存失败');
   					}
   				  });
     			}
        	});
        };
        return oInit;
    };
	// 删除处理业务处理
	function toDel(id) {
	    ajaxSubmitGet("../../tmwh/deleteBarcode/" + id,function(data) {
	        if (data) {
	            if (parseInt(data['status']) > 0) {
	                toastr.success('删除成功');
	                toQuery();
	            } else {
	                toastr.error('删除失败');
	            }
	        }
	    });
	}
	//查询
	function toQuery(){
		paramS.barcode_khbh = $("#Search_barcode_khbh").val();
		paramS.barcode_sc = $("#Search_barcode_sc").val();
        $("#barcodegrid").bootstrapTable('refresh');
	}
	
	function checkBarcodeKhbh(){
		var khbh =  $("#barcode_khbh").val();
		if(khbh!=''&&khbh!=null){
			ajaxSubmitGet("../../tmwh/checkBarcodeKhbh/" + khbh,function(data) {
				if(data){
					if (data['falg']==true) {
						toastr.warning('此编号已经存在，请更改编号！');
					}
				}
			});
		}
	}

