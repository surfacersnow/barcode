package com.ruisoft.base.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import com.ruisoft.common.CommonUtil;
import com.ruisoft.common.DateTool;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.dml.entity.AddEntity;
import com.ruisoft.core.dml.entity.DMLEntity;
import com.ruisoft.core.dml.entity.DeleteEntity;
import com.ruisoft.core.dml.entity.QueryEntity;
import com.ruisoft.core.dml.entity.UpdateEntity;
import com.ruisoft.core.json.JSONPagerMapper;
import com.ruisoft.core.key.KeyGenerator;
import com.ruisoft.core.param.RequestUtil;

/**
 * 公共操作数据库类
 * @author lenovo
 *
 */
@Repository
public class BaseDao {
    private static final Logger LOG = Logger.getLogger(BaseDao.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private QueryEntity qEntity = null;
    private AddEntity aEntity = null;
    private UpdateEntity uEntity = null;
    private DeleteEntity dEntity = null;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

//    @Autowired
//    protected KeyGenerator keyGenerator = null;

    /**
     * 单独执行SQL
     * @param sql
     */
    public void execute(String sql){
        jdbcTemplate.execute(sql);
    }

    public int update(String sql){
    	return jdbcTemplate.update(sql);
    }
    
    public Map<String, Object> queryForMap(String sql){
    	return jdbcTemplate.queryForMap(sql);
    }
    
    public Map<String, Object> queryForMap(String sql,Object[] args, int[] argTypes){
    	return jdbcTemplate.queryForMap(sql, args, argTypes);
    }
    
    public int queryForInt(String seqname){
    	String sql = "SELECT "+seqname.toUpperCase()+".NEXTVAL from dual";
    	return jdbcTemplate.queryForInt(sql);
    }
    /**
     * 上传附件文件保存到数据库中
     * @param filebytes
     * @param id
     * @throws Exception
     */
    public void updateFile(final byte[] filebytes,final String id,String sql) throws Exception{
//        String sql = "UPDATE mesg_file SET annex_content = ? WHERE annex_id = ?";
        final LobHandler lobHandler=new DefaultLobHandler();
        jdbcTemplate.execute(sql,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                            throws SQLException {
                        lobCreator.setBlobAsBytes(ps, 1, filebytes);
                        lobCreator.setClobAsString(ps, 2, id);
                    }
                });
    }

