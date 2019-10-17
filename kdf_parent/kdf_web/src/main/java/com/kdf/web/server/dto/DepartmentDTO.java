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
public class DepartmentDTO {

	private Integer id;

	private String title;

	private Boolean checked;
	
	private Boolean disabled;
	
	private Integer type;
	
	private List<DepartmentDTO> children;
}
