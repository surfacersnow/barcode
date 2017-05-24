package com.ruisoft.business.barcode_tm.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruisoft.base.controller.BaseController;
import com.ruisoft.business.barcode_fhd.util.excelLayoutUtil;
import com.ruisoft.business.barcode_tm.service.TmwhService;
import com.ruisoft.business.exportExcel.bean.Project;
import com.ruisoft.business.exportExcel.util.ExcelUtil;
import com.ruisoft.core.json.JSONMap;

/**
 * 
 * @author LFC
 *
 */
@Controller
@RequestMapping("/tmwh/")
public class TmwhController extends BaseController{

	private static final Logger LOG = Logger.getLogger(TmwhController.class);

	@Autowired
	private TmwhService tmwhService;
	
	/**
	 * 导入大地条码基本数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("excelUpload")
	public void heatExcelUpload(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		String msg="";
		try {
			String url = request.getSession().getServletContext().getRealPath("/");
			url=url+File.separator+"upload";
			String excelFilePath = uploadFile(url);
			System.out.println("------------excelFilePath::::::::::"+ excelFilePath);
			msg= tmwhService.saveBarcodeData(excelFilePath);
			json.put("status", "1");
			json.put("msg", msg);
		} catch (Exception e) {
			LOG.error("导入excel发生错误", e);
			json.put("status", "-2");
			json.put("msg", "导入excel发生错误");
		} finally {
			try {
				response(json);
			} catch (IOException e) {
				LOG.error("导入excel发生错误", e);
			}
		}
	}
	
	
	@RequestMapping("query")
	public void query() {
		JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		if (queryData == null)
			queryData = getQueryData();
		try {
			List<JSONObject> result = tmwhService.searchForPage(queryData);
			queryResponse(queryData, result);
		} catch (Exception e) {
			LOG.error("查询条码基本数据错误！", e);
			json.put("status", "-2");
			json.put("msg", "执行查询条码基本数据操作发生错误");
		} finally {
			try {
				response(json);
			} catch (IOException e) {
				LOG.error("查询返回信息时发生错误", e);
			}
			queryData = null;
		}
	}
	
	@RequestMapping("saveBarcode")
	public void saveBarcode() {
		JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		try {
			JSONObject reqData = getReqData();
			JSONObject result = new JSONObject();
			result = tmwhService.saveBarcode(reqData);
			json.put("result", result);
			json.put("status", "1");
		} catch (Exception e) {
			LOG.error("保存条码基本数据信息发生错误", e);
			json.put("status", "-2");
			json.put("msg", "保存条码基本数据信息发生错误");
		} finally {
			try {
				response(json);
			} catch (IOException e) {
				LOG.error("返回信息时发生错误", e);
			}
		}
	}
	
	@RequestMapping("deleteBarcode/{id}")
    public void deleteBarcode(@PathVariable String id) throws Exception{
        int status = tmwhService.deleteBarcode(id);
        JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        json.put("status", status);
        response(json);
    }
	
	 @RequestMapping("updateBarcode")
	   public void updateBarcode() {
	       JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
	       try {
	           JSONObject reqData = getReqData();
	           JSONObject result = new JSONObject();
	           result = tmwhService.updateBarcode(reqData);
	           json.put("data", result);
	           json.put("status", "1");
	       } catch (Exception e) {
	           LOG.error("更新条码基本数据发生错误", e);
	           json.put("status", "-2");
	           json.put("msg", "更新条码基本数据发生错误");
	       } finally {
	           try {
	               response(json);
	           } catch (IOException e) {
	               LOG.error("返回信息时发生错误", e);
	           }
	       }
	   }
	 
	 @RequestMapping("searchBarcode/{khbh}")
		public void searchBarcode(@PathVariable String khbh) throws Exception {
		 JSONObject result = tmwhService.searchBarcode(khbh);
		 JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		 json.put("data", result);
		 json.put("status",1);
		 try {
			 response(json);
		 } catch (IOException e) {
			 LOG.error("查询返回信息时发生错误", e);
		 }
			
		}
	 @RequestMapping("printBarcode/{khbh}")
		public void printBarcode(@PathVariable String khbh) throws Exception {
		 JSONObject result = tmwhService.printBarcode(khbh);
		 JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		 json.put("data", result);
		 json.put("status",1);
		 try {
			 response(json);
		 } catch (IOException e) {
			 LOG.error("查询返回信息时发生错误", e);
		 }
			
		}
	 
	 @RequestMapping("checkBarcodeKhbh/{khbh}")
		public void checkBarcodeCp(@PathVariable String khbh) throws Exception {
		 JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		 boolean result = tmwhService.isExistsBh(khbh);
		 json.put("falg", result);
		 try {
			 response(json);
		 } catch (IOException e) {
			 LOG.error("查询返回信息时发生错误", e);
		 }
			
		}
	 
	 @RequestMapping("downloadExcel")
	    public String downloadExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		 	String url = request.getSession().getServletContext().getRealPath("/")+"download";
		 	String filename="条码基础数据信息导入模板格式.xls";
		 	String filePath = url + File.separator + filename;
		 	File file = new File(filePath);  //根据文件路径获得File文件
		 	response.setContentType("application/vnd.ms-excel");
		 	response.setHeader("Content-Disposition", "attachment;filename=\""
		            + new String(filename.getBytes("GBK"), "ISO8859-1") + "\"");
		 	response.setContentLength((int) file.length());
		 	byte[] buffer = new byte[2048];// 缓冲区
	        BufferedOutputStream output = null;
	        BufferedInputStream input = null;
	        try {
	            output = new BufferedOutputStream(response.getOutputStream());
	            input = new BufferedInputStream(new FileInputStream(file));
	            int n = -1;
	            //遍历，开始下载
	            while ((n = input.read(buffer, 0, buffer.length)) > -1) {
	               output.write(buffer, 0, n);
	            }
	            output.flush();   //不可少
	            response.flushBuffer();//不可少
	          } catch (Exception e) {
	            //异常自己捕捉       
	          } finally {
	             //关闭流，不可少
	             if (input != null)
	                  input.close();
	             if (output != null)
	                  output.close();
	          }
			return null;
	 }
	 
		@RequestMapping("queryDetailPrint/{ckdh}")
		public void queryDetailPrint(@PathVariable String ckdh) {
			List<JSONObject> barcodelist=null;
			JSONObject jsn = new JSONObject();
			jsn.put("order_ckdh", ckdh);
			try {
				barcodelist = tmwhService.queryDetailPrint(jsn);
			   String barlist = JSONArray.fromObject(barcodelist).toString();
			   response(barlist);
			} catch (Exception e) {
				LOG.error("查询发货单明细发生错误", e);
			} 
		}
		
		/**
		 * 导出发货单excel
		 * @param request
		 * @param response
		 * @throws IOException
		 */
		@RequestMapping("exportExcel")
		 public void exportExcel(HttpServletRequest request,HttpServletResponse response) throws IOException {
	         String ckdh = request.getParameter("ckdh");
	         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//可以方便地修改日期格式
	         String today = dateFormat.format(new Date()); 
	         String fileName=today+"("+ckdh+")集装箱发货清单";
	         JSONObject jsn = new JSONObject();
			 jsn.put("order_ckdh", ckdh);
			 List<JSONObject> barcodelist = null;
			 JSONObject CkdPrint = new JSONObject();
			 ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					barcodelist = tmwhService.queryDetailPrint(jsn);
					CkdPrint = tmwhService.queryCkdPrint(ckdh);
					 HSSFWorkbook workbook =  excelLayoutUtil.createWorkBook(barcodelist,CkdPrint);
		        	 workbook.write(os);
				} catch (Exception e) {
					e.printStackTrace();
				}
	         byte[] content = os.toByteArray();
	         InputStream is = new ByteArrayInputStream(content);
	         // 设置response参数，可以打开下载页面
	         response.reset();
	         response.setContentType("application/vnd.ms-excel");
	         response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes("GBK"), "iso-8859-1"));
	         ServletOutputStream out = response.getOutputStream();
	         BufferedInputStream bis = null;
	         BufferedOutputStream bos = null;
	         try {
	             bis = new BufferedInputStream(is);
	             bos = new BufferedOutputStream(out);
	             byte[] buff = new byte[2048];
	             int bytesRead;
	             while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                 bos.write(buff, 0, bytesRead);
	             }
	         } catch (final IOException e) {
	             throw e;
	         } finally {
	             if (bis != null)
	                 bis.close();
	             if (bos != null)
	                 bos.close();
	         }
	     }
		 
		@RequestMapping("queryCkdPrint/{ckdh}")
		public void queryCkdPrint(@PathVariable String ckdh) {
			 JSONObject result = new JSONObject();
			try {
				result = tmwhService.queryCkdPrint(ckdh);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			 JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
			 json.put("data", result);
			 json.put("status",1);
			 try {
				 response(json);
			 } catch (IOException e) {
				 LOG.error("查询返回信息时发生错误", e);
			 }
		}
		
	    @RequestMapping("/queryBarcodeSc")
	    public void getBarcodeSc() {
	        try {
	            String result = tmwhService.getBarcodeSc();
	            response(result);
	        } catch (Exception e) {
	            LOG.error("获取字典项定义发生错误", e);
	        }
	    }
		
}
