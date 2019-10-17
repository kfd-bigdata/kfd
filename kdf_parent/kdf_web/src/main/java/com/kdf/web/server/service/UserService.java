package com.kdf.web.server.service;

import java.util.List;
import java.util.Map;

import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.dto.UserDTO;

/**
 * 用户业务接口
 * @ClassName: UserService   
 * @author: PéiGǔangTíng
 * @date: 2019年8月5日 下午2:44:15
 */
public interface UserService {

    Map<String, Object> findByDelFlag(int i, Integer page, Integer limit);

    void save(UserDTO bean);

    void update(UserDTO userDTO, int userId);

    void delByIds(Integer[] ids);

	PbUser findByUsernameAndPassword(String username, String password);

	PbUser findByUserName(String username);

	PbUser findUserByUserId(Integer userId);

	void save(PbUser user);

    List<PbUserDepartment> findByUserId(Integer userId);

    Integer delUserDepartmentByUserId(Integer userId);
	
}
