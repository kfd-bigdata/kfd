package com.kdf.etl.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @ClassName: DataSourceProperties
 * @Description: hivetemplet 配置
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Data
@Component
@ConfigurationProperties(prefix = DataSourceProperties.DATASOURCE, ignoreUnknownFields = false)
public class DataSourceProperties {
	final static String DATASOURCE = "kfd.datasource";

	private Map<String, String> hive;

	private Map<String, String> commonConfig;

}