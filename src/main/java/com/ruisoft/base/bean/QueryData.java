package com.ruisoft.base.bean;

import net.sf.json.JSONObject;

//import org.json.JSONObject;

/**
 * 查询分页公共对象
 * @author Administrator
 *
 */
public class QueryData {

	public JSONObject paramsData;

	// 分页相关
	public int curPage;
	public int pageSize;
	public boolean isPage;

	// 列表数据展示形式
	// l-list 线性结构，默认；t-tree 树型结构
	private boolean isTreeGrid;

	// 查询实体配置name
	private String queryEntityName;

	public JSONObject getParamsData() {
		return paramsData;
	}

	public void setParamsData(JSONObject paramsData) {
		this.paramsData = paramsData;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isPage() {
		return isPage;
	}

	public void setPage(boolean isPage) {
		this.isPage = isPage;
	}

	public boolean isTreeGrid() {
		return isTreeGrid;
	}

	public void setTreeGrid(boolean isTreeGrid) {
		this.isTreeGrid = isTreeGrid;
	}

	public String getQueryEntityName() {
		return queryEntityName;
	}

	public void setQueryEntityName(String queryEntityName) {
		this.queryEntityName = queryEntityName;
	}

}
