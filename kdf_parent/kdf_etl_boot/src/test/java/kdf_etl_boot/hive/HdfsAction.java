package kdf_etl_boot.hive;
//package com.kdf.etl.contoller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.hadoop.fs.BlockLocation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.kdf.etl.service.HdfsService;
//
//@RestController
//@RequestMapping("/hadoop/hdfs")
//public class HdfsAction {
//
//	@Autowired
//	private HdfsService HdfsService;
//
//	private static Logger LOGGER = LoggerFactory.getLogger(HdfsAction.class);
//
//	/**
//	 * 创建文件夹
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "mkdir", method = RequestMethod.POST)
//	@ResponseBody
//	public void mkdir(@RequestParam("path") String path) throws Exception {
//		if (StringUtils.isEmpty(path)) {
//			LOGGER.debug("请求参数为空");
//		}
//		// 创建空文件夹
//		boolean isOk = HdfsService.mkdir(path);
//		if (isOk) {
//			LOGGER.debug("文件夹创建成功");
//		} else {
//			LOGGER.debug("文件夹创建失败");
//		}
//	}
//
//	/**
//	 * 读取HDFS目录信息
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/readPathInfo")
//	public String readPathInfo(@RequestParam("path") String path) throws Exception {
//		List<Map<String, Object>> list = HdfsService.readPathInfo(path);
//		return "读取HDFS目录信息成功" + list;
//	}
//
//	/**
//	 * 获取HDFS文件在集群中的位置
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/getFileBlockLocations")
//	public void getFileBlockLocations(@RequestParam("path") String path) throws Exception {
//		BlockLocation[] blockLocations = HdfsService.getFileBlockLocations(path);
//		System.out.println("获取HDFS文件在集群中的位置" + blockLocations);
//	}
//
//	/**
//	 * 创建文件
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/createFile")
//	public void createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file)
//			throws Exception {
//		if (StringUtils.isEmpty(path) || null == file.getBytes()) {
//		}
//		HdfsService.createFile(path, file);
//	}
//
//	/**
//	 * 读取HDFS文件内容
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/readFile")
//	public void readFile(@RequestParam("path") String path) throws Exception {
//		String targetPath = HdfsService.readFile(path);
//		System.out.println("读取HDFS文件内容" + targetPath);
//	}
//
//	/**
//	 * 读取HDFS文件转换成Byte类型
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
////	@PostMapping("/openFileToBytes")
////	public void openFileToBytes(@RequestParam("path") String path) throws Exception {
////		byte[] files = HdfsService.openFileToBytes(path);
////		return new void(void.SUCCESS, "读取HDFS文件转换成Byte类型", files);
////	}
//
//	/**
//	 * 读取HDFS文件装换成User对象
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
////	@PostMapping("/openFileToUser")
////	public void openFileToUser(@RequestParam("path") String path) throws Exception {
////		User user = HdfsService.openFileToObject(path, User.class);
////	}
//
//	/**
//	 * 读取文件列表
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/listFile")
//	public void listFile(@RequestParam("path") String path) throws Exception {
//		List<Map<String, String>> returnList = HdfsService.listFile(path);
//	}
//
//	/**
//	 * 重命名文件
//	 * 
//	 * @param oldName
//	 * @param newName
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/renameFile")
//	public void renameFile(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName)
//			throws Exception {
//		if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
//		}
//		boolean isOk = HdfsService.renameFile(oldName, newName);
//	}
//
//	/**
//	 * 删除文件
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/deleteFile")
//	public void deleteFile(@RequestParam("path") String path) throws Exception {
//		boolean isOk = HdfsService.deleteFile(path);
//	}
//
//	/**
//	 * 上传文件
//	 * 
//	 * @param path
//	 * @param uploadPath
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/uploadFile")
//	public void uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
//			throws Exception {
//		HdfsService.uploadFile(path, uploadPath);
//	}
//
//	/**
//	 * 下载文件
//	 * 
//	 * @param path
//	 * @param downloadPath
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/downloadFile")
//	public void downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
//			throws Exception {
//		HdfsService.downloadFile(path, downloadPath);
//	}
//
//	/**
//	 * HDFS文件复制
//	 * 
//	 * @param sourcePath
//	 * @param targetPath
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/copyFile")
//	public void copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath)
//			throws Exception {
//		HdfsService.copyFile(sourcePath, targetPath);
//	}
//
//	/**
//	 * 查看文件是否已存在
//	 * 
//	 * @param path
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("/existFile")
//	public void existFile(@RequestParam("path") String path) throws Exception {
//		boolean isExist = HdfsService.existFile(path);
//	}
//
//}