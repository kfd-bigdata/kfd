package com.kdf.etl.bean;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "kfd_uv_time_distribution")
public class UvTimeDistribution extends Common {

	/**
	 * uvCount 用户统计
	 */
	private Long uvCount;

}
