package com.kdf.web.server.bean;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the pb_role database table.
 * 
 */
@Data
@Entity
@Table(name="pb_role")
@NamedQuery(name="PbRole.findAll", query="SELECT p FROM PbRole p")
public class PbRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="create_user_id")
	private Integer createUserId;

	@Column(name="del_flag")
	private Integer delFlag;

	@Column(name="delete_time")
	private Date deleteTime;

	@Column(name="delete_user_id")
	private Integer deleteUserId;

	@Column(name="role_describe")
	private String roleDescribe;

	@Column(name="role_name")
	private String roleName;

	@Column(name="update_time")
	private Date updateTime;

	@Column(name="update_user_id")
	private Integer updateUserId;

	
}