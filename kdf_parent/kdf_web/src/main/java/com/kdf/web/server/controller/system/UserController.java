package com.kdf.web.server.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.web.server.base.BaseResponse;
import com.kdf.web.server.base.ResultCode;
import com.kdf.web.server.bean.PbDepartment;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.dto.DepartmentDTO;
import com.kdf.web.server.dto.ReturnDTO;
import com.kdf.web.server.dto.UserDTO;
import com.kdf.web.server.service.DepartmentService;
import com.kdf.web.server.service.UserService;
import com.kdf.web.server.utils.GetUserInfoUtil;
import com.kdf.web.server.utils.SecurityUtil;

/**
 * 用户控制器 
 * @ClassName: UserController   
 * @author: PéiGǔangTíng
 * @date: 2019年8月5日 下午1:59:17
 */
@RestController
@RequestMapping("user")
public class UserController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private UserService userService;

    @GetMapping("")
    private ModelAndView toRole() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("html/system/user");

        List<PbDepartment> departmentList = departmentService.findByDelFlag(0);
        mv.addObject("department", departmentList);
        return mv;
    }
    
    /**
     * list 用户列表
     * 
     * @param page  页码
     * @param limit 数量
     * @return
     */
    @GetMapping("list")
    public ReturnDTO list(Integer page, Integer limit) {
        Map<String, Object> map = userService.findByDelFlag(0, page, limit);
        ReturnDTO returnBean = new ReturnDTO();
        returnBean.setCode(200);
        returnBean.setMsg("成功");
        returnBean.setCount(Long.valueOf(map.get("count").toString()));
        returnBean.setData(map.get("data"));
        return returnBean;
    }
    
    /**
     * add 添加
     * @param session
     * @param bean    用户信息
     * @return
     */
    @PostMapping("add")
    public Boolean add(HttpSession session, UserDTO bean) {
        Boolean b = true;
        try {
            userService.save(bean);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
    
    /**
     * update 修改
     * @param session
     * @param bean    用户信息
     * @return
     */
    @PostMapping("update")
    public Boolean update(HttpSession session, UserDTO userDTO) {
        Boolean b = true;
        try {
            PbUser user = GetUserInfoUtil.getUserInfo(session);
            Integer userId = user.getUserId();
            userService.update(userDTO, userId);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
    
    
    /**
     * del 批量删除
     * 
     * @param ids 用户id
     * @return
     */
    @PostMapping("del")
    public Boolean del(Integer[] ids) {
        Boolean b = true;
        try {
            userService.delByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
    
    /**
     * 
     * @Title: getPassword   
     * @Description: 修改密码
     * @param:  oldPassword
     * @param:  userId
     * @param: @return      
     * @return: String      
     * @throws
     */
    @PostMapping("updatePassword")
	public ResponseEntity<BaseResponse<String>> getPassword(HttpSession session,String oldPassword , Integer userId ,String newPassword) {
    	BaseResponse<String> res = null;
    	res = new BaseResponse<String>(ResultCode.OK.getCode(), "修改密码成功");
		try {
			PbUser user = userService.findUserByUserId(userId);
			String oldPwd = SecurityUtil.getLogpwd(oldPassword,user.getSaltVal());
			if (!user.getPassword().equals(oldPwd)) {
				res = new BaseResponse<String>(ResultCode.ERROR.getCode(), "修改失败，旧密码错误");
				return res.sendResponse();
			}
			String newPwd = SecurityUtil.getLogpwd(newPassword,user.getSaltVal());
			user.setPassword(newPwd);
			// 更新新密码
			userService.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.sendResponse();
	}
    
    
    
    /**
     * 查询
     * @Title: departmentInfo     
     * @return ReturnDTO
     * @throws
     */
    @GetMapping("departmentInfo")
    public Map<String, Object> departmentInfo(Integer userId) {
        List<PbDepartment> departmentList = departmentService.findByDelFlag(0);
        List<DepartmentDTO> depDtoList = findChildren(departmentList, 0, userId);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("department", depDtoList);
        return map;
    }
    
    /**
     * 递归查询子节点
     * @Title: findChildren     
     * @throws
     */
    private List<DepartmentDTO> findChildren(List<PbDepartment> departmentList, Integer parentId, Integer userId) {
        List<DepartmentDTO> depDtoList = new ArrayList<DepartmentDTO>();
        // 用户岗位关联表数据
        List<PbUserDepartment> userDepartmentList = userService.findByUserId(userId);
        for (PbDepartment department : departmentList) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            Integer type = department.getType();
            Integer departmentId = department.getDepartmentId();
            departmentDTO.setId(departmentId);
            departmentDTO.setTitle(department.getDepartmentName());
            departmentDTO.setType(type);
            Boolean flag = false;
            // 修改回显选中
            if (userId > 0) {
                for (PbUserDepartment userDepartment : userDepartmentList) {
                    if (userDepartment.getDepartmentId().equals(departmentId)) {
                        flag = true;
                    }
                }
            }
            departmentDTO.setChecked(flag);
            if (type == 1) {
                departmentDTO.setDisabled(true);
            }
            if (parentId.equals(department.getDepartmentParentId())) {
                for (PbDepartment department2 : departmentList) {
                    if (department.getDepartmentId().equals(department2.getDepartmentParentId())) {
                        departmentDTO.setChildren(findChildren(departmentList, department.getDepartmentId(), userId));
                        break;
                    }
                }
                depDtoList.add(departmentDTO);
            }
        }
        return depDtoList;
    }
}
