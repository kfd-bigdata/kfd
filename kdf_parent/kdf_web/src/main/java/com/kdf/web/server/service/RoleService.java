package com.kdf.web.server.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.kdf.web.server.bean.PbRole;
import com.kdf.web.server.dto.RoleDTO;

/**    
 * 角色service
 * @Package: com.admin.server.service 
 * @author: LiWenLong  
 * @date: 2019年8月5日 下午1:59:32 
 */
public interface RoleService {

	Page<PbRole> findByDelFlagPage(Integer delflag, Integer page, Integer limit);

	List<PbRole> findByDelFlag(Integer delflag);

	PbRole save(PbRole roleBean);

	void del(Integer id, Integer userId);

	List<Map<String,Object>> selectButtonsByMenuId(Integer menuId, Integer roleId);
	
	List<RoleDTO> findByDelFlag(Integer delFlag, Integer departmentId);
}
