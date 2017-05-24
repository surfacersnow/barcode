package com.ruisoft.core.param;

import java.util.ArrayList;
import java.util.List;

import com.ruisoft.common.SysConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.hibernate.mapping.Array;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: LFC
 * Date: 2015/10/12
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class RequestUtil {

    /**
     * 获取当前Request对象.
     * @return 当前Request对象或{@code null}.
     * @throws IllegalStateException 当前线程不是web请求抛出此异常.
     */
    public static HttpServletRequest currentRequest() throws IllegalStateException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return attrs.getRequest();
    }

    /**
     * 获取当前session对象. 若当前线程不是web请求或当前尚未创建{@code session}则返回{@code null}.
     * @return 当前session对象或{@code null}.
     */
    public static HttpSession currentSession() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return  attrs.getRequest().getSession(false);
    }
    
    /**
     * 获取当前用户Session Json 信息
     * @return
     */
    public static JSONObject getUserSessionJson() {
    	if(currentSession() == null){
    		return null;
    	}
        try {
            return (JSONObject) currentSession().getAttribute(SysConstants.USER_INFO.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取当前用户 组织 Session Json 信息
     * @return
     */
    public static JSONObject getUserORGSessionJson() {
    	if(currentSession() == null){
    		return null;
    	}
        try {
            return (JSONObject) currentSession().getAttribute(SysConstants.ORG_INFO.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取当前用户名称
     * @return
     */
    public static String getCurrentUserName() {
    	if(getUserSessionJson() != null&&getUserSessionJson().has("uname"))
    		return getUserSessionJson().getString("uname");
    	return null;
    }

    /**
     * 获取当前用户编号
     * @return
     */
    public static String getCurrentUserCode() {
    	if(getUserSessionJson() != null&&getUserSessionJson().has("code"))
    		return getUserSessionJson().getString("code");
    	return null;
    }
    
    /**
     * 获取当前用户登录帐号
     * @return
     */
    public static String getCurrentUserAccount() {
    	if(getUserSessionJson() != null&&getUserSessionJson().has("account"))
    		return getUserSessionJson().getString("account");
    	return null;
    }

    /**
     * 获取当前用户id
     * @return
     */
    public static String getCurrentUserID() {
    	if(getUserSessionJson() != null&&getUserSessionJson().has("userid"))
    		return getUserSessionJson().getString("userid");
    	return null;
    }


    /**
     * 获取当前用户所属部门ID
     * @return
     */
    public static String getCurrentOrgId() {
    	if(getUserORGSessionJson() != null&&getUserORGSessionJson().has("oid"))
    		return getUserSessionJson().getString("oid");
    	return null;    	
    }


    /**
     * 获取当前用户所属部门名称
     * @return
     */
    public static String getCurrentOrgName() {
    	if(getUserORGSessionJson() != null&&getUserORGSessionJson().has("oname"))
    		return getUserSessionJson().getString("oname");
    	return null;   
    }

    /**
     * 获取当前用户所属公司ID
     * @return
     */
    public static String getCurCompanyId() {
    	if(getUserORGSessionJson() != null&&getUserORGSessionJson().has("cid"))
    		return getUserORGSessionJson().getString("cid");
    	// 上级部门为空时，当前部门即为本公司
    	if(getCurrentOrgId() != null)
    		return getCurrentOrgId();
    	return null;    	
    }
    /**
     * 当前用户资源权限
     * @return
     */
    public static List<JSONObject> getCurrentRoles() {
    	if(currentSession() == null){
    		return null;
    	}
    	List<JSONObject> json = new ArrayList<JSONObject>() ;
        try {
        	json = (List<JSONObject>)currentSession().getAttribute(SysConstants.ROLES_INFO.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    
}
