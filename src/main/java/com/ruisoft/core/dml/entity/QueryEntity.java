package com.ruisoft.core.dml.entity;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

//import org.json.JSONException;
//import org.json.JSONObject;

public class QueryEntity extends DMLEntity {
    public QueryEntity() {
        dmlType = QueryEntity.QUERY_NORMAL;
    }

    private String order = null;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    private boolean desc = false;

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    private String group = null;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * 获取组装后的SQL语句
     * @param json  条件变量
     * @return
     * @throws JSONException
     */
    public String getSql(JSONObject json) throws JSONException {
        //获取组装后条件的SQL语句
        String sql = super.getSql(json);
        //动态组织排序部分
        if (order != null && !"".equals(order)) {
            sql = sql.concat(" ORDER BY ").concat(order);
            if (isDesc())
                sql = sql.concat(" DESC");
        }
        //组装分组条件
        if (group != null && !"".equals(group))
            sql = sql.concat(" GROUP BY ").concat(group);

        return sql;
    }
}
