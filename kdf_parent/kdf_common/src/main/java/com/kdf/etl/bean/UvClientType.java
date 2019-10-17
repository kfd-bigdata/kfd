package com.kdf.etl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "kfd_uv_client_type")
public class UvClientType extends Common {

	private String appid;
	
	private Date requestTime;
	
	private Long mobileUvCount;
	
	private Long pcUvCount;
}
