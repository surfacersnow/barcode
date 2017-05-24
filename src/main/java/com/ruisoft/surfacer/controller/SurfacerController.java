package com.ruisoft.surfacer.controller;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruisoft.base.controller.BaseController;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.json.JSONMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/surfacer/")
public class SurfacerController extends BaseController {
	private static final String RESPONSE_STR = "{\"rows\":{rows},\"total\":\"{total}\"}";
    private JSONObject reqData = new JSONObject(true);;
    @RequestMapping("query")
	    public void surfacer() {
		 JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
	        try {
	            if (reqData.isNullObject())
	                reqData = getReqData();
	            if(!reqData.has("data")){
	            	reqData.put("data", new JSONObject());
	            }
	            // 查询实体名称
	            String entityName = reqData.getString("sql");
	            if (entityName == null || SysCache.get(entityName) == null) {
	                returnJson.put("status", "-1");
	                returnJson.put("msg", "没有指定查询实体名称");
	            } else {
	                // l-list 线性结构，默认；t-tree 树型结构
	                String returnType = !reqData.has("rt") ? "l" : reqData.getString("rt");
	                // 是否分页
	                boolean isPager = false;
	                if(reqData.has("p")){
	                	isPager = reqData.getBoolean("p");
	                }else if(reqData.has("cp")){
	                	isPager = true;
	                }
	                // 当前页码
	                int currentPage = !reqData.has("cp") ? 1 : reqData.getInt("cp");
	                // 每页记录数
	                int pageSize = !reqData.has("ps") ? 20 : reqData.getInt("ps");

	                List<JSONObject> result = null;
	                String rows = null, total = "0";
	                if (isPager) {
	                    result = baseDAO.queryForPage(entityName, reqData.getJSONObject("data"), currentPage, pageSize);
	                    total = result.get(0).getString("total");
	                    result.remove(0);
	                } else {
	                    result = baseDAO.query(reqData.getJSONObject("data"), entityName);
	                }

	                if ("t".equalsIgnoreCase(returnType)) {
	                    rows = treeData(result);
	                } else {
	                    rows = JSONArray.fromObject(result).toString();
	                }

	                returnJson.put("status", "1");
	                returnJson.put("data", RESPONSE_STR.replaceFirst("\\{total\\}", total).replaceFirst("\\{rows\\}", rows));
	                response(RESPONSE_STR.replaceFirst("\\{total\\}", total).replaceFirst("\\{rows\\}", rows));
	            }
	        } catch (Exception e) {
	            returnJson.put("status", "-2");
	            returnJson.put("msg", "执行查询操作发生错误");
	        } finally {
	            try {
	                response(returnJson);
	            } catch (IOException e) {
	            }
	            reqData = new JSONObject(true);
	        }
	    }
}