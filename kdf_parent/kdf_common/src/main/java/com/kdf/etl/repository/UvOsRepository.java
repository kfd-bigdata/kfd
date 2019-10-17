package com.kdf.etl.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.UvOs;
import com.kdf.etl.vo.BrowserAndOsUvVO;
/**
 * 
 * @title: UvOsRepository.java 
 * @package com.kdf.etl.repository 
 * @description: 操作系统UV repository
 * @author: 、T
 * @date: 2019年10月15日 上午11:11:11 
 * @version: V1.0
 */
@Repository
public interface UvOsRepository extends JpaRepository<UvOs, Long> {

	/**
	 * 
	 * @title: getOsVoList 
	 * @description: 根据appid与时间获取操作系统的uv
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:11:32
	 * @param appid
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws:
	 */
	@Query("select new com.kdf.etl.vo.BrowserAndOsUvVO(osName,  SUM(uvCount)) from UvOs where appid = ?1 and requestTime between ?2 and ?3 group by osName")
	public List<BrowserAndOsUvVO> getOsVoList(String appid, Date startDate, Date endDate);
}
