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
@Table(name = "kfd_user_project_relation")
public class KfdUserProjectRelation {
	/**
	 * id ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * createTime 创建时间
	 */
	private Date createTime = new Date();

	private Long userId;
	
	private Long projectId;

}
