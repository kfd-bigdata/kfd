package com.kdf.etl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "kfd_pv_http_client")
public class PvHttpClient {
	
	/**
	 * id 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * createTime 创建时间
	 */
	private Date createTime = new Date();
	
	/**
	 * ip
	 */
	private String appid;

	/**
	 * pv总数
	 */
	private Long pvCount;
	
	/**
	 * method
	 */
	private String method;

	/**
	 * RequestTime 请求时间（yyyy-mm-dd HH:00:00）
	 */
	private Date requestTime;

}
