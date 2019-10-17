package com.kdf.etl.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdf.etl.service.HttpClientPVService;

@RestController
public class HttpClientPVController{

	@Autowired
	private HttpClientPVService httpClientPVService;
	
	@RequestMapping("/test422")
	public void getBrowserUv() {
		httpClientPVService.saveHttpClient("2019_10_10_10");
	}
	
}
