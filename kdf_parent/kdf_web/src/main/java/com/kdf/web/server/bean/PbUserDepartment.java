package com.kdf.web.server.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**    
 * 功能描述：
 * @Package: com.admin.server.bean 
 * @author: LiWenLong  
 * @date: 2019年8月12日 上午8:50:24 
 */
@Data
@Entity
@Table(name = "pb_user_department")
public class PbUserDepartment {

	@Id
	@Column(name="user_department_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userDepartmentId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="department_id")
	private Integer departmentId;
}
