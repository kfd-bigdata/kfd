package com.kdf.etl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @title: ClientTypeVO.java 
 * @package com.kdf.etl.vo 
 * @description: 客户端类型uv页面显示vo
 * @author: 、T
 * @date: 2019年10月15日 下午1:21:16 
 * @version: V1.0
 */
@Data
@AllArgsConstructor
public class ClientTypeVO {

	/**
	 * 请求时间HH:mm-HH:mm
	 */
	private String requestTime;
	/**
	 * pcuv数量
	 */
	private Long pcCount;
	/**
	 * mobileuv数量
	 */
	private Long mobileCount;
}
