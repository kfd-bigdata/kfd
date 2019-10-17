package com.kdf.etl.service;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.kdf.etl.constant.Constants;

public class BaseService {
	protected Connection connection;

	public Configuration getConf() {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", Constants.HDFS);
		conf.set("hbase.zookeeper.quorum", Constants.ZK_HOST);
		conf = HBaseConfiguration.create(conf);
		try {
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conf;
	}
}
