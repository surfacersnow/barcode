package com.ruisoft.system.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruisoft.base.controller.BaseController;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.json.JSONMap;
import com.ruisoft.core.param.RequestUtil;

@Controller
@RequestMapping("/system/")
public class SystemController  extends BaseController {
    private static final Logger LOG = Logger.getLogger(SystemController.class);

    @Resource
    protected BaseDao baseDAO = null;

    public void setBaseDAO(BaseDao baseDAO) {
        this.baseDAO = baseDAO;
    }
    
    private static final String RESPONSE_STR = "{\"rows\":{rows},\"total\":\"{total}\"}";

    private JSONObject reqData = new JSONObject(true);;
   
    @RequestMapping("query")
    public void query() {
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
            LOG.error("执行查询操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行查询操作发生错误");
        } finally {
            try {
                LOG.debug(returnJson);
                response(returnJson);
            } catch (IOException e) {
                LOG.error("查询返回信息时发生错误", e);
                ;
            }
            reqData = new JSONObject(true);
        }
    }

    @RequestMapping("userperson")
    public void userperson(){
    	JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
    	try {
    		String entityName = "system.select.queryUser";
    		JSONObject json = new JSONObject();
    		json.put("id", RequestUtil.getCurrentUserID());
    		List<JSONObject> result = baseDAO.query(json, entityName);
    		JSONObject rows = result.get(0);
    		returnJson.put("status", "1");
    		returnJson.put("data", rows);
    		response(returnJson);
    	} catch (Exception e) {
    		LOG.error("执行查询操作发生错误", e);
    		returnJson.put("status", "-2");
    		returnJson.put("msg", "执行查询操作发生错误");
    	} finally {
    		try {
    			LOG.debug(returnJson);
    			response(returnJson);
    		} catch (IOException e) {
    			LOG.error("查询返回信息时发生错误", e);
    		}
    	}
    }
    
    @RequestMapping("add")
    public void add() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            reqData = (JSONObject) (!reqData.has("data") ? reqData.put("data", "{}") : reqData);
            // 查询实体名称
            String entityName = reqData.getString("sql");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定插入实体名称");
            } else {
                JSONObject added = baseDAO.add(reqData.getString("data"), entityName);
                int r = 1;
                if (added.size() < 1) {
                    r = -3;
                    returnJson.put("msg", "未能成功添加数据");
                } else {
                    returnJson.put("added", added);
                }
                returnJson.put("status", r);
            }
        } catch (Exception e) {
            LOG.error("执行插入操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行插入操作发生错误");
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
                LOG.error("插入返回信息时发生错误", e);
            }
            reqData = new JSONObject(true);
        }
    }
    
    @RequestMapping("batchAdd")
    public void batchAdd() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            reqData = (JSONObject) (!reqData.has("data") ? reqData.put("data", "{}") : reqData);
            // 查询实体名称
            String entityName = reqData.getString("sql");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定插入实体名称");
            } else {
                int r = 1;
                JSONObject[] added = baseDAO.batchAdd(reqData.getJSONArray("data"), entityName);
                if (added.length < reqData.getJSONArray("data").size()) {
                    r = -3;
                    returnJson.put("msg", "未能成功添加数据");
                } else {
                    returnJson.put("added", JSONArray.fromObject(added));
                }
                returnJson.put("status", r);
            }
        } catch (Exception e) {
            LOG.error("执行插入操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行插入操作发生错误");
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
                LOG.error("插入返回信息时发生错误", e);
            }
            reqData = new JSONObject(true);;
        }
    }

    @RequestMapping("update")
    public void update() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            reqData = (JSONObject) (!reqData.has("data") ? reqData.put("data", "{}") : reqData);
            // 查询实体名称
            String entityName = reqData.getString("sql");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定更新实体名称");
            } else {
                int r = baseDAO.update(reqData.getString("data"), entityName);
                if (r < 1) {
                    r = -3;
                    returnJson.put("msg", "未能成功添加数据");
                } else if (r > 1) {
                    r = 1;
                }
                returnJson.put("status", r);
            }
        } catch (Exception e) {
            LOG.error("执行更新操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行更新操作发生错误");
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
                LOG.error("更新返回信息时发生错误", e);
            }
            reqData = new JSONObject(true);;
        }
    }

    @RequestMapping("batchUpdate")
    public void batchUpdate() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            reqData = (JSONObject) (!reqData.has("data") ? reqData.put("data", "{}") : reqData);
            // 查询实体名称
            String entityName = reqData.getString("sql");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定更新实体名称");
            } else {
                int r = baseDAO.batchUpdate(reqData.getJSONArray("data"), entityName);
                if (r < reqData.getJSONArray("data").size()) {
                    r = -3;
                    returnJson.put("msg", "未能成功添加数据");
                } else {
                    r = 1;
                }
                returnJson.put("status", r);
            }
        } catch (Exception e) {
            LOG.error("执行插入操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行插入操作发生错误");
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
                LOG.error("插入返回信息时发生错误", e);
            }
            reqData = new JSONObject(true);
        }
    }

    @RequestMapping("del")
    public void delete() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            reqData = (JSONObject) (!reqData.has("data") ? reqData.put("data", "{}") : reqData);
            // 查询实体名称
            String entityName = reqData.getString("sql");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定删除实体名称");
                return;
            } else {
                int r = baseDAO.delete(reqData.getString("data"), entityName);
                if (r < 1) {
                    r = -3;
                    returnJson.put("msg", "未能成功删除数据");
                } else if (r > 1) {
                    r = 1;
                }
                returnJson.put("status", r);
            }
        } catch (Exception e) {
            LOG.error("执行删除操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行删除操作发生错误");
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
                LOG.error("删除返回信息时发生错误", e);
            }
            reqData = new JSONObject(true);
        }
    }

    @RequestMapping("batchDelete")
    public void batchDelete() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            reqData = (JSONObject) (!reqData.has("data") ? reqData.put("data", "{}") : reqData);
            // 查询实体名称
            String entityName = reqData.getString("sql");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定删除实体名称");
            } else {
                int r = baseDAO.batchDelete(reqData.getJSONArray("data"), entityName);
                if (r < reqData.getJSONArray("data").size()) {
                    r = -3;
                    returnJson.put("msg", "未能成功添加数据");
                }
                returnJson.put("status", r);
            }
        } catch (Exception e) {
            LOG.error("执行删除操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行删除操作发生错误");
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
                LOG.error("删除返回信息时发生错误", e);
            }
            reqData = new JSONObject(true);
        }
    }
    
}
