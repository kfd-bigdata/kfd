package com.kdf.web.server.dto;

import java.util.List;

import lombok.Data;
/**
 * 
 * @ClassName:  DepartmentDTO   
 * @Description:部门DTO
 * @author: LiShuangshuang
 * @date:   2019年8月5日 下午4:52:14
 */
@Data
public class RoleDTO {

	private Integer roleId;

	private String roleName;

	private String roleDescribe;

	private Boolean checked;

	private List<RoleDTO> children;
}
