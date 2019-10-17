package com.kdf.web.server.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 部门
 * @ClassName: PbDepartment   
 * @author: PéiGǔangTíng
 * @date: 2019年8月5日 下午3:26:40
 */
@Data
@Entity
@Table(name = "pb_department")
public class PbDepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="department_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer departmentId;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="create_user_id")
	private Integer createUserId;

	@Column(name="del_flag")
	private Integer delFlag;

	@Column(name="department_name")
	private String departmentName;

	@Column(name="department_parent_id")
	private Integer departmentParentId;

	@Column(name="department_remark")
	private String departmentRemark;

	@Column(name="modify_time")
	private Date modifyTime;

	private Integer sort;

	private Integer type;

}