package com.ruisoft.core.key;

import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.common.Constants;
import com.ruisoft.common.SysConstants;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KeyGenerator {
    private static final Logger LOG = Logger.getLogger(KeyGenerator.class);

    @Autowired
    private BaseDao baseDao = null;

    private static Map<String, KeyEntity> keyConfig = null;

    public void setKeyConfig(Map<String, KeyEntity> keyConfig) {
        KeyGenerator.keyConfig = keyConfig;
    }

    /**
     * 获取UID
     * @return   uid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取32位UID
     * @return   32位uid
     */
    public static String get32UUID() {
        return getUUID().replaceAll("-", "");
    }

    /**
     * 获取主键ID
     * @return   时间-16位UID
     */
    public static String getPKUUID() {
        String pkid = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS).format(new Date());
        pkid =pkid.concat("-").concat(get32UUID().substring(0,16));
        return pkid;
    }

    /**
     * 根据配置文件定义编号规则
     * @param keyId  配置文件中定义的ID
     * @return  编码规则的编号
     * @throws Exception
     */
    public String getKeyByRule(String keyId) {
        if (!keyConfig.containsKey(keyId))
            throw new IllegalArgumentException("KEYID[".concat(keyId).concat("]不存在"));

        KeyEntity entity = keyConfig.get(keyId);
        char[] rule = entity.getRule().toCharArray();
        StringBuffer sign = new StringBuffer();
        StringBuffer key = new StringBuffer();
        // 单引号的优先级高于{}
        String type = null;
        for (char r : rule) {
            if (r == '\'') {
                if ("text".equals(type)) {
                    // 文本结束
                    key.append(sign);
                    type = null;
                    sign.delete(0, sign.length());
                } else {
                    // 文本起始
                    type = "text";
                }
            } else if (r == '{' && type == null) {
                // 变量起始
                type = "arg";
            } else if (r == '}' && "arg".equals(type)) {
                // 变量结束
                key.append(invokeArg(sign.toString(), keyId));
                type = null;
                sign.delete(0, sign.length());
            } else {
                sign.append(r);
            }
        }
        return key.toString();
    }

    /**
     * 根据类型获取数值
     * @param arg   规则类型
     * @param keyId  获取规律编码的key值
     * @return  规则类型的数值
     * @throws Exception
     */
    private String invokeArg(String arg, String keyId) {
        LOG.debug("arg = " + arg);

        if (arg.startsWith(Constants.DATE)) {
            String format = Constants.YYYYMMDD;
            if (arg.contains("("))
                format = arg.replaceAll("date\\s*\\(\\s*|\\s*\\)\\s*", "");
            return getDate(format);
        } else if (Constants.YEAR.equals(arg)) {
            return getDate(Constants.YYYY);
        } else if (Constants.MONTH.equals(arg)) {
            return getDate(Constants.MM);
        } else if (Constants.DAY.equals(arg)) {
            return getDate(Constants.DD);
        } else if (Constants.DATE.equals(arg)) {
            return getDate(Constants.YYYYMMDD);
        } else if (arg.startsWith(Constants.SEQ)) {
            return getSeq(arg, keyId);
        } else if (Constants.UUID.equals(arg)) {
            return getUUID();
        } else if (Constants.UUID32.equals(arg)) {
            return get32UUID();
        } else if (Constants.NUM.equals(arg)) {
            return getSeq(Constants.SEQ, keyId);
        }
        return null;
    }

    /**
     * 根据配置文件定义编号规则
     * @param keyId  配置文件中定义的ID
     * @param values  配置文件中引用的json中的字段数据
     * @return  编码规则的编号
     * @throws Exception
     */
    public String getKeyByRule(String keyId, JSONObject values)
            throws Exception {
        if (!keyConfig.containsKey(keyId))
            throw new IllegalArgumentException("KEYID[".concat(keyId).concat("]不存在"));

        KeyEntity entity = keyConfig.get(keyId);
        char[] rule = entity.getRule().toCharArray();
        StringBuffer sign = new StringBuffer();
        StringBuffer key = new StringBuffer();
        // 单引号的优先级高于{}
        String type = null;
        for (char r : rule) {
            if (r == '\'') {
                if ("text".equals(type)) {
                    // 文本结束
                    key.append(sign);
                    type = null;
                    sign.delete(0, sign.length());
                } else {
                    // 文本起始
                    type = "text";
                }
            } else if (r == '{' && type == null) {
                // 变量起始
                type = "arg";
            } else if (r == '}' && "arg".equals(type)) {
                // 变量结束
                key.append(invokeArg(sign.toString(), keyId, values));
                type = null;
                sign.delete(0, sign.length());
            } else {
                sign.append(r);
            }
        }
        return key.toString();
    }

    /**
     * 根据类型获取数值
     * @param arg   规则类型
     * @param keyId  获取规律编码的key值
     * @param values  json数据
     * @return  规则类型的数值
     * @throws Exception
     */
    private String invokeArg(String arg, String keyId, JSONObject values)
            throws Exception {
        LOG.debug("arg = " + arg);

        if (arg.startsWith(Constants.DATE)) {
            String format = Constants.YYYYMMDD;
            if (arg.contains("("))
                format = arg.replaceAll("date\\s*\\(\\s*|\\s*\\)\\s*", "");
            return getDate(format);
        } else if (Constants.YEAR.equals(arg)) {
            return getDate(Constants.YYYY);
        } else if (Constants.MONTH.equals(arg)) {
            return getDate(Constants.MM);
        } else if (Constants.DAY.equals(arg)) {
            return getDate(Constants.DD);
        } else if (Constants.DATE.equals(arg)) {
            return getDate(Constants.YYYYMMDD);
        } else if (arg.startsWith(Constants.SEQ)) {
            return getSeq(arg, keyId);
        } else if (Constants.UUID.equals(arg)) {
            return getUUID();
        } else if (Constants.UUID32.equals(arg)) {
            return get32UUID();
        } else if (Constants.NUM.equals(arg)) {
            return getSeq(Constants.SEQ, keyId);
        } else if (arg.startsWith("$")) {
            //在json数据中获取规则编码
            return values.getString(arg.substring(1));
        }

        return null;
    }

    /**
     * 获取格式化时间
     * @param format 格式化类型
     * @return  格式化后的日期时间
     */
    private String getDate(String format) {
        SimpleDateFormat date = new SimpleDateFormat(format);
        return date.format(new Date());
    }

    /**
     * 获取规则的序列号
     * @param seq  序列编码格式
     * @param keyId  获取规则编码的key值
     * @return  规则的序列号
     */
    private String getSeq(String seq, String keyId) {
        int len = 0;
        if (seq.contains("(")) {
            len = Integer.valueOf(seq.replaceAll("seq\\s*\\(\\s*|\\s*\\)\\s*", ""));
        }
        try {
            JSONObject json = new JSONObject();
            json.put(Constants.KEYID, keyId);

            KeyEntity entity = keyConfig.get(keyId);
            String orgId = "";
            if (entity.isGroup()) {
                orgId = getOrg();
            }
            json.put(Constants.ORGID, orgId);

            List<JSONObject> keyDef = baseDao.query(json, "key.select.key.generator.query");
            String val = null;
            String circle = entity.getCircle();
            String c = getCircleVal(circle);
            if (keyDef.isEmpty()) {
                // 首次获取，添加记录
                val = String.valueOf(entity.getInit());
                addNewKey(keyId, c);
            } else {
                json = keyDef.get(0);
                String cCircle = json.getString(Constants.CIRCLE);

                if (!"none".equals(circle)) {
                    // 判断是否需要重新计数
                    boolean isReset = false;
                    if ((Constants.YEAR.equals(circle) || Constants.MONTH.equals(circle) || Constants.DAY
                            .equals(circle)||Constants.DATE.equals(circle)) && !c.equals(cCircle)) {
                        isReset = true;
                    } else if ("def".equals(circle)) {
                        // 自定义计数

                    }
                    if (isReset) {
                        resetCircle(keyId, orgId, c);
                        val = String.valueOf(entity.getInit());
                    }
                }
                if (val == null) {
                    val = String.valueOf(json.getInt(Constants.MAX_VAL) + entity.getStep());
                    updateMaxVal(keyId, orgId, entity.getStep());
                }
            }
            return fillStr(val, entity.getFillChar(), len);
        } catch (Exception e) {
            LOG.error("生成序号发生错误", e);
        }
        return null;
    }

    /**
     * 在session中获取组织ID
     * @return  组织ID
     */
    private String getOrg() {
        try {
//            return ((JSONObject) request.getSession().getAttribute(
//                    SysConstants.USER_INFO.toString())).getString("oid");
            return "";
        } catch (JSONException e) {
            LOG.error("获取当前机构发生错误", e);
        }
        return null;
    }

    /**
     * 获取循环的周期数
     * @param circle  周期类型（年、月、日）
     * @return
     */
    private String getCircleVal(String circle) {
        if (Constants.YEAR.equals(circle))
            return getDate(Constants.YYYY);
        else if (Constants.MONTH.equals(circle))
            return getDate(Constants.MM);
        else if (Constants.DAY.equals(circle))
            return getDate(Constants.DD);
        else if (Constants.DATE.equals(circle))
            return getDate(Constants.YYYYMMDD);
        return "";
    }

    /**
     * 重置计数周期
     * @param keyId  规则编码的key值
     * @param orgId  组织ID
     * @param newCircle  新的周期数值
     * @return 是否设置成功（true or false）
     */
    private synchronized boolean resetCircle(String keyId, String orgId,
                                             String newCircle) {
        try {
            JSONObject values = new JSONObject();
            values.put(Constants.MAX_VAL, keyConfig.get(keyId).getInit());
            values.put(Constants.NEW_CIRCLE, newCircle);
            values.put(Constants.KEYID, keyId);
            values.put(Constants.ORGID, orgId);
            return (baseDao.update(values, "key.update.key.generator.reset") > 0);
        } catch (Exception e) {
            LOG.error("重置计数周期发生错误", e);
        }

        return false;
    }

    /**
     * 根据序列号的位数进行补位
     * @param seq 序列号格式
     * @param filler  补位字符(默认为0)
     * @param len 长度
     * @return  补位后的序列号
     */
    private String fillStr(String seq, String filler, int len) {
        int oLen = seq.length();
        if (oLen >= len)
            return seq;
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < len - oLen; i++) {
            str.append(filler);
        }
        return str.append(seq).toString();
    }

    /**
     * 插入新的编码ID
     * @param keyId 新定义的编码ID
     * @param circleVal 编码周期
     * @return 插入是否成功
     */
    private synchronized boolean addNewKey(String keyId, String circleVal) {
        KeyEntity entity = keyConfig.get(keyId);

        try {
            JSONObject values = new JSONObject();
            values.put(Constants.KEYID, keyId);
            values.put(Constants.MAX_VAL, entity.getInit());
            values.put(Constants.CIRCLE, circleVal);
            values.put(Constants.ORGID, "");
            values.put(Constants.DESC, entity.getDescription());

            return (baseDao.add(values, "key.add.key.generator.add").size() > 0);
        } catch (Exception e) {
            LOG.error("新增主键生成器配置发生错误", e);
        }
        return false;
    }

    /**
     * 更新主键最大值
     * @param keyId 主键ID
     * @param orgId 组织ID
     * @param step  步长（默认为1）
     * @return   是否更新成功
     */
    private synchronized boolean updateMaxVal(String keyId, String orgId,
                                              int step) {
        try {
            JSONObject values = new JSONObject();
            values.put("incr", step);
            values.put(Constants.KEYID, keyId);
            values.put(Constants.ORGID, orgId);
            return (baseDao.update(values, "key.update.key.generator.update_max") > 0);
        } catch (Exception e) {
            LOG.error("更新主键最大值发生错误", e);
        }

        return false;
    }
}
