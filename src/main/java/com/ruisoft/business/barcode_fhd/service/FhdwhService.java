package com.ruisoft.business.barcode_fhd.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruisoft.base.bean.QueryData;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.base.service.BaseService;
import com.ruisoft.common.CommonUtil;
import com.ruisoft.core.param.RequestUtil;

@Service
public class FhdwhService extends BaseService{

	@Autowired
	public BaseDao baseDao;

	public List<JSONObject> searchForPage(QueryData q) throws Exception {
		List<JSONObject> itemData = search(q, "fhdwh.select.crm_order.query");
		return itemData;
	}

	public JSONObject saveFhd(JSONObject jsonData) throws Exception {
		baseDao.add(jsonData, "fhdwh.add.crm_order.add");
		return jsonData;
	}

	public int deleteFhd(String id) throws Exception {
		int status = 0;
		try {
			baseDao.deleteCode(id,"fhdwh.delete.crm_order.delete");
			baseDao.deleteCode(id,"fhdwh.delete.crm_orderdetail.delete");
			baseDao.deleteCode(id,"fhdwh.delete.crm_orderdetailsum.delete");
			status=1;
		} catch (Exception e) {
			status=-1;
		}
        return status;
	}

	public JSONObject updateFhd(JSONObject jsonData) throws Exception {
		baseDao.update(jsonData, "fhdwh.update.crm_order.update");
        return jsonData;
	}
	public List<JSONObject> queryFhdInfoByXml(JSONObject json) throws Exception {
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.crm_order.queryXml");
    	return DataList;
	}
	public int queryFhdFenyeInfoByXml(String sql) throws Exception {
		int count=baseDao.count(sql);
    	return count;
	}
	public List<JSONObject> queryBarcodeInfoByXml(JSONObject json) throws Exception {
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.crm_barcode.queryXml");
    	return DataList;
	}
	public JSONObject saveSmxx(String orderckdh,String orderkhbhs,String ordermtgs) throws Exception {
		String bzr=RequestUtil.getCurrentUserAccount();
		String bzsj=CommonUtil.getCurDateTime();
		String[] khbhs=orderkhbhs.split("@");
		JSONObject result = new JSONObject();
		for(int i=0;i<khbhs.length;i++){
			JSONObject smxxJson = new JSONObject();
			UUID uuid = UUID.randomUUID();
			smxxJson.put("orderDetail_id", uuid.toString().replace("-", ""));
			smxxJson.put("orderDetail_ckdh", orderckdh);//出库单号
			smxxJson.put("orderDetail_khbh", khbhs[i]);//客户编号
			smxxJson.put("orderDetail_mtgs", Integer.parseInt(ordermtgs));//马托个数
			smxxJson.put("orderDetail_sl", 1);//数量
			smxxJson.put("orderDetail_bzsj", bzsj);//编制时间
			smxxJson.put("orderDetail_bzr", bzr);//编制人
			result=baseDao.add(smxxJson, "fhdwh.add.crm_order_detail.add");
		}
		JSONObject json = new JSONObject();
		json.put("orderDetail_ckdh", orderckdh);
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.crm_order_detailhz.queryXml");
		String sql2="delete from crm_order_detail_sum where detailsum_ckdh='"+orderckdh+"'";
		baseDao.execute(sql2);
		for(int i=0;i<DataList.size();i++){
			JSONObject jsn = (JSONObject) DataList.get(i);
			System.out.println("jsn= "+jsn);
			String orderDetail_khbh = jsn.get("orderdetail_khbh").toString();
			String orderdetail_sl = jsn.get("orderdetail_sl").toString();
			String barcode_mjz = jsn.get("barcode_mjz").toString();
			String barcode_djz = jsn.get("barcode_djz").toString();
			String barcode_djt = jsn.get("barcode_djt").toString();
			
			JSONObject smxxJson = new JSONObject();
			UUID uuid = UUID.randomUUID();
			smxxJson.put("detailsum_id", uuid.toString().replace("-", ""));
			smxxJson.put("detailsum_ckdh", orderckdh);//出库单号
			smxxJson.put("detailsum_khbh", orderDetail_khbh);//客户编号
			smxxJson.put("detailsum_sl", Integer.parseInt(orderdetail_sl));//数量
			smxxJson.put("detailsum_zmz", Double.parseDouble(barcode_mjz));//总毛重
			smxxJson.put("detailsum_zjz", Double.parseDouble(barcode_djz));//总净重
			smxxJson.put("detailsum_ztj", Double.parseDouble(barcode_djt));//总体积
			smxxJson.put("detailsum_bzsj", bzsj);//编制时间
			smxxJson.put("detailsum_bzr", bzr);//编制人
			smxxJson.put("detailsum_xgsj", bzsj);//修改时间
			smxxJson.put("detailsum_xgr", bzr);//修改人
			result=baseDao.add(smxxJson, "fhdwh.add.crm_order_detail_sum.add");
			
		}
		
		
		return result;
	}
	public Map<String, Object> queryMtgsByCkdh(String sql) throws Exception {
		Map<String, Object> map=baseDao.queryForMap(sql);
    	return map;
	}
	public List<JSONObject> getDetailsum(JSONObject json) throws Exception {
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.detailsum.queryXml");
    	return DataList;
	}
	public int qrfhd(String sql) throws Exception {
		int result=baseDao.update(sql);
		return result;
	}
	public List<JSONObject> queryDetailInfoByXml(JSONObject json) throws Exception {
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.detailinfo.queryXml");
    	return DataList;
	}
	public void deleteDetailByid(String sql,String orderDetail_ckdh) throws Exception {
		baseDao.execute(sql);
		
		String sql2="delete from crm_order_detail_sum where detailsum_ckdh='"+orderDetail_ckdh+"'";
		baseDao.execute(sql2);
		
		JSONObject json = new JSONObject();
		json.put("orderDetail_ckdh", orderDetail_ckdh);
		List<JSONObject> DataList = baseDao.query(json,  "fhdwh.select.crm_order_detailhz.queryXml");
		String bzr=RequestUtil.getCurrentUserAccount();
		String bzsj=CommonUtil.getCurDateTime();
		for(int i=0;i<DataList.size();i++){
			JSONObject jsn = (JSONObject) DataList.get(i);
			System.out.println("jsn= "+jsn);
			String orderDetail_khbh = jsn.get("orderdetail_khbh").toString();
			String orderdetail_sl = jsn.get("orderdetail_sl").toString();
			String barcode_mjz = jsn.get("barcode_mjz").toString();
			String barcode_djz = jsn.get("barcode_djz").toString();
			String barcode_djt = jsn.get("barcode_djt").toString();
			
			JSONObject smxxJson = new JSONObject();
			UUID uuid = UUID.randomUUID();
			smxxJson.put("detailsum_id", uuid.toString().replace("-", ""));
			smxxJson.put("detailsum_ckdh", orderDetail_ckdh);//出库单号
			smxxJson.put("detailsum_khbh", orderDetail_khbh);//客户编号
			smxxJson.put("detailsum_sl", Integer.parseInt(orderdetail_sl));//数量
			smxxJson.put("detailsum_zmz", Double.parseDouble(barcode_mjz));//总毛重
			smxxJson.put("detailsum_zjz", Double.parseDouble(barcode_djz));//总净重
			smxxJson.put("detailsum_ztj", Double.parseDouble(barcode_djt));//总体积
			smxxJson.put("detailsum_bzsj", bzsj);//编制时间
			smxxJson.put("detailsum_bzr", bzr);//编制人
			smxxJson.put("detailsum_xgsj", bzsj);//修改时间
			smxxJson.put("detailsum_xgr", bzr);//修改人
			baseDao.add(smxxJson, "fhdwh.add.crm_order_detail_sum.add");
			
		}
	}
	public int judgeCkdhRepeat(String sql) throws Exception {
		int count=baseDao.count(sql);
    	return count;
	}
}
