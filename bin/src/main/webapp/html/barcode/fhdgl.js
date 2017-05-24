var paramS = new Object();
var opt;
var username="";

    $(function () {
    	
    	$.ajax({
    		url : "../../rbac/cmUser?method=getUserInfo",
    		type : "GET",
    		dataType : "json",
    		contentType : "application/json; charset=utf-8",
    		success : function(data) {
    			if (data["status"] === "1")
    				username=data["uname"];
    		},
    		error : function(data) {
    		}
    	});
    	
    	
        //1.初始化Table
        var oTable = new TableInit();
        oTable.Init();
        //2.初始化Button的点击事件
        var oButtonInit = new ButtonInit();
        oButtonInit.Init();
    	$('#fhdgrid').on('click-row.bs.table', function (e, row, $element) {
              $('.success').removeClass('success');
              $($element).addClass('success');
          });
    	
    	
    });
    var TableInit = function () {
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#fhdgrid').bootstrapTable({
                url: '../../fhdwh/query',         //请求后台的URL（*）
                method: 'post',                      //请求方式（*）
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                showHeader:true,
                showColumns:false,
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
                uniqueId: "order_ckdh",           //每一行的唯一标识，一般为主键列
                showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
                cardView:  false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                showExport: false,   
                exportDataType: "basic",              //basic', 'all', 'selected'.
                exportTypes: ['excel'],
                columns: [{radio: true},
                          {field: 'order_ckdh', title: '发货单号',halign:'center',valign:'middle'},
                          {field: 'order_tdh', title: '提单号',halign:'center',valign:'middle'},
                	      {field: 'order_khmc', title: '客户名称',halign:'center',valign:'middle'},
                          {field: 'order_mdh', title: '目的港',halign:'center',valign:'middle'},
                          {field: 'order_gh',title: '柜号',halign:'center',valign:'middle'}, 
                          {field: 'order_fhrq',title: '发货日期',halign:'center',valign:'middle'}, 
                          {field: 'order_zdsj',title: '制单时间',halign:'center',valign:'middle'}, 
                          {field: 'order_zdr',title: '制单人',halign:'center',valign:'middle'}, 
                          {field: 'order_fhzt',title: '状态',halign:'center',valign:'middle'}, 
                ]
            });
        };

        //得到查询的参数
        oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                ps: params.pageSize,   //页面大小
                cp: params.pageNumber,  //页码
                data:paramS,
                sql:'fhdwh.select.crm_order.query',
            };
            return temp;
        };
        return oTableInit;
    };
    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + " " + date.getHours() + seperator2 + date.getMinutes()
                + seperator2 + date.getSeconds();
        return currentdate;
    }
    var ButtonInit = function () {
        var oInit = new Object();
        var postdata = {};
        oInit.Init = function () {
        	 
            //新增
        	$('#btn_add').bind('click', function() {
        		//parent.f_addTab('add_heatflow', '新增','./html/heatflow/add_heatflow.html');
        		 $("#modaltitle").text("新增");
        		 opt = 'add';
        		 $("#order_ckdh").removeAttr("readonly","readonly");
        		 $("#order_tdh").removeAttr("readonly","readonly");
        		 $("#order_fhrq").removeAttr("readonly","readonly");
        		 $("#order_khmc").removeAttr("readonly","readonly");
        		 $("#order_mdh").removeAttr("readonly","readonly");
        		 $("#order_gh").removeAttr("readonly","readonly");
        		 $("#order_fh").removeAttr("readonly","readonly");
        		 //$("#order_zdr").removeAttr("readonly","readonly");
        		// $("#order_zdsj").removeAttr("readonly","readonly");
        		 $("#addfhd_modal").find(".form-control").val("");
        		 
        		 $('.form_date').datetimepicker({
    	    	    language:  'zh-CN',
    	    	    weekStart: 1,
    	    	    todayBtn:  1,
    	    		autoclose: 1,
    	    		todayHighlight: 1,
    	    		startView: 2,
    	    		minView: 2,
    	    		forceParse: 0,
    	    		format: 'yyyy-mm-dd'
    	    	});
    	    	$("#order_zdsj").val(getNowFormatDate());
    	    	$("#order_zdr").val(username);
    	    	$("#order_fhrq").val(getToDay());
        		$('#addfhd_modal').modal('show');
        		$("#btn_submit").show();
    		});
            //修改
        	$('#btn_edit').bind('click', function() {
        		var arrselections = $("#fhdgrid").bootstrapTable('getSelections');
        		 if (arrselections.length > 1) {
        			 toastr.warning("只能选择一行进行编辑!");
        			return;
        		 }
        		 if (arrselections.length <= 0) {
        			 toastr.warning("请选择有效数据!");
         			return;
         		 }
        		 var fhzt=arrselections[0].order_fhzt;
        		 if(fhzt=="已发货确认"){
        			 toastr.warning("请选择未确认发货的单据!");
          			return;
        		 }
        		 var fhdinfo=arrselections[0].order_ckdh+"&"+arrselections[0].order_tdh+"&"+arrselections[0].order_fhrq
	     			+"&"+arrselections[0].order_khmc+"&"+arrselections[0].order_mdh
	     			+"&"+arrselections[0].order_gh+"&"+arrselections[0].order_fh
	     			+"&"+arrselections[0].order_zdr+"&"+arrselections[0].order_zdsj
	     			+"&"+arrselections[0].order_fhzt;
        		 var urlstr="./html/barcode/fhdgledit.html?fhdinfo="+fhdinfo;
				 parent.$("#rightPart").attr("src", encodeURI(urlstr));
				 parent.$(".dhxx").html("发货单管理 / 编辑");
    		});
        	 //明细
        	$('#btn_detail').bind('click', function() {
        		var arrselections = $("#fhdgrid").bootstrapTable('getSelections');
        		 if (arrselections.length > 1) {
        			 toastr.warning("只能选择一行查看明细!");
        			return;
        		 }
        		 if (arrselections.length <= 0) {
        			 toastr.warning("请选择有效数据!");
         			return;
         		 }
        	     
        	     var fhdinfo=arrselections[0].order_ckdh+"&"+arrselections[0].order_tdh+"&"+arrselections[0].order_fhrq
        	     			+"&"+arrselections[0].order_khmc+"&"+arrselections[0].order_mdh
        	     			+"&"+arrselections[0].order_gh+"&"+arrselections[0].order_fh
        	     			+"&"+arrselections[0].order_zdr+"&"+arrselections[0].order_zdsj
        	     			+"&"+arrselections[0].order_fhzt;
        	     
        		 parent.$("#rightPart").attr("src", "./html/barcode/fhdgldetail.html?fhdinfo="+encodeURI(fhdinfo));
        		 parent.$(".dhxx").html("发货单管理 / 明细");
    		});
        	//删除
        	$('#btn_delete').bind('click', function() {
        		var arrselections = $("#fhdgrid").bootstrapTable('getSelections');
       			 if (arrselections.length <= 0) {
       				 toastr.warning("请选择有效数据!");
        			return;
        		 }
       			var fhzt=arrselections[0].order_fhzt;
	       		 if(fhzt=="已发货确认"){
	       			 toastr.warning("请选择未确认发货的单据!");
	         			return;
	       		 }
       			Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
       				if (!e) {
       				 return;
       				}
       				var order_ckdh = arrselections[0].order_ckdh;
       				toDel(order_ckdh);
       			})
    		});
        	//打印
        	$('#btn_print').bind('click', function() {
        		var arrselections = $("#fhdgrid").bootstrapTable('getSelections');
       			 if (arrselections.length <= 0) {
       				 toastr.warning("请选择有效数据!");
        			return;
        		 }
       			var fhzt=arrselections[0].order_fhzt;
	       		 if(fhzt!="已发货确认"){
	       			 toastr.warning("请选择已发货确认的单据进行打印!");
	         			return;
	       		 }
       			Ewin.confirm({ message: "确认要打印选择的数据吗？" }).on(function (e) {
       				if (!e) {
       				 return;
       				}
       				var order_ckdh = arrselections[0].order_ckdh;
       			    window.open("./printfhd.html?ckdh="+order_ckdh)
       			})
    		});
        	//数据导出
           	$('#btn_eaport').bind('click', function() {
        		var arrselections = $("#fhdgrid").bootstrapTable('getSelections');
       			 if (arrselections.length <= 0) {
       				 toastr.warning("请选择有效数据!");
        			return;
        		 }
       			var fhzt=arrselections[0].order_fhzt;
	       		 if(fhzt!="已发货确认"){
	       			 toastr.warning("请选择已发货确认的单据进行导出打印!");
	         			return;
	       		 }
       			Ewin.confirm({ message: "确认要导出选择的数据吗？" }).on(function (e) {
       				if (!e) {
       				 return;
       				}
       				var order_ckdh = arrselections[0].order_ckdh;
       			    window.open("../../tmwh/exportExcel?ckdh="+order_ckdh)
       			})
    		});
        	
        	$("#btn_submit").click(function () {
        		var order_ckdh = $("#order_ckdh").val();
                 postdata.order_ckdh=$("#order_ckdh").val();
                 postdata.order_tdh=$("#order_tdh").val();
                 postdata.order_fhrq=$("#order_fhrq").val();
                 postdata.order_khmc=$("#order_khmc").val();
    		     postdata.order_mdh=$("#order_mdh").val();
    		     postdata.order_gh=$("#order_gh").val();
    		     postdata.order_fh=$("#order_fh").val();
    		     postdata.order_zdr=$("#order_zdr").val();
    		     postdata.order_zdsj=$("#order_zdsj").val();
    		     postdata.order_fhzt="1";
    		     postdata.order_fhdy="0";
     			var url;
     			if(order_ckdh==''||order_ckdh==null){
     				toastr.error('发货单号不能为空！');
     				return;
     			}
     			if (opt == 'update') {
     				url = "../../fhdwh/updateFhd";
     			}else{
     				url = "../../fhdwh/saveFhd";
     			}
     			// 提交后台保存数据转换为JSON 格式
     			var data = JSON.stringify(postdata);
     			//保存提交后台
     			if (url){
     				ajaxSubmit(url, data, function(data) {
   					if (parseInt(data['status']) > 0) {
   						 toastr.success('信息保存成功!');
   						$("#addfhd_modal").modal("hide")
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
	    ajaxSubmitGet("../../fhdwh/deleteFhd/" + id,function(data) {
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
		paramS.order_ckdh = $("#Search_order_ckdh").val();
        $("#fhdgrid").bootstrapTable('refresh');
	}
	function judgeRepeat(obj){
		var ckdhh=$(obj).val();
		$.ajax({
	   	   	url:"../../fhdwh/judgeCkdhRepeat?ckdhh="+ckdhh,
	   	   	type:"POST",
	   	   	dataType:"xml",  
	   	   	error: function(xml){  
	   	       	alert('数据加载失败！');  
	   	   	},
	   		success: function(xml){ 
	   			var barcodehtml="";
	   	   		$(xml).find("item").each(function(i){ 
	   	   			var jls=$(this).attr("jls");//客户编号
	   	   			if(jls=="0"){
	   	   				
	   	   			}else{
	   	   				alert("发货单号重复");
	   	   				$(obj).val("").focus();
	   	   			}
	   	    	});
	   	  	}
	   	});
	}