    /**
     * 读取数据库中的大对象文件内容
     * @param os 输出流
     * @param id 附件数据存储ID
     */
    public void queryFile(final OutputStream os,final String id){
        final LobHandler lobHandler=new DefaultLobHandler();
        //查询附件文件内容SQL
        String sql = "select annex_content from mesg_file where annex_id=?";
        //读取数据到输出流中
        jdbcTemplate.query(sql,
                new AbstractLobStreamingResultSetExtractor(){
                    protected void streamData(ResultSet rs)
                            throws SQLException,IOException,DataAccessException{
                        FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, 1), os);
                    }
                },id);
    }

    /**
     * 查询bean类型的list数据
     * @param clazz bean类
     * @param info  数组类型查询条件
     * @param sqlid  查询SQL的SQLid
     * @param <T>   实体类
     * @return  查询实体类的所有数据
     */
    public <T> List<T> queryList(Class<T> clazz,Object[] info,String sqlid) {
        String sql = SysCache.get(sqlid, qEntity).getPreparedSQL();
        List<?> list = jdbcTemplate.query(sql,info,BeanPropertyRowMapper.newInstance(clazz));
        return (List<T>) list;
    }

    /**
     * 查询bean类型的list数据
     * @param clazz  bean类
     * @param id  查询ID
     * @param sqlid  查询SQL的SQLid
     * @param <T>     实体类
     * @return    查询实体类的所有数据
     */
    public <T> List<T> queryList(Class<T> clazz,String id,String sqlid) {
        Object[] info ={id};
        String sql = SysCache.get(sqlid, qEntity).getPreparedSQL();
        List<?> list = jdbcTemplate.query(sql,info,BeanPropertyRowMapper.newInstance(clazz));
        return (List<T>) list;
    }

    /**
     *
     * @param clazz  bean类
     * @param json  json格式的查询条件
     * @param sqlid  查询SQL的SQLid
     * @param <T>   实体类
     * @return   查询实体类的所有数据
     * @throws Exception
     */
    public <T> List<T> queryList(Class<T> clazz,JSONObject json,String sqlid) throws Exception {
        QueryEntity query = SysCache.get(sqlid, qEntity);
        String sql = query.getSql(json);
        List<?> list = new ArrayList<Object>();
        if (QueryEntity.COND.equals(query.getDmlType())) {
            list = jdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(clazz));
        }else{
            Object[] info = getPreparedParam(json, query);
            list = jdbcTemplate.query(sql,info,BeanPropertyRowMapper.newInstance(clazz));
        }
        return (List<T>) list;
    }
    /**
     * 新增处理
     * @param json
     * @param add
     * @return
     * @throws Exception
     */
    public JSONObject add(JSONObject json, AddEntity add)  throws Exception{
        String sql = add.getPreparedSQL();
        LOG.info("执行新增：" + sql);
        Map<String, Object> kv = new HashMap<String, Object>();
        kv = getPreparedParamMap(json, add);
        LOG.info("新增SQL值kv：" +  kv.values().toString());
        int r = jdbcTemplate.update(sql, kv.values().toArray());
        if (r > 0) {
            return JSONObject.fromObject(kv);
        } else {
            return new JSONObject();
        }
    }
    /**
     * 新增处理， 实体ID参数
     * @param values
     * @param id
     * @return
     * @throws Exception
     */
    public JSONObject add(JSONObject values, String id) throws Exception {
        return add(values, SysCache.get(id, aEntity));
    }
     
    public JSONObject add(String values, AddEntity add) throws Exception {
        return add(JSONObject.fromObject(values), add);
    }

    public JSONObject add(String values, String id)  throws Exception {
       // System.out.println(values);
        return add(JSONObject.fromObject(values), SysCache.get(id, aEntity));
    }

    /**
     * 批量新增处理
     * @param values
     * @param add
     * @return
     * @throws Exception
     */
    public JSONObject[] batchAdd(JSONArray values, AddEntity add) throws Exception {
        String sql = add.getPreparedSQL();
        LOG.debug("执行批量新增：" + sql);
        JSONObject[] retJSON = new JSONObject[values.size()];
        Map<String, Object> kv = null;
        for (int i = 0; i < values.size(); i++) {
            kv = getPreparedParamMap(values.getJSONObject(i), add);
            retJSON[i] = JSONObject.fromObject(kv);
            jdbcTemplate.update(sql, kv.values().toArray());
        }

        return retJSON;
    }

    public JSONObject[] batchAdd(JSONArray values, String id) throws Exception {
        return batchAdd(values, SysCache.get(id, aEntity));
    }
    
    /**
     * 更新处理
     * @param values
     * @param update
     * @return
     * @throws Exception
     */
    public int update(JSONObject values, UpdateEntity update) throws Exception {
        String sql = update.getPreparedSQL();
        LOG.debug("执行更新：" + sql);
        Object[] params = getPreparedParam(values, update);
        return jdbcTemplate.update(sql, params);
    }

    public int update(JSONObject values, String id) throws Exception {
        return update(values, SysCache.get(id, uEntity));
    }

    public int update(String values, UpdateEntity update) throws Exception {
        return update(JSONObject.fromObject(values), update);
    }

    public int update(String values, String id) throws Exception {
        return update(JSONObject.fromObject(values), SysCache.get(id, uEntity));
    }
    /**
     * 批量新增处理
     * @param values
     * @param update
     * @return
     * @throws Exception
     */
    public int batchUpdate(JSONArray values, UpdateEntity update) throws Exception {
        String sql = update.getPreparedSQL();
        LOG.debug("执行批量更新：" + sql);
        int counter = 0;
        Object[] params = null;
        for (int i = 0; i < values.size(); i++) {
            params = getPreparedParam(values.getJSONObject(i), update);
            LOG.info("执行批量更新：".concat(sql));
            counter += jdbcTemplate.update(sql, params);
        }

        return counter;
    }

    public int batchUpdate(JSONArray values, String id) throws Exception {
        return batchUpdate(values, SysCache.get(id, uEntity));
    }

    /**
     * 查询方法
     * @param json
     * @param id
     * @return
     * @throws Exception
     */
    public List<JSONObject> query(JSONObject json, String id) throws Exception {
        return query(json, SysCache.get(id, qEntity));
    }

    public List<JSONObject> query(String str, QueryEntity query) throws Exception {
        return query(JSONObject.fromObject(str), query);
    }

    public List<JSONObject> query(String str, String id) throws Exception {
        return query(JSONObject.fromObject(str), id);
    }

    public List<JSONObject> query(JSONObject json, QueryEntity query) throws Exception {
        String sql = query.getSql(json);
        LOG.info("执行查询:" + sql);

        if (QueryEntity.COND.equals(query.getDmlType())) {
            return jdbcTemplate.query(sql, new JSONMapper());
        }

        Object[] info = getPreparedParam(json, query);
        return jdbcTemplate.query(sql, info, new JSONMapper());
    }
    
    public List<JSONObject> query(String sql) throws Exception {
        return jdbcTemplate.query(sql, new JSONMapper());
    }
    
    
    /**
     * 按 id 查询
     * @param id
     * @param sqlid
     * @return
     * @throws Exception
     */
    public List<JSONObject> queryId(String id, String sqlid) throws Exception {
        return queryId(id, SysCache.get(sqlid, qEntity));
    }

    public List<JSONObject> queryId(String id, QueryEntity query) throws Exception {
        String sql = query.getPreparedSQL();
        LOG.info("执行查询:" + sql);
        Object[] info = new Object[1];
        info[0] = id;
        return jdbcTemplate.query(sql, info, new JSONMapper());
    }
    
    /**
     * 按查询条件参数查询
     * @param sqlid
     * @param parm
     * @return
     */
    public List<JSONObject> queryForList(String sqlid, Object[] parm) {
        QueryEntity query = SysCache.get(sqlid, qEntity);
        String sql = query.getPreparedSQL();
        LOG.debug(sql);
        JSONPagerMapper rowMapper = new JSONPagerMapper();
        List<JSONObject> results = jdbcTemplate.query(sql, parm, rowMapper);
        return results;
    }
    /**
     * 按条件查询
     * @param sqlid
     * @param qCond
     * @return
     * @throws Exception
     */
    public List<JSONObject> queryForList(String sqlid,  JSONObject qCond)  throws Exception {
        QueryEntity query = SysCache.get(sqlid, qEntity);
        String sql = query.getPreparedSQL();
        LOG.debug(sql);
        JSONPagerMapper rowMapper = new JSONPagerMapper();
        List<JSONObject> results = null;
        if (!DMLEntity.COND.equals(query.getDmlType())) {
            Object[] info = getPreparedParam(qCond, query);
            results = jdbcTemplate.query(sql, info, rowMapper);
        } else {
            results = jdbcTemplate.query(sql, rowMapper);
        }
        return results;
    }
    
    /**
     * 按条件分页查询
     * @param query
     * @param qCond
     * @param page
     * @param pageSize
     * @return
     * @throws ParameterException 
     * @throws Exception
     */
    public List<JSONObject> queryForPage(QueryEntity query, JSONObject qCond, int page, int pageSize) throws ParameterException  {
        if (page < 1 || pageSize < 1)
            throw new ParameterException("分页信息有误");

        String sql = null;
        if (DMLEntity.COND.equals(query.getDmlType()))
            sql = query.getSql(qCond);
        else
            sql = query.getPreparedSQL();

        LOG.debug(sql);

        final int rStart = (page - 1) * pageSize + 1;
        final int rEnd = rStart + pageSize - 1;
        JSONPagerMapper rowMapper = new JSONPagerMapper();
        rowMapper.setrStart(rStart);
        rowMapper.setrEnd(rEnd);
        List<JSONObject> results = null;
        int c = 0;
        if (!DMLEntity.COND.equals(query.getDmlType())) {
            Object[] info = getPreparedParam(qCond, query);
            results = jdbcTemplate.query(sql, info, rowMapper);
            c = count(sql,info);
        } else {
            results = jdbcTemplate.query(sql, rowMapper);
            c = count(sql);
        }
        JSONObject countJ = JSONObject.fromObject("{\"total\":\"" + c + "\"}");
        results.add(0, countJ);
        return results;
    }
    
    /**
     * 复杂SQL分页查询函数
     * @param sql
     * @param page
     * @param pageSize
     * @return
     */
    public List<JSONObject> queryForPage(String sql, int page, int pageSize){
        final int rStart = (page - 1) * pageSize + 1;
        final int rEnd = rStart + pageSize - 1;
        JSONPagerMapper rowMapper = new JSONPagerMapper();
        rowMapper.setrStart(rStart);
        rowMapper.setrEnd(rEnd);
        List<JSONObject> results = null;
        results = jdbcTemplate.query(sql, rowMapper);
        int c = 0;
        c = count(sql);
        JSONObject countJ = JSONObject.fromObject("{\"total\":\"" + c + "\"}");
        results.add(0, countJ);

        return results;
    }

    public List<JSONObject> queryForPage(QueryEntity query, String qCond, int page, int pageSize) throws Exception {
        return queryForPage(query, JSONObject.fromObject(qCond), page, pageSize);
    }

    public List<JSONObject> queryForPage(String entityName, String qCond, int page, int pageSize) throws ParameterException, JSONException {
        return queryForPage(SysCache.get(entityName, qEntity), JSONObject.fromObject(qCond), page, pageSize);
    }

    public List<JSONObject> queryForPage(String entityName, JSONObject qCond, int page, int pageSize) throws Exception {
        return queryForPage(SysCache.get(entityName, qEntity), qCond, page, pageSize);
    }
    
     /**
     * 删除处理
     * @param values
     * @param delete
     * @return
     * @throws Exception
     */
    public int delete(JSONObject values, DeleteEntity delete) throws Exception {
        String sql = null;
        if (DMLEntity.COND.equals(delete.getDmlType())) {
            sql = delete.getSql(values);
            LOG.info("执行删除：" + sql);
            return jdbcTemplate.update(sql);
        } else {
            sql = delete.getPreparedSQL();
            LOG.info("执行删除：" + sql);
            Object[] params = getPreparedParam(values, delete);
            return jdbcTemplate.update(sql, params);
        }
    }

    public int delete(JSONObject values, String id) throws Exception {
        return delete(values, SysCache.get(id, dEntity));
    }

    public int delete(String values, DeleteEntity delete) throws Exception {
        return delete(JSONObject.fromObject(values), delete);
    }

    public int deleteCode(String code, String sqlid) throws Exception {
        DeleteEntity delete =   SysCache.get(sqlid, dEntity);
        String sql = delete.getPreparedSQL();
        Object[] params = new Object[1];
        params[0] = code;
        return jdbcTemplate.update(sql, params);
    }


    public int delete(String values, String id) throws Exception {
        return delete(JSONObject.fromObject(values), SysCache.get(id, dEntity));
    }
    
    /**
     * 批量删除处理
     * @param values
     * @param delete
     * @return
     * @throws Exception
     */
    public int batchDelete(JSONArray values, DeleteEntity delete) throws Exception {
        String sql = null;
        int counter = 0;
        Object[] params = null;
        for (int i = 0; i < values.size(); i++) {
            if (DMLEntity.COND.equals(delete.getDmlType())) {
                LOG.info("执行批量删除：" + sql);
                sql = delete.getSql(values.getJSONObject(i));
                counter += jdbcTemplate.update(sql);
            } else {
                params = getPreparedParam(values.getJSONObject(i), delete);
                sql = delete.getPreparedSQL();
                LOG.info("执行批量删除：" + sql);
                counter += jdbcTemplate.update(sql, params);
            }
        }
        return counter;
    }
    
    public int batchDelete(JSONArray values, String id) throws Exception {
        return batchDelete(values, SysCache.get(id, dEntity));
    }

    public int count(String sql) {
        return jdbcTemplate.queryForInt("SELECT COUNT(1) NUM FROM (".concat(sql).concat(") TC"));
    }

    public int count(String sql, Object... args) {
        return jdbcTemplate.queryForInt("SELECT COUNT(1) NUM FROM (".concat(sql).concat(") TC"), args);
    }

    
    /**
     * 处理预置参数
     * @param json
     * @param entity
     * @return
     * @throws Exception
     */
    protected Object[] getPreparedParam(JSONObject json, DMLEntity entity)  {
        if (entity.getConditions() == null)
            return new Object[0];

        return getPreparedParamMap(json, entity).values().toArray();
    }

    /**
     * 处理预置参数
     * @param json
     * @param entity
     * @return
     * @throws Exception
     */
    protected Map<String, Object> getPreparedParamMap(JSONObject json, DMLEntity entity)    {
        Map<String, String> cond = entity.getConditions();
        //System.out.println(cond);
        //System.out.println(json);
        if (cond == null)
            return null;

        Map<String, Object> retMap = new LinkedHashMap<String, Object>();
        Object val = null;
        String datatype = null;
        String k = null;
        for (String kt : cond.keySet()) {
            datatype = cond.get(kt);
            k = kt.replaceFirst("~\\d+$", "");
            if (k.startsWith("#"))
                val = parseSharp(k.substring(1), json);
            else if (!json.has(k))
                val = null;
            else if ("str".equals(datatype))
                val = json.getString(k);
            else if ("int".equals(datatype)) {
                val = 0;
                if (StringUtils.isNotEmpty(json.get(k).toString()))
                    val = Integer.parseInt(json.get(k).toString());
            } else if ("double".equals(datatype)) {
                val = 0;
                if (StringUtils.isNotEmpty(json.get(k).toString()))
                    val = Double.parseDouble(json.get(k).toString());
            } else if ("long".equals(datatype)) {
                val = 0;
                if (StringUtils.isNotEmpty(json.get(k).toString()))
                    val = Long.parseLong(json.get(k).toString());
            } else if ("bool".equals(datatype))
                val = json.getBoolean(k);
//            System.out.println("k值"+k);
//            System.out.println("val值"+val);
            retMap.put(kt, val);
        }

        return retMap;
    }
    
    /**
     * "#" 开头的参数解析
     * 1、当前支持解析 #pk_uuid,  时间戳（15位）+'_'+ 16位uuid组成 32位 pkuuid;其他uuid格式支持， #uuid(36位),#uuid32(36位去掉‘-’);
     * 2、支持从session及request中解析
     * 3、支持日期解析
     * 4、支持通过编码key获取编码
     * @param key
     * @param json
     * @return
     * @throws Exception
     */
    private Object parseSharp(String key, JSONObject json)   {
    	//1、解析配置的主键参数
        if (key.equals("pk_uuid")) {
            return CommonUtil.getPKUUID();
        } else if (key.equals("uuid")) {
            return CommonUtil.getUUID();
        } else if (key.equals("uuid32")) {
            return CommonUtil.get32UUID();
        } else if (key.equals("curdate")) {
        	//3、配置 日期参数
            return DATE_FORMAT.format(new Date());
        } else if (key.equals("curuid")){
        	return RequestUtil.getCurrentUserAccount();
        } else if (key.equals("curtime")){
        	return DateTool.getCurrentTimeMillisAsString("yyyy-MM-dd HH:mm:ss");
        }
        //4、生成主键
        return KeyGenerator.getPKUUID();
    }
    

    /**
     * JSON
     * @author wws
     *
     */
    public class JSONMapper implements RowMapper<JSONObject> {
        @Override
        public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            ResultSetMetaData meta = rs.getMetaData();
            int i = meta.getColumnCount();
            String col = null;
            JSONObject json = new JSONObject();
            for (int j = 1; j <= i; j++) {
                col = meta.getColumnLabel(j);
                try {
                    json.put(col.toLowerCase(), rs.getString(col));
                } catch (JSONException e) {
                    throw new SQLException();
                }
            }
            return json;
        }
    }
    
}