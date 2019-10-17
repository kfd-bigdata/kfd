package com.kdf.web.server.bean;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the pb_role_menu database table.
 * 
 */
@Data
@Entity
@Table(name="pb_role_menu")
@NamedQuery(name="PbRoleMenu.findAll", query="SELECT p FROM PbRoleMenu p")
public class PbRoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String buttons;

	@Column(name="role_id")
	private Integer roleId;

	@Column(name="menu_id")
	private Integer menuId;

	
}