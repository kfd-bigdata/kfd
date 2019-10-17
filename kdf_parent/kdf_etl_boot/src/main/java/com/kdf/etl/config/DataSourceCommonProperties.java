package com.kdf.etl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @ClassName: DataSourceCommonProperties
 * @Description: hive 数据源配置
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Data
@Component
@ConfigurationProperties(prefix = DataSourceCommonProperties.DS, ignoreUnknownFields = false)
public class DataSourceCommonProperties {
	final static String DS = "kfd.datasource.common-config";

	private int initialSize = 10;
	private int minIdle;
	private int maxIdle;
	private int maxActive;
	private int maxWait;
	private int timeBetweenEvictionRunsMillis;
	private int minEvictableIdleTimeMillis;
	private String validationQuery;
	private boolean testWhileIdle;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private boolean poolPreparedStatements;
	private int maxOpenPreparedStatements;
	private String filters;

	private String mapperLocations;
	private String typeAliasPackage;
}