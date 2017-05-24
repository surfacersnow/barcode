package com.ruisoft.core.dml.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
//import org.json.JSONException;
//import org.json.JSONObject;



import com.ruisoft.common.CommonUtil;

/**
 * 通用实体类
 * @author lenovo
 *
 */
public class DMLEntity {

    private static final Logger LOG = Logger.getLogger(DMLEntity.class);

    public static final String QUERY_NORMAL = "NORMAL";
    //动态条件模式
    public static final String COND = "COND";
    public static final String ADD = "ADD";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    //定义SQL语句
    private String sql = null;

    //编译后的SQL语句
    private String preparedSQL = null;

    /**
     * 查询类型
     * 1：默认值，固定条件查询或无条件查询；
     * 2：有条件查询且条件不固定COND
     */
    protected String dmlType = null;

    //将正则表达式"{}"编译并赋予给Pattern类
    private static final Pattern PATTERN = Pattern.compile("\\{[^\\}]+\\}");

    //动态定义查询条件
    private Map<String, String> conditions = null;

    private Map<String, ConditionConfig> condConf =  null;

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public Map<String, ConditionConfig> getCondConf() {
        return condConf;
    }

    public void setCondConf(Map<String, ConditionConfig> condConf) {
        this.condConf = condConf;
    }

    public void setSql(String sql) {
        this.sql = sql.trim();
    }

    public String getSql() {
        return sql;
    }

    public String getSql(String qCond) throws JSONException {
        //判断是否为动态条件组装SQL
        if (!COND.equals(dmlType))
            return getPreparedSQL();//非组织动态条件，可以直接获取编译后的SQL
        //组织动态条件SQL
        return getSql(JSONObject.fromObject(qCond));
    }

    /**
     * 根据传入参数，动态拼接条件，组织完整SQL
     * @param json  条件变量
     * @return  动态组织完成的SQL语句
     * @throws JSONException
     */
    public String getSql(JSONObject json) throws JSONException {
        //获取编译后的SQL
        String sql = getPreparedSQL();
        //判断是否存在动态条件
        if (!COND.equals(dmlType) || condConf.isEmpty())
            return sql;

        Iterator keys = json.keys();
        StringBuffer where = new StringBuffer();
        ConditionConfig cCfg = null;
        String matchType = null, likeF = null, val = null;
        String k = null;
        while (keys.hasNext()) {
            //获取条件变量的JSON数据的KEY值
            k = keys.next().toString();
            //从JSON数据中根据KEY值获取参数值
            val = json.getString(k);
            //判读条件参数值是否为null
            if (val == null || "".equals(val.trim()))
                continue;
            //获取此变量动态组织SQL的组织结构
            cCfg = condConf.get(k);
            if (val == null || cCfg == null)
                continue;
            //条件连接形式与列名称，AND COLNAME
            where.append(cCfg.getLinkType()).append(" ").append(cCfg.getColName());
            //获取动态条件的匹配方式
            matchType = cCfg.getMatchType();
            //判读是否存在like形式
            if (matchType.startsWith("LIKE")) {
                where.append(" LIKE '");
                if (matchType.contains("(")) {
                    likeF = matchType.replaceAll("LIKE\\s*\\(|\\)", "").trim();
                } else {
                    likeF = "LR";
                }
                if ("LR".equals(likeF)) {
                    where.append("%").append(val).append("%' ");
                } else if ("L".equals(likeF)) {
                    where.append("%").append(val).append("' ");
                } else if ("R".equals(likeF)) {
                    where.append(val).append("%'");
                }
            } else if (matchType.startsWith("IN")){
                //判断是否使用in的形式
                where.append(" in (").append(val).append(") ");
            } else {
                //添加动态条件的默认匹配模式【=】
                where.append(matchType);
                //判断是否为字符类型，添加单引号
                if ("str".equals(cCfg.getColType())) {
                    where.append(" '").append(val).append("' ");
                } else {
                   //非字符类型，直接使用参数变量
                    where.append(val).append(" ");
                }
            }
        }
         //判断动态组装条件是否存在
        if (where.length() > 0)
            //拼接SQL的条件，WHERE ，去掉组织条件中的第一个连接符
            return sql.concat(" WHERE ").concat(
                    where.toString().replaceFirst("^[^ ]+\\s+", ""));
        //返回组装好的SQL语句
        return sql;
    }

