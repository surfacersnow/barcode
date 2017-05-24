package com.ruisoft.business.barcode_tm.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruisoft.base.bean.QueryData;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.base.service.BaseService;
import com.ruisoft.business.excelUtil.ExcelReader;

@Service
public class TmwhService   extends BaseService{

	@Autowired
	public BaseDao baseDao;

	public String saveBarcodeData(String excelFilePath) throws  Exception {
		StringBuffer warn = new StringBuffer();
		String success = "恭喜，导入excel成功！";
		ExcelReader excel = new ExcelReader(excelFilePath);
		List dataList = excel.getAllData(0);
		if(dataList.size()>0 && dataList !=null) {
			for(int i=1; i<dataList.size(); i++) {
				String[] row = (String[]) dataList.get(i);
				if(isExistsBh(row[3])){
					warn.append("第"+(i+1)+"行编号已经存在，没有导入！");
					continue;
				}
				JSONObject barcodeData=new JSONObject();
				barcodeData.put("barcode_khmc", row[1]);
				barcodeData.put("barcode_ks", row[2]);
				if("".equals(row[3].trim())){
					warn.append("第"+(i+1)+"行客户编号为空，没有导入！");
					continue;
				}
				barcodeData.put("barcode_khbh", row[3]);
				if("".equals(row[4].trim())){
					warn.append("第"+(i+1)+"行客户UPC为空，没有导入！");
					continue;
				}
				barcodeData.put("barcode_upc", row[4]);
				if("".equals(row[5].trim())){
					warn.append("第"+(i+1)+"行生产编号为空，没有导入！");
					continue;
				}
				barcodeData.put("barcode_sc", row[5]);
				barcodeData.put("barcode_gg", row[6]);
				barcodeData.put("barcode_pj", row[7]);
				barcodeData.put("barcode_pcd", row[8]);
				barcodeData.put("barcode_zxk", row[9]);
				barcodeData.put("barcode_bmzt", row[10]);
				barcodeData.put("barcode_bmztyw", row[11]);
				if("".equals(row[12].trim())){
					row[12] = "0";
				}
				barcodeData.put("barcode_djz", row[12]);
				if("".equals(row[13].trim())){
					row[13] = "0";
				}
				barcodeData.put("barcode_mjz", row[13]);
				if("".equals(row[14].trim())){
					row[14] = "0";
				}
				barcodeData.put("barcode_djt", row[14]);
				barcodeData.put("barcode_bzfs", row[15]);
				String bqbh = "V1";
				if(row.length>15){
					if(!"".equals(row[16].trim())&&row[16]!=null){
						bqbh = row[16].trim();
					}
				}
				barcodeData.put("barcode_bqbh", bqbh);
				JSONObject json= baseDao.add(barcodeData, "tmwh.add.barcode.add");
			}
		}
		if(!"".equals(warn.toString())){
			success =  warn.toString();
		}
		return success;
	}


	public List<JSONObject> searchForPage(QueryData q) throws Exception {
		List<JSONObject> itemData = search(q, "tmwh.select.barcode.query");
		return itemData;
	}

	public JSONObject saveBarcode(JSONObject jsonData) throws Exception {
		//保存数据
		baseDao.add(jsonData, "tmwh.add.barcode.add");
		return jsonData;
	}

	public int deleteBarcode(String id) throws Exception {
		int status = 0;
        status = baseDao.deleteCode(id,"tmwh.delete.barcode.delete");
        return status;
	}

	public JSONObject updateBarcode(JSONObject jsonData) throws Exception {
		baseDao.update(jsonData, "tmwh.update.barcode.update");
        return jsonData;
	}
	
	public JSONObject searchBarcode(String barcode_bh) throws Exception{
		List<JSONObject> barcodeDataList = baseDao.queryId(barcode_bh, "tmwh.select.barcode.barcode_bh");
		JSONObject barcodedata = new JSONObject();
		if(barcodeDataList.size()>0)
			barcodedata = barcodeDataList.get(0);
		return barcodedata;
	}
	
	public JSONObject printBarcode(String scbh) throws Exception{
		List<JSONObject> barcodeDataList = baseDao.queryId(scbh, "tmwh.select.barcode.barcode_sc");
		JSONObject barcodedata = new JSONObject();
		if(barcodeDataList.size()>0)
			barcodedata = barcodeDataList.get(0);
		return barcodedata;
	}
	
	
	public boolean isExistsBh(String barcode_bh){
		boolean flag = false;
		try {
			List<JSONObject> barcodeDataList = baseDao.queryId(barcode_bh, "tmwh.select.barcode.barcode_bh");
			if(barcodeDataList!=null&&barcodeDataList.size()>0){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public boolean isBigDecimal(String str) {
		try {
			new BigDecimal(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<JSONObject> queryDetailPrint(JSONObject json) throws Exception {
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.detailsum.queryXml");
    	return DataList;
	}
	
	public JSONObject queryCkdPrint(String order_ckdh) throws Exception{
		List<JSONObject> CkdPrintDataList = baseDao.queryId(order_ckdh, "fhdwh.select.crm_order.order_ckdh");
		JSONObject CkdPrint = new JSONObject();
		if(CkdPrintDataList.size()>0)
			CkdPrint = CkdPrintDataList.get(0);
		return CkdPrint;
	}
	
	public String getBarcodeSc() throws Exception{
		List<JSONObject> BarcodeScDataList = baseDao.query(" SELECT BARCODE_SC FROM crm_barcode ");
		JSONArray itemArray = new JSONArray();  
		for (JSONObject i : BarcodeScDataList) {
			itemArray.add(i.getString("barcode_sc"));
	        }
		 String barscstr = itemArray.toString();
		return barscstr;
	}
	
}
