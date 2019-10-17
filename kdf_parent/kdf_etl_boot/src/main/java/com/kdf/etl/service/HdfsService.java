package com.kdf.etl.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kdf.etl.constant.Constants;

@Component
public class HdfsService extends BaseService {

	private String username = "root";
	private final int bufferSize = 1024 * 1024 * 64;

	/**
	 * 获取HDFS文件系统对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public FileSystem getFileSystem() throws Exception {
		// 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
		// DHADOOP_USER_NAME=hadoop
		// 也可以在构造客户端fs对象时，通过参数传递进去
		FileSystem fileSystem = FileSystem.get(new URI(Constants.HDFS), getConf());
		return fileSystem;
	}

	/**
	 * 在HDFS创建文件夹
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public boolean mkdir(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		if (existFile(path)) {
			return true;
		}
		FileSystem fs = getFileSystem();
		// 目标路径
		Path srcPath = new Path(path);
		boolean isOk = fs.mkdirs(srcPath);
		fs.close();
		return isOk;
	}

	/**
	 * 判断HDFS文件是否存在
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public boolean existFile(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		FileSystem fs = getFileSystem();
		Path srcPath = new Path(path);
		boolean isExists = fs.exists(srcPath);
		return isExists;
	}

	/**
	 * 读取HDFS目录信息
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> readPathInfo(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		if (!existFile(path)) {
			return null;
		}
		FileSystem fs = getFileSystem();
		// 目标路径
		Path newPath = new Path(path);
		FileStatus[] statusList = fs.listStatus(newPath);
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != statusList && statusList.length > 0) {
			for (FileStatus fileStatus : statusList) {
				Map<String, Object> map = new HashMap<>();
				map.put("filePath", fileStatus.getPath());
				map.put("fileStatus", fileStatus.toString());
				list.add(map);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * HDFS创建文件
	 * 
	 * @param path
	 * @param file
	 * @throws Exception
	 */
	public void createFile(String path, MultipartFile file) throws Exception {
		if (StringUtils.isEmpty(path) || null == file.getBytes()) {
			return;
		}
		String fileName = file.getOriginalFilename();
		FileSystem fs = getFileSystem();
		// 上传时默认当前目录，后面自动拼接文件的目录
		Path newPath = new Path(path + "/" + fileName);
		// 打开一个输出流
		FSDataOutputStream outputStream = fs.create(newPath);
		outputStream.write(file.getBytes());
		outputStream.close();
		fs.close();
	}

