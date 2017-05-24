package com.ruisoft.surfacer.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.ruisoft.base.bean.QueryData;
import com.ruisoft.base.service.BaseService;

import net.sf.json.JSONObject;

public class SurfacerService extends BaseService {
	//private final static Logger LOG = Logger.getLogger(SurfacerService.class);

	public  List<JSONObject> surfacerTest(QueryData q) throws Exception {
		//LOG.debug("Surfacer测试：" + json.getString("uname"));

		List<JSONObject> itemData = search(q, "surfacer.select.surfacer");
		return itemData;
		
//		List<JSONObject> results = baseDao.query(q, "");
//
//		if (results.isEmpty())
//			return null;
//
//		return results.get(0);
	}
}