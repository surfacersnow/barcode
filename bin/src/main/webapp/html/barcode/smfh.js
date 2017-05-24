$(document).ready(function(){
	getPageInfo("1");
 });
var ckdh="";//出库单号
var ysmsl="";//已扫描数量
function saomiao(obj,obj2){
	ckdh=obj;//出库单号
	ysmsl=obj2;//已扫描数量
	getMtgs();
	$("#smjsid").html(0);//扫码计数
	$("#smhjid").html(ysmsl*1);//扫码合计
	$("#smtab tr:not(:first):not(:last)").remove();
	$('#smcpxx').modal('show');
	setTimeout("$('#focid').val('').focus()",1000);
}
function getMtgs(){
	$.ajax({
   	   	url:"../../fhdwh/queryMtgsByCkdh?ckdh="+ckdh,
   	   	type:"POST",
   	   	dataType:"xml",  
   	   	error: function(xml){  
   	       	alert('数据加载失败！');  
   	   	},
   		success: function(xml){ 
   			var fhdhtml="";
   	   		$(xml).find("item").each(function(i){ 
   	   			var mt=$(this).attr("mtgs");
   	   			$("#mtgsid").html(mt*1+1);//马托个数
   	    	});
   	  	}
   	});
}
function changeYs(){
	getPageInfo("1");
 }
 function getPageInfo(currentPage){
	 var onePageJls=$("#onePageJlsId").val();
	  $("#currentpageid").html(currentPage);
	  $.ajax({
	   	   	url:"../../fhdwh/queryFhdInfoByXml?currentPage="+currentPage+"&onePageJls="+onePageJls,
	   	   	type:"POST",
	   	   	dataType:"xml",  
	   	   	error: function(xml){  
	   	       	alert('数据加载失败！');  
	   	   	},
	   		success: function(xml){ 
	   			var fhdhtml="";
	   	   		$(xml).find("item").each(function(i){ 
	   	   			var order_ckdh=$(this).attr("order_ckdh");
	   	   			var order_fhrq=$(this).attr("order_fhrq");
	   	   			var order_khmc=$(this).attr("order_khmc");
	   	   			var order_mdh=$(this).attr("order_mdh");
	   	   			var order_gh=$(this).attr("order_gh");
	   	   			var order_fh=$(this).attr("order_fh");
	   	   			var order_ysmsl=$(this).attr("order_ysmsl");
		   	   		fhdhtml=fhdhtml+"<table class='table'>";
			   	   	fhdhtml=fhdhtml+"			   	 <tr>";
			   	   	fhdhtml=fhdhtml+"				   <td class='active' align='center' width='80%'>";
			   	   	fhdhtml=fhdhtml+"				  	 <strong>发货单号："+order_ckdh+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			   	   	fhdhtml=fhdhtml+"				  	 	 发货日期："+order_fhrq+"&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;";
			   	   	fhdhtml=fhdhtml+"				  	 	 已扫描数量："+order_ysmsl+"</strong>";
			   	   	fhdhtml=fhdhtml+"				   </td>";
			   	   	fhdhtml=fhdhtml+"				   <td class='active' rowspan='2' align='center' style='vertical-align: middle;'>";
//			   	   	fhdhtml=fhdhtml+"				  	 <button type='button' class='btn btn-success btn-lg' onclick='saomiao(\'"+order_ckdh+"\',\'"+order_ysmsl+"\')'>扫描</button>";
			   	   	fhdhtml=fhdhtml+'<button type="button" class="btn btn-success btn-lg" onclick="saomiao(\''+order_ckdh+'\',\''+order_ysmsl+'\')">扫描</button>';
			   	   	fhdhtml=fhdhtml+"				   </td>";
			   	   	fhdhtml=fhdhtml+"				 </tr>";
			   	   	fhdhtml=fhdhtml+"			 	<tr>";
			   	   	fhdhtml=fhdhtml+"				<td class='active' align='center'>";
			   	   	fhdhtml=fhdhtml+"				  	客户名称："+order_khmc+"&nbsp;&nbsp;";
			   	   	fhdhtml=fhdhtml+"				  	目的港："+order_mdh+"&nbsp;&nbsp;";
			   	   	fhdhtml=fhdhtml+"				  	柜号："+order_gh+" &nbsp;&nbsp;";
			   	   	fhdhtml=fhdhtml+"				  	封号："+order_fh+"</td>";
			   	   	fhdhtml=fhdhtml+"				</tr>";
			   	   	fhdhtml=fhdhtml+"			  </table>";
	   	    	});
	   	   		$("#fhdinfo").html(fhdhtml);
	   	   		getFenyeInfo(onePageJls);
	   	  	}
	   	});
 }

 function getFenyeInfo(onePageJls){
	$.ajax({
	   	url:"../../fhdwh/queryFhdFenyeInfoByXml?onePageJls="+onePageJls,
	   	type:"POST",  
	   	dataType:"xml",  
	   	error: function(xml){  
	       	alert('数据加载失败！');  
	   	},  
   		success: function(xml){ 
	   		var zjls=0;
        	var onePageJls=0;
        	var totalPagenum=0;
	   		$(xml).find("fenyeInfo").each(function(i){ 
	        	zjls=$(this).attr("zjls");
	        	onePageJls=$(this).attr("onePageJls");
	        	totalPagenum=$(this).attr("totalPagenum");
		   	});
			var htmlstr="<li><a href='javascript:void(0)' onclick='getPageInfo(1)'>首页</a></li>";
			for(var i=1;i<=totalPagenum*1;i++){
				htmlstr=htmlstr+"<li><a href='javascript:void(0)' onclick='getPageInfo("+i+")'>"+i+"</a></li>";
			}
			htmlstr=htmlstr+"<li><a href='javascript:void(0)' onclick='getPageInfo("+totalPagenum+")'>尾页</a></li>"
	   		$("#footer").html(htmlstr);
			$("#zjlsid").html(zjls);
			$("#zysid").html(totalPagenum);
      	}
   	});
 }
 
 
 function addTr(tab, row, trHtml){
	 //获取table最后一行 $("#tab tr:last")
	 //获取table第一行 $("#tab tr").eq(0)
	 //获取table倒数第二行 $("#tab tr").eq(-2)
	 var $tr=$("#"+tab+" tr").eq(row);
	 if($tr.size()==0){
		 alert("指定的table id或行数不存在！");
		 return;
	 }
	 $tr.after(trHtml);
 }

