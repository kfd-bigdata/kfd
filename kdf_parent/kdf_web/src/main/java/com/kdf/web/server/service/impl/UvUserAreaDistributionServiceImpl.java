package com.kdf.web.server.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.bean.UvUserAreaDistribution;
import com.kdf.etl.repository.UvUserAreaDistributionRepository;
import com.kdf.web.server.service.UvUserAreaDistributionService;

/**
 * 地域分布业务实现类
 * 
 * @ClassName: UvUserAreaDistributionServiceImpl
 * @author: PéiGǔangTíng QQ：1396968024
 * @date: 2019年10月16日 上午9:48:05
 */
@Service
public class UvUserAreaDistributionServiceImpl implements UvUserAreaDistributionService {

	@Autowired
	private UvUserAreaDistributionRepository userAreaDistributionRepository;

	@Override
	public List<UvUserAreaDistribution> findAll() {
		return userAreaDistributionRepository.findAll();
	}

	@Override
	public List<Map<String, Object>> countGroupByProvince(String appid) {
		return userAreaDistributionRepository.countGroupByProvince(appid);
	}

}
