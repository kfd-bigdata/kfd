package com.kdf.web.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kdf.web.server.bean.PbDepartment;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.dto.UserDTO;
import com.kdf.web.server.repository.DepartmentRepository;
import com.kdf.web.server.repository.UserDepartmentRepository;
import com.kdf.web.server.repository.UserRepository;
import com.kdf.web.server.service.UserService;
import com.kdf.web.server.utils.SecurityUtil;

/**
 * 用户业务实现
 * @ClassName: UserServiceImpl   
 * @author: PéiGǔangTíng
 * @date: 2019年8月5日 下午2:44:26
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserDepartmentRepository userDepartmentRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public Map<String, Object> findByDelFlag(int delFlag, Integer page, Integer limit) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<PbUser> userBeanPage = userRepository.findByDelFlag(delFlag, pageable);

        List<PbUser> userBeanList = userBeanPage.getContent();
        List<UserDTO> userDTOList = new ArrayList<UserDTO>(userBeanList.size());
        for (PbUser userBean : userBeanList) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userBean, userDTO);

            List<PbUserDepartment> userDepartmentList = userDepartmentRepository.findByUserId(userDTO.getUserId());
            StringBuilder sb = new StringBuilder();
            for (PbUserDepartment userDepartment : userDepartmentList) {
                PbDepartment department = departmentRepository.getOne(userDepartment.getDepartmentId());
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(department.getDepartmentName());
            }
            userDTO.setDepartmentNameArr(sb.toString());
            userDTOList.add(userDTO);
        }

        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("data", userDTOList);
        map.put("count", userBeanPage.getTotalElements());

        return map;
    }

    @Override
    @Transactional
    public void save(UserDTO userDTO) {
        PbUser userBean = new PbUser();
        BeanUtils.copyProperties(userDTO, userBean);
        // 生成盐值
        String saltVal = SecurityUtil.getNewPsw();
        // 生成加密后的登陆密码
        String pwd = SecurityUtil.getLogpwd(userDTO.getPassword(), saltVal);
        userBean.setDelFlag(0);
        userBean.setCreateTime(new Date());
        userBean.setSaltVal(saltVal);
        userBean.setPassword(pwd);
        userRepository.save(userBean);
        // 插入用户部门关联表
        saveUserDepartment(userDTO.getDepartmentIds(), userBean.getUserId());
    }

    /**
     * 插入用户部门关联表
     * @Title: saveUserDepartment     
     * @throws
     */
    private void saveUserDepartment(String departmentIds,Integer userId) {
        // 插入部门用户关联表
        if (!StringUtils.isEmpty(departmentIds)) {
            String[] departmentList = departmentIds.split(",");
            for (String dempartmentIdStr : departmentList) {
                Integer dempartmentId = Integer.parseInt(dempartmentIdStr);
                PbUserDepartment userDepartment = new PbUserDepartment();
                userDepartment.setDepartmentId(dempartmentId);
                userDepartment.setUserId(userId);
                userDepartmentRepository.save(userDepartment);
            }
        }
    }

    @Override
    @Transactional
    public void update(UserDTO userDTO, int userId) {
        Integer id = userDTO.getUserId();
        // 删除该id下所有关联部门信息
        delUserDepartmentByUserId(id);
        PbUser userBean = userRepository.findById(id).get();

        String userName = userDTO.getUserName();
        if (!StringUtils.isEmpty(userName)) {
            userBean.setUserName(userName);
        }
        String password = userDTO.getPassword();
        if (!StringUtils.isEmpty(password)) {
            String pwdSalt = SecurityUtil.getLogpwd(password, userBean.getSaltVal());
            userBean.setPassword(pwdSalt);
        }
        String nickname = userDTO.getNickname();
        if (!StringUtils.isEmpty(nickname)) {
            userBean.setNickname(nickname);
        }
        String phone = userDTO.getPhone();
        if (!StringUtils.isEmpty(phone)) {
            userBean.setPhone(phone);
        }
        String email = userDTO.getEmail();
        if (!StringUtils.isEmpty(email)) {
            userBean.setEmail(email);
        }
        // 插入用户部门关联表
        saveUserDepartment(userDTO.getDepartmentIds(), userDTO.getUserId());
        userRepository.save(userBean);
    }
    
    @Override
    @Transactional
    public void delByIds(Integer[] ids) {
        for (Integer id : ids) {
            PbUser userBean = userRepository.findById(id).get();
            userBean.setDelFlag(1);
            userRepository.save(userBean);
            delUserDepartmentByUserId(id);
        }
    }

	@Override
	public PbUser findByUsernameAndPassword(String username, String password) {
		return userRepository.findByUserNameAndPassword(username, password);
	}

	@Override
	public PbUser findByUserName(String username) {
		return userRepository.findByUserName(username);
	}

	@Override
	public PbUser findUserByUserId(Integer userId) {
		return userRepository.getOne(userId);
	}

	@Override
	public void save(PbUser user) {
		userRepository.save(user);
	}

    @Override
    public List<PbUserDepartment> findByUserId(Integer userId) {
        return userDepartmentRepository.findByUserId(userId);
    }

    @Override
    public Integer delUserDepartmentByUserId(Integer userId) {
        return userDepartmentRepository.deleteByUserId(userId);
    }
}
