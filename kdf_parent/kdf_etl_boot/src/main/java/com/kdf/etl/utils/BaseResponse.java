package com.kdf.etl.utils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ip地址解析 响应参数
 * 
 * @date 2017-05-09
 * @author 王者の南が少ない
 *
 */
public class BaseResponse {

    private String code;
    private IpResponse data;

    public IpResponse getData() {
        return data;
    }

    public void setData(IpResponse data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * {"code":0,"data": {"country":"\u4e2d\u56fd", "country_id":"CN", "area":"\u534e\u5317", "area_id":"100000",
     * "region":"\u5317\u4eac\u5e02", "region_id":"110000", "city":"\u5317\u4eac\u5e02", "city_id":"110100",
     * "county":"", "county_id":"-1", "isp":"\u963f\u91cc\u4e91", "isp_id":"1000323", "ip":"47.93.165.98"}}
     */

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
