package com.kdf.web.server.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;


/**
 * The persistent class for the pb_menu database table.
 * 
 */
@Data
@Entity
@Table(name="pb_menu")
@NamedQuery(name="PbMenu.findAll", query="SELECT p FROM PbMenu p")
public class PbMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="menu_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menuId;

	private String buttons;

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

	private String name;

	@Column(name="parent_id")
	private Integer parentId;

	private Integer sort;

	private Integer type;

	@Column(name="update_time")
	private Date updateTime;

	@Column(name="update_user_id")
	private Integer updateUserId;

	private String url;


}