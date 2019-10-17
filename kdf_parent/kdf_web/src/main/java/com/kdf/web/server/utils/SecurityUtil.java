package com.kdf.web.server.utils;

import java.math.BigInteger;

import com.kdf.web.server.utils.uuid.UUIDUtil;

import cn.hutool.crypto.SecureUtil;

/**
 * 密码安全帮助类
 */
public final class SecurityUtil {


    private static final BigInteger N = new BigInteger(
            "7318321375709168120463791861978437703461807315898125152257493378072925281977");

    private SecurityUtil() {

    }


    /**
     * 生成密码安全码
     */
    public static String getNewPsw() {
        return SecureUtil.md5(String.valueOf(System.currentTimeMillis())) + UUIDUtil.getUUID();
    }

    /**
     * 生成加密后的密码
     */
    public static String getLogpwd( String logpwd, String psw) {
        return SecureUtil.md5( SecureUtil.md5(logpwd) + psw);
    }
    //test
    public static void main(String[] args) {
    	System.out.println(getLogpwd("mengpp@123","c31e92da957445b371a514808a223bd97ffffe93b0b18b8a08f7a6aa7b817452"));
//    	System.out.println(getNewPsw());
    }
}
