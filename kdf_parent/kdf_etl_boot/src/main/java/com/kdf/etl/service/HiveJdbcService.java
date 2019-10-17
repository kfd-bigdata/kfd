package com.kdf.etl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HiveJdbcService {

	@Autowired
	@Qualifier("hiveJdbcTemplate")
	private JdbcTemplate hiveJdbcTemplate;
	
	/**
	 * 
	 * @title: createTable 
	 * @description: hive创建表
	 * @author: 、T
	 * @date: 2019年10月9日 下午4:29:15
	 * @param sql   建表语句
	 * @return
	 * @throws:
	 */
	public Boolean createTable(String sql) {
		boolean flag = true;
		try {
			hiveJdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = false;
			log.error("hive create table fail! {}", e.getMessage());
		}
		return flag;
	}
	
	/**
	 * 
	 * @title: deleteTable 
	 * @description: hive删除表
	 * @author: 、T
	 * @date: 2019年10月9日 下午4:29:37
	 * @param tableName	要删除表的表名
	 * @return
	 * @throws:
	 */
	public Boolean deleteTable(String tableName) {
		boolean flag = true;
		String sql = "DROP TABLE IF EXISTS " + tableName;
		try {
			hiveJdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = false;
			log.error("hive delete table fail! {}", e.getMessage());
		}
		
		return flag;
	}
	
	
}
