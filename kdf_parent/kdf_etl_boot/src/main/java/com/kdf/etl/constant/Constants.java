package com.kdf.etl.constant;

/**
 * 
 * @ClassName: Constants
 * @Description: 常量类
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
public class Constants {

	public static String IP = "192.168.31.37";
	/**
	 * hdfs 地址
	 */
	public static String HDFS = "hdfs://" + IP + ":9000";
	/**
	 * zk host 地址
	 */
	public static String ZK_HOST = IP + ":2181";
	/**
	 * hbase表名
	 */
	public final static String HBASE_TABLENAME = "pv_log_";
	/**
	 * hbase列族
	 */
	public final static String HBASE_COLF = "log";

}
