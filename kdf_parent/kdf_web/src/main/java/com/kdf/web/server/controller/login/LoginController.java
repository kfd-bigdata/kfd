package com.kdf.web.server.controller.login;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.etl.bean.KfdProject;
import com.kdf.etl.bean.KfdUserProjectRelation;
import com.kdf.etl.repository.KfdProjectRepository;
import com.kdf.etl.repository.KfdUserProjectRelationRepository;
import com.kdf.web.server.base.BaseResponse;
import com.kdf.web.server.base.ResultCode;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.dto.MenuDTO;
import com.kdf.web.server.service.MenuService;
import com.kdf.web.server.service.UserService;
import com.kdf.web.server.utils.SecurityUtil;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private KfdUserProjectRelationRepository kfdUserProjectRelationRepository;

	@Autowired
	private KfdProjectRepository kfdProjectRepository;

	@GetMapping("")
	public ModelAndView index() {
		return new ModelAndView("html/index");
	}

	/**
	 * loginAjax 登录
	 * 
	 * @param session
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("/loginAjax")
	@ResponseBody
	public ResponseEntity<BaseResponse<String>> loginAjax(HttpSession session, String username, String password,
			String code) {
		BaseResponse<String> res = null;
		// 验证码
		Object verificationCode = session.getAttribute("verificationCode");
		if (!verificationCode.equals(code)) {
			res = new BaseResponse<String>(ResultCode.ERROR.getCode(), "验证码输入有误");
			return res.sendResponse();
		}
		// 用户名密码
		PbUser userBean = userService.findByUserName(username);
		if (userBean == null) {
			res = new BaseResponse<String>(ResultCode.ERROR.getCode(), "用户不存在");
			return res.sendResponse();
		}
		String logpwd = SecurityUtil.getLogpwd(password, userBean.getSaltVal());
		if (!logpwd.equals(userBean.getPassword())) {
			res = new BaseResponse<String>(ResultCode.ERROR.getCode(), "用户名密码错误");
			return res.sendResponse();
		}
		// 获取菜单
		List<MenuDTO> menuDTOList = menuService.findByUserId(userBean.getUserId());
		session.setAttribute("user", userBean);
		session.setAttribute("menu", menuDTOList);

		// 配置项目
		List<KfdUserProjectRelation> kfdUserProjectRelationList = kfdUserProjectRelationRepository
				.findByUserId(userBean.getUserId().longValue());

		if (!CollectionUtils.isEmpty(kfdUserProjectRelationList)) {
			KfdUserProjectRelation kfdUserProjectRelation = kfdUserProjectRelationList.get(0);

			Long projectId = kfdUserProjectRelation.getProjectId();
			Optional<KfdProject> project = kfdProjectRepository.findById(projectId);

			if (project.isPresent()) {
				String appid = project.get().getAppid();
				session.setAttribute("appid", appid);
			}

		}

		res = new BaseResponse<String>(ResultCode.OK.getCode(), "成功");
		return res.sendResponse();
	}

	/**
	 * outLogin 退出登录
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/outLogin")
	public ModelAndView outLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		return new ModelAndView("redirect:");
	}

	/**
	 * defaultKaptcha 生成验证码
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws Exception
	 */
	@GetMapping("/defaultKaptcha")
	public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {
			CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 4);
			httpServletRequest.getSession().setAttribute("verificationCode", captcha.getCode());
			ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
			captcha.write(responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
