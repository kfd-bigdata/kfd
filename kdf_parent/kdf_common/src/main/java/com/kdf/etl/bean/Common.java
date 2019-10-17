package com.kdf.etl.bean;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

/**
 * 公用
 * 
 * @author mengpp
 * @date 2019年10月10日13:27:34
 *
 */
@Data
@MappedSuperclass
public class Common {

	/**
	 * id ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * appid APPID
	 */
	private String appid;

	/**
	 * createTime 创建时间
	 */
	private Date createTime = new Date();

	/**
	 * requestTime 请求时间（yyyy-mm-dd HH:00:00）
	 */
	private Date requestTime;

}
