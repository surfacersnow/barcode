package com.ruisoft.core.param;

public class ListParamEntity  implements ParamEntity  {
	/** 是否需要缓存 */
	private boolean cache = true;

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}


}
