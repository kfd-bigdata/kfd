package com.kdf.etl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "kfd_uv_browser")
public class UvBrowser extends Common {

	private String appid;
	
	private Date requestTime;
	
	private String browserName;
	
	private Long uvCount;
}
