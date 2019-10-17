package kdf_etl_boot.hive;
//package com.kdf.etl.contoller;
//import java.io.IOException;
//import org.apache.hadoop.fs.FileSystem;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
// 
//import com.scheduler.util.Constant;
//import com.scheduler.util.Messages;
// 
//import com.scheduler.util.ColumnVO;
// 
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//import com.scheduler.service.HiveService;
//import org.apache.sqoop.client.SubmissionCallback;
//import org.apache.commons.lang.StringUtils;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.LocatedFileStatus;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.fs.RemoteIterator;
//import org.apache.log4j.Logger;
// 
// 
//import javax.annotation.Resource;
// 
// 
///**
// * <p>
// * Title: manageplatform_[Hive]
// * </p>
// * <p>
// * Description: [HiveService实现层]
// * </p>
// *
// * @author GLJ
// * @author (latest modification by $Author$)
// * @version $Revision$ 2015-03-18
// * @since 20130601
// */
//@Service("hiveServiceImpl")
//public class HiveServiceImpl implements HiveService {
// 
//    protected Logger log = Logger.getLogger(DataSynMysqlAndHiveImpl.class);
// 
//    private static String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
// 
//    private static String jdbcHiveUrl = Messages.getString("jdbcHiveUrl");
// 
//    private static String hiveUser = Messages.getString("hiveUser");
// 
//    private static String hivePwd = Messages.getString("hivePwd");
// 
//    private static String exportDatabase = Messages.getString("export_target_database_url");
// 
//    private static String exportUsername = Messages.getString("export_target_database_username");
// 
//    private static String exportPassword = Messages.getString("export_target_database_password");
// 
//    private static String jdbcMysqlDriver = Messages.getString("jdbc_mysql_driver");
// 
// 
// 
// 
//    public HiveServiceImpl() {
//    }
// 
// 
//    @Override
//    public boolean existTable(String table) throws SQLException {
//        boolean flag = false;
//        try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            log.error("hive链接出错", e);
//        }
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//                hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        String sql = "show tables '" + table + "'";
//        log.info("sql：" + sql);
//        ResultSet set = stmt.executeQuery(sql);
//        while (set.next()) {
//            String reTableName = set.getString(1);
//            if ((table.toLowerCase()).equals(reTableName.toLowerCase())) {
//                flag = true;
//                break;
//            }
//        }
//        return flag;
//    }
// 
//    @Override
//    public boolean createTableAsSelect(String targetTableName, String select)
//            throws SQLException {
//        String create = "CREATE TABLE " + targetTableName;
//        String option = " row format delimited fields terminated by '\001' "; // you
//        // can
//        // change
//        // it
//        String as = " AS " + select; // here you can decide which column, table
//        // to select, join table or more
//        // comprehension clause
// 
//        String sql = create + option + as;
//        log.info("创建数据表sql：" + sql);
//        System.out.println("Running: " + sql);
//        try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//                hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        stmt.execute(sql);
//        stmt.close();
//        con.close();
//        return true;
//    }
// 
// 
//    //11111111111111
//    @Override
//    public void deleteHiveTrueTable(String tableName) throws SQLException {
//        String deleteSql = "drop table if exists " + tableName;
//        System.out.println("Running: " + deleteSql);
//        log.info("删除数据表sql：" + deleteSql);
//        try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//                hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        stmt.execute(deleteSql);
//        stmt.close();
//        con.close();
//    }
// 
// 
//    @Override
//    public List<Map<String, String>> getHiveColunmsByTableName(String hiveurl,
//                                                               String userName, String password, String tableName) {
//        List<Map<String, String>> colsAndType = new ArrayList<Map<String, String>>();
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        try {
//            con = DriverManager.getConnection(hiveurl, userName, password);
//            Statement stmt = con.createStatement();
//            String sql = "desc " + tableName;
//            log.info("获取表字段sql" + sql);
//            ResultSet resultSet = stmt.executeQuery(sql);
//            while (resultSet.next()) {
//                Map<String, String> map = new HashMap<String, String>();
//                String colunm = resultSet.getString(1);
//                String type = resultSet.getString(2);
//                map.put("column", colunm);
//                map.put("type", type);
//                colsAndType.add(map);
//            }
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            log.error("sql执行出错", e);
//        }
// 
//        return colsAndType;
//    }
// 
//    @Override
//    public List<String> getColumnValues(String tableName, String colName) {
//        String jdbcHiveUrl = Messages.getString("jdbcHiveUrl");
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String sql = "select distinct " + colName + " from " + tableName;
//        try {
//            final String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//            Connection con;
//            con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//            final Statement stmt = con.createStatement();
//            log.info("sql:" + sql);
//            final ResultSet datSet = stmt.executeQuery(sql);
//            List<String> values = new ArrayList<String>();
//            while (datSet.next()) {
//                values.add(datSet.getString(1));
//            }
//            return values;
//        } catch (final ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//            return null;
//        } catch (SQLException e) {
//            log.error("sql执行出错", e);
//            e.printStackTrace();
//            return null;
//        }
//    }
// 
//	/*
//     * 得到所有表
//	 */
//	/*private ArrayList<String> getTables() throws SQLException {
//		try {
//			Class.forName(jdbcHiveDriver);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			log.error("hive链接出错",e);
//		}
//		Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//				hivePwd);
//		java.sql.Statement stmt = con.createStatement();
//		if (stmt == null)
//			return null;
//		String sql = "show tables";
//		ArrayList<String> result = new ArrayList<String>();
//		log.info("sql:"+sql);
//		ResultSet res = stmt.executeQuery(sql);
//		while (res.next()) {
//			result.add(res.getString(1));
//		}
//		stmt.close();
//		con.close();
//		return result;
//	}*/
// 
// 
//    @Override
//    public List<String> getTablesColName(String url, long resourceId,
//                                         String userName, String password, String goOnTableName) {
//        List<String> tableList = new LinkedList<String>();
//        if (url.contains("jdbc:sybase:Tds")) {
//            tableList = this.getColNameOfSybase(url, resourceId, userName,
//                    password, goOnTableName);
//            return tableList;
//        }
//        try {
//            String jdbcMysqlDriver = Messages.getString("jdbc_mysql_driver");
//            if (url.contains("jdbc:oracle")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_oracle_driver");
//            } else if (url.contains("jdbc:sqlserver")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_sqlserver_driver");
//            }
//            Class.forName(jdbcMysqlDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Connection con;
//        try {
//            con = DriverManager.getConnection(url, userName, password);
//            Statement stmt = con.createStatement();
//            ResultSet tableSet = null;
//            PreparedStatement pStatement = null;
//            if (url.contains("jdbc:oracle")) {
//                String sql1 = Messages.getString("oracle_show_tables");
//                log.info("sql:" + sql1);
//                pStatement = con.prepareStatement(sql1);
//                tableSet = pStatement.executeQuery();
//            } else if (url.contains("jdbc:sqlserver")) {
//                String sql2 = Messages.getString("sqlserver_show_tables");
//                log.info("sql:" + sql2);
//                pStatement = con.prepareStatement(sql2);
//                tableSet = pStatement.executeQuery();
//            } else {
//                String[] type = {"TABLE"};
//                tableSet = con.getMetaData().getTables("", "", "", type);
//            }
//            Boolean id = false;
//            while (tableSet.next()) {
//                String tableName = null;
//                if (url.contains("jdbc:oracle")) {
//                    tableName = tableSet.getString(1);
//                } else if (url.contains("jdbc:sqlserver")) {
//                    tableName = tableSet.getString(1);
//                } else {
//                    tableName = tableSet.getString("TABLE_NAME");
//                }
//                if (goOnTableName == null || goOnTableName.equals("")
//                        || goOnTableName.equals(" ")) {
//                    id = true;
//                } else {
//                    if (tableName.equals(goOnTableName))
//                        id = true;
//                }
//                if (id) {
//                    tableList.add(tableName);
//                }
//            }
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            log.error("SQL执行异常", e);
//            e.printStackTrace();
//        }
//        return tableList;
//    }
// 
//    private List<String> getColNameOfSybase(String url, long resourceId,
//                                            String userName, String password, String goOnTableName) {
//        List<String> tableList = new LinkedList<String>();
//        String jdbcMysqlDriver = Messages.getString("jdbc_sybase_driver");
//        try {
//            Class.forName(jdbcMysqlDriver);
//            String sql = Messages.getString("sybase_show_tables");
//            Connection con = DriverManager.getConnection(url, userName,
//                    password);
//            Statement stmt = con.createStatement(
//                    ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            log.info("sql:" + sql);
//            PreparedStatement pStatement = con.prepareStatement(sql);
//            ResultSet tableSet = pStatement.executeQuery();
//            Boolean id = false;
//            while (tableSet.next()) {
//                String tableName = tableSet.getString("TABLE_NAME");
//                if (goOnTableName == null || goOnTableName.equals("")
//                        || goOnTableName.equals(" ")) {
//                    id = true;
//                } else {
//                    if (tableName.equals(goOnTableName))
//                        id = true;
//                }
//                if (id) {
//                    tableList.add(tableName);
//                }
//            }
//            stmt.close();
//            con.close();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            log.error("hive链接出错", e);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            log.error("SQL执行异常", e);
//        }
//        return tableList;
//    }
// 
//    @Override
//    public List<String> getViewsColName(String url, long resourceId,
//                                        String userName, String password, String schemaName,
//                                        String goOnViewName) {
//        List<String> viewList = new LinkedList<String>();
//        if (url.contains("jdbc:sybase:Tds")) {
//            viewList = getSybaseView(url, resourceId, userName, password,
//                    goOnViewName);
//            return viewList;
//        }
//        try {
//            String jdbcMysqlDriver = Messages.getString("jdbc_mysql_driver");
//            if (url.contains("jdbc:oracle")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_oracle_driver");
//            } else if (url.contains("jdbc:sqlserver")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_sqlserver_driver");
//            }
//            Class.forName(jdbcMysqlDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("jdbc链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        try {
//            con = DriverManager.getConnection(url, userName, password);
//            Statement stmt = con.createStatement();
//            ResultSet viewSet = null;
//            PreparedStatement pStatement = null;
//            String vn = "name";
//            if (url.contains("jdbc:oracle")) {
//                String sql1 = Messages.getString("oracle_show_views");
//                log.info("sql：" + sql1);
//                pStatement = con.prepareStatement(sql1);
//                viewSet = pStatement.executeQuery();
//                vn = "VIEW_NAME";
//            } else if (url.contains("jdbc:sqlserver")) {
//                String sql2 = Messages.getString("sqlserver_show_views");
//                log.info("sql：" + sql2);
//                pStatement = con.prepareStatement(sql2);
//                viewSet = pStatement.executeQuery();
//            } else {
//                String sql3 = Messages.getString("mysql_show_views") + "'"
//                        + schemaName + "'";
//                log.info("sql：" + sql3);
//                pStatement = con.prepareStatement(sql3);
//                viewSet = pStatement.executeQuery();
//                vn = "table_name";
//            }
//            Boolean id = false;
//            while (viewSet.next()) {
//                String tableName = viewSet.getString(vn);
//                if (goOnViewName == null || goOnViewName.equals("")
//                        || goOnViewName.equals(" ")) {
//                    id = true;
//                } else {
//                    if (tableName.equals(goOnViewName))
//                        id = true;
//                }
//                if (id) {
//                    viewList.add(tableName);
//                }
//            }
// 
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            log.error("SQL执行异常", e);
// 
//            e.printStackTrace();
//        }
//        return viewList;
//    }
// 
//    private List<String> getSybaseView(String url, long resourceId,
//                                       String userName, String password, String goOnTableName) {
//        List<String> viewList = new LinkedList<String>();
//        String jdbcMysqlDriver = Messages.getString("jdbc_sybase_driver");
//        try {
//            Class.forName(jdbcMysqlDriver);
//            String sql = Messages.getString("sybase_show_views")
//                    + "'sysquerymetrics'";
//            Connection con = DriverManager.getConnection(url, userName,
//                    password);
//            Statement stmt = con.createStatement(
//                    ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            log.info("sql：" + sql);
//            PreparedStatement pStatement = con.prepareStatement(sql);
//            ResultSet tableSet = pStatement.executeQuery();
//            Boolean id = false;
//            while (tableSet.next()) {
//                String tableName = tableSet.getString("name");
//                if (goOnTableName == null || goOnTableName.equals("")
//                        || goOnTableName.equals(" ")) {
//                    id = true;
//                } else {
//                    if (tableName.equals(goOnTableName))
//                        id = true;
//                }
//                if (id) {
//                    viewList.add(tableName);
//                }
//            }
//            stmt.close();
//            con.close();
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        } catch (SQLException e) {
//            log.error("SQL执行异常", e);
//            e.printStackTrace();
//        }
//        return viewList;
//    }
////111111111111111111111
//    @Override
//    public boolean createHiveTable(String tableName,String sql) throws SQLException {
// 
//        boolean success= true;
// 
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String hiveUrl=Messages.getString("jdbcHiveUrl");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//            success = false;
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        try {
//            deleteFdfsByHiveTable(tableName);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            log.error("hive连接异常", e);
//        } // 同时删除对应的hdfs文件，因为是建外表
//        long startTime = System.currentTimeMillis();
//        String dropIfExistsTable = "drop table if exists " + tableName;
//        long endTime = System.currentTimeMillis();
//        System.out.println("删除已存在的表所花时间(针对全量导入)：" + (endTime - startTime));
//        stmt.execute(dropIfExistsTable);
//        log.info("createSql:" + sql);
//        stmt.execute(sql);
// 
//        stmt.close();
//        con.close();
// 
//        return success;
//    }
// 
//    /**
//     * 根据表名删除该hive表对应的hdfs文件，主要针对hive中的外表
//     *11111111111111
//     * @param tableName
//     * @return
//     * @throws ClassNotFoundException
//     * @throws SQLException
//     */
//    public boolean deleteFdfsByHiveTable(String tableName)
//            throws ClassNotFoundException, SQLException {
//        boolean b = false;
//        String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//        String jdbcHiveUrl = Messages.getString("jdbcHiveUrl");
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String sqoopOutput = Messages.getString("sqoopOutput");
//        String HDFSpath = Messages.getString("HDFSpath");
//        System.setProperty("HADOOP_USER_NAME", Messages.getString("hiveUser"));
//        String rootPath = Messages.getString("HDFSUrl");
//        Class.forName(jdbcHiveDriver);
//        String path = null;
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//                hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        // 判断该表是否存在
//        String sqlHad = "show tables '" + tableName + "'";
//        ResultSet had = stmt.executeQuery(sqlHad);
//        if (!had.next()) {
//            return true;
//        }
//        String sql = "describe formatted " + tableName;
//        log.info("sql:" + sql);
//        ResultSet set = stmt.executeQuery(sql);
//        while (set.next()) {
//            String location = set.getString(1);
//            if (location != null
//                    && "Location:".equals(location.replace(" ", "")))
//                path = set.getString(2);
//        }
//        set.close();
//        stmt.close();
//        con.close();
//        if (path != null) {
//            String[] paths = null;
//            if (path.contains(sqoopOutput)) {
//                paths = path.split(sqoopOutput);
//            } else if (path.contains(HDFSpath)) {
//                paths = path.split(HDFSpath);
//            }
//            if (paths != null && paths.length > 0) {
//                String dfs = paths[0];
//                path = path.replace(dfs, rootPath);
//                Path p = new Path(path);
//                Configuration conf = new Configuration();
//                try {
//                    FileSystem fs = p.getFileSystem(conf);
//                    boolean isHad = fs.exists(p);
//                    if (isHad) {
//                        b = fs.delete(p, true);
//                    } else {
//                        b = true;
//                    }
//                    // boolean b = fs.createNewFile(p);
//                    fs.close();
//                } catch (IOException e) {
//                    log.error("HDFS文件读取异常", e);
//                    e.printStackTrace();
//                }
//            }
//        }
//        return b;
//    }
// 
//    @Override
//    public boolean isExistHiveTable(String tableName) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String hiveUrl = Messages.getString("jdbcHiveUrl");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        boolean exist = false;
//        if (tableName == null || tableName.trim().equals(""))
//            return false;
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
// 
//        String showTablesql = "show tables '" + tableName + "'";
//        log.info("showTablesql:" + showTablesql);
//        ResultSet tableSet = stmt.executeQuery(showTablesql);
//        if (tableSet.next()) {
//            exist = true;
//        }
//        return exist;
//    }
// 
// 
//    /**
//     * 创建Hive textfiled表
//     */
//    public String createHiveTempTable(String tableName,String sql, String HDFSPAth) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String hiveUrl = Messages.getString("jdbcHiveUrl");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
// 
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
// 
// 
//        String dropIfExistsTable = "drop table if exists " + tableName;
//        log.info("dropIfExistsTable:" + dropIfExistsTable);
//        stmt.execute(dropIfExistsTable);
// 
//        log.info("createSql:" + sql);
//        stmt.execute(sql);
// 
//        String loadData = "LOAD DATA INPATH '" + HDFSPAth + "' INTO TABLE " + tableName;
//        log.info("loadData:" + loadData);
//        stmt.execute(loadData);
// 
//        stmt.close();
//        con.close();
//        return tableName;
//    }
// 
//    /**
//     * 创建hive表 add by yangqi 2015/10/10
//     */
//    @Override
//    public String createHiveORCTable(String tableName,String primaryKey, String sql) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String hiveUrl = Messages.getString("jdbcHiveUrl");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
// 
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
// 
//        try {
//            deleteFdfsByHiveTable(tableName);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        } // 同时删除对应的hdfs文件，因为是建外表
//        String dropIfExistsTable = "drop table if exists " + tableName;
//        log.info("dropIfExistsTable:" + dropIfExistsTable);
//        stmt.execute(dropIfExistsTable);
// 
//        stmt.execute("set ngmr.partition.automerge = true");
//        String createSql= sql+" CLUSTERED BY ("
//                + primaryKey
//                + ") INTO "
//                + "100"
//                + " BUCKETS ROW FORMAT DELIMITED FIELDS TERMINATED BY '\001' STORED AS ORC TBLPROPERTIES "
//                + "('transactional'='true')";
// 
//        System.out.println(createSql);
//        log.info("createSql:" + createSql);
//        stmt.execute(createSql.toString().trim());
// 
//        stmt.close();
//        con.close();
// 
//        //
//        return tableName;
//    }
// 
//    /**
//     * 创建hiveorc表 add by yangqi 2015/10/10
//     *///1111111111111111
//    // 将数据从hive的textFile表导入到orc表中
//    @Override
//    public void insertIntoHiveOrcTable(String textfileTableName,
//                                       String orcTableName, String hiveUrl) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        //获取text表的大小，根据这个大小来判断task的量
//        Map<String,String> map = getCountAndSize(textfileTableName, Messages.getString("jdbcHiveUrl"));
//        stmt.execute("set ngmr.partition.automerge = true");
//        long count = Long.parseLong(map.get("count"));
//        if(count>=50000000){
//            stmt.execute("set mapred.reduce.tasks=100");
//        }else if(10000000<=count&&count<=50000000){
//            stmt.execute("set mapred.reduce.tasks=20");
//        }else{
//            stmt.execute("set mapred.reduce.tasks=10");
//        }
//        String insertSql = "insert into table " + orcTableName
//                + " select * from " + textfileTableName + " where resource_flag = 0 distribute by rand()";
//        log.info("insertSql:" + insertSql);
//        stmt.execute(insertSql);
//        stmt.close();
//        con.close();
// 
//    }
// 
//    /**
//     * 根据表名统计数据表的记录数和文件大小
//     *
//     * @author ZYY
//     * @since 2015/1/14
//     */
//    @Override
//    public Map<String, String> getCountAndSize(String tableName, String hiveUrl)
//            throws SQLException {
// 
//        Map<String, String> map = new HashMap<String, String>(); //返回结果map
//        String[] pathAndSize = new String[2];                    //存储数据大小，地址数组变量
//        String count = "";                                       //数据表记录量变量
// 
//		/*
//		 * 获取用户名，密码，得到jdbchive链接
//		 * */
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", Messages.getString("hiveUser"));
//        String rootPath = Messages.getString("HDFSUrl");
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con = DriverManager
//                .getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
// 
//        //定义获取数据表记录总量的sql
//        String countSql = "select count(*) from " + tableName;
//        log.info("获取数据表记录总量的sql" + countSql);
//        try {
//            ResultSet rs = stmt.executeQuery(countSql);
//            if (rs.next()) {
//                count = rs.getString(1);
//            }
//        } catch (Exception e) {
//            log.error("SQL执行异常", e);
//            e.printStackTrace();
//        }
// 
//        //定义获取hive中数据大小和地址sql
//        String sizesql = "describe formatted " + tableName;
//        ResultSet set = stmt.executeQuery(sizesql);
//        while (set.next()) {
//            String location = set.getString(1);
//            if (location != null
//                    && "Location:".equals(location.replace(" ", "")))
//                pathAndSize[0] = set.getString(2);
//            String totalSize = set.getString(2);
//            if (totalSize != null
//                    && "totalSize".equals(totalSize.replace(" ", "")))
//                pathAndSize[1] = set.getString(3);
//        }
// 
//        // 由于hive创建的是外表，对path和siz进行处理
//        // 将path中的节点信息改为port
//        if (pathAndSize[0] != null && !pathAndSize[0].contains(rootPath)) {
//            String path = pathAndSize[0];
//            String[] paths = path.split("://");
//            if (paths.length > 1) {
//                String dfs = paths[1];
//                String[] filPaths = dfs.split("/");
//                if (filPaths.length > 0) {
//                    String f = filPaths[0];
//                    path = dfs.replace(f, rootPath);
//                    pathAndSize[0] = path;
// 
//                }
//            }
//        }
//        // hive外表不能获取size的处理
//        if (pathAndSize[1] == null || pathAndSize[1].equals("") || "0".equals(pathAndSize[1].trim())) {
//            if (pathAndSize[0] != null) {
//                String path = pathAndSize[0];
//                Path p = new Path(path);
//                long total = 0;
//                Configuration conf = new Configuration();
//                try {
//                    FileSystem fs = p.getFileSystem(conf);
//                    boolean isHad = fs.exists(p);
//                    if (isHad) {
//                        RemoteIterator<LocatedFileStatus> fd = fs.listFiles(p,
//                                true);// 获取文件夹下所有文件
//                        while (fd.hasNext()) {
//                            LocatedFileStatus lf = fd.next();// 获取文件
//                            System.out.println(lf.getLen());
//                            total = total + lf.getLen();// 文件大小
//                        }
//                    }
////                    将单位由b转换为kb
// 
//                    total =total/1024;
//                    pathAndSize[1] = total + "";
//                    fs.close();
//                } catch (IOException e) {
//                    log.error("Hive文件读取出错", e);
//                    e.printStackTrace();
//                }
//            }
//        }
// 
//        //关闭结果集，事务和数据库链接
//        set.close();
//        stmt.close();
//        con.close();
// 
//        //将结果存入到结果map
//        map.put("count", count);
//        map.put("size", pathAndSize[1]);
// 
//        return map;
//    }
//    /**
//     * 增11111111量导入的数据，在hive中全部删除
//     */
//    public void deleteIncrementDataExistInOrcTable(String textfileTable,
//                                                   String orcTableName, String primaryKey, String hiveUrl)
//            throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        String deleteSql = "delete from " + orcTableName + " where "
//                + primaryKey + " in (select " + primaryKey + " from "
//                + textfileTable + ")";
//        log.info("deleteSql:" + deleteSql);
//        stmt.execute(deleteSql);
//        stmt.close();
//        con.close();
// 
//    }
// 
//    /**
//     * merge临时表和orc add by yangqi 2015/10/14
//     */
//    @Override
//    public void mergeIntoHiveOrcTable(Map<String, String[]> map,
//                                      String hiveUrl, String primaryKey) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        String resourceId = map.get("resourceId")[0];
//        String tableName = map.get("tableName")[0];
//        String orcTableName = resourceId + "_orc_" + tableName;
//        String tempOrcTable = resourceId + "_" + tableName;
//        StringBuffer mergeSql = new StringBuffer("MERGE INTO " + orcTableName + " a USING "
//                + tempOrcTable + " b ON (a." + primaryKey + " = b."
//                + primaryKey + ") WHEN MATCHED THEN UPDATE SET ");
//        String[] cols = map.get(tableName);
//        if (cols != null && cols.length > 0) {
//            for (int i = 0; i < cols.length; i++) {
// 
//                if (0 == i) {
//                    mergeSql.append(cols[i].split(" ")[0] + " = b."
//                            + cols[i].split(" ")[0]);
//                } else {
//                    mergeSql.append(", " + cols[i].split(" ")[0]
//                            + " = b." + cols[i].split(" ")[0]);
//                }
//            }
//        }
//        mergeSql.append(" WHEN NOT MATCHED THEN INSERT (");
//        if (cols != null && cols.length > 0) {
//            for (int i = 0; i < cols.length; i++) {
//                if (0 == i) {
//                    mergeSql.append(cols[i].split(" ")[0]);
//                } else {
//                    mergeSql.append(", " + cols[i].split(" ")[0]);
//                }
//            }
//        }
//        mergeSql.append(") VALUES(");
//        if (cols != null && cols.length > 0) {
//            for (int i = 0; i < cols.length; i++) {
//                if (0 == i) {
//                    mergeSql.append("b." + cols[i].split(" ")[0]);
//                } else {
//                    mergeSql.append(", " + "b." + cols[i].split(" ")[0]);
//                }
//            }
//        }
// 
//        mergeSql.append(");");
//        log.info("mergeSql" + mergeSql);
//        stmt.execute(mergeSql.toString().trim());
//        stmt.close();
//        con.close();
//    }
// 
//    /**
//     * 创建orc临时表 yangqi 2015/10/23
//     */
//    @Override
//    public String createTempHiveORCTable(Map<String, String[]> map,
//                                         String primaryKey, String hiveUrl) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        if (map == null || map.get("tableName") == null
//                || map.get("tableName").length == 0)
//            return null;
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        String resourceId = map.get("resourceId")[0];
//        String tableName = map.get("tableName")[0];
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        String[] cols = map.get(tableName);
//        String table = resourceId + "_temp_orc_" + tableName;// 标识为orc表
//        try {
//            deleteFdfsByHiveTable(table);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } // 同时删除对应的hdfs文件，因为是建外表
//        String dropIfExistsTable = "drop table if exists " + table;
//        stmt.execute(dropIfExistsTable);
//        StringBuffer createSql = new StringBuffer("create external table " + table + "(");
//        if (cols != null && cols.length > 0) {
//            for (int i = 0; i < cols.length; i++) {
//                if (i == 0) {
//                    createSql.append("`" + cols[i].replace(" ", "` "));
//                } else {
//                    createSql.append("," + "`" + cols[i].replace(" ", "` "));
//                }
//            }
//        }
//        createSql.append(
//                ") CLUSTERED BY ("
//                        + primaryKey
//                        + ") INTO "
//                        + "10"
//                        + " BUCKETS ROW FORMAT DELIMITED FIELDS TERMINATED BY '\001' STORED AS ORC TBLPROPERTIES "
//                        + "('transactional'='true')"
//        );
// 
//        log.info("createSql" + createSql);
//        stmt.execute(createSql.toString().trim());
//        stmt.close();
//        con.close();
//        return table;
// 
//    }
// 
//    /**
//     * 将hive临时表数据导入到hive orc表中
//     */
//    @Override
//    public void insertIntoTempOrcTable(String textfileTableName,
//                                       String tempOrcTable, String hiveUrl) throws SQLException {
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        String insertSql = "insert overwrite table " + tempOrcTable
//                + " select * from " + textfileTableName;
//        log.info("insertSql" + insertSql);
//        stmt.execute(insertSql);
//        stmt.close();
//        con.close();
//    }
// 
//    public String createOrUpdateHiveTable(Map<String, String[]> map,
//                                          String pCol, String hiveUrl, String HDFSPAth) throws SQLException {
// 
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        if (map == null || map.get("tableName") == null
//                || map.get("tableName").length == 0)
//            return null;
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        String resourceId = map.get("resourceId")[0];
//        String tableName = map.get("tableName")[0];
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        String[] cols = map.get(tableName);
//        String table = resourceId + "_" + tableName;
//        // try {
//        // deleteFdfsByHiveTable(table);
//        // } catch (ClassNotFoundException e) {
//        // // TODO Auto-generated catch block
//        // e.printStackTrace();
//        // } // 同时删除对应的hdfs文件，因为是建外表
// 
//        // String dropIfExistsTable = "drop table if exists " + table;
//        // stmt.execute(dropIfExistsTable);
// 
//        // 创建分区表
//        StringBuffer createSql = new StringBuffer("CREATE TABLE IF NOT EXISTS " + table + "(");
//        if (cols != null && cols.length > 0)
//            for (int i = 0; i < cols.length; i++)
//                createSql.append(cols[i] + ",");
// 
//        createSql.append(") PARTITIONED BY (p_column String) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\001' ");
//        createSql = new StringBuffer(createSql.toString().replace(",)", ")"));
//        log.info("hive建表语句:" + createSql);
//        stmt.execute(createSql.toString().trim());
// 
//        // 修改表创建分区
//        String alterSql = "ALTER TABLE " + table
//                + " ADD IF NOT EXISTS PARTITION (p_column='" + pCol + "')";
//        // + "LOCATION '" + HDFSPAth + "'";
//        log.info("hive修改分区语句:" + alterSql);
//        stmt.execute(alterSql);
// 
//        // load数据
//        String loadSql = "LOAD DATA INPATH '" + HDFSPAth
//                + "' OVERWRITE INTO TABLE " + table + " PARTITION(p_column='"
//                + pCol + "')";
//        log.info("hive分区导入数据:" + loadSql);
//        stmt.execute(loadSql);
// 
//        stmt.close();
//        con.close();
//        return table;
//    }
// 
//    @Override
//    public void deleteHiveTruePartition(String tableName, String partitionStr)
//            throws SQLException {
//        // TODO Auto-generated method stub
//        String deleteSql = "ALTER TABLE " + tableName
//                + " DROP IF EXISTS PARTITION (" + partitionStr + ")";
//        System.out.println("Running: " + deleteSql);
//        try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            log.error("hive连接异常", e);
//        }
//        log.info("deleteSql" + deleteSql);
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//                hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        stmt.execute(deleteSql);
//        stmt.close();
//        con.close();
//    }
// 
//    @Override
//    public String createHivePartitionTable(String tableName,List<ColumnVO> columnVOs, String HDFSPAth) throws SQLException {
//        System.out.println("in to createHivePartitionTable");
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String hiveUrl = Messages.getString("jdbcHiveUrl");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
// 
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive连接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
// 
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
// 
// 
//        StringBuffer createSql = new StringBuffer("create table IF NOT EXISTS " + tableName + "_tmp (");
//        StringBuffer columnSql = new StringBuffer();
//        for (int i = 0; i < columnVOs.size()-1; i++) {
//            createSql.append(columnVOs.get(i).getColumnName() + " string,");
//            columnSql.append(columnVOs.get(i).getColumnName()+",");
//        }
//        createSql.append("p_column String,");
//        createSql.append(columnVOs.get(columnVOs.size()-1).getColumnName() + " string");
//        columnSql.append(columnVOs.get(columnVOs.size()-1).getColumnName()+",");
// 
//        createSql.append(") ROW FORMAT DELIMITED FIELDS TERMINATED BY '\001' LOCATION ");
// 
//        createSql.append(" '" + HDFSPAth + "'");
//        log.info("createSql:" + createSql);
//        System.out.println(createSql);
//        stmt.execute(createSql.toString().trim());
// 
//        String createPartitionSql = createSql.toString().replace("_tmp", "")
//                .replace(",p_column String", "")
//                .replace(") ROW", ") partitioned by (p_hive String) ROW")
//                .split("LOCATION ")[0];
//        System.out.println(createPartitionSql);
//        log.info("sql:" + createPartitionSql);
//        stmt.execute(createPartitionSql);
// 
//        stmt.execute("set hive.exec.dynamic.partition=true");
//        stmt.execute("set hive.exec.dynamic.partition.mode=nonstrict");
//        String insertPartitionSql = "insert overwrite table " + tableName
//                + " partition(p_hive) select " + columnSql.toString()
//                + "substr(p_column,1,length( p_column )-1) p_hive FROM "
//                + tableName + "_tmp";
// 
// 
//        System.out.println(insertPartitionSql);
//        log.info("sql:" + insertPartitionSql);
//        stmt.execute(insertPartitionSql);
// 
//        String dropIfExistsTable = "drop table if exists " + tableName + "_tmp";
//        log.info("sql:" + dropIfExistsTable);
//        stmt.execute(dropIfExistsTable);
// 
// 
//        stmt.close();
//        con.close();
// 
//        //
//        return tableName;
//    }
// 
//    @Override
//    public List<String> getTablesColName(String tableName) {
// 
//        List<String> result = null;
// 
//        if (!StringUtils.isBlank(tableName)) {
// 
//            tableName = tableName.trim();
// 
//            boolean tableExist = false;// 标示表是否存在
// 
//            try {
//                Class.forName(jdbcHiveDriver);
// 
//            } catch (ClassNotFoundException e) {
//                log.error("Hive链接异常", e);
//                e.printStackTrace();
//            }
// 
//            try {
//                tableExist = existTable(tableName);
//            } catch (SQLException e1) {
//                log.error("SQL执行异常", e1);
//                log.error(e1.getMessage());
//            }
// 
//            if (tableExist) {
// 
//                Connection con = null;
//                Statement stmt = null;
//                try {
//                    con = DriverManager.getConnection(jdbcHiveUrl, hiveUser,
//                            hivePwd);
//                    stmt = con.createStatement();
//                    ResultSet resultSet = null;
//                    log.info("sql:" + "select * from " + tableName + " limit 1");
//                    resultSet = stmt.executeQuery("select * from " + tableName
//                            + " limit 1");
// 
//                    result = new ArrayList<>();
// 
//                    // 获取列名
//                    ResultSetMetaData metaData = resultSet.getMetaData();
//                    for (int i = 0; i < metaData.getColumnCount(); i++) {
//                        // resultSet数据下标从1开始
//                        String columnName = metaData.getColumnName(i + 1);
//                        result.add(columnName);
//                    }
//                } catch (SQLException e) {
//                    log.error("SQL执行异常", e);
//                    log.error(e.getMessage());
//                } finally {// 释放资源
//                    try {
//                        if (null != stmt)
//                            stmt.close();
//                        if (null != con)
//                            con.close();
// 
//                    } catch (SQLException e) {
//                        log.error("hive链接关闭异常", e);
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return result;
//    }
// 
// 
// 
//    @Override
//    public Map<String, String[]> getTablesCol(String url, long resourceId,
//                                              String userName, String password, String goOnTableName,
//                                              String tableName) {
// 
//        Map<String, String[]> map = new HashMap<String, String[]>();
//        try {
//            String jdbcMysqlDriver = Messages.getString("jdbc_mysql_driver");
//            if (url.contains("jdbc:oracle")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_oracle_driver");
//            } else if (url.contains("jdbc:sqlserver")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_sqlserver_driver");
//            } else if (url.contains("jdbc:sybase:Tds")) {
//                jdbcMysqlDriver = Messages.getString("jdbc_sybase_driver");
//            }
//            Class.forName(jdbcMysqlDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("jdbc链接异常", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        try {
//            con = DriverManager.getConnection(url, userName, password);
//            Statement stmt = con.createStatement();
//            Boolean id = false;
// 
//            String sql = null;
//            String sqltableComments = "";//查找表对应的comments字段的SQL语句
//            String sqlColumnInfo = "";//查找表中所有字段的信息
//            if (url.contains("jdbc:oracle")) {
//                sql = "select * from  " + tableName + " where rownum<=1";
//                sqltableComments = "select comments from user_tab_comments WHERE table_name = '"+tableName+"'";
//                sqlColumnInfo = "select COLUMN_NAME,DATA_TYPE from user_tab_columns where table_name = '"+tableName+"'";
//            } else if (url.contains("jdbc:sqlserver")) {
//                sql = "select top 1 * from " + tableName;
//                sqltableComments = "select * from TABLES where TABLE_SCHEMA='my_db' and table_name='"+tableName+"'";
//                sqlColumnInfo = "select * from INFORMATION_SCHEMA.columns where table_name = '"+tableName+"'";
//            } else if (url.contains("jdbc:sybase:Tds")) {
//                sql = "select top 1 * from " + tableName;
//            } else {
//                sql = "select * from " + tableName + " limit 1";
//                sqltableComments = "SHOW TABLE STATUS LIKE \'" + tableName + "\'";
//                sqlColumnInfo = "show full fields from " + tableName;
//            }
//            log.info("sql" + sql);
//            String[] tableRemarkInfo = new String[1];
//            ResultSet colsSet = stmt.executeQuery(sql);
// 
//            System.out.println(sql);
// 
//            ResultSetMetaData data = colsSet.getMetaData();
//            int count = data.getColumnCount();
//            String[] resourceIds = {resourceId + ""};
//            String[] cols = new String[count];
//            String[] colsNameAndType = new String[count];//存储字段名和字段类型 added by XH 2016-2-16 10:15:58
//            String[] colsRemarks = new String[count];//存储字段备注
//            String[] parColumn = {""};
// 
//            colsSet.close(); //查完表信息先关
// 
//            if (!sqltableComments.isEmpty() && !sqlColumnInfo.isEmpty()) {
//                ResultSet tableRemarkSet = stmt.executeQuery(sqltableComments);
//                while (tableRemarkSet.next()) {
//                    if (url.contains("jdbc:mysql")) {
//                        tableRemarkInfo[0] = tableRemarkSet.getString("Comment");
//                    } else if(url.contains("jdbc:oracle")){
//                        tableRemarkInfo[0] = tableRemarkSet.getString("comments");
//                    }else{
//                        tableRemarkInfo[0] = tableRemarkSet.getString(1);
//                    }
//                    break;
//                }
//                tableRemarkSet.close();
// 
//                ResultSet colSet = stmt.executeQuery(sqlColumnInfo);
//                int i = 0;
//                while (colSet.next()) {
//                    String ColumnName = "";
//                    String ColumnType = "";
//                    String ColumnRemark = "";
//                    if(url.contains("jdbc:oracle")){
//                        ColumnName = colSet.getString("COLUMN_NAME");
//                        ColumnType = colSet.getString("DATA_TYPE");
//                        String sqlcolumnComment = "select comments from user_col_comments where table_name='"+tableName+"' and COLUMN_NAME = '"+ColumnName+"'";
//                        ResultSet columnCommentSet = stmt.executeQuery(sqlcolumnComment);
//                        while(columnCommentSet.next()){
//                            ColumnRemark = columnCommentSet.getString("comments");
//                            break;
//                        }
//                        columnCommentSet.close();
//                    }
//                    cols[i] = ColumnName + " " + "String";
//                    colsNameAndType[i] = ColumnName + " " + ColumnType;//设置字段名和字段类型
//                    colsRemarks[i++] = ColumnRemark;
// 
//                }
//                colSet.close();
//            } else {
//                for (int i = 1; i <= count; i++) {
//                    String cloName = data.getColumnName(i); //字段名 commtens added by XH 2016-2-3 10:44:19
//                    String cloType = data.getColumnTypeName(i);// 字段类型 comments added by XH 2016-2-3 10:44:34
//                    cols[i - 1] = cloName + " " + "String";
// 
//                    colsNameAndType[i - 1] = cloName + " " + cloType;//设置字段名和字段类型
// 
//                    if (parColumn[0].equals("")) {
//                        if (!cloType.equals("DATE")) {
//                            parColumn[0] = cloName;
//                        }
//                    }
//                }
//            }
// 
//            if (goOnTableName == null || goOnTableName.equals("")
//                    || goOnTableName.equals(" ")) {
//                id = true;
//            } else {
//                if (tableName.equals(goOnTableName))
//                    id = true;
//            }
//            if (id) {
//                //导入hive表新增一个导入平台时间字段
//                String colsTime = "load_bigdata_time" + " " + "String";//创建hive表字段
//                String colsNameAndTypeTime = "load_bigdata_time" + " " + "datetime";//保存到元数据字段表字段
//                if(!isHaveStr(cols, "load_bigdata_time")) {//导入的表中不存在新增字段
//                    // 字段数组中增加新增字段元素
//                    List<String> listCol = new ArrayList<String>();
//                    List<String> listColAndType = new ArrayList<String>();
//                    List<String> listColsRemark = new ArrayList<String>();
//                    for (int j = 0; j < cols.length; j++) {
//                        listCol.add(cols[j]);
//                        listColAndType.add(colsNameAndType[j]);
//                        listColsRemark.add(colsRemarks[j]);
//                    }
//                    listCol.add(colsTime);
//                    listColAndType.add(colsNameAndTypeTime);
//                    listColsRemark.add("导入平台时间");
//                    // 返回String型的数组
//                    cols = listCol.toArray(new String[0]);//创建hive表用
//                    colsNameAndType = listColAndType.toArray(new String[0]);//保存元数据字段表用
//                    colsRemarks = listColsRemark.toArray(new String[0]);//保存元数据字段表用
//                }
// 
//                map.put(tableName, cols);
//                String[] talbelNames = {tableName};
//                map.put("tableName", talbelNames);
//                map.put("resourceId", resourceIds);
//                map.put("partitionColumn", parColumn);
//                map.put("colsNameAndType", colsNameAndType);
//                map.put("tableRemark", tableRemarkInfo);
//                map.put("colsRemark", colsRemarks);
//            }
//            //tmt.close();
//            //ctmt.close();
// 
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            log.error("SQL执行异常", e);
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
// 
//        }
//        return map;
//    }
// 
//    /**
//     * 此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串
//     * @param strs
//     * @param s
//     * @return true包含，false不包含
//     */
//    public static boolean isHaveStr(String[] strs,String s){
//        for(int i=0;i<strs.length;i++){
//            if(strs[i].indexOf(s)!=-1){//循环查找字符串数组中的每个字符串中是否包含所有查找的内容
//                return true;//查找到了就返回真，不在继续查询
//            }
//        }
//        return false;//没找到返回false
//    }
// 
// 
// 
//    @Override
//    public boolean grantTableSelectToUser(String tableName, String username) {
//        boolean flag = false;
//        try {
//            String sql = "grant select on table " + tableName + " to user " + username;
//            log.info("数据库授权语句" + sql);
//            Class.forName(jdbcHiveDriver);
//            Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//            Statement statement = con.createStatement();
//            statement.execute(sql);
//            statement.close();
//            con.close();
//            System.out.println(sql);
//            flag = true;
//        } catch (Exception e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        return flag;
//    }
// 
//    @Override
//    public boolean revokeTableSelectFromUser(String tableName, String username) {
//        boolean flag = false;
//        try {
//            String sql = "revoke select on table " + tableName + " from user " + username;
//            log.info("sql" + sql);
//            Class.forName(jdbcHiveDriver);
//            Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//            Statement statement = con.createStatement();
//            statement.execute(sql);
//            statement.close();
//            con.close();
//            System.out.println(sql);
//            flag = true;
//        } catch (Exception e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        return flag;
//    }
// 
//    @Override
//    public boolean importMongoDBToHive(List<String> list, String tableName, String mongoUrl) {
//        Connection con = null;       //定义链接并初始化
//        Statement statement = null;  //定义事务并初始化
//        //得到hive链接，若失败，抛出异常并且返回
//        try {
//            Class.forName(jdbcHiveDriver);
//            con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//            statement = con.createStatement();
//        } catch (Exception e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//            return false;
//        }
// 
//        String dropIfExistsTable = "drop table if exists " + tableName + "_temp";
//        log.info("dropIfExistsTable" + dropIfExistsTable);
//        try {
//            statement.execute(dropIfExistsTable);
//        } catch (SQLException e1) {
//            log.error("SQL异常", e1);
//            e1.printStackTrace();
//            return false;
//        }
//        //根据传入的值，得到创建hive与mongodb关联sql语句
//        StringBuffer createSql = new StringBuffer();
//        createSql.append("create external table " + tableName + "_temp (");
//        for (int i = 0; i < list.size(); i++) {
//            createSql.append(list.get(i) + " String");
//            if (i != list.size() - 1) {
//                createSql.append(",");
//            }
//        }
//        createSql.append(") stored by 'com.mongodb.hadoop.hive.MongoStorageHandler' with serdeproperties('mongo.columns.mapping'='{");
//        for (int i = 0; i < list.size(); i++) {
//            createSql.append("\"" + list.get(i) + "\" : \"" + list.get(i) + "\"");
//            if (i != list.size() - 1) {
//                createSql.append(",");
//            }
//        }
//        createSql.append("}') tblproperties('mongo.uri'='" + mongoUrl + "') ");
//        log.info("createSql" + createSql);
//        System.out.println(createSql);
//        //执行sql语句并且返回结果，若执行失败，抛出异常并且返回
//        try {
//            statement.execute(createSql.toString().trim());
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("SQL执行异常", e);
//            return false;
//        }
//        String dropIfExistsHiveTable = "drop table if exists " + tableName;
//        log.info("dropIfExistsHiveTableSql" + dropIfExistsHiveTable);
//        try {
//            statement.execute(dropIfExistsHiveTable);
//        } catch (SQLException e1) {
//            log.error("SQL执行异常", e1);
//            e1.printStackTrace();
//            return false;
//        }
//        //创建hive表
//        String createHiveSql = "create table " + tableName + " as select * from " + tableName + "_temp";
//        System.out.println(createHiveSql);
//        log.info("createHiveSql" + createHiveSql);
//        //执行sql语句并且返回结果，若执行失败，抛出异常并且返回
//        try {
//            statement.execute(createHiveSql);
//        } catch (Exception e) {
//            log.error("SQL执行异常", e);
//            e.printStackTrace();
//            return false;
//        }
//        //向创建好的hive表中插入数据
//        String insertHiveSql = "insert overwrite table " + tableName + " select * from " + tableName + "_temp";
//        log.info("insertHiveSql" + insertHiveSql);
//        System.out.println(insertHiveSql);
//        //执行sql语句并且返回结果，若执行失败，抛出异常并且返回
//        try {
//            statement.execute(insertHiveSql);
// 
//        } catch (Exception e) {
//            log.error("SQL执行异常", e);
//            e.printStackTrace();
//            return false;
//        }
// 
//        String dropIfExistsTempTable = "drop table if exists " + tableName + "_temp";
//        log.info("dropIfExistsTempTable" + dropIfExistsTempTable);
//        try {
//            statement.execute(dropIfExistsTempTable);
//        } catch (SQLException e1) {
//            log.error("SQL执行异常", e1);
//            e1.printStackTrace();
//            return false;
//        }
//        try {
//            statement.close();
//            con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            log.error("hive JDBC链接关闭异常", e);
//            return false;
//        }
// 
//        return true;
//    }
// 
// 
// 
//    @Override
//    public boolean createNewTable(String sql) throws SQLException {
// 
// 
//        log.info("创建数据表sql：" + sql);
//        System.out.println("Running: " + sql);
//        try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        String testSql = "SET transaction.type = inceptor";
//        stmt.execute(testSql);
//        stmt.execute(sql);
//        stmt.close();
//        con.close();
//        return true;
//    }
// 
//    @Override
//    public boolean insertDateToTabel(String sql) throws SQLException {
//        log.info("创建数据表sql：" + sql);
//        System.out.println("Running: " + sql);
//        try {
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//        java.sql.Statement stmt = con.createStatement();
//        String testSql = "SET transaction.type = inceptor";
//        stmt.execute(testSql);
//        stmt.execute(sql);
//        stmt.close();
//        con.close();
//        return true;
//    }
// 
//    @Override
//    public ArrayList<String[]> searchBySelcetAll(String selectSql) throws SQLException {
//        ArrayList<String[]> datas = new ArrayList<String[]>();
//        try {
//            final String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (final ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con;
//        con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//        final Statement stmt = con.createStatement();
//        ResultSet datSet = null;
//        try {
//            datSet = stmt.executeQuery(selectSql);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("sql执行出错", e);
//            if (e instanceof SQLException) {
// 
//                throw new SQLException(e.getCause());
//            }
//        }
//        final ResultSetMetaData col = datSet.getMetaData();
//        final int count = col.getColumnCount();
//        final String[] cols = new String[count];
//        for (int i = 1; i <= count; i++) {
//            final String cloName = col.getColumnName(i);
//            cols[i - 1] = cloName;
//        }
//        datas.add(cols);
//        while (datSet.next()) {
//            final String[] colDatas = new String[count];
//            for (int j = 1; j <= count; j++) {
//                colDatas[j - 1] = datSet.getString(j);
//            }
//            datas.add(colDatas);
//        }
//        stmt.close();
//        con.close();
//        return datas;
//    }
// 
//    @Override
//    public boolean judgeUserHadSelectAuthorToTable(String tableName,
//                                                   String userName, String password) {
// 
//        //如果表名或者用户名为空，直接返回false（没有权限）
//        if (tableName == null || userName == null) {
//            return false;
//        }
// 
//        boolean had = true;//默认是有权限
// 
//        try {
//            final String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (final ClassNotFoundException e) {
//            log.error("hive链接出错", e);
//            e.printStackTrace();
//        }
//        Connection con = null;
//        Statement stmt = null;
//        try {
//            con = DriverManager.getConnection(jdbcHiveUrl, userName, password);
//            stmt = con.createStatement();
//            String selectSql = "select * from " + tableName + " limit 1";
//            if(tableName.toLowerCase().indexOf("select")>-1){
//                if(tableName.toLowerCase().indexOf("limit")>-1){
//                    selectSql = tableName;
//                }else{
//                    selectSql = tableName + " limit 1";
// 
//                }
// 
//            }
//            selectSql = selectSql.replaceAll(" +"," ");
//            log.info(selectSql);
//            stmt.executeQuery(selectSql);//只要查询不报错就是有权限
// 
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//            had = false;
//        }
// 
//        try {
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return had;
//    }
// 
//    @Override
//    public List<Map<String, Object>> queryBySql(String sql) {
//        //创建集合列表用以保存所有查询到的记录
//        List<Map<String, Object>> list = new LinkedList<>();
//        ResultSet resultSet = null;
//        Statement statement = null;
//        Connection con = null;
// 
//        try {
//            final String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//            con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//            statement = con.createStatement();
//            resultSet = statement.executeQuery(sql);
//            //ResultSetMetaData 是结果集元数据，可获取关于 ResultSet 对象中列的类型和属性信息的对象 例如：结果集中共包括多少列，每列的名称和类型等信息
//            ResultSetMetaData rsmd = resultSet.getMetaData();
//            //获取结果集中的列数
//            int columncount = rsmd.getColumnCount();
//            //while条件成立表明结果集中存在数据
//            while (resultSet.next()) {
//                //创建一个HashMap用于存储一条数据
//                HashMap<String, Object> onerow = new HashMap<>();
//                //循环获取结果集中的列名及列名所对应的值，每次循环都得到一个对象，形如：{TEST_NAME=aaa, TEST_NO=2, TEST_PWD=aaa}
//                for (int i = 0; i < columncount; i++) {
//                    //获取指定列的名称，注意orcle中列名的大小写
//                    String columnName = rsmd.getColumnName(i + 1);
//                    onerow.put(columnName, resultSet.getObject(i + 1));
//                }
//                //将获取到的对象onewrow={TEST_NAME=aaa, TEST_NO=2, TEST_PWD=aaa}放到集合列表中
//                list.add(onerow);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if (null != resultSet)
//                    resultSet.close();
// 
//                if (null != statement) statement.close();
// 
//                if (null != con) con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }
// 
// 
//    @Override
//    public void dropUserFunction(String name) throws Exception {
//        Class.forName(jdbcHiveDriver);
//        Connection con = DriverManager.getConnection(jdbcHiveUrl, hiveUser, hivePwd);
//        String sql = "drop temporary function "+name+";";
//        Statement statement = con.createStatement();
//        statement.execute(sql);
//        statement.close();
//        con.close();
//    }
//    @Override
//    public String createHiveTableForText(String tableName, String columnAndTypes, String localPath, String tableSeperator) throws SQLException{
//        String hiveUser = Messages.getString("hiveUser");
//        String hivePwd = Messages.getString("hivePwd");
//        String hiveUrl = Messages.getString("jdbcHiveUrl");
//        System.setProperty("HADOOP_USER_NAME", hiveUser);
//        if (tableName == null || tableName.trim().equals("")
//                || columnAndTypes == null || columnAndTypes.trim().equals(""))
//            return null;
//        try {
//            String jdbcHiveDriver = Messages.getString("jdbcHiveDriver");
//            Class.forName(jdbcHiveDriver);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            log.error("hive链接异常", e);
//        }
//        Connection con;
//        con = DriverManager.getConnection(hiveUrl, hiveUser, hivePwd);
//        Statement stmt = con.createStatement();
//        String table = tableName;
// 
//        String showTablesql = "show tables '" + table + "'";
//        ResultSet tableSet = stmt.executeQuery(showTablesql);
//        if (tableSet.next()) {
//            return "exist";
//        }
// 
//        StringBuffer createSql = new StringBuffer("create external table "
//                + table + "(");
//        createSql.append(columnAndTypes);
//        createSql.append(") ROW FORMAT DELIMITED FIELDS TERMINATED BY '"
//                + tableSeperator + "' STORED AS TEXTFILE");
//        log.info("createSql:" + createSql);
//        stmt.execute(createSql.toString().trim());
//        String loadSql = "load data local inpath '" + localPath
//                + "' into table " + table;
//        log.info("loadSql:" + loadSql);
//        stmt.execute(loadSql);
//        stmt.close();
//        con.close();
// 
//        return table;
//    }
//}