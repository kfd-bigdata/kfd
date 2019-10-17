package com.kdf.web.server.utils;

import javax.servlet.http.HttpSession;

import com.kdf.web.server.bean.PbUser;

public class GetUserInfoUtil {
	
	public static PbUser getUserInfo(HttpSession session) {
		return (PbUser) session.getAttribute("user");
	}
}
