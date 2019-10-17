package com.kdf.web.server.service;

import java.util.List;
import java.util.Map;

import com.kdf.etl.bean.UvUserAreaDistribution;

/**
 * 地域分布业务接口类
 * 
 * @ClassName: UvUserAreaDistributionService
 * @author: PéiGǔangTíng QQ：1396968024
 * @date: 2019年10月16日 上午9:47:46
 */
public interface UvUserAreaDistributionService {

	List<UvUserAreaDistribution> findAll();

	/**
	 * countGroupByProvince 通过市区进行分组统计启动次数
	 * 
	 * @param appid
	 * @return
	 */
	List<Map<String, Object>> countGroupByProvince(String appid);

}
