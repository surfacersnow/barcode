package com.ruisoft.business.exportExcel.controller;

import com.ruisoft.business.exportExcel.bean.Project;
import com.ruisoft.business.exportExcel.util.ExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/2.
 */
@Controller
@RequestMapping("/export/")
public class ExportController {

     @RequestMapping("download")
     public String download(HttpServletRequest request,HttpServletResponse response) throws IOException {
         String fileName="excel文件";
         //填充projects数据
         List<Project> projects= createData();;
         List<Map<String,Object>> list=createExcelRecord(projects);
         String columnNames[]={"ID","项目名","销售人","负责人","所用技术","备注"};//列名
         String keys[] = {"id","name","saler","principal","technology","remarks"};//map中的key
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         try {
             ExcelUtil.createWorkBook(list, keys, columnNames).write(os);
         } catch (IOException e) {
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
             // Simple read/write loop.
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
         return null;
     }

     private List<Project> createData() {
         //自己实现
         List dataList = new ArrayList();
         for(int i=1;i<5;i++){
             Project project = new Project();
             project.setId(i);
             project.setName("name"+i);
             project.setRemarks("Remarks"+i);
             project.setTechnology("Technology"+i);
             project.setSaler("saler"+i);
             dataList.add(project);
         }
         return dataList;
     }

     private List<Map<String, Object>> createExcelRecord(List<Project> projects) {
         List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("sheetName", "sheet1");
         listmap.add(map);
         Project project=null;
         for (int j = 0; j < projects.size(); j++) {
             project=projects.get(j);
             Map<String, Object> mapValue = new HashMap<String, Object>();
             mapValue.put("id", project.getId());
             mapValue.put("name", project.getName());
             mapValue.put("technology", project.getTechnology());
             mapValue.put("remarks", project.getRemarks());
             mapValue.put("saler",project.getSaler());
             listmap.add(mapValue);
         }
         return listmap;
     }

}
