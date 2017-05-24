package com.ruisoft.business.barcode_fhd.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 * 装箱单格式导出到excel
 * @author lenovo
 *
 */
public class excelLayoutUtil {

	@SuppressWarnings("deprecation")
	public static HSSFWorkbook createWorkBook(List<JSONObject> list,JSONObject CkdPrint){
        // 创建excel工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
        String columnNames[] = {"序号","客户编号","生产编号","规格","数量","状态","单净重","单毛重","总净重","总毛重","单位","单体积","总体积","包装方式"};
        String keys[] = {"xuhao","detailsum_khbh","barcode_sc","barcode_gg","detailsum_sl","barcode_bmzt","barcode_djz","barcode_mjz","detailsum_zjz","detailsum_zmz","dw","barcode_djt","detailsum_ztj","barcode_bzfs"};
        String sheetName = "成品库发货清单";
        // 创建第一个sheet（页），并命名
        HSSFSheet sheet = wb.createSheet(sheetName);
        //设置列宽
        sheet.setColumnWidth(0, 15*256);//序号
        sheet.setColumnWidth(1, 20*256);//客户编号
        sheet.setColumnWidth(2, 20*256);//生产编号
        sheet.setColumnWidth(3, 10*256);//规格
        sheet.setColumnWidth(4, 10*256);//数量
        sheet.setColumnWidth(5, 25*256);//状态
        sheet.setColumnWidth(6, 10*256);//单净重
        sheet.setColumnWidth(7, 10*256);//单毛重
        sheet.setColumnWidth(8, 10*256);//总净重
        sheet.setColumnWidth(9, 10*256);//总毛重
        sheet.setColumnWidth(10, 10*256);//单位
        sheet.setColumnWidth(11, 10*256);//单体积
        sheet.setColumnWidth(12, 10*256);//总体积
        sheet.setColumnWidth(13, 15*256);//包装方式
//        for(int i=0;i<columnNames.length;i++){
              //自动设置列宽
//            sheet.setColumnWidth(i, 10);
//        }
        // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
        // 创建第一行
        HSSFRow titlerow = sheet.createRow((short) 0);
        HSSFCell titlecell = titlerow.createCell(0);
        titlecell.setCellValue("泰安和新精工科技有限公司成品库发货清单");
        // 创建标题行格式
        HSSFCellStyle titleStyle = wb.createCellStyle();
        //创建标题行字体
        HSSFFont titleFont =  wb.createFont(); 
        titleFont.setFontName("宋体");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setCharSet(HSSFFont.DEFAULT_CHARSET);
        titleFont.setColor(HSSFColor.BLUE_GREY.index);
        
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        titleStyle.setFont(titleFont);//字体
        titleStyle.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);// 背景色
        titlecell.setCellStyle(titleStyle);
        //合并单元格
        sheet.addMergedRegion( new CellRangeAddress(0, 0, 0, 13));
        //创建第2行，副标题行
        HSSFRow row2 = sheet.createRow((short) 1);
        row2.createCell(0).setCellValue("提货单号：");
        row2.createCell(1).setCellValue(CkdPrint.get("order_tdh").toString());
        row2.createCell(9).setCellValue("发货编号：");
        row2.createCell(10).setCellValue(CkdPrint.get("order_ckdh").toString());
        sheet.addMergedRegion( new CellRangeAddress(1, 1, 10, 13));
        HSSFRow row3 = sheet.createRow((short) 2);
        row3.createCell(0).setCellValue("客户：");
        row3.createCell(1).setCellValue(CkdPrint.get("order_khmc").toString());
        row3.createCell(2).setCellValue("目的港：");
        row3.createCell(3).setCellValue(CkdPrint.get("order_mdh").toString());
        sheet.addMergedRegion( new CellRangeAddress(2, 2, 3, 4));
        row3.createCell(5).setCellValue("柜号：");
        row3.createCell(6).setCellValue(CkdPrint.get("order_gh").toString());
        sheet.addMergedRegion( new CellRangeAddress(2, 2, 6, 8));
        row3.createCell(9).setCellValue("封号：");
        row3.createCell(10).setCellValue(CkdPrint.get("order_fh").toString());
        sheet.addMergedRegion( new CellRangeAddress(2, 2, 10, 13));
        //创建第4行，表头行
        HSSFRow headrow = sheet.createRow((short) 3);
        // 创建表头行格式
        HSSFCellStyle headStyle = wb.createCellStyle();
        //创建表头行字体
        HSSFFont headFont =  wb.createFont(); 
        headFont.setFontName("宋体");//
        headFont.setFontHeightInPoints((short) 11);//
        headFont.setCharSet(HSSFFont.DEFAULT_CHARSET);//
        
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        headStyle.setFont(headFont);//字体
//        titleStyle.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);// 背景色
        //设置列名
        for(int i=0;i<columnNames.length;i++){
        	HSSFCell cell = headrow.createCell(i);
        	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        	cell.setCellValue(columnNames[i]);
        	cell.setCellStyle(headStyle);
        }
        int count = list.size();
//        HSSFCell datcell = null;
        //设置每行每列的值
        for (int i = 0; i < count; i++) {
        	// 创建一行，在页sheet上
        	HSSFRow datarow = sheet.createRow((short) i+4);
        	JSONObject data = list.get(i);
//        	datcell = datarow.createCell(keys.length);
        	// 在row行上创建一个方格
        	for(int j=0;j<keys.length;j++){
        		HSSFCellStyle dataStyle = wb.createCellStyle();
        		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        		dataStyle.setFont(headFont);//字体
        		HSSFCell cell = datarow.createCell(j);
        		if(j==0){
        			cell.setCellValue(i+1);
        		}else if(j==10){
        			cell.setCellValue("kgs");
        		}else if(j==4){
        			BigDecimal sl = new BigDecimal("0");
        			if(data.get(keys[j]) != null&&!"".equals(data.get(keys[j]).toString())){
        				sl = new BigDecimal(data.get(keys[j]).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        			}
        			cell.setCellValue(sl.doubleValue());
        			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        			HSSFDataFormat format = wb.createDataFormat();
        			dataStyle.setDataFormat(format.getFormat("0"));//设置单元类型
        		}else if(j>=6&&j<=12){
        			BigDecimal sl = new BigDecimal("0");
        			if(data.get(keys[j]) != null&&!"".equals(data.get(keys[j]).toString())){
        				sl = new BigDecimal(data.get(keys[j]).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        			}
        			cell.setCellValue(sl.doubleValue());
        			//设置6-12列为数值类型，格式为0.00
        			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        			HSSFDataFormat format = wb.createDataFormat();
        			dataStyle.setDataFormat(format.getFormat("0.00"));//设置单元类型
        		} else{
        			cell.setCellValue(data.get(keys[j]) == null?"": data.get(keys[j]).toString());
        		}
        		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        		cell.setCellStyle(dataStyle);
        	}
        }
        int lastrow = count+4;
        //编辑合计行
        HSSFRow hjrow = sheet.createRow((short)lastrow);
        for(int i=0;i<columnNames.length;i++){
        	HSSFCell hjcell = hjrow.createCell(i);
        	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        	hjcell.setCellStyle(headStyle);
        }
//    	HSSFCellStyle hjStyle = wb.createCellStyle();
//    	hjStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
//    	hjStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
//    	hjStyle.setFont(headFont);//字体
//    	hjStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//    	hjStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
//    	hjStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//    	hjStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//    	hjrow.getCell(0).setCellStyle(hjStyle);
    	hjrow.getCell(0).setCellValue("合计");
    	//合计单元格
    	sheet.addMergedRegion( new CellRangeAddress(lastrow, lastrow, 0, 3));
//    	HSSFCell hjcell4 = hjrow.createCell(4);
//    	HSSFCell hjcell8 = hjrow.createCell(8);
//    	HSSFCell hjcell9 = hjrow.createCell(9);
//    	HSSFCell hjcell12 = hjrow.createCell(12);
    	hjrow.getCell(4).setCellFormula("SUM(E5:E"+lastrow+")");
    	hjrow.getCell(8).setCellFormula("SUM(I5:I"+lastrow+")");
    	hjrow.getCell(9).setCellFormula("SUM(J5:J"+lastrow+")");
    	hjrow.getCell(12).setCellFormula("SUM(M5:M"+lastrow+")");
//    	HSSFCell hjcell5 = hjrow.createCell(5);
//    	HSSFCell hjcell6 = hjrow.createCell(6);
//    	HSSFCell hjcell7 = hjrow.createCell(7);
//    	HSSFCell hjcell10 = hjrow.createCell(10);
//    	HSSFCell hjcell11 = hjrow.createCell(11);
//    	HSSFCell hjcell13 = hjrow.createCell(13);
//    	hjcell5.setCellStyle(hjStyle);
//    	hjcell6.setCellStyle(hjStyle);
//    	hjcell7.setCellStyle(hjStyle);
//    	hjcell10.setCellStyle(hjStyle);
//     	hjcell11.setCellStyle(hjStyle);
//     	hjcell13.setCellStyle(hjStyle);
    	//联
//    	HSSFCellStyle style = wb.createCellStyle();
//    	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
//    	style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
//    	style.setWrapText(true);//自动换行
//    	datcell.setCellValue("一联:仓库 二联:财务 三联:出门");
//    	style.setRotation((short)90);//文本旋转，这里的取值是从-90到90，而不是0-180度。
//    	datcell.setCellStyle(style);
//    	sheet.addMergedRegion( new CellRangeAddress(4, lastrow, 14, 14));
    	//审核行
    	HSSFRow shrow = sheet.createRow((short)lastrow+2);
    	shrow.createCell(1).setCellValue("批准：");
    	shrow.createCell(4).setCellValue("审核：");
    	shrow.createCell(8).setCellValue("仓库：");
    	//冻结列数，冻结的行数，右边区域可见的首列序号，下边区域可见的首行序号
    	sheet.createFreezePane(14, 4);//冻结行
        return wb;
	}
	
}
