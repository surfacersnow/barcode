package com.ruisoft.core.dict.controller;

import com.ruisoft.base.controller.BaseController;
import com.ruisoft.base.dao.BaseDao;
import com.ruisoft.base.service.BaseService;
import com.ruisoft.common.Constants;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.dml.entity.DMLEntity;
import com.ruisoft.core.json.JSONMap;
import com.ruisoft.core.param.ParamGenerator;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

/**
 *
 * @author wws
 *
 */
@Controller
@RequestMapping("/dict")
public class DictManageController extends BaseController {

    private static final Logger LOG = Logger.getLogger(DictManageController.class);

    @Autowired
    public BaseService baseService;
    
	@Resource
	protected BaseDao baseDAO = null;

    @Autowired
    private ParamGenerator paramGenerator;
    
    /**
     * 分页查找字典主表数据
     * @throws Exception
     */
    @RequestMapping("/queryDict")
    public void queryDict(){
        super.query("rbac.select.dict.def.query");
    }

    @RequestMapping("/queryDictItem")
    public void queryDictItem(){
        super.query("rbac.select.dict.def.queryItem");
    }
  /*  *//**
     * 查询单条字典数据，主从表数据均展示
     * @param id 字典主表ID
     * @throws Exception
     *//*
    @RequestMapping(value = "/searchdict/{id}", method = RequestMethod.GET)
    public void searchDict(@PathVariable String id) throws Exception {
        JSONObject result = baseService.searchDetail(id,"rbac.select.dict.def.queryId","rbac.select.dict.def.queryItem");
        JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        json.put("data", result);
        json.put("status",1);
        response(json);
    }*/

    /**
     * 查询单条字典数据所对应的明细数据
     * @param id 字典主表ID
     * @throws Exception
     *//*
    @RequestMapping(value = "/searchdictitem/{id}", method = RequestMethod.GET)
    public void searchDictItem(@PathVariable String id) throws Exception {
        List<JSONObject> result = baseService.searchData(id,"rbac.select.dict.def.queryItem");
        JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        json.put("data", new JSONArray(result).toString());
        json.put("status",1);
        response(json);
    }

    *//**
     * 根据字典明细表id，查询单条字典明细数据
     * @param id 字典明细表ID
     * @throws Exception
     *//*
    @RequestMapping(value = "/searchitem/{id}", method = RequestMethod.GET)
    public void searchItem(@PathVariable String id) throws Exception {
        List<JSONObject> result = baseService.searchData(id,"rbac.select.item.def.query");
        JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        json.put("data", new JSONArray(result).toString());
        json.put("status",1);
        response(json);
    }
*/

/*
    *//**
     * 新增字典表数据(主从表同时新增)
     *//*
    @RequestMapping("/addDictDef")
    public void addDictDef() {
        try {
            JSONObject reqData = getReqData();
            JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
            JSONObject result = new JSONObject();
            String code = reqData.getString("code");
            boolean checkflag = checkDictOnly(code);
            int status = 0;
            if(!checkflag){
                result = baseService.addDetail(reqData, "rbac.add.dict.def.add", "rbac.add.dict.item.add");
                status = 1;
            }else{
                status = -1;
            }
            json.put("data", reqData);
            json.put("status", status);
            response(json);
        } catch (Exception e) {
            LOG.error("新增字典定义发生错误", e);
        }
    }

     *//*
     * 修改字典表数据
     *//*
    @RequestMapping("/updateDictDef")
    public void updateDictDef() {
        try {
            JSONObject reqData = getReqData();
            JSONObject result = baseService.updateDetail(reqData, "rbac.update.dict.def.update","rbac.update.dict.item.update");
            JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
            json.put("data", result);
            json.put("status",1);
            response(json);
        } catch (Exception e) {
            LOG.error("更新字典定义发生错误", e);
        }
    }*/


