package com.kdf.web.server.base;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
 * @ClassName: ResultCode
 * @Description: 结果码
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年7月2日 下午4:04:44
 * 
 */

public enum ResultCode {
	
	/**
	 * 10000 到50000 以内位 业务正常 ,》=50000 异常
	 */
	OK("code-10000", "成功", 200),

	ERROR("code-50000", "失败", 500),
	
	ERROR_TOKEN("code-50001", "TOKEN失败", 401), 
	
	ERROR_SGIN("code-50002", "签名校验失败", 401),
	
	PARAM_ERROR("code-50003", "参数错误", 500),
	
	NULL_USER("code-50007", "登录失败，用户不存在", 500),
	
	ERROR_USER("code-50008", "登录失败，用户名或密码错误", 200),
	
	STORE_NAME_EXISTS("code-50009", "商户名称已经存在", 500),
	
	STORE_LOGIN_NAME_EXISTS("code-50010", "商户登录名已经存在", 500),
	
	UPDATE_STOREPWD_ERROR_1("code-50012","老密码输入不对",200),
	
	UPDATE_STOREPWD_ERROR_2("code-50013","新密码两次输入不对",200),
	
	HAS_BEEN_ASSOCIATED("code-50014","请先删除角色菜单关联的按钮后再进行修改",200)
	
	;

	private String code;

	private String msg;

	private int httpCode;

	ResultCode(String code, String msg, int httpCode) {
		this.code = code;
		this.msg = msg;
		this.httpCode = httpCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public static Optional<ResultCode> getResultCode(String code) {
		return Stream.of(ResultCode.values()).filter(s -> code.equals(s.getCode())).findFirst();
	}

//	/**
//	 * 操作成功
//	 */
//	public static String RESULTCODE_SUCCESS = "K000000";
//
//	/**
//	 * 操作exception；
//	 */
//	public static String RESULTCODE_EXCEPTION = "K5000000";
//	
//	/**
//	 * 操作error；
//	 */
//	public static String RESULTCODE_ERROR = "K4000000";
}
