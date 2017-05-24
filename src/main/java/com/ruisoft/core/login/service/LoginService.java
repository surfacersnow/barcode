package com.ruisoft.core.login.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ruisoft.base.service.BaseService;
import com.ruisoft.common.SysCache;
import com.ruisoft.core.dml.entity.QueryEntity;

@Service
public class LoginService extends BaseService{

	private final static Logger LOG = Logger.getLogger(LoginService.class);

	public JSONObject login(JSONObject json) throws Exception {
		LOG.debug("登录操作：" + json.getString("uname"));

		List<JSONObject> results = baseDao.query(json, "rbac.select.user.login");

		if (results.isEmpty())
			return null;

		return results.get(0);
	}

	public int getUserName(JSONObject json) throws Exception {
		LOG.debug("验证用户是否存在:"+json.getString("uname"));

		String querySql = "SELECT ACCOUNT FROM CM_USER WHERE ACCOUNT = '".concat(json.getString("uname").trim()).concat("'");

		int result = count(querySql);

		return result;
	}

	public List<JSONObject> getRole(String userId) throws Exception {
		LOG.debug("获取角色的用户：" + userId);

		List<JSONObject> results = baseDao.query("{\"userId\":\"" + userId + "\"}",
				"rbac.select.getRoles.byUser");
		if (results.isEmpty())
			return null;
		LOG.debug("用户：" + userId + "的角色列表" + results.toString());
		return results;
	}

	public List<String> getRoleCodes(String userId) throws Exception{
		Iterator<JSONObject> iterator=getRole(userId).iterator();
		List<String> roleCodes = new ArrayList<String>();
		while(iterator.hasNext()){
			roleCodes.add(iterator.next().getString("code"));			
		}
		LOG.debug("用户：" + userId + "的角色列表(code list)" + roleCodes.toString());
		return roleCodes;

	}

	/**
	 * 获取用户所属的组织
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public JSONObject getUserORG(String userId) throws Exception {
		List<JSONObject> results = baseDao.query("{\"userId\":\"" + userId + "\"}",
				((QueryEntity) SysCache.get("rbac.select.org.byUser.query")));
		if (results.isEmpty())
			return null;
		return results.get(0);
	}


    /**
     * 根据用户ID获取菜单
     * @param userId 用户ID
     * @return
     * @throws Exception
     */
    public String getMenu(String userId) throws Exception {
        List<JSONObject> results = baseDao.query("{\"userId\":\"" + userId + "\"}",
                ((QueryEntity) SysCache.get("rbac.select.menu.query")));
        // 初始化菜单对象
        String menus = JSONArray.fromObject(results).toString();
        LOG.debug(menus);

        return menus;
    }
}
