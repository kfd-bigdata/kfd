package com.kdf.etl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "kfd_uv_os")
public class UvOs extends Common {

	private String appid;
	
	private Date requestTime;
	
	private String osName;
	
	private Long uvCount;
}
