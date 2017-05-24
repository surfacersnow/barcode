package com.ruisoft.base.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.avalon.framework.parameters.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruisoft.base.bean.QueryData;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.common.CommonUtil;
import com.ruisoft.core.key.KeyGenerator;

@Service
public class BaseService {
    @Autowired
    public BaseDao baseDao;

    @Autowired
    protected KeyGenerator keyGenerator;

    /**
     * 根据SQL查询
     * @param sql
     * @return
     */
    public int count(String sql) {
        return baseDao.count(sql);
    }

    /**
     * 主从表查询
     * @param id
     * @param sqlz
     * @param sqlc
     * @return
     * @throws Exception
     */
    public JSONObject searchDetail(String id,String sqlz,String sqlc) throws Exception {
        List<JSONObject> zhudata =  baseDao.queryId(id,sqlz);
        List<JSONObject> items =  baseDao.queryId(id,sqlc);
        JSONObject result = zhudata.get(0);
        result.put("items", JSONArray.fromObject(items).toString());
        return result;
    }

    /**
     * 根据ID查询表数据
     * @param id
     * @param sql
     * @return
     * @throws Exception
     */
    public List<JSONObject> searchData(String id,String sql) throws Exception {
        List<JSONObject> data =  baseDao.queryId(id,sql);
        return data;
    }

    /**
     * 分页查找
     * @param json
     * @param sqlid
     * @return
     * @throws JSONException 
     * @throws ParameterException 
     * @throws Exception
     */
    public List<JSONObject> searchPage(JSONObject json,String sqlid) throws ParameterException, JSONException  {
        List<JSONObject> result = null;
        // 当前页码
        int pageNum = !json.has("pageNum") ? 1 : json.getInt("pageNum");
        // 每页记录数
        int pageSize = !json.has("pageSize") ? 20 : json.getInt("pageSize");

        String data = json.getString("data");
        result = baseDao.queryForPage(sqlid,data,pageNum,pageSize);
        result.remove(0);
        return result;
    }
    
    /**
     * 分页查找
     * @param json
     * @param sqlid
     * @return
     * @throws Exception
     */
    public List<JSONObject> search(QueryData q,String sqlid) throws Exception{
        List<JSONObject> result = null;
        if(q.isPage){
        	result = baseDao.queryForPage(sqlid,q.paramsData,q.curPage,q.pageSize);
        }else{
        	result = baseDao.query(q.paramsData,sqlid);
        }
        return result;
    }

    /**
     * 单表插入数据
     * @param json
     * @param sqlid
     * @return
     * @throws Exception
     */
    public JSONObject add(JSONObject json,String sqlid) throws Exception {
        return  baseDao.add(json, sqlid);
    }

    /**
     * 主从表插入数据
     * @param json
     * @param sqlz
     * @param sqlc
     * @return
     * @throws Exception
     */
    public JSONObject addDetail(JSONObject json,String sqlz,String sqlc) throws Exception {
        JSONObject returnJson = new JSONObject();
        int status =0;
        JSONArray itemData = json.getJSONArray("items");
        baseDao.add(json,sqlz);
        if (itemData.size() > 0) {
            JSONObject[] added_item = baseDao.batchAdd(itemData, sqlc);
            status = 1;
        }
        returnJson.put("data",json);
        returnJson.put("status",status);
        return  returnJson;
    }

    /**
     * 单表更新
     * @param json
     * @param sqlid
     * @return
     * @throws Exception
     */
    public int update(JSONObject json, String sqlid) throws Exception {
        return baseDao.update(json,sqlid);
    }

    /**
     * 主从表更新数据
     * @param json
     * @param sqlz
     * @param sqlc
     * @return
     * @throws Exception
     */
    public JSONObject updateDetail(JSONObject json,String sqlz,String sqlc) throws Exception {
        JSONObject returnJson = new JSONObject();
        int status =0;
        JSONArray itemData = json.getJSONArray("items");
        baseDao.update(json,sqlz);
        if (itemData.size() > 0) {
            int update_items = baseDao.batchUpdate(itemData, sqlc);
            status = 1;
        }
        returnJson.put("data",json);
        returnJson.put("status",status);
        return  returnJson;
    }

    /**
     * 单表删除
     * @param code
     * @param sqlid
     * @return
     * @throws Exception
     */
    public int deletecode(String code,String sqlid) throws Exception {
        return baseDao.deleteCode(code, sqlid);
    }

    /**
     * 主从表删除
     * @param code
     * @param sqlz
     * @param sqlc
     * @return
     * @throws Exception
     */
    public int deleteDetail(String code,String sqlz,String sqlc) throws Exception {

        int r = baseDao.deleteCode(code, sqlz);
        if(r >= 0){
            r = baseDao.deleteCode(code, sqlc);
        }
        return r;
    }

    /**
     * 根据key值获取规则的编号
     * @param keyId
     * @return
     */
    public String getKeyByRule(String keyId){
        return keyGenerator.getKeyByRule(keyId);
    }

	/**
	 * set id,
	 * 	   company_id,dept_id,
	 * 	   create_uid,create_date,
	 *     write_uid, write_date 
	 * @param item
	 * @return
	 */
 	protected JSONObject setAddCommonJson(JSONObject jsonData,JSONObject item) { 
 		
		item.put("id", CommonUtil.getPKUUID()); 
		
		item.put("company_id", jsonData.getString("company_id"));
		item.put("dept_id", jsonData.getString("dept_id"));
		
		item.put("create_uid", jsonData.getString("create_uid")); 
		item.put("create_date", CommonUtil.getCurDateTime()); 
		
		setUpCommonJson(jsonData,item);
		return item;
	}
	/**
	 * set write_uid, write_date 
	 * @param item
	 * @return
	 */
	protected JSONObject setUpCommonJson(JSONObject jsonData,JSONObject item) {
 		// write_uid, write_date,busi_date		 
		item.put("write_uid", jsonData.getString("write_uid"));		 
		item.put("write_date",CommonUtil.getCurDateTime()); 
		return item;
	}
 
}
