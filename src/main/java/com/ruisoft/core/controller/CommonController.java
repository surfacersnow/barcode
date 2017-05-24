package com.ruisoft.core.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruisoft.base.controller.BaseController;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.json.JSONMap;

@Controller
@RequestMapping("/comm/")
public class CommonController extends BaseController {
    private static final Logger LOG = Logger.getLogger(CommonController.class);

    @Resource
    protected BaseDao baseDAO = null;

    public void setBaseDAO(BaseDao baseDAO) {
        this.baseDAO = baseDAO;
    }

    private static final String RESPONSE_STR = "{\"Rows\":{rows},\"Total\":\"{total}\"}";

    private JSONObject reqData = new JSONObject(true);;

    @RequestMapping(params = "m=c")
    public void dml() {
        try {
            reqData = getReqData();
            String type = reqData.getString("t");
            if ("q".equals(type))
                query();
            else if ("a".equals(type))
                add();
            else if ("u".equals(type))
                update();
            else if ("d".equals(type))
                delete();
        } catch (Exception e) {
            LOG.error("Common DML Error!", e);
        }
    }

    @RequestMapping("q")
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
                boolean isPager = !reqData.has("p")? false:reqData.getBoolean("p");
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

    protected String treeData(List<JSONObject> data) {
        return new JSONMap<String, String>(JSONMap.TYPE.ARRAY).toString();
    }

    @RequestMapping("a")
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

    @RequestMapping("ba")
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

    @RequestMapping("u")
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

    @RequestMapping("bu")
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

    @RequestMapping("d")
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

    @RequestMapping("bd")
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

    /**
     * 主从结构数据保存处理
     */
    @RequestMapping("ms")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveMasterSlave() {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            if (reqData.isNullObject())
                reqData = getReqData();
            //获得主表数据
            JSONObject data = JSONObject.fromObject(reqData.getString("data"));
            // 查询实体名称，同时获得主从表的新增、修改查询DML 标记
            String entityName_a = null;
            if (data.has("en_a"))
                entityName_a = data.getString("en_a");
            String entityName_sa = null;
            if (data.has("en_sa"))
                entityName_sa = data.getString("en_sa");
            String entityName_u = null;
            if (data.has("en_u"))
                entityName_u = data.getString("en_u");
            String entityName_su = null;
            if (data.has("en_su"))
                entityName_su = data.getString("en_su");
            // 判断 查询实体名称 不为空
            if (entityName_a == null || SysCache.get(entityName_a) == null || entityName_sa == null || SysCache.get(entityName_sa) == null || entityName_u == null
                    || SysCache.get(entityName_u) == null || entityName_su == null || SysCache.get(entityName_su) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定插入实体名称");
            } else {
                //定义，主从表数据处理状态，默认是执行成功。
                int r = 1;
                int sr = 1;
                // 保存处理主表数据，根据_status 进行新增和更新处理。
                JSONObject added_m = null;
                if (data.has("__status")) {
                    // 新增主表数据
                    if ("add".equals(data.getString("__status"))) {
                        added_m = baseDAO.add(reqData.getString("data"), entityName_a);

                        if (added_m.size() < 1) {
                            r = -3;
                            returnJson.put("msg", "未能成功添加主表数据");
                        } else {
                            returnJson.put("added", added_m);
                        }

                    }
                    // 修改主表数据
                    if ("update".equals(data.getString("__status"))) {
                        r = baseDAO.update(reqData.getString("data"), entityName_u);
                        if (r < 1) {
                            r = -3;
                            returnJson.put("msg", "未能成功更新主表数据");
                        } else if (r > 1) {
                            r = 1;
                        }
                    }
                }
                //对从表数据进行处理，通过每条明细的 _status 对明细数据进行分类处理，新增或是更新处理。
                JSONArray itemData = reqData.getJSONArray("data_s");
                // 新增数据集合
                JSONArray addItemData = new JSONArray();
                // 跟新数据集合
                JSONArray updateItemData = new JSONArray();
                for (int i = 0; i < itemData.size(); i++) {
                    JSONObject item = (JSONObject) itemData.get(i);
                    if ("update".equals(item.getString("__status")))
                        updateItemData.add(item);

                    if ("add".equals(item.getString("__status"))) {
                        // 若主表数据时新增时，根据主从表关联键字段取值。
                        // 若编辑时，在前端赋值。
                        String MasterSloveKeyField = null;
                        if (data.has("MasterSloveKeyField"))
                            MasterSloveKeyField = (String) data.get("MasterSloveKeyField");
                        String MasterSloveKeyValueField= null;
                        if (data.has("MasterSloveKeyValueField"))
                            MasterSloveKeyValueField = (String) data.get("MasterSloveKeyValueField");
                        // 根据主表是否是新增 判断是否从 added_m 中的 MasterSloveKeyValueField中取值
                        if (added_m != null && MasterSloveKeyField != null&& MasterSloveKeyValueField !=null){
                            if(added_m.has(MasterSloveKeyValueField))
                                item.put(MasterSloveKeyField, added_m.getString(MasterSloveKeyValueField));
                        }

                        addItemData.add(item);
                    }

                }
                // 明细表中需要新增的数据,调用 batchAdd 处理
                if (addItemData.size() > 0) {
                    JSONObject[] added_s = baseDAO.batchAdd(addItemData, entityName_sa);
                    if (added_s.length < addItemData.size()) {
                        r = -3;
                        returnJson.put("msg", "未能成功添加明细数据");
                    } else {
                        returnJson.put("added_s",JSONArray.fromObject(added_s));
                    }
                }
                // 明细表中需要更新的数据，调用 batchUpdate 处理
                if (updateItemData.size() > 0) {
                    sr = baseDAO.batchUpdate(updateItemData, entityName_su);
                    if (sr < updateItemData.size()) {
                        sr = -3;
                        returnJson.put("msg", "未能成功跟新明细数据");
                    } else {
                        sr = 1;
                    }
                }
                // 对保存状态进行赋值
                if (sr < r) {
                    returnJson.put("status", sr);
                } else {
                    returnJson.put("status", r);
                }

            }

        } catch (Exception e) {
            LOG.error("执行保存操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行保存操作发生错误");
            throw new RuntimeException(e);
        } finally {
            try {
                response(returnJson);
            } catch (IOException e) {
            }
            reqData = new JSONObject(true);
        }
    }
}
