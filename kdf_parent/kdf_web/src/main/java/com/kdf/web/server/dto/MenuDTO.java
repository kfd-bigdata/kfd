package com.kdf.web.server.dto;

import java.util.List;

import lombok.Data;

@Data
public class MenuDTO {

	private Integer id;

	private String title;

	private String url;

	private Boolean checked;

	private List<MenuDTO> children;

}