function addrow(){
	var khbh=$("#focid").val();
	if($.trim(khbh)==""){
		return;
	}
	var trhtml="";
 	trhtml=trhtml+"<tr>";
 	trhtml=trhtml+"   <td>"+khbh+"</td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td></td>";
 	trhtml=trhtml+"   <td><a href='javascript:void(0)' onclick='removerow(this)'>删除</a></td>";
 	trhtml=trhtml+" </tr>";
 	addTr("smtab", -2, trhtml);
 	var trcount = $("#smtab tr").length;//table总行数
 	$("#smjsid").html(trcount*1-2);//扫码计数
 	$("#smhjid").html(ysmsl*1+(trcount*1-2));//扫码合计

 	$("#focid").val("").focus();
}

function removerow(obj){
	 $(obj).parent().parent().remove(); 
	 var trcount = $("#smtab tr").length;//table总行数
	 $("#smjsid").html(trcount*1-2);//扫码计数
	 $("#smhjid").html(ysmsl*1+(trcount*1-2));//扫码合计
	 this.focus();
	 $("#focid").val("").focus();
}
$(document).keyup(function (e) {//捕获文档对象的按键弹起事件  
    if (e.keyCode == 13) {//按键信息对象以参数的形式传递进来了  
        addrow();
    }  
}); 
function saveSmxx(){
	var mtgss=$("#mtgsid").html();
    var khbhs="";
    var table = document.getElementById("smtab");
    for(var i = 1;i<table.rows.length-1;i++) // 索引 从1开始 不取表头的值
    {
    	var khbh =table.rows[i].cells[0].innerHTML; 
    	var scbh =table.rows[i].cells[1].innerHTML;
    	if(scbh==""){
 			alert("存在生产编号为空的行，请点击【查看详细】按钮获取生产编号或者删除此行编号不存在的数据！");
 			return ;
 		}
        if(i==table.rows.length-2){
 			khbhs=khbhs+khbh;
 		}else{
 			khbhs=khbhs+khbh+"@";
 		}
    }
    
    if(ckdh==""){
    	alert("出库单号为空，请检查！");
    	this.focus();
   	 	$("#focid").val("").focus();
    	return;
    }
    if(khbhs==""){
    	alert("您还未录入条码信息！");
    	this.focus();
    	$("#focid").val("").focus();
    	return;
    }
    var postdata = {};
    postdata.orderckdh=ckdh;
    postdata.orderkhbhs=khbhs;
    postdata.ordermtgs=mtgss;
	var	url = "../../fhdwh/saveSmxx";
	// 提交后台保存数据转换为JSON 格式
	var data = JSON.stringify(postdata);
	//保存提交后台
	if (url){
		ajaxSubmit(url, data, function(data) {
			if (parseInt(data['status']) > 0) {
				window.location.reload();
			} else {
				alert('保存条码明细信息失败');
			}
		  });
	}
	
}

