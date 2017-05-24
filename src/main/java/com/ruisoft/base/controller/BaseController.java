package com.ruisoft.base.controller;

import com.ruisoft.base.bean.QueryData;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.common.CommonUtil;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.json.JSONMap;
import com.ruisoft.core.json.JSONUtil;
import com.ruisoft.core.param.RequestUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * @author Administrator
 *
 */
public class BaseController {
    private static final Logger LOG = Logger.getLogger(BaseController.class);

    protected HttpServletRequest request = null;

    protected HttpSession session = null;

    protected HttpServletResponse response = null;

    protected QueryData queryData = null;

    public static final Pattern PAT_PARAM = Pattern.compile("^(?:[^=&]+=[^=&]*&)*(?:[^=&]+=[^=&]*)$");
    // liger Grid 需要的返回 response 格式
    private static final String RESPONSE_STR = "{\"rows\":{rowsdata},\"total\":\"{totaldata}\"}";

    private JSONObject reqData = new JSONObject(true);

    @Resource
    protected BaseDao baseDAO = null;
    
    
    protected final void response(String json) throws IOException {
        new JSONUtil().outputJSON(response, json);
    }

    protected final void response(JSONMap<? extends Object, ? extends Object> json) throws IOException {
        new JSONUtil().outputJSON(response, json);
    }

