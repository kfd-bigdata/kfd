package com.kdf.etl.contoller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseSynchronizationManager;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kdf.etl.base.BaseHadoop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test3")
public class TestController3 extends BaseHadoop {

	@Autowired
	private HbaseTemplate hbaseTemplate;

	static final String ip = "192.168.31.37";

	private static final String HDFS_HOST = "hdfs://" + ip + ":9000";
	private static String ZK_HOST = ip + ":2181";
	private final static String TABLENAME = "table_name_test";// 表名
	public final static String COLF = "log";// 列族

	@Autowired
	private HiveTemplate hiveTemplate;

	@GetMapping
	public List<Map<String, String>> test() throws Exception {
		System.err.println("==================");
		List<Map<String, String>> mapList = Lists.newArrayList();
		hbaseTemplate.find("my_test", "log", (rowMapper, rowNum) -> {
			Map<String, String> map = Maps.newHashMap();
			Cell[] cells = rowMapper.rawCells();
			for(Cell c : cells){
//				String columnFamily = new String(CellUtil.cloneFamily(c));
                String rowName = new String(CellUtil.cloneQualifier(c));
                String value = new String(CellUtil.cloneValue(c));
                map.put(rowName, value);
                mapList.add(map);
			}
			return rowMapper;
		});
		mapList.stream().distinct().forEach(System.err::println);
		return mapList.stream().distinct().collect(Collectors.toList());
		
//		List<String> query = hiveTemplate.query("select *  from emp limit 0,10");

//		System.out.println("=================");
//		Scan scan = new Scan();
//		List<Result> list = hbaseTemplate.find("table_name_test", scan, (rowMapper, rowNum) -> rowMapper);
//		System.err.println("======================" + list);
//		Configuration conf = getConf();
//		processArgs(conf);
//		Job job = Job.getInstance(conf, TestController3.class.getName());
//		job.setJarByClass(TestController3.class);
//		job.setMapperClass(MyMapper.class);
//		job.setMapOutputKeyClass(NullWritable.class);
//		job.setMapOutputValueClass(Put.class);
//		TableMapReduceUtil.initTableReducerJob(TABLENAME, null, job, null, null, null, null, false);
//		job.setNumReduceTasks(0);
////		job.setReducerClass(MyReduce.class);
//		// 设置输入路径
//		setJobInputPaths(job);
//		job.waitForCompletion(true);
//		log.info("===============执行完成==============");

//		System.out.println(HbaseSynchronizationManager.getTableNames() + "========================");

//		return "pook";
	}

	public static void main(String[] args) throws Exception {

	}

	private static Connection connection;

	public Configuration getConf() {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", HDFS_HOST);
		conf.set("hbase.zookeeper.quorum", ZK_HOST);
		conf = HBaseConfiguration.create(conf);
		try {
			connection = ConnectionFactory.createConnection(conf);
			createTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conf;
	}

	private void createTable() throws IOException {
		Admin admin = connection.getAdmin();
		TableName tableName = TableName.valueOf(TABLENAME);
		if (admin.tableExists(tableName)) {
			System.out.println("0table is already exists!");
			// 删除表
//		admin.disableTable(tableName);
//		admin.deleteTable(tableName);
		} else {
			// 创建表
			HTableDescriptor desc = new HTableDescriptor(tableName);
			HColumnDescriptor family = new HColumnDescriptor(COLF);
			desc.addFamily(family);
			admin.createTable(desc);
		}

//		 finally {
//	            try {
//	                if (admin != null) {
//	                    admin.close();
//	                }
//
//	                if (connection != null && !connection.isClosed()) {
//	                    connection.close();
//	                }
//	            } catch (Exception e2) {
//	                e2.printStackTrace();
//	            }
//	        }

	}

	/**
	 * 处理参数
	 * 
	 * @param conf
	 * @param args
	 */
	private void processArgs(Configuration conf) {
		conf.set("file", "");
	}

	private static void setJobInputPaths(Job job) {
		Configuration conf = job.getConfiguration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
			Path inputPath = new Path(HDFS_HOST + "/test.log");
			if (!fs.exists(inputPath)) {
				throw new RuntimeException("文件不存在:" + inputPath);
			}
			FileInputFormat.addInputPath(job, inputPath);
		} catch (IOException e) {
			throw new RuntimeException("设置输入路径出现异常", e);
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
