package com.kdf.etl.contoller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdf.etl.constant.Constants;
import com.kdf.etl.hadoop.MyMapper;
import com.kdf.etl.service.HdfsService;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据清洗
 * 
 * @author Lenovo
 *
 */
@Slf4j
@RestController
@RequestMapping("/rest")
public class EtlController {

	@Autowired
	HdfsService hdfsService;

	/**
	 * test 类
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("qq")
	public String qq() throws Exception {
		// hdfs 目录下所有文件
		String hdfsFilePath = "/flume/webdata/";
		List<Map<String, String>> list = hdfsService.listFile(Constants.HDFS + "/" + hdfsFilePath);

		System.out.println("==============================================");
		System.out.println(list);
		System.out.println("==============================================");

		return "ok";
	}

	@GetMapping("start")
	public String start() throws Exception {
		log.info("===============数据清洗执行开始==============\"");

		Configuration conf = getConf();
		processArgs(conf);
		Job job = Job.getInstance(conf, EtlController.class.getName());
		job.setJarByClass(EtlController.class);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Put.class);
		TableMapReduceUtil.initTableReducerJob(Constants.HBASE_TABLENAME, null, job, null, null, null, null, false);
		job.setNumReduceTasks(0);
//		job.setReducerClass(MyReduce.class);
		// 设置输入路径
		setJobInputPaths(job);
		job.waitForCompletion(true);
		log.info("===============数据清洗执行完成==============");

		return "ok";
	}

	private static Connection connection;

	public static Configuration getConf() {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", Constants.HDFS);
		conf.set("hbase.zookeeper.quorum", Constants.ZK_HOST);
		conf = HBaseConfiguration.create(conf);
		try {
			connection = ConnectionFactory.createConnection(conf);
			createTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conf;
	}

	private static void createTable() throws IOException {

		Admin admin = connection.getAdmin();
		// 删除表
		TableName tableName = TableName.valueOf(Constants.HBASE_TABLENAME);
		if (admin.tableExists(tableName)) {
			System.out.println("0table is already exists!");
			// 删除表
//		admin.disableTable(tableName);
//		admin.deleteTable(tableName);
		} else {
			// 创建表
			HTableDescriptor desc = new HTableDescriptor(tableName);
			HColumnDescriptor family = new HColumnDescriptor(Constants.HBASE_COLF);
			desc.addFamily(family);
			admin.createTable(desc);
		}

	}

	/**
	 * 处理参数
	 * 
	 * @param conf
	 * @param args
	 */
	private static void processArgs(Configuration conf) {
		conf.set("file", "");
	}

	private static void setJobInputPaths(Job job) {
		Configuration conf = job.getConfiguration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
			Path inputPath = new Path(Constants.HDFS + "/flum/webdata/");
			FileInputFormat.addInputPath(job, inputPath);

//			if (fs.exists(inputPath)) {
//				FileInputFormat.addInputPath(job, inputPath);
//			} else {
//				throw new RuntimeException("文件不存在:" + inputPath);
//			}
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
