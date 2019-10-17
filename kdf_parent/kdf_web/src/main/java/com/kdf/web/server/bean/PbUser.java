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
 * 用户
 */
@Data
@Entity
@Table(name = "pb_user") 
public class PbUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="del_flag")
	private Integer delFlag;

	private String email;

	@Column(name="last_login_time")
	private Date lastLoginTime;

	@Column(name="modify_id")
	private Integer modifyId;

	@Column(name="modify_time")
	private Date modifyTime;

	private String nickname;

	private String password;

	private String phone;

	@Column(name="salt_val")
	private String saltVal;

	@Column(name="user_name")
	private String userName;
	
	private Integer superAdminFlag;
	
}