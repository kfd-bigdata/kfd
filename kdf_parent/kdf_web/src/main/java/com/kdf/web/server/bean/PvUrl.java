package com.kdf.web.server.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "kfd_pv_url")
@Data
public class PvUrl {
    /**
     * id 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  appid
     */
    private String appId;

    /**
     * 请求次数
     */
    private Integer pvCount;

    /**
     * url
     */
    private String url;

    /**
     * createTime 创建时间
     */
    private Date createTime;

    /**
     * requestTime 请求时间（yyyy-mm-dd HH:00:00）
     */
    private Date requestTime;
}