    /**
     * 校验编码code是否唯一
     * @param code
     * @return
     * @throws Exception
     */
    public boolean checkDictOnly(String code) throws Exception {
        String checkSql = ((DMLEntity) SysCache.get("rbac.select.dict.def.query")).getSql()
                .concat(" WHERE CODE='").concat(code).concat("'");
        int ex = baseService.count(checkSql);
        if(ex > 0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 新增字典主表数据
     */
    @RequestMapping("/addDict")
    public void addDict() {
        JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);

        try {
            JSONObject reqData = getReqData();
            JSONObject result = new JSONObject();
            String code = reqData.getString("code");
            boolean checkflag = checkDictOnly(code);
            int status = 0;
            if (!checkflag) {
                result = baseService.add(reqData, "rbac.add.dict.def.add");
                status = 1;
            } else {
                status = -1;
                json.put("msg", "此字典编号已经存在！");
            }
            json.put("data", result);
            json.put("status", status);
        } catch (Exception e) {
            json.put("msg", "新增字典定义发生错误！");
            LOG.error("新增字典定义发生错误", e);
        } finally {
            try {
                response(json);
            } catch (Exception e) {
                LOG.error("插入返回信息时发生错误", e);
            }
        }
    }


    /**
     * 新增字典明细表数据
     */
    @RequestMapping("/addItem")
    public void addItem() {
        try {
            JSONObject reqData = getReqData();
            JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
            JSONObject result = new JSONObject();
            int status = -1;
            result = baseService.add(reqData,"rbac.add.dict.item.add");
            status = 1;
            json.put("data", reqData);
            json.put("status", status);
            response(json);
        } catch (Exception e) {
            LOG.error("新增字典定义发生错误", e);
        }
    }

    /**
     * 修改字典主表数据
     */
    @RequestMapping("/updateDict")
    public void updateDict() {
        try {
            JSONObject reqData = getReqData();
            int  status = baseService.update(reqData, "rbac.update.dict.def.update");
            JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
            json.put("data", reqData);
            json.put("status",status);
            response(json);
        } catch (Exception e) {
            LOG.error("更新字典定义发生错误", e);
        }
    }

    /**
     * 修改字典主表数据
     */
    @RequestMapping("/updateItem")
    public void updateItem() {
        try {
            JSONObject reqData = getReqData();
            int  status = baseService.update(reqData, "rbac.update.dict.item.update");
            JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
            json.put("data", reqData);
            json.put("status",status);
            response(json);
        } catch (Exception e) {
            LOG.error("更新字典定义发生错误", e);
        }
    }

    /**
     * 删除一条字典数据
     * @param id 字典ID
     * @throws Exception
     */
    @RequestMapping(value = "deleteDictDef/{id}", method = RequestMethod.GET)
    public void deleteDictDef(@PathVariable String id) throws Exception {
        int status = 1;
        int r = baseService.deleteDetail(id, "rbac.delete.dict.def.delete", "rbac.delete.dict.def.deleteitem");
        JSONMap<String, Object> json = new JSONMap<String, Object>(
                JSONMap.TYPE.OBJECT);
        if(r < 0){
            //删除字典明细出错
            status = -1;
        }
        json.put("status", status);
        response(json);
    }

    /**
     * 删除字典明细数据
     * @param id 字典明细ID
     * @throws Exception
     */
    @RequestMapping(value = "deleteDictItem/{id}", method = RequestMethod.GET)
    public void deleteDictItem(@PathVariable String id) throws Exception {
        int status = 1;
        int r = baseService.deletecode(id, "rbac.delete.dict.item.delete");
        if(r < 0){
            //删除字典明细出错
            status = -1;
        }
        JSONMap<String, Object> json = new JSONMap<String, Object>(
                JSONMap.TYPE.OBJECT);
        json.put("status", status);
        response(json);
    }

    /**
     * 字典数据项加载缓存
     */
    @RequestMapping("/cachedict")
    public void getDictItem() {
        try {
            // 请求JSON对象
            JSONObject reqObj = getReqData();
            // 要转换的字典字义名称
            String dictName = reqObj.getString("d");
            String result = paramGenerator.generate(dictName, reqObj);
            response(result);
        } catch (Exception e) {
            LOG.error("获取字典项定义发生错误", e);
        }
    }
}
