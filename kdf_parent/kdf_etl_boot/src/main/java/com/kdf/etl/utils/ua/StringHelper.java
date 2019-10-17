package com.kdf.etl.utils.ua;

/**
 * 
 * @title: StringHelper.java 
 * @package com.kdf.etl.utils.ua 
 * @description: user_agent解析字符串工具类
 * @author: 、T
 * @date: 2019年10月12日 下午5:23:01 
 * @version: V1.0
 */
public class StringHelper {
	
    /**
     * 
     * @title: toUpperCaseFirst 
     * @description: 将单词的首字母大写
     * @author: 、T
     * @date: 2019年10月12日 下午5:23:44
     * @param old
     * @return
     * @throws:
     */
    public static String toUpperCaseFirst(String old){   
        return old.substring(0,1).toUpperCase() + old.substring(1);
    }
}