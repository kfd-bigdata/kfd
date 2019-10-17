package com.kdf.etl.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.UvClientType;
import com.kdf.etl.vo.ClientTypeVO;

/**
 * 
 * @title: UvClientTypeRepository.java 
 * @package com.kdf.etl.repository 
 * @description: 客户端类型uv repository
 * @author: 、T
 * @date: 2019年10月15日 上午11:10:18 
 * @version: V1.0
 */
@Repository
public interface UvClientTypeRepository extends JpaRepository<UvClientType, Long> {

	/**
	 * 
	 * @title: getClientTypeVoList 
	 * @description: 根据appid与时间获取客户端类型的uv
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:10:36
	 * @param appid
	 * @param startDate
	 * @param endDate
	 * @return  ClientTypeVO
	 * @throws:
	 */
	@Query("select new com.kdf.etl.vo.ClientTypeVO(DATE_FORMAT(requestTime,'%Y-%m-%d %H:%i:%S'), sum(mobileUvCount), sum(pcUvCount)) from UvClientType where appid = ?1 and requestTime between ?2 and ?3 group by requestTime")
	public List<ClientTypeVO> getClientTypeVoList(String appid, Date startDate, Date endDate);
}
