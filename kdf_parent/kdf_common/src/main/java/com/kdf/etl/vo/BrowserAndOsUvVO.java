package com.kdf.etl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @title: BrowserAndOsUvVO.java 
 * @package com.kdf.etl.vo 
 * @description: 浏览器与操作系统页面显示vo
 * @author: 、T
 * @date: 2019年10月15日 下午1:19:57 
 * @version: V1.0
 */
@Data
@AllArgsConstructor
public class BrowserAndOsUvVO {

	/**
	 * 浏览器名字或操作系统的名称+版本
	 */
	private String name;
	/**
	 * UV数量
	 */
	private Long uvCount;
}
