package com.ruisoft.core.param;

import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.core.dml.entity.QueryEntity;
import com.ruisoft.core.json.JSONMap;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wenwws
 *
 */
public class ParamGenerator {

    private static final Logger LOG = Logger.getLogger(ParamGenerator.class);

    private static final String DICT_ITEM_QUERY = "rbac.select.dict.item.query";

    private static final String PARAM_RETURN = "{\"param\":{p},\"tree\":\"{t}\"}";

    private static Map<String, ParamEntity> config = null;

    public Map<String, ParamEntity> getConfig() {
        return config;
    }

    public void setConfig(Map<String, ParamEntity> config) {
        ParamGenerator.config = config;
    }

    @Autowired
    private BaseDao baseDAO = null;

    private static final HashMap<String, String> cache = new HashMap<String, String>();

    public String generate(String id, JSONObject args) {
        // 查找缓存
        if (cache.containsKey(id)) {
        	LOG.debug("获取缓存的数据：id=" + id); 
            return cache.get(id);
        }

        String params = null, treeAttr = "";
        boolean needCache = true;
        try {
        	LOG.debug("获取缓存的数据：id=" + id);  
            if (config.containsKey(id)) {
                ParamEntity entity = config.get(id);
                if (entity instanceof SQLParamEntity) {
                    params = generate((SQLParamEntity) entity, args);
                    treeAttr = ((SQLParamEntity) entity).getTreeAttr();
                    needCache = ((SQLParamEntity) entity).isCache();
                } else if (entity instanceof ListParamEntity) {
                    params = generate((ListParamEntity) entity);
                    needCache = ((ListParamEntity) entity).isCache();
                }
            } else {
                // 参数字典表取值
                params = genFormDict(id, args);
            }

            params = PARAM_RETURN.replaceFirst("\\{p\\}", params)
                    .replaceFirst("\\{t\\}", treeAttr);

            if (needCache)
                cache.put(id, params);

            LOG.debug(params);
            return params;
        } catch (Exception e) {
            LOG.error("生成字典项[".concat(id).concat("]发生错误"), e);
        }

        return null;
    }

    public String genFormDict(String id, JSONObject args) throws Exception {
        List<JSONObject> items = baseDAO.query(args, DICT_ITEM_QUERY);
        return reorgParam(items);
    }

    public String generate(SQLParamEntity entity, JSONObject args) throws Exception {
        if (args == null || !args.has("d")) {
            LOG.error("未提供目标参数定义名称");
            return null;
        }

        QueryEntity qEntity = entity.getSql();
        List<JSONObject> items = baseDAO.query(args, qEntity);
        return reorgParam(items);
    }

    public String generate(ListParamEntity entity) {
        return null;
    }

    private String reorgParam(List<JSONObject> items) throws JSONException {
        JSONMap<String, String> item = new JSONMap<String, String>(JSONMap.TYPE.OBJECT);
        for (JSONObject i : items) {
            item.put(i.getString("value"), i.toString());
        }
        String param = item.toString();
        return param;
    }

    /**
     * 重新加载缓存数据
     * @param id  缓存ID
     * @param args  查找数据源的条件
     * @return
     */
    public String reloadCache(String id,JSONObject args) {
    	LOG.debug("加载数据库的数据到缓存中：id=" + id);  
        String params = null, treeAttr = "";
        boolean needCache = true;
        try {
            if (config.containsKey(id)) {
                ParamEntity entity = config.get(id);
                if (entity instanceof SQLParamEntity) {
                    params = generate((SQLParamEntity) entity, args);
                    treeAttr = ((SQLParamEntity) entity).getTreeAttr();
                    needCache = ((SQLParamEntity) entity).isCache();
                } else if (entity instanceof ListParamEntity) {
                    params = generate((ListParamEntity) entity);
                    needCache = ((ListParamEntity) entity).isCache();
                }
            } else {
                // 参数字典表取值
                params = genFormDict(id, args);
            }
            params = PARAM_RETURN.replaceFirst("\\{p\\}", params)
                    .replaceFirst("\\{t\\}", treeAttr);
            if (needCache)
                cache.put(id, params);
        } catch (Exception e) {
            LOG.error("加载缓存[".concat(id).concat("]发生错误"), e);
        }
         return params;
    }
}
