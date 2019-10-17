package com.kdf.web.server.service;

import java.util.List;
import java.util.Map;

public interface PvUrlService {

	/**
	 *  获取当前项目的url总pv
	 * @param appId
	 * @return
	 */
    List<Map<String, Object>> selectPvUrlList(String appId);

    /**
     * 获取当前项目的url时间分布图
     * @param currentAppid
     * @return
     */
	List<Map<String, Object>> selectPvUrlTime(String appId);
}
