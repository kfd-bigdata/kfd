package com.kdf.web.server.dto;


import com.kdf.web.server.bean.PbUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends PbUser {

    /**
     * @Fields serialVersionUID : 用一句话描述这个变量表示什么
     */
    private static final long serialVersionUID = 1L;

    private String departmentIds;

    private String departmentNameArr;

}