function lookdetail(){
	var khbh=$("#focid").val();
	var cols="";
	var table = document.getElementById("smtab");
    for(var i = 1;i<table.rows.length-1;i++) // 索引 从1开始 不取表头的值
    {
    	var khbh =table.rows[i].cells[0].innerHTML; 
        if(i==table.rows.length-2){
        	cols=cols+khbh;
 		}else{
 			cols=cols+khbh+",";
 		}
    }
    if(cols==""){
    	return ;
    }

	$.ajax({
   	   	url:"../../fhdwh/queryBarcodeInfoByXmlnew?khbh="+cols,
   	   	type:"POST",
   	   	dataType:"xml",  
   	   	error: function(xml){
   	       	alert('数据加载失败！');
   	   	},
   		success: function(xml){ 
//   			var barcodehtml="";
   			//清空表格
//   			$("#smtab tr:not(:first):not(:last)").html("");
//   			return;
   	   		$(xml).find("item").each(function(i){ 
   	   			var barcode_khbh=$(this).attr("barcode_khbh");//客户编号
   	   			var barcode_sc=$(this).attr("barcode_sc");//生产编号
   	   			var barcode_upc=$(this).attr("barcode_upc");//客户UPC码
   	   			var barcode_ks=$(this).attr("barcode_ks");//款式
   	   			var barcode_gg=$(this).attr("barcode_gg");//规格
   	   			var barcode_pj=$(this).attr("barcode_pj");//偏距
   	   			var barcode_pcd=$(this).attr("barcode_pcd");//PCD
   	   			var barcode_zxk=$(this).attr("barcode_zxk");//中心孔
   	   			var barcode_bmzt=$(this).attr("barcode_bmzt");//表面状态
   	   			var sl="1";//数量
   	   			table.rows[i+1].cells[1].innerHTML=barcode_sc;
   	   			table.rows[i+1].cells[2].innerHTML=barcode_upc;
   	   			table.rows[i+1].cells[3].innerHTML=barcode_ks;
   	   			table.rows[i+1].cells[4].innerHTML=barcode_gg;
   	   			table.rows[i+1].cells[5].innerHTML=barcode_pj;
   	   			table.rows[i+1].cells[6].innerHTML=barcode_pcd;
   	   			table.rows[i+1].cells[7].innerHTML=barcode_zxk;
   	   			table.rows[i+1].cells[8].innerHTML=barcode_bmzt;
   	   			table.rows[i+1].cells[9].innerHTML=sl;
//	   	   		var trhtml="";
//		   	 	trhtml=trhtml+"<tr>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_khbh+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_sc+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_upc+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_ks+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_gg+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_pj+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_pcd+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_zxk+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+barcode_bmzt+"</td>";
//		   	 	trhtml=trhtml+"   <td>"+sl+"</td>";
//		   	 	trhtml=trhtml+"   <td><a href='javascript:void(0)' onclick='removerow(this)'>删除</a></td>";
//		   	 	trhtml=trhtml+" </tr>";
//		   	 	addTr("smtab", -2, trhtml);
		   	 	//var trcount = $("#smtab tr").length;//table总行数
		   	 	//$("#smjsid").html(trcount*1-2);//扫码计数
		   	 	//$("#smhjid").html(ysmsl*1+(trcount*1-2));//扫码合计
   	    	});
   	  	}
   	});
	
	$("#focid").val("").focus();
}