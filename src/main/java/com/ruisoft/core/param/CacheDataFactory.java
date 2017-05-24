package com.ruisoft.core.param;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA. User: LFC Date: 2015/8/26 Time: 21:47 To change
 * this template use File | Settings | File Templates.
 */
@Repository
public class CacheDataFactory extends ParamGenerator {

	private static final Logger LOG = Logger.getLogger(CacheDataFactory.class);

	/**
	 * 根据ID获取所对应的名称
	 * 
	 * @param cacheName
	 *            缓存ID
	 * @param id
	 *            转换id
	 * @return
	 */
	public String getNameById(String cacheName, String id) {
		String objName = "";
		JSONObject json = new JSONObject();
		json.put("d", cacheName);
		String paramJsonstr = generate(cacheName, json);
		JSONObject paramJson = JSONObject.fromObject(paramJsonstr);
		// JSONObject objJson = (JSONObject) ((JSONObject)
		// paramJson.get("param")).get(id);
		JSONObject objJson = null;

		if (((JSONObject) paramJson.get("param")).has(id)) {
			objJson = (JSONObject) ((JSONObject) paramJson.get("param")).get(id);
		}
		if (objJson == null || !objJson.has("name")) {
			// 查找数据库，重新加载缓存
			paramJsonstr = reloadCache(cacheName, json);
			paramJson = JSONObject.fromObject(paramJsonstr);
			// objJson = (JSONObject) ((JSONObject)
			// paramJson.get("param")).get(id);
			if (((JSONObject) paramJson.get("param")).has(id)) {
				objJson = (JSONObject) ((JSONObject) paramJson.get("param")).get(id);
			}
		}

		if (objJson != null) {
			objName = (String) (JSONObject.fromObject(objJson.toString())).get("name");
		}

		return objName;
	}

	/**
	 * 根据ID获取所对应的缓存数据对象
	 * 
	 * @param cacheName
	 *            缓存ID
	 * @param id
	 *            转换id
	 * @return 返回 json格式数据
	 */
	public JSONObject getJsonDataById(String cacheName, String id) {

		JSONObject json = new JSONObject();
		json.put("d", cacheName);
		String paramJsonstr = generate(cacheName, json);
		JSONObject paramJson = JSONObject.fromObject(paramJsonstr);
		JSONObject objJson = null;

		if (((JSONObject) paramJson.get("param")).has(id)) {
			objJson = (JSONObject) ((JSONObject) paramJson.get("param")).get(id);
		}

		if (objJson == null) {
			// 查找数据库，重新加载缓存
			paramJsonstr = reloadCache(cacheName, json);
			paramJson = JSONObject.fromObject(paramJsonstr);
			if (((JSONObject) paramJson.get("param")).has(id)) {
				objJson = (JSONObject) ((JSONObject) paramJson.get("param")).get(id);
			}
		}
		JSONObject objData = null;
		if (objJson != null)
			objData = JSONObject.fromObject(objJson.toString());
		return objData;
	}
}
