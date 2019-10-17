package com.kdf.web.server.dto;

import lombok.Data;

@Data
public class ReturnDTO {

	private Integer code;

	private String msg;

	private Long count;

	private Object data;

}