    /**
     * 获取编译后的SQL
     * @return  获取编译后的SQL
     */
    public String getPreparedSQL() {
        if (preparedSQL != null)
            return preparedSQL;
        //有条件查询且条件不固定COND
        if (COND.equals(dmlType)) {
            preparedSQL = sql.trim();
            //判断是否定义了条件
            if (conditions != null && !conditions.isEmpty()) {
                condConf = new HashMap<String, ConditionConfig>(conditions.size());
                ConditionConfig condCfg = null;
                String[] cfg = null;
                for (String k : conditions.keySet()) {
                    //初始化条件数据对象
                    condCfg = new ConditionConfig();
                    //设置条件中的列名
                    condCfg.setColName(k);
                    condCfg.setParam(k);
                    //获取条件中对应的编辑格式（str:like(lr):and--数据类型：匹配方式：连接方式）
                    cfg = conditions.get(k).split("[,:|]");
                    try {
                        if (!"".equals(cfg[0])) // 数据类型
                            condCfg.setColType(cfg[0].trim());
                        if (!"".equals(cfg[1])) // 匹配方式 (=/!=/<>/LIKE/IN/NOT LIKE/NOT IN)
                            condCfg.setMatchType(transferSign(cfg[1].trim()));
                        if (!"".equals(cfg[2])) // 条件连接类型(AND/OR)
                            condCfg.setLinkType(cfg[2].trim());
                        if (!"".equals(cfg[3])){
                            condCfg.setColName(cfg[3].trim());
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        LOG.debug("条件列[".concat(k).concat("]注入完成->").concat(condCfg.toString()));
                    } finally {
                        //编译后的动态条件组装模式存入条件Map中
                        condConf.put(k.replaceAll("\\.", ""), condCfg);
                    }
                }
            }
        } else {
            // 固定条件查询或无条件查询的SQL编译
           //创建一个Matcher实例
            Matcher matcher = PATTERN.matcher(sql);
            conditions = new LinkedHashMap<String, String>();
            String m = null, g = null, t = null;
            // 尝试在目标字符串里查找下一个匹配子串
            while (matcher.find()) {
                //返回当前查找而获得的与组匹配的所有子串内容
                m = matcher.group().replaceAll("[{}]", "");
                //判断匹配出的字符串是否包含分隔符：，[:]用来定义变量名称与类型
                if (m.contains(":")) {
                    //获取变量名称
                    g = m.split(":")[0].trim();
                    //获取数据类型
                    t = m.split(":")[1].trim();
                } else {
                    //如果sql中没有定义数据类型，默认为字符类型
                    g = m;
                    t = "str";
                }
                //
                //TODO
                for (int i = 1;conditions.containsKey(g);i++) {
                    g = g.replaceFirst("~\\d+$", "").concat("~") + i;
                }

                conditions.put(g, t);
            }

            //将目标字符串里与既有模式相匹配的子串全部替换为指定的字符串
            preparedSQL = matcher.replaceAll("?");
        }
        //返回编译后的SQL语句
        return preparedSQL.trim();
    }

    private static final String SIGN_GT = "&gt;";
    private static final String SIGN_GT_A = ">";
    private static final String SIGN_LT = "&lt;";
    private static final String SIGN_LT_A = "<";

    /**
     * 运算符号替换
     * @param sign
     * @return
     */
    private String transferSign(String sign) {
        return sign.replaceAll(SIGN_GT, SIGN_GT_A)
                .replaceAll(SIGN_LT, SIGN_LT_A);
    }

    public String getDmlType() {
        return dmlType;
    }

    public void setDmlType(String dmlType) {
        this.dmlType = dmlType.toUpperCase();
    }

    /**
     *条件组成部分
     */
    protected class ConditionConfig {
        //列名称
        private String colName = null;
        //变量
        private String param = null;
        //列属性，默认为字符型
        private String colType = "str";
        //匹配方式，默认为等于
        private String matchType = "=";
        //连接方式，默认为AND连接
        private String linkType = "AND";

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName.toUpperCase();
        }
        
        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param.toUpperCase();
        }

        public String getColType() {
            return colType;
        }

        public void setColType(String colType) {
            this.colType = colType;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType.toUpperCase();
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType.toUpperCase();
        }

        public String toString() {
            return CommonUtil.pojoString(this);
        }
    }
}