    @ModelAttribute
    protected void setContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }
    
    /**
     * 请求参数及数据处理，转换为 json格式数据，
     * 同时统一处理新增、修改是的公共字段赋值，如id、company_id、create_date、 write_uid
     * 在请求数据中增加 user session 数据
     * @return
     * @throws IOException
     */
    protected JSONObject getReqData() throws IOException {
        String info = request.getReader().readLine();
        JSONObject json = new JSONObject();
        LOG.debug("请求参数：" + info);
        // 处理请求参数
        if (PAT_PARAM.matcher(info).matches()) {
           
            String[] param = info.split("&");
            String k = null, v = null;
            for (String p : param) {
                try {
                    k = p.split("=")[0];
                    v = p.split("=")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    v = "";
                } finally {
                    json.put(k, v);
                }
            }
            return json;
        }
        //请求数据转换为 json格式数据
        json = JSONObject.fromObject(info);
        return json ;
    }
    
    /**
     * 分页查询，按指定的查询实体名称即配置SQL ID，
     * 返回 Liger grid 格式的response
     * @param entityName
     */
    public void query(String entityName) {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        try {
            if (reqData.isNullObject())
                reqData = getReqData();

            if(!reqData.has("data")){
            	reqData.put("data", new JSONObject());
            }
            // 查询实体名称
            // String entityName = reqData.getString("en");
            if (entityName == null || SysCache.get(entityName) == null) {
                returnJson.put("status", "-1");
                returnJson.put("msg", "没有指定查询实体名称");
            } else {
                // l-list 线性结构，默认；t-tree 树型结构
                String returnType = !reqData.has("rt") ? "l" : reqData.getString("rt");
                // 是否分页
                boolean isPager = reqData.getBoolean("p");
                // 当前页码
                int currentPage = !reqData.has("cp") ? 1 : reqData.getInt("cp");
                // 每页记录数
                int pageSize = !reqData.has("ps") ? 20 : reqData.getInt("ps");

                List<JSONObject> result = null;
                String rows = null, total = "0";
                if (isPager) {
                    result = baseDAO.queryForPage(entityName, reqData.getString("data"), currentPage, pageSize);
                    total = result.get(0).getString("total");
                    result.remove(0);
                } else {
                    result = baseDAO.query(reqData.getString("data"), entityName);
                }

                if ("t".equalsIgnoreCase(returnType)) {
                    rows = treeData(result);
                } else {
                    rows = JSONArray.fromObject(result).toString();
                }

                returnJson.put("status", "1");
                returnJson.put("data", RESPONSE_STR.replaceFirst("\\{totaldata\\}", total).replaceFirst("\\{rowsdata\\}", rows));
                response(RESPONSE_STR.replaceFirst("\\{totaldata\\}", total).replaceFirst("\\{rowsdata\\}", rows));
            }
        } catch (Exception e) {
            LOG.error("执行查询操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行查询操作发生错误");
        } finally {
            try {
                LOG.debug(returnJson);
                response(returnJson);
            } catch (IOException e) {
                LOG.error("查询返回信息时发生错误", e);
                ;
            }
            reqData = new JSONObject(true);
        }
    }
    /**
     * 分页查询，按分页查询对象queryData及查询结果集，
     * 返回 Liger grid 格式的response
     * @param q
     * @param result
     */
    public void queryResponse(QueryData q, List<JSONObject> result) {
        JSONMap<String, Object> returnJson = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        try {
            String rows = null, total = "0";
            if (q.isPage) {
                total = result.get(0).getString("total");
                result.remove(0);
            }

            if (q.isTreeGrid()) {
                rows = treeData(result);
            } else {
                rows = JSONArray.fromObject(result).toString();
            }
            returnJson.put("status", "1");
            returnJson.put("data", RESPONSE_STR.replaceFirst("\\{totaldata\\}", total).replaceFirst("\\{rowsdata\\}", rows));
            response(RESPONSE_STR.replaceFirst("\\{totaldata\\}", total).replaceFirst("\\{rowsdata\\}", rows));

        } catch (Exception e) {
            LOG.error("执行查询操作发生错误", e);
            returnJson.put("status", "-2");
            returnJson.put("msg", "执行查询操作发生错误");
        } finally {
            try {
                LOG.debug(returnJson);
                response(returnJson);
            } catch (IOException e) {
                LOG.error("查询返回信息时发生错误", e);
                ;
            }
            reqData = new JSONObject(true);
        }
    }

    protected String treeData(List<JSONObject> data) {
        return new JSONMap<String, String>(JSONMap.TYPE.ARRAY).toString();
    }


    // 获取request信息，转换为queryData
    public QueryData getQueryData() {
        try {
            if (reqData.isNullObject()) {
                reqData = getReqData();
            }

            if(!reqData.has("data")){
            	reqData.put("data", new JSONObject());
            }
            if (queryData == null) {
                queryData = new QueryData();
            }

            queryData.setParamsData(reqData.getJSONObject("data"));
            if(reqData.has("cp")){
            	queryData.setPage(true);
            }else{
            	queryData.setPage(false);
            }
            // 当前页码
            int curPage = !reqData.has("cp") ? 1 : reqData.getInt("cp");
            queryData.setCurPage(curPage);
            // 每页记录数
            int pageSize = !reqData.has("ps") ? 10 : reqData.getInt("ps");
            queryData.setPageSize(pageSize);

        } catch (Exception e) {
            LOG.error("执行查询操作发生错误", e);
        } finally {
            reqData = new JSONObject(true);;
        }
        return queryData;
    }

    /**
     * set id, company_id,dept_id, create_uid,create_date, write_uid, write_date
     *
     * @param json
     * @return
     */
    protected JSONObject setAddCommonJson(JSONObject json) {
        json.put("id", CommonUtil.getPKUUID());

        json.put("company_id", RequestUtil.getCurCompanyId());
        json.put("dept_id", RequestUtil.getCurrentOrgId());

        json.put("create_uid", RequestUtil.getCurrentUserAccount());
        json.put("create_date", CommonUtil.getCurDateTime());

        setUpCommonJson(json);
        return json;
    }

    /**
     * set write_uid, write_date
     *
     * @param json
     * @return
     */
    protected JSONObject setUpCommonJson(JSONObject json) {
        // write_uid, write_date,busi_date
        json.put("write_uid", RequestUtil.getCurrentUserAccount());
        json.put("write_date", CommonUtil.getCurDateTime());
        return json;
    }

    /**
     * 上传文件到数据库中
     * @param id 附件文件数据ID
     * @throws IOException
     */
    protected void insertFile(String id,String sql) throws IOException {
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    if (myFileName.trim() != "") {
                        System.out.println(myFileName);
                        //获取文件字节数组
                        byte[] filebytes = file.getBytes();
                        try {
                            //保存文件附件到数据库中
                            baseDAO.updateFile(filebytes,id,sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 上传文件到服务器
     * @param uploadPath 上传路径
     * @throws IOException
     */
    protected String uploadFile(String uploadPath) throws IOException {
    	String path="";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        System.out.println(myFileName);
                        //重命名上传后的文件名
                        String fileName = File.separator+ file.getOriginalFilename();
                        System.out.println(fileName);
                        //定义上传路径
                        path = uploadPath + fileName;
                        //生成本地文件
                        File localFile = new File(path);
                        //将文件上传到服务器生成的文件中
                        file.transferTo(localFile);
                    }
                }
            }
        }
        return path;
    }
    /**
     * 下载数据库中保存的附件文件
     * @param id 附件ID
     * @param fileName  附件名称
     * @throws IOException
     */
    public void  downloadFile(String id,String fileName) throws IOException {
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;fileName="+ fileName);
        OutputStream os = response.getOutputStream();
        baseDAO.queryFile(os,id);

    }
    @RequestMapping("getHtmlStringByXmlhttp")
    public void getHtmlStringByXmlhttp(){
    	String str="";
    	String sql1 = request.getParameter("sql")==null?"":request.getParameter("sql");
	 	if (!"".equals(sql1)) {
	 		Map<String, Object> map=baseDAO.queryForMap(sql1);
	 		for (Map.Entry<String, Object> entry : map.entrySet()) {  
	 			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
	 			str=(String) entry.getValue();
	 		}  
		}
    	 StringBuffer xmlstr = new StringBuffer();
		 xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		 xmlstr.append("<root>");
		 xmlstr.append("<item str='"+str
				+"'></item>");
		 xmlstr.append("</root>");
		 try {
			response.setContentType("text/xml;charset=utf-8");
			response.getWriter().write(xmlstr.toString());
			response.getWriter().flush();
			response.getWriter().close();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
    }
}