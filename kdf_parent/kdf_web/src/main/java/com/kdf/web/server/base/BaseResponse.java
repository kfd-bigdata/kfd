package com.kdf.web.server.base;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 
 * @ClassName: BaseResponse
 * @Description: BaseResponse
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年7月2日 下午4:04:44
 * 
 */
@Data
@AllArgsConstructor
@ToString
public class BaseResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5684989672669043110L;

	public BaseResponse(ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMsg();
	}

	public BaseResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 结果码
	 */
	private String code;

	/**
	 * 消息内容
	 */
	private String message;

	/**
	 * 数据域
	 */
	private T data;

	public ResponseEntity<BaseResponse<T>> sendResponse() {
		return ResponseEntity.status(ResultCode.getResultCode(this.getCode()).get().getHttpCode()).body(this);
	}

}
