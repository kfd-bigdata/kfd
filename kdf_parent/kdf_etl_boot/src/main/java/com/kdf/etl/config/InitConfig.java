package com.kdf.etl.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hive.HiveClientFactory;
import org.springframework.data.hadoop.hive.HiveClientFactoryBean;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @ClassName: InitConfig
 * @Description: 初始化bean
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Configuration
public class InitConfig {

	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Autowired
	private DataSourceCommonProperties dataSourceCommonProperties;

	@Bean()
	@Qualifier("hiveTemplate")
	public HiveTemplate hiveTemplate() {
		HiveClientFactoryBean hiveClientFactoryBean = new HiveClientFactoryBean();
		hiveClientFactoryBean.setHiveDataSource(dataSource());
		try {
			hiveClientFactoryBean.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HiveClientFactory hiveClientFactory = hiveClientFactoryBean.getObject();
		HiveTemplate hiveTemplate = new HiveTemplate(hiveClientFactory);
		return hiveTemplate;
	}

	@Bean
	@Qualifier("hiveJdbcTemplate")
	public JdbcTemplate hiveJdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();
		/**
		 * 配置数据源属性
		 */
		datasource.setUrl(dataSourceProperties.getHive().get("url"));
		datasource.setUsername(dataSourceProperties.getHive().get("username"));
		datasource.setPassword(dataSourceProperties.getHive().get("password"));
		datasource.setDriverClassName(dataSourceProperties.getHive().get("driver-class-name"));

		/**
		 * 配置统一属性
		 */
		datasource.setInitialSize(dataSourceCommonProperties.getInitialSize());
		datasource.setMinIdle(dataSourceCommonProperties.getMinIdle());
		datasource.setMaxActive(dataSourceCommonProperties.getMaxActive());
		datasource.setMaxWait(dataSourceCommonProperties.getMaxWait());
		datasource.setTimeBetweenEvictionRunsMillis(dataSourceCommonProperties.getTimeBetweenEvictionRunsMillis());
		datasource.setMinEvictableIdleTimeMillis(dataSourceCommonProperties.getMinEvictableIdleTimeMillis());
		datasource.setValidationQuery(dataSourceCommonProperties.getValidationQuery());
		datasource.setTestWhileIdle(dataSourceCommonProperties.isTestWhileIdle());
		datasource.setTestOnBorrow(dataSourceCommonProperties.isTestOnBorrow());
		datasource.setTestOnReturn(dataSourceCommonProperties.isTestOnReturn());
		datasource.setPoolPreparedStatements(dataSourceCommonProperties.isPoolPreparedStatements());
		try {
			datasource.setFilters(dataSourceCommonProperties.getFilters());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datasource;
	}

}
