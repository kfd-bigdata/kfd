package com.kdf.web.server.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.web.server.bean.PbUserDepartment;

/**    
 * 功能描述：
 * @Package: com.admin.server.repository 
 * @author: LiWenLong  
 * @date: 2019年8月12日 上午8:52:22 
 */
@Repository
public interface UserDepartmentRepository extends JpaRepository<PbUserDepartment, Integer> {

	/**   
	 * @Title: findByUserId   
	 * @Description: TODO(描述这个方法的作用)   
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<PbUserDepartment>      
	 * @throws   
	 */  
	List<PbUserDepartment> findByUserId(Integer userId);
	
    @Transactional
    @Modifying
    @Query(value = "DELETE from pb_user_department WHERE user_id = ?1", nativeQuery = true)
    int deleteByUserId(Integer userId);

	List<PbUserDepartment> findByDepartmentId(Integer departmentId);

}
