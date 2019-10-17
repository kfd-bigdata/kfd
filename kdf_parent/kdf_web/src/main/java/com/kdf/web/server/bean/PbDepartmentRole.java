package com.kdf.web.server.bean;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the pb_department_role database table.
 * 
 */
@Data
@Entity
@Table(name="pb_department_role")
@NamedQuery(name="PbDepartmentRole.findAll", query="SELECT p FROM PbDepartmentRole p")
public class PbDepartmentRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="department_id")
	private Integer departmentId;

	@Column(name="role_id")
	private Integer roleId;


}