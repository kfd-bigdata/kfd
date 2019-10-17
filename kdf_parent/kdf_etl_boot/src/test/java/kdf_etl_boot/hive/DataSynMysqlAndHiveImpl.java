package kdf_etl_boot.hive;
//package com.kdf.etl.contoller;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//import org.apache.log4j.Logger;
//import org.apache.sqoop.client.SqoopClient;
//import org.apache.sqoop.client.SubmissionCallback;
//import org.apache.sqoop.model.MConnection;
//import org.apache.sqoop.model.MConnectionForms;
//import org.apache.sqoop.model.MJob;
//import org.apache.sqoop.model.MJobForms;
//import org.apache.sqoop.model.MSubmission;
//import org.apache.sqoop.submission.SubmissionStatus;
//import org.apache.sqoop.validation.Status;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.scheduler.service.DataSynMysqlAndHive;
// 
// 
//// http://sqoop.apache.org/docs/1.99.7/dev/ClientAPI.html
//// https://blog.csdn.net/qq_30629571/article/details/77993320
//@Service("DataSynMysqlAndHiveImpl")
//public class DataSynMysqlAndHiveImpl implements DataSynMysqlAndHive {
//	
//	protected Logger log = Logger.getLogger(DataSynMysqlAndHiveImpl.class);
//	
//	private static String jdbcHiveDriver = "";
//	
//	private static String jdbcHiveUrl = "";
// 
//    private static String hiveUser ;
// 
//    private static String hivePwd ;
//    
//    private static String exportDatabase ;
// 
//    private static String exportUsername ;
// 
//    private static String exportPassword ;
//    
//    private static String jdbcMysqlDriver;
//    
//    private static String pollingStartTime ;
//	private  static SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
//	private  static SimpleDateFormat yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private static Date polling_start_time = null;   //轮询开始时间
// 
//    private static String sqoopServerUrl ;
// 
//	private SqoopClient sqoopClient;// sqoop客户端对象
// 
//    @Autowired
//	private JDBCService jdbcService; // 增加JDBC服务
//	@Autowired
//	private HiveService hfs;
// 
//	@Override
//	public String exportHiveData(String tableName) {
//		String flag = "success";
//		try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//        	flag = "error";
//            e.printStackTrace();
//            log.error("hive链接出错", e);
//        }
//		//获取当天时间以及前一天的时间
//		Date nowDate = new Date();
//		Calendar calendar = Calendar.getInstance();  
//		calendar.setTime(nowDate);  
//		calendar.add(Calendar.DAY_OF_MONTH, -1);  
//		Date predate = calendar.getTime();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String predateString = dateFormat.format(predate) + " " + pollingStartTime;
//		String nowdateString = dateFormat.format(nowDate) + " " + pollingStartTime;
//		String sql = "select * from  " + tableName + " where resource_flag = 1 and create_time <= \'" + nowdateString +"\' and create_time >\'" + predateString +"\'";
//		log.info("sql：" + sql);
//		System.out.println("sql：" + sql);
//		try {
//			Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//					hivePwd);
//			java.sql.Statement stmt = con.createStatement();
//			ResultSet resultSet = stmt.executeQuery(sql);
//			if (resultSet.next()) {//如果查询hive有数据则进行更新，如果没有数据那么不更新
//				String exportSql = generateExportSql(sql,tableName);
//				ResultSet set = stmt.executeQuery(exportSql);
//				System.out.println("导出sql为:"+exportSql);
//				if (set.next()) {
//					int result = set.getInt(1);
//					if (result == 1) {
//						flag = "error";
//					}
//				}
//			}
//			closeConnection(con, stmt, resultSet);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			flag = "error";
//		}
//		return flag;
//	}
// 
//	/**
//	 * @param sql
//	 * @param tableName
//	 * @return
//	 */
//	private String generateExportSql(String selectSql, String tableName) {
//		//拼接sql,使用udf函数导出
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("select dboutput(\'");
//		buffer.append(exportDatabase);
//		buffer.append("\',\'");
//		buffer.append(exportUsername);
//		buffer.append("\',\'");
//		buffer.append(exportPassword);
//		buffer.append("\',\'");
//		//定义数据库链接
//		Connection conn = null;
//		//定义数据库查询结果劫
//		ResultSet rs = null;
//		try {
//			//设置编码
//			/*if (exportDatabase.contains("jdbc:mysql") && !exportDatabase.contains("characterEncoding")) {
//				exportDatabase = exportDatabase + "?characterEncoding=UTF-8";//设置utf-8编码
//			}*/
//			//获取数据库链接
//			conn=getConnection(jdbcMysqlDriver, exportDatabase, exportUsername, exportPassword);
//			//获取结果
//			rs=conn.getMetaData().getColumns(null, null, tableName, null);
//		    //循环获取所有结果
//			String columnNames = "";
//			String value = "";
//			while(rs.next()){
//				if (!StringUtils.equals("id", rs.getString("COLUMN_NAME"))) {
//					columnNames = columnNames + rs.getString("COLUMN_NAME") + ",";
//					value = value + "?,";
//				}
//			}
//			columnNames = columnNames.substring(0, columnNames.length()-1);
//			value = value.substring(0, value.length()-1);
//			String insertSql = "insert into " + tableName + "(" + columnNames +") values(" +value + ")";
//			buffer.append(insertSql+"\',");
//			buffer.append(columnNames);
//			buffer.append(") from ");
//			buffer.append("("+selectSql.replace("*", columnNames)+")");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeConnection(conn, null, rs);
//		System.out.println("导出的sql为："+buffer.toString());
//		return buffer.toString();
//	}
//	
//	public void closeConnection(Connection connection, java.sql.Statement pStatement, ResultSet resultSet){
//		   try {
//				if (resultSet != null) {
//					resultSet.close();
//				}
//				if (pStatement != null) {
//					pStatement.close();
//				}
//				
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		   
//		   
//	   }
// 
//	public Connection getConnection(String driver, String url, String userName,
//			String password) {
// 
//		//定义链接
//		Connection connection = null;
//		//加载数据库驱动
//		try {
//			Class.forName(driver);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			System.out.println("The Driver loaded error,please contact to your Software Designer!");
//		}
//		//得到数据库链接
//		try {
//			Properties props =new Properties();
//			props.put("remarksReporting","true");
//			props.put("user", userName);
//			props.put("password", password);
//			connection = DriverManager.getConnection(url, props);
//			//connection = DriverManager.getConnection(url, userName, password);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return connection;
//	}
// 
// 
// 
//	/**
//	 * <p>
//	 * Description:[mysql向hive中导入]
//	 *
//	 */
//	@Override
//	public String importHiveData(String sourceTableName) {
//		//判断有没有数据更新
//		try {
//			Date nowTime = yMdHms.parse(yMdHms.format(new Date()));
//			//前一天时间
//			String preDate = yMdHms.format(TimeHelper.dateAddDay(nowTime,-1));
//			//
//			Timestamp aftTimestamp = getAfterMaxTimestamp(sourceTableName,preDate,"create_time");
//			if (null == aftTimestamp ){
//				return "检测没有新数据";
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		//定义全局变量监控抽取过程是否出现错误
//		boolean hasError = false;
// 
//		//1.初始化sqoop客户端并且得到sqoop连接
//		MConnection con =initSqoop();
//		//如果得到的连接为空，打印日志，结束该任务
//		if (con == null) {
//			System.out.print("连接为空");
//			return "error";
//		}
// 
//		//2.创建sqoop任务,任务类型为导入任务
// 
//		MJob newjob = sqoopClient.newJob(con.getPersistenceId(),org.apache.sqoop.model.MJob.Type.IMPORT);
// 
//		CallBack callback = new CallBack(sourceTableName);
//		//获取该表的表信息
//		List<TableVO> tableVOs = jdbcService.getTables(exportDatabase, exportUsername, exportPassword, null, null, sourceTableName, null);
//		//获取该表的列信息
//		List<ColumnVO> columnVOs = jdbcService.getColumns(exportDatabase, exportUsername, exportPassword, sourceTableName);
// 
// 
//		boolean isFirst = true;
// 
// 
//		String primaryKey = jdbcService.getPrimaryKey(exportDatabase,exportUsername,exportPassword,null,null,sourceTableName);
// 
//		String hdfsFilePath= "";
// 
// 
//		hdfsFilePath=updateIncrementSqoopJob(newjob,sourceTableName,columnVOs);
// 
// 
//		//启用线程监控sqoop采集时长
//		Thread thread = monitorRuntime(sqoopClient,3*60*60,newjob);
//		//定义任务开始时间变量
//		long startTime = System.currentTimeMillis();
//		//开始sqoop任务采集，并返回sqoop任务采集状态
//		MSubmission submission = startSqoopTask(0,newjob,thread,callback);
// 
//		//将sqoop导入时间字段添加到column中
//		columnVOs=addSqoopTimeColumn(columnVOs);
//		if (submission.getStatus().compareTo(SubmissionStatus.SUCCEEDED) == 0) {// 任务执行成功，则把数据写入到hive中
//			hasError=createOrcHiveAfterSqoop(sourceTableName,columnVOs, hdfsFilePath, startTime, startTime, false);
//		}
// 
//		if (submission.getStatus().compareTo(SubmissionStatus.FAILED) == 0|| submission.getExceptionInfo() != null) {// 任务执行出错，打印出错信息，并记录到任务日志中
//			System.out.println(submission.getExceptionInfo());
//			//出现错误，记录日志，删除hdfs文件
//			addLogCaseSqoopFail(submission,newjob,hdfsFilePath,thread);
//			//标记发生错误
//			hasError = true;
//			return "error";
//		}
//		//afterFinishTask(hasError);
//		return "success";
//	}
// 
//	/**
//	 * <p>
//	 * Description:[初始化sqoop客户端，得到sqoop链接]
//	 * </p>
//	 * @return MConnection sqoop连接
//	 */
//	public MConnection initSqoop(){
//		//初始化客户端
//		this.sqoopClient = new SqoopClient(sqoopServerUrl);
//		//获取该数据源的sqoop链接id
//		Long conId = createSqoopConnection("zheda",exportDatabase,exportUsername,exportPassword,jdbcMysqlDriver);
//		//根据sqoop xid 获得链接
//		MConnection con =sqoopClient.getConnection(conId);
//		//将该链接返回
//		return con;
//	}
// 
//	public long createSqoopConnection(String resourceName, String jdbcUrl,
//									  String name, String passwd, String driver) {
//		SqoopClient sqoopClient = new SqoopClient(Messages.getString("sqoopServerUrl"));
//		MConnection newCon = sqoopClient.newConnection(1);
//		MConnectionForms conForms = newCon.getConnectorPart();
//		MConnectionForms frameworkForms = newCon.getFrameworkPart();
//		newCon.setName(resourceName);
//		conForms.getStringInput("connection.connectionString").setValue(jdbcUrl);// 数据库连接url字符串
//		conForms.getStringInput("connection.jdbcDriver").setValue(driver);// 数据库驱动
//		conForms.getStringInput("connection.username").setValue(name);// 数据库用户名
//		conForms.getStringInput("connection.password").setValue(passwd);// 数据库密码
//		frameworkForms.getIntegerInput("security.maxConnections").setValue(0);// sqoop的最大连接数
//		try {
//			Status status = sqoopClient.createConnection(newCon);
//			if (status.canProceed()) {
//				return newCon.getPersistenceId();
//			} else {
//				log.info("Check for status and forms error ");
//				System.out.println("Check for status and forms error ");
//				return -1;
//			}
//		} catch (Exception e) {
//			log.error("创建连接出错！:"+e.getMessage());
//			System.out.println(e.getMessage());
//			return -1;
//		}
//	}
// 
//	/**
//	 * <p>
//	 * Description:[初始化sqoop客户端，得到sqoop链接]
//	 * </p>
//	 *
//	 */
//	// sqoop任务执行回调内部类
//	class CallBack implements SubmissionCallback {
//		private String tableName;
//		public String getTableName() {
//			return tableName;
//		}
//		public void setTableName(String tableName) {
//			this.tableName = tableName;
//		}
// 
//		public CallBack() {
//			super();
//		}
// 
//		public CallBack(String tableName){
//			super();
//			this.tableName= tableName;
//		}
// 
//		@Override
//		public void submitted(MSubmission mSubmission) {
// 
//		}
// 
//		@Override
//		public void updated(MSubmission mSubmission) {
// 
//		}
// 
//		// sqoop任务完成回调函数
//		@Override
//		public void finished(MSubmission arg0) {
//		}
// 
//	}
// 
//	/**
//	 * <p>
//	 * Description:[启用线程监控sqoop任务执行时长，如果超过执行时长，停止执行该任务]
//	 * </p>
//	 *
//	 * @param SqoopClient sqoop客户端
//	 * @param int 任务执行时长
//	 * @param final long sqoop任务Id
//	 * @return Thread 当前的监控线程
//	 */
//	public Thread monitorRuntime(SqoopClient sqc,int taskTime,final MJob sJob){
//		//获取监听时间，如果没有指定监听时间，默认为24小时
//		final int job_timeout_time = taskTime != 0 ? taskTime :20;
//		// 启用一个线程，用于监听sqoop执行任务的时间，如果时间超过最大执行时间，则停止掉该任务
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					//监听任务执行时长，如果超过最大时间，停掉sqoop任务
//					Thread.sleep(job_timeout_time * 60 * 60 * 1000);
//					sqoopClient.stopSubmission(sJob.getPersistenceId());
//				} catch (InterruptedException e) {
//					log.error("sqoop全量任务发生异常！",e);
//				}
//			}
//		});
//		thread.start();
//		//将该线程返回
//		return thread;
//	}
// 
//	/**
//	 * <p>
//	 * Description:[任务采集后，根据原表中的字段信息以及hdfs文件地址创建hive表]
//	 * </p>
//	 *
//	 * @param tableName 表名称
//	 * @param columnVOs 表字段
//	 * @param hdfsPath hdfs文件地址
//	 * @return boolean 是否创建成功
//	 */
//	public boolean createHiveTable(String tableName,List<ColumnVO> columnVOs,String hdfsPath){
//		boolean hasError = false;
//		//组装sql
//		StringBuffer createSql = new StringBuffer("create table " + tableName + "(");
//		for (int i = 0; i < columnVOs.size(); i++) {
//			if (i == 0) {
//				createSql.append("`" + columnVOs.get(i).getColumnName()+ "` string");
//			} else {
//				createSql.append(",`"+ columnVOs.get(i).getColumnName()+ "` string");
//			}
//		}
//		createSql.append(") ROW FORMAT DELIMITED FIELDS TERMINATED BY '\001' LOCATION ");
//		createSql.append(" '" + hdfsPath + "'");
//		log.info("createSql:" + createSql);
//		String sql =  createSql.toString().trim();
//		//创建表
//		try {
//			boolean success = hfs.createHiveTable(tableName, sql);
//			//如果返回的结果有错误，则标记hive创建出现错误
//			if(!success){
//				hasError = true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			hasError =true;
//		}
//		//返回结果
//		return hasError;
//	}
// 
//	/**
//	 * <p>
//	 * Description:[hive表创建失败后，记录日志并且删除对应的hdfs文件]
//	 * </p>
//	 *
//	 * @param tableName 表名称
//	 * @param hdfsPath hdfs文件地址
//	 * @param jobId sqoopJobid
//	 */
//	public void addLogCaseCreatehiveTableError(String tableName,String hdfsPath,long jobId){
//		//记录日志，
//		//addTaskLog("create hiveTable "+tableName+" failed!", jobId);
//		//删除hdfs文件
//		deleteHdfsHiveTable(hdfsPath,tableName);
//	}
// 
//	/**
//	 * <p>
//	 * Description:[启动sqoop采集任务]
//	 * </p>
//	 * @param loopTime 任务执行次数标识，用于判断创建或者更新任务
//	 * @param newjob sqoopJob实体
//	 * @param Thread 监控任务执行时长的线程
//	 * @param callback sqoop回调类
//	 * @return MSubmission Sqoop提交结果
//	 */
//	public MSubmission  startSqoopTask(int loopTime,MJob newjob,Thread thread,CallBack callback){
// 
//		MSubmission submission= null;
// 
//		//第一次执行，则创建新的任务，否则，更新任务
//		if (loopTime == 0) {
//			sqoopClient.createJob(newjob);
//		} else {
//			sqoopClient.updateJob(newjob);
//		}
// 
//		//执行sqoop任务
//		try {
//			submission = sqoopClient.startSubmission(newjob.getPersistenceId(), callback, 100);
//		} catch (InterruptedException e1) {
//			// 发生异常停止掉
//			if (thread.isAlive()) {
//				thread.interrupt();
//			}
//			log.error("sqoop提交全量任务出错！:",e1);
//		}
// 
//		//返回结果
//		return submission;
//	}
// 
//	/**
//	 * <p>
//	 * Description:[sqoop任务失败时，添加日志，删除hdfs文件等]
//	 * </p>
//	 * @param MSubmission Sqoop提交结果
//	 * @param MJob sqoopJob实体
//	 * @param String hdfs文件地址
//	 * @param Thread 监控任务执行时长的线程
//	 * @return void
//	 */
//	public void addLogCaseSqoopFail(MSubmission submission,MJob sJob,String hdfsUrl,Thread thread){
//		//后台打印出错误信息
//		System.out.println(submission.getExceptionInfo());
// 
//		// 删除hdfs文件
//		deleteHdfsFiles(hdfsUrl);
// 
//		//如果监控线程还在继续，则停止线程
//		if (thread.isAlive()) {
//			thread.interrupt();// 发生异常停止掉
//		}
//	}
//	/**
//	 * <p>
//	 * Description:[根据传入的表名和列信息，组装成创建表的sql]
//	 * </p>
//	 * @param tableName 表名称
//	 * @param columnVOs 表字段
//	 * @return String 生成的sql
//	 */
//	public String  getCreateTableSQL(String tableName,List<ColumnVO> columnVOs,boolean isText){
//		//组装sql
//		StringBuffer createSql = new StringBuffer("create table " + tableName + "(");
//		for (int i = 0; i < columnVOs.size(); i++) {
//			if (i == 0) {
//				createSql.append("`" + columnVOs.get(i).getColumnName()+ "` string");
//			} else {
//				createSql.append(",`"+ columnVOs.get(i).getColumnName()+ "` string");
//			}
//		}
//		createSql.append(")");
// 
//		if (isText) {
//			createSql.append(" ROW FORMAT DELIMITED FIELDS TERMINATED BY '\001' ");
//		}
// 
//		log.info("createSql:" + createSql);
//		String sql =  createSql.toString().trim();
//		//返回结果
//		return sql;
//	}
// 
// 
//	/**
//	 * <p>
//	 * Description:[根据传入列对象，组装列信息]
//	 * </p>
//	 *
//	 * @param columnVOs 表字段
//	 * @return String 生成的sql
//	 */
//	public String  getColumns(List<ColumnVO> columnVOs){
//		//组装sql
//		StringBuffer columns = new StringBuffer("");
//		for (int i = 0; i < columnVOs.size(); i++) {
//			if (i == 0) {
//				columns.append("`" + columnVOs.get(i).getColumnName()+ "` string");
//			} else {
//				columns.append(",`"+ columnVOs.get(i).getColumnName()+ "` string");
//			}
//		}
//		log.info("createSql:" + columns);
//		String column =  columns.toString().trim();
//		//返回结果
//		return column;
//	}
// 
//	/**
//	 * <p>
//	 * Description:[增量sqoop导入完成之后，创建hiveorc表，插入orc数据，实现增量，保存源数据信息]
//	 * </p>
//	 *
//	 * @param tableVOs 源表信息
//	 * @param columnVOs 源表字段信息
//	 * @param hdfsFilePath sqoop导入成功后hdfs文件地址
//	 * @param jobId sqoopJobid 用于保存任务日志信息
//	 * @param startTime 任务开始时间用于保存该任务总共花费时间
//	 * @return boolean 整个过程是否发生错误，true 存在错误， false 正常执行，不存在错误
//	 */
//	public boolean createOrcHiveAfterSqoop(String table, List<ColumnVO> columnVOs,String hdfsFilePath,long jobId,long startTime,boolean isFirst){
//		boolean hasError = false;
//		// 定义表名
//		String orcTableName = table;
//		String sourceTableName= table;
// 
//		String primaryKey = jdbcService.getPrimaryKey(exportDatabase,exportUsername,exportPassword,null,null,sourceTableName);
//		try {
//			if(primaryKey == null || primaryKey.trim().equals("")) {
//				primaryKey = columnVOs.get(0).getColumnName();
//			}
//			//textfileTable在这里表示 增量数据临时表的表名，先将增量数据放在临时表，再将临时表的数据导入目标表
//			String textfileTable = orcTableName+"_temp";
//			//获取sql
//			String sql = getCreateTableSQL(textfileTable,columnVOs,true);
//			// 创建hive表，并把增量的数据导入到hive表中
//			hfs.createHiveTempTable(textfileTable, sql,hdfsFilePath);
// 
//				// 非第一次导入，先将hive中相关的数据删除，再插入相关数据
//			long incrementInsertTime = System.currentTimeMillis();
//			hfs.deleteIncrementDataExistInOrcTable(textfileTable, orcTableName, primaryKey,
//						jdbcHiveUrl);
//			hfs.insertIntoHiveOrcTable(textfileTable, orcTableName, jdbcHiveUrl);
//			long incrementInsertTimeEnd = System.currentTimeMillis();
//			System.out.println("orc增量新增和更新数据到orc表所用时间：" + (incrementInsertTimeEnd - incrementInsertTime));
//			log.info("orc增量新增和更新数据到orc表所用时间：" + (incrementInsertTimeEnd - incrementInsertTime));
// 
//		} catch (Exception e) {
//			hasError = true;
//			log.error("全量任务创建hive表出错！",e);
//		}
// 
//		return hasError;
//	}
// 
//	/**
//	 * <p>
//	 * Description:[在获取的源表的字段列表中加入sqoop的loadtime字段，字段名称为“load_bigdata_time”]
//	 * </p>
//	 * @param List<ColumnVO> 源表字段信息
//	 * @return List<ColumnVO>
//	 */
//	public List<ColumnVO> addSqoopTimeColumn(List<ColumnVO> cVos){
//		ColumnVO cVo= new ColumnVO();
//		cVo.setColumnName("load_bigdata_time");
//		cVo.setComment("Sqoop导入时间");
//		cVo.setType("datetime");
//		cVos.add(cVo);
//		return cVos;
//	}
// 
//	/**
//	 * 在sqoop导入时出现问题，删除已经生成的hdfs文件，hive在创建表时出现问题，删除已经创建的表和hdfs文件
//	 *
//	 * @param HDFSPath
//	 * @param HiveTableName
//	 */
//	private void deleteHdfsHiveTable(String HDFSPath, String HiveTableName) {
//		String HDFSUrl = Messages.getString("HDFSUrl");
//		String HDFSFilePath = HDFSUrl + HDFSPath;
//		System.setProperty("HADOOP_USER_NAME", Messages.getString("hiveUser"));
//		try {
//			try {
//				hfs.deleteFdfsByHiveTable(HiveTableName);
//				hfs.deleteHiveTrueTable(HiveTableName);
//			} catch (ClassNotFoundException e1) {
//				e1.printStackTrace();
//			} // 如果表存在，删除表
// 
//			// 删除hdfs文件
//			Path p = new Path(HDFSFilePath);
//			Configuration conf = new Configuration();
//			try {
//				FileSystem fs = p.getFileSystem(conf);
//				boolean isHad = fs.exists(p);
//				if (isHad) {
//					fs.delete(p, true);
//				}
//				// boolean b = fs.createNewFile(p);
//				fs.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
// 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
// 
//	}
// 
//	public void deleteHdfsFiles(String hdfsPath) {
//		String HDFSFilePath = jdbcHiveUrl + hdfsPath;
//		System.setProperty("HADOOP_USER_NAME", hiveUser);
//		try {
//			// 删除hdfs文件
//			Path p = new Path(HDFSFilePath);
//			Configuration conf = new Configuration();
//			FileSystem fs = p.getFileSystem(conf);
//			boolean isHad = fs.exists(p);
//			if (isHad) {
//				fs.delete(p, true);
//			}
//			fs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
// 
//	//判断从上一次更新之后数据的最大时间
// 
//	public Timestamp getAfterMaxTimestamp( String tableName, String preTimestamp, String columnName) {
//		Timestamp timestamp = null;
//		Connection connection = JdbcConnection.getConnection(jdbcMysqlDriver, exportDatabase,
//				exportUsername, exportPassword);
//		PreparedStatement pStatement = null;
//		ResultSet resultSet = null;
//		String sql = "select max(date_format(" + columnName + ",'%Y-%m-%d %H:%i:%S')) from "
//				+ "(select * from " + tableName + " where date_format(" + columnName + ",'%Y-%m-%d %H:%i:%S') > '" + preTimestamp + "') as increment";
//		/*如果是Oracle {
//			sql = "select max(to_char(" + columnName + ",'yyyy-MM-dd hh24:mi:ss')) from ("
//					+ "select * from " + tableName + " where to_char(" + columnName + ",'yyyy-MM-dd hh24:mi:ss') > '" + preTimestamp + "')";
//		} 如果是Sybase {
//			sql = "select * from " + tableName;
//		} 如果是sql server {
//			sql = "select max(Convert(varchar," + columnName + ",120)) from ("
//					+ "select * from " + tableName + " where Convert(varchar," + columnName + ",120) > '" + preTimestamp + "') as increment";
//		}*/
// 
//		try {
//			pStatement = connection.prepareStatement(sql);
//			resultSet = pStatement.executeQuery();
//			if (resultSet.next()) {
//				//timestamp = changeToTimestamp(resultSet.getString(1));
//				if(resultSet.getString(1) == null) {
//					return timestamp;
//				}
//				timestamp =Timestamp.valueOf(resultSet.getString(1));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			JdbcConnection.closeConnection(connection, pStatement, resultSet);
//		}
//		return timestamp;
//	}
// 
//	/**
//	 * 1111更新increment sqoop Job配置
//	 */
//	private String  updateIncrementSqoopJob(MJob newjob, String tableName, List<ColumnVO> columns) {
//		MJobForms connectorForm = newjob.getConnectorPart();
//		MJobForms frameworkForm = newjob.getFrameworkPart();
//		newjob.setName("ImportJob_zheda");
//		//获取源表的主键
//		String primaryKey = jdbcService.getPrimaryKey(exportDatabase,exportUsername,exportPassword,null,null,tableName);
//		//如果主键不为空，设定“partitionColumn”参数为主键，并且设置任务执行的map数为10
//		if(primaryKey != null && !primaryKey.trim().equals("")) {
//			frameworkForm.getIntegerInput("throttling.extractors").setValue(10);// 指定map的个数
//			connectorForm.getStringInput("table.partitionColumn").setValue(primaryKey);
//			//如果主键为空，选取不为时间类型的字段为“partitionColumn”参数，并指定map数为1
//		}else {
//			//选取不为时间类型的字段
//			for(int i=0;i<columns.size();i++){
//				if (!columns.get(i).getType().toUpperCase().contains("TIME")&&!columns.get(i).getType().toUpperCase().contains("DATE")) {
//					primaryKey = columns.get(i).getColumnName();
//					break;
//				}
//			}
//			//设定“partitionColumn”参数
//			connectorForm.getStringInput("table.partitionColumn").setValue(primaryKey);
//			// 指定map的个数
//			frameworkForm.getIntegerInput("throttling.extractors").setValue(1);
//		}
// 
//		// 控制增量导入
//		//获取当天时间以及前一天的时间
//		Date nowDate = new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(nowDate);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		Date predate = calendar.getTime();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String predateString = dateFormat.format(predate) + " " + pollingStartTime;
//		String nowdateString = dateFormat.format(nowDate) + " " + pollingStartTime;
// 
//		String sql = "select * FROM " + tableName + " where ";
//		String charStr = " Convert(varchar,"+"create_time"+",120) ";
// 
//		charStr = "date_format(" +"create_time" + ",'%Y-%m-%d %H:%i:%S') ";
// 
//		sql += charStr + " > '" + predateString + "' and " + charStr + " <= '" + nowdateString
//				+ "' and ${CONDITIONS}";
// 
//		System.out.println("SQL ::"+sql);
// 
//		connectorForm.getStringInput("table.sql").setValue(sql);
// 
// 
//		String hdfdFilePath = Messages.getString("sqoopOutput") + new Date().getTime() + tableName;
//		frameworkForm.getEnumInput("output.storageType").setValue(Messages.getString("storageType"));
//		frameworkForm.getEnumInput("output.outputFormat").setValue(Messages.getString("outputFormat"));
//		frameworkForm.getStringInput("output.outputDirectory").setValue(hdfdFilePath);
//		frameworkForm.getIntegerInput("throttling.extractors").setValue(1);// 指定map的个数
//		return hdfdFilePath;
//	}
// 
//}