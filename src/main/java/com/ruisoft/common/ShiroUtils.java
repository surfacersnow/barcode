package com.ruisoft.common;

import java.util.List;

import javax.servlet.http.HttpSession;



import net.sf.json.JSONException;
import net.sf.json.JSONObject;


//import org.json.JSONException;
//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * 
 * @author wws
 * 
 */
@Component
public class ShiroUtils {

	private static final Logger log = LoggerFactory
			.getLogger(ShiroUtils.class);

	@SuppressWarnings("unchecked")
	public List<String> getRoleGroups(HttpSession session) {

		return (List<String>) session.getAttribute(SysConstants.ROLE_CODES_INFO
				.toString());
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	public String getAssigneesStr(HttpSession session) {

		String[] assignees = getAssignees(session);

		StringBuffer assStr = new StringBuffer();

		for (String actor : assignees) {
			assStr.append("'" + actor + "',");
		}

		assStr.deleteCharAt(assStr.length() - 1);
		log.info(assStr.toString());
		return assStr.toString();

	}

	public String[] getAssignees(HttpSession session) {
		List<String> list = this.getRoleGroups(session);
		//增加判断，不重复增加
		String userCode=this.getCurrentUser(session);
		if(!list.contains(userCode))
		list.add(this.getCurrentUser(session));
		log.info(list.toString());
		String[] assignees = new String[list.size()];
		list.toArray(assignees);
		return assignees;
	}

	public String getCurrentUser(HttpSession session) {

		try {
			return ((JSONObject) session.getAttribute(SysConstants.USER_INFO
					.toString())).getString("code");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

}
