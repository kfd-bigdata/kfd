package com.kdf.etl.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "kfd_pv_all")
public class PvAll extends Common {

	/**
	 * pv总数
	 */
	private Long pvCount;

}
