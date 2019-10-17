package com.kdf.web.server.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.repository.PvUrlRepository;
import com.kdf.web.server.service.PvUrlService;

@Service
public class PvUrlServiceImpl implements PvUrlService {

    @Autowired
    private PvUrlRepository pvUrlRepository;

    @Override
    public List<Map<String, Object>> selectPvUrlList(String appId) {
        return pvUrlRepository.selectPvUrlList(appId);
    }

	@Override
	public List<Map<String, Object>> selectPvUrlTime(String appId) {
		return pvUrlRepository.selectPvUrlTime(appId);
	}
}
