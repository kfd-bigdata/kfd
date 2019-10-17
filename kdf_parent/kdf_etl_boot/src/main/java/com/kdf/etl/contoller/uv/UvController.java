package com.kdf.etl.contoller.uv;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdf.etl.service.UvHiveService;

@RestController("/uv")
public class UvController {

	@Autowired
	private UvHiveService uvHiveService;
	
	@GetMapping
	public List<Map<String, String>> getBrowserUv() {
		return uvHiveService.getBrowserUv("2019_10_10_17");
	}
	
}
