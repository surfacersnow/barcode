package com.ruisoft.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.sf.json.JSONObject;

public class CommonInterceptor extends HandlerInterceptorAdapter{  
    private final Logger log =  LoggerFactory.getLogger(CommonInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("==============执行顺序: 1、preHandle================");    
        String requestUri = request.getRequestURI();  
        String contextPath = request.getContextPath();  
        String url = requestUri.substring(contextPath.length());  
        
        System.out.println("requestUri:"+requestUri);    
        System.out.println("contextPath:"+contextPath);    
        System.out.println("url:"+url);    
          if("/rbac/cmUser".equals(url)||"/heatflowchart/chartForQueryInit".equals(url)
        		  ||"/heatflowchart/chartForSearch".equals(url)||"/heatflowchart/chartForExportExcel".equals(url)
        		  ||"/heatflowchart/chartForPoint".equals(url)||"/heatflowchart/downloadTjclExcel".equals(url)
        		  ||"/heatflowchart/register".equals(url)){
        	  return true;
          }else{
        	  JSONObject  user =  (JSONObject )request.getSession().getAttribute(SysConstants.USER_INFO.toString());   
	        if(user == null){  
	            log.info("Interceptor：跳转到login页面！");  
//	            request.getRequestDispatcher("/heatflow/login.jsp").forward(request, response); 
	            response.sendRedirect("/login.html");
	            return false;  
	        }else { 
	            return true;   
	        }
          }
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("==============执行顺序: 2、postHandle================");    
		System.out.println("==============执行顺序: 2、postHandle================");    
        if(modelAndView != null){  //加入当前时间    
            modelAndView.addObject("var", "测试postHandle");    
        } 
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("==============执行顺序: 3、afterCompletion================");    
		System.out.println("==============执行顺序: 3、afterCompletion================");    
	}  

    
}