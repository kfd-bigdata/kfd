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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


/**
 * The persistent class for the pb_button database table.
 * 
 */
@Data
@Entity
@Table(name="pb_button")
@NamedQuery(name="PbButton.findAll", query="SELECT p FROM PbButton p")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PbButton implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="button_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer buttonId;

	@Column(name="button_name")
	private String buttonName;

	private String colour;

	private String event;

	private Integer type;

	@Column(name="create_user_id")
	private Integer createUserId;
	
	private Date createTime;
	
	@Column(name="del_flag")
	private Integer delFlag;

	
}