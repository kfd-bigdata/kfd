package com.kdf.web.server.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * SecurityInterceptor 资源拦截器
 * 
 * @author mengpp
 * @date 2019年6月27日14:20:04
 */
public class SecurityInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		if (null != obj) {
			return true;
		} else {
			StringBuffer url = request.getRequestURL();
			String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
					.append(request.getServletContext().getContextPath()).append("/").toString();
			response.sendRedirect(tempContextUrl);
			return false;
		}
	}

}
