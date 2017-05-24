package com.ruisoft.core.json;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

public class JSONUtil {
	public JSONObject convert2json(ServletRequest request) throws IOException, JSONException {
		String info = request.getReader().readLine();
		if (info == null) {
			info = "{}";
		}
		return convert2json(info);
	}
	
	public JSONObject convert2json(String str) throws JSONException {
		return JSONObject.fromObject(str);
	}
	
	public void outputJSON(HttpServletResponse response, String json)
			throws IOException {
		response.setContentType("text/json; charset=UTF-8");
		Writer writer = response.getWriter();
		writer.write(json.toString());
		writer.flush();
		writer.close();
	}
	
	public void outputJSON(HttpServletResponse response, JSONObject json)
			throws IOException {
		outputJSON(response, json.toString());
	}
	
	public void outputJSON(HttpServletResponse response, JSONArray json)
			throws IOException {
		outputJSON(response, json.toString());
	}
	
	public void outputJSON(HttpServletResponse response, JSONMap<?, ?> json)
			throws IOException {
		outputJSON(response, json.toString());
	}
}

