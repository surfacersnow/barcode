package com.ruisoft.core.json;

//import org.json.JSONException;
//import org.json.JSONObject;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class JSONPagerMapper implements RowMapper<JSONObject> {


    private int rStart = 0;

    private int rEnd = 0;

    public int getrStart() {
        return rStart;
    }

    public void setrStart(int rStart) {
        this.rStart = rStart;
    }

    public int getrEnd() {
        return rEnd;
    }

    public void setrEnd(int rEnd) {
        this.rEnd = rEnd;
    }

    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
//        if (rStart > 1 && rowNum == 0) {
//            for(int i=1;i<=rStart;i++){
//            	rs.next();
//            }
//        }
        if (rStart > 1 && rowNum == 0) {
            rs.absolute(rStart);
        }
        ResultSetMetaData meta = rs.getMetaData();
        int i = meta.getColumnCount();
        String col = null, val = null;
        JSONObject json = new JSONObject();
        for (int j = 1; j <= i; j++) {
            col = meta.getColumnLabel(j);
            val = rs.getString(col);
            try {
                json.put(col.toLowerCase(), (val == null ? "null" : val));
            } catch (JSONException e) {
                throw new SQLException();
            }
        }

        if (rowNum == rEnd - rStart) {
            rs.afterLast();
        }

        return json;
    }
    
}