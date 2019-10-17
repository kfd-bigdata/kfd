package com.kdf.etl.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.UvBrowser;
import com.kdf.etl.vo.BrowserAndOsUvVO;
/**
 * 
 * @title: UvBrowserRepository.java 
 * @package com.kdf.etl.repository 
 * @description: 浏览器UV  repository
 * @author: 、T
 * @date: 2019年10月15日 上午11:09:02 
 * @version: V1.0
 */
@Repository
public interface UvBrowserRepository extends JpaRepository<UvBrowser, Long> {

	/**
	 * 
	 * @title: getBrowserVoList 
	 * @description: 根据appid与时间获取浏览器的uv
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:09:34
	 * @param appid
	 * @param startDate
	 * @param endDate
	 * @return  BrowserAndOsUvVO
	 * @throws:
	 */
	@Query("select new com.kdf.etl.vo.BrowserAndOsUvVO(browserName,  SUM(uvCount)) from UvBrowser where appid = ?1 and requestTime between ?2 and ?3 group by browserName")
	public List<BrowserAndOsUvVO> getBrowserVoList(String appid, Date startDate, Date endDate);
}
