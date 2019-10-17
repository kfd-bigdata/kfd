package com.kdf.etl.utils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ip地址解析 响应参数data域
 * @date 2019-10-09
 * @author 刘岩
 *
 */
public class IpResponse {

    private String country;
    private String area;
    private String region;
    private String city;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