	/**
	 * 读取HDFS文件内容
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public String readFile(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		if (!existFile(path)) {
			return null;
		}
		FileSystem fs = getFileSystem();
		// 目标路径
		Path srcPath = new Path(path);
		FSDataInputStream inputStream = null;
		try {
			inputStream = fs.open(srcPath);
			// 防止中文乱码
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String lineTxt = "";
			StringBuffer sb = new StringBuffer();
			while ((lineTxt = reader.readLine()) != null) {
				sb.append(lineTxt);
			}
			return sb.toString();
		} finally {
			inputStream.close();
			fs.close();
		}
	}

	/**
	 * 读取HDFS文件列表
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> listFile(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		if (!existFile(path)) {
			return null;
		}

		FileSystem fs = getFileSystem();
		// 目标路径
		Path srcPath = new Path(path);
		// 递归找到所有文件
		RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(srcPath, true);
		List<Map<String, String>> returnList = new ArrayList<>();
		while (filesList.hasNext()) {
			LocatedFileStatus next = filesList.next();
			String fileName = next.getPath().getName();
			Path filePath = next.getPath();
			Map<String, String> map = new HashMap<>();
			map.put("fileName", fileName);
			map.put("filePath", filePath.toString());
			returnList.add(map);
		}
		fs.close();
		return returnList;
	}

	/**
	 * HDFS重命名文件
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 * @throws Exception
	 */
	public boolean renameFile(String oldName, String newName) throws Exception {
		if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
			return false;
		}
		FileSystem fs = getFileSystem();
		// 原文件目标路径
		Path oldPath = new Path(oldName);
		// 重命名目标路径
		Path newPath = new Path(newName);
		boolean isOk = fs.rename(oldPath, newPath);
		fs.close();
		return isOk;
	}

	/**
	 * 删除HDFS文件
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFile(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		if (!existFile(path)) {
			return false;
		}
		FileSystem fs = getFileSystem();
		Path srcPath = new Path(path);
		boolean isOk = fs.deleteOnExit(srcPath);
		fs.close();
		return isOk;
	}

	/**
	 * 上传HDFS文件
	 * 
	 * @param path
	 * @param uploadPath
	 * @throws Exception
	 */
	public void uploadFile(String path, String uploadPath) throws Exception {
		if (StringUtils.isEmpty(path) || StringUtils.isEmpty(uploadPath)) {
			return;
		}
		FileSystem fs = getFileSystem();
		// 上传路径
		Path clientPath = new Path(path);
		// 目标路径
		Path serverPath = new Path(uploadPath);

		// 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
		fs.copyFromLocalFile(false, clientPath, serverPath);
		fs.close();
	}

	/**
	 * 下载HDFS文件
	 * 
	 * @param path
	 * @param downloadPath
	 * @throws Exception
	 */
	public void downloadFile(String path, String downloadPath) throws Exception {
		if (StringUtils.isEmpty(path) || StringUtils.isEmpty(downloadPath)) {
			return;
		}
		FileSystem fs = getFileSystem();
		// 上传路径
		Path clientPath = new Path(path);
		// 目标路径
		Path serverPath = new Path(downloadPath);

		// 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
		fs.copyToLocalFile(false, clientPath, serverPath);
		fs.close();
	}

	/**
	 * HDFS文件复制
	 * 
	 * @param sourcePath
	 * @param targetPath
	 * @throws Exception
	 */
	public void copyFile(String sourcePath, String targetPath) throws Exception {
		if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
			return;
		}
		FileSystem fs = getFileSystem();
		// 原始文件路径
		Path oldPath = new Path(sourcePath);
		// 目标路径
		Path newPath = new Path(targetPath);

		FSDataInputStream inputStream = null;
		FSDataOutputStream outputStream = null;
		try {
			inputStream = fs.open(oldPath);
			outputStream = fs.create(newPath);

			IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
		} finally {
			inputStream.close();
			outputStream.close();
			fs.close();
		}
	}

	/**
	 * 打开HDFS上的文件并返回byte数组
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
//	public static byte[] openFileToBytes(String path) throws Exception {
//		if (StringUtils.isEmpty(path)) {
//			return null;
//		}
//		if (!existFile(path)) {
//			return null;
//		}
//		FileSystem fs = getFileSystem();
//		// 目标路径
//		Path srcPath = new Path(path);
//		try {
//			FSDataInputStream inputStream = fs.open(srcPath);
//			return IOUtils.readFullyToByteArray(inputStream);
//		} finally {
//			fs.close();
//		}
//	}

	/**
	 * 打开HDFS上的文件并返回java对象
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
//	public static <T extends Object> T openFileToObject(String path, Class<T> clazz) throws Exception {
//		if (StringUtils.isEmpty(path)) {
//			return null;
//		}
//		if (!existFile(path)) {
//			return null;
//		}
//		String jsonStr = readFile(path);
//		return JSONUtil.fromObject(jsonStr, clazz);
//	}

	/**
	 * 获取某个文件在HDFS的集群位置
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public BlockLocation[] getFileBlockLocations(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		if (!existFile(path)) {
			return null;
		}
		FileSystem fs = getFileSystem();
		// 目标路径
		Path srcPath = new Path(path);
		FileStatus fileStatus = fs.getFileStatus(srcPath);
		return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
	}

}