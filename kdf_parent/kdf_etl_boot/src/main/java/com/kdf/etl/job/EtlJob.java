package com.kdf.etl.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kdf.etl.base.BaseHadoop;
import com.kdf.etl.constant.Constants;
import com.kdf.etl.hadoop.MyMapper;
import com.kdf.etl.service.HbaseService;
import com.kdf.etl.service.HdfsService;
import com.kdf.etl.service.HiveJdbcService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: EtlJob
 * @Description: mr数据清洗
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Slf4j
@Component
public class EtlJob extends BaseHadoop {

	@Autowired
	private HdfsService hdfsService;
	@Autowired
	private HbaseService hbaseService;

	@Autowired
	private HiveJdbcService hiveJdbcService;

	static String dfs_year_month = "yyyyMMdd";
	static String dfs_hour = "HH";

	String sqlTemplet = "CREATE EXTERNAL TABLE IF NOT EXISTS  ^HIVETABLENAME^ \r\n"
			+ "(key string,appid string,  method string,ip string,port string,url string,request_time string,country string,province string,city string,os_name string,os_version string,browser_name string,browser_version string,device_type string)  \r\n"
			+ "STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'  \r\n"
			+ "WITH SERDEPROPERTIES (\"hbase.columns.mapping\" = \":key,log:appid,log:method,log:ip,log:port,log:url,log:request_time,log:country,log:province,log:city,log:os_name,log:os_version,log:browser_name,log:browser_version,log:device_type\")  \r\n"
			+ "TBLPROPERTIES (\"hbase.table.name\" = \"^HBASETABLENAME^\")";

//	@Scheduled(fixedRate = 100000)
	@Scheduled(cron = "0 0 */1 * * ?")
	public void etl() throws Exception {
		log.info("===============数据清洗执行开始=============");
		LocalDateTime ldtOne = LocalDateTime.now().minusHours(1);

		String nowDate = super.getYyyyMmDdHh();
		String hbaseTableName = Constants.HBASE_TABLENAME + nowDate;
		String hiveTableName = Constants.HBASE_TABLENAME + "hive_" + nowDate;

		String year = DateTimeFormatter.ofPattern(dfs_year_month).format(ldtOne);
		String hour = DateTimeFormatter.ofPattern(dfs_hour).format(ldtOne);

		String hdfsFilePath = "flume/nginx/" + year + "/" + hour + "/";
		// 创建hbase&hive表
		hbaseService.createTable(hbaseTableName, Constants.HBASE_COLF);
		String createHiveSql = sqlTemplet.replace("^HIVETABLENAME^", hiveTableName);
		createHiveSql = createHiveSql.replace("^HBASETABLENAME^", hbaseTableName);
		hiveJdbcService.createTable(createHiveSql);

		log.info("===hdfsFilePath=====" + hdfsFilePath);
		// hdfs 目录下所有文件
		List<Map<String, String>> list = hdfsService.listFile(Constants.HDFS + "/" + hdfsFilePath);
//		List<Map<String, String>> list = hdfsService.listFile(Constants.HDFS+"/"+"flume/nginx/20191010/09/");

		if (CollectionUtils.isNotEmpty(list)) {
			Configuration conf = hbaseService.getConf();
			Job job = Job.getInstance(conf, EtlJob.class.getName());
			job.setJarByClass(EtlJob.class);
			job.setMapperClass(MyMapper.class);
			job.setMapOutputKeyClass(NullWritable.class);
			job.setMapOutputValueClass(Put.class);
			TableMapReduceUtil.initTableReducerJob(hbaseTableName, null, job, null, null, null, null, false);
			job.setNumReduceTasks(0);
			for (Map<String, String> map : list) {
				if (log.isDebugEnabled()) {
					log.debug("hdfs for nginx path {}", map.get("filePath"));
				}
				FileInputFormat.addInputPath(job, new Path(map.get("filePath")));
			}
			job.waitForCompletion(true);
		}
		log.info("===============数据清洗执行完成==============");
	}

}
