package com.kdf.etl.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户地域分布实体
 * @ClassName: UvUserAreaDistribution
 * @author: PéiGǔangTíng QQ：1396968024
 * @date: 2019年10月12日 上午11:11:14
 */
@Data
@Entity
@Table(name = "kfd_uv_user_area_distribution")
public class UvUserAreaDistribution extends Common {

    /**
     * 启动次数
     */
    @Column(name="start_count")
    private Long startCount;

    /**
     * 分布地区（国家）
     */
    @Column(name="country")
    private String country;
    
    /**
     * 分布地区（省份）
     */
    @Column(name="province")
    private String province;
    
    /**
     * 分布地区（城市）
     */
    @Column(name="city")
    private String city;
    

}
