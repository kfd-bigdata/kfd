package com.kdf.web.server.service.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.web.server.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private HttpSession httpSession;

	@Override
	public String getCurrentAppid() {
		String appid = (String) httpSession.getAttribute("appid");
		return appid;
	}

}
