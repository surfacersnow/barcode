package com.ruisoft.core.login.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruisoft.core.json.JSONMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruisoft.base.controller.BaseController;
import com.ruisoft.common.SysConstants;
import com.ruisoft.core.login.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("rbac/cmUser")
public class LoginController extends BaseController {

    private final static Logger LOG = Logger.getLogger(LoginController.class);

    @Autowired
    public LoginService loginService;

    @RequestMapping(params = "method=login")
    public void login() {
        try {
            System.out.println("开始登陆。。。。。。。。。。");
            JSONObject json = getReqData();
            System.out.println(json.toString());
            if (json == null) {
                response("[{\"status\":\"1\",\"valid\":\"0\"}]");
                return;
            }
            JSONObject user  = loginService.login(json);
            System.out.println(user.toString());
            int valid = 1;
            if (user == null) // 登录失败
                valid = 0;
            else if (!"1".equals(user.getString("status")))
                valid = 2;
            else { // 登录成功
            	// 用户信息
            	session.setAttribute(SysConstants.USER_INFO.toString(), user);
            	// 菜单项
            	List<JSONObject> roles = loginService.getRole(user.getString("userid"));
            	if(roles==null){
            		valid = 3;
            	}else{
            		// 权限信息
            		session.setAttribute(SysConstants.ROLES_INFO.toString(), roles);
            		List<String> role_codes = loginService.getRoleCodes(user.getString("userid"));
            		// 权限信息
            		session.setAttribute(SysConstants.ROLE_CODES_INFO.toString(), role_codes);
            		// 机构信息
            		JSONObject org = loginService.getUserORG(user.getString("userid"));
            		// 添加机构信息
            		session.setAttribute(SysConstants.ORG_INFO.toString(), org);
            	}
            }
            JSONMap<String, Object> rjson = new JSONMap<String, Object>(
                    JSONMap.TYPE.OBJECT);

            rjson.put("status", 1);
            rjson.put("valid", valid);
            rjson.put("data", user);
            response(rjson.toString());
        } catch (Exception e) {
            LOG.error("登录操作发生错误", e);
        }
    }

    @RequestMapping(params = "method=getUserName")
    public void getUserName() {
        try {
            JSONObject json = getReqData();
            if (json == null) {
                response("[{\"valid\":\"0\"}]");
                return;
            }
            boolean flag = false;
            int user = loginService.getUserName(json);
            if (user==0) {
                flag = false;
            }else {
                flag = true;
            }
            response("{\"valid\":\"" + flag + "\"}");
        } catch (Exception e) {
            LOG.error("登录操作发生错误", e);
        }
    }

    @RequestMapping(params = "method=menu")
    public void getMenu() {
        try {
            HttpSession session = request.getSession();
            String userId = ((JSONObject) session
                    .getAttribute(SysConstants.USER_INFO.toString()))
                    .getString("userid");
            response(loginService.getMenu(userId));
        } catch (Exception e) {
            LOG.error("登录操作发生错误", e);
        }
    }

    @RequestMapping(params = "method=logout")
    public void logout(HttpServletRequest request,
                         HttpServletResponse response) {
        session.removeAttribute(SysConstants.USER_INFO.toString());
    	// 权限信息
		session.removeAttribute(SysConstants.ROLES_INFO.toString());
		// 权限信息
		session.removeAttribute(SysConstants.ROLE_CODES_INFO.toString());
		// 机构信息
		// 添加机构信息
		session.removeAttribute(SysConstants.ORG_INFO.toString());
        try {
            response.sendRedirect(request.getContextPath() + "/login.html");
        } catch (IOException e) {
            LOG.error(e);
        }
    }
    /**
     * 获取用户信息
     * @return
     */
    @RequestMapping(params = "method=getUserInfo")
    public void getCurrentUser(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject) session.getAttribute(SysConstants.USER_INFO.toString());
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                jsonObject.put("status", "0");
            } else {
                jsonObject.put("status", "1");
//                jsonObject.put("date",
//                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                jsonObject.put("sDate",
//                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            }
            response(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
