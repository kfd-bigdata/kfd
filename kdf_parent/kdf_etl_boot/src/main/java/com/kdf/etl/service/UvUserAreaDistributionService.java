package com.kdf.etl.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.kdf.etl.bean.UvUserAreaDistribution;
import com.kdf.etl.repository.UvUserAreaDistributionRepository;
import com.kdf.etl.utils.DateUtils;
import com.kdf.etl.utils.IpDataHandler;
import com.kdf.etl.utils.IpResponse;

/**
 * 用户地域分布图Service
 * @ClassName: UvUserAreaDistributionService   
 * @author: PéiGǔangTíng QQ：1396968024
 * @date: 2019年10月12日 上午11:04:22
 */
@Service
public class UvUserAreaDistributionService {

    @Autowired
    private HiveTemplate hiveTemplate;
    
    @Autowired
    private UvUserAreaDistributionRepository uvUserAreaDistributionRepository;
    
    
    /**
     * 根据时间查询Hive 用户地域分布数据
     * @Title: getAreaDistributionHive     
     * @param yearMonthDayHour 年月日时    
     * @throws
     */
    public List<Map<String, Object>> getAreaDistributionHive(String yearMonthDayHour) {
         String hiveSql = "select count(1) as startCount,ip,appid from pv_log_hive_" + yearMonthDayHour + " group by ip, appid";
//        String hiveSql = "select count(1) as startCount,ip,appid from pv_log_hive group by ip,appid;";
        List<Map<String, Object>> resultListMap = hiveTemplate.execute(new HiveClientCallback<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> doInHive(HiveClient hiveClient) throws Exception {
                List<Map<String, Object>> listMap = Lists.newArrayList();
                java.sql.Connection conn = hiveClient.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(hiveSql);
                while (rs.next()) {
                    Map<String, Object> map = Maps.newHashMap();
                    String ip = rs.getString("ip");
                    Long startCount = rs.getLong("uvStartCount");
                    String appid = rs.getString("appid");
                    // 根据ip获取地区名称
                    IpResponse ipResponse = IpDataHandler.getIpInfo(ip);
                    String country = ipResponse.getCountry();
                    String region = ipResponse.getRegion();
                    String city = ipResponse.getCity();
                    // 分布地区（国家）
                    map.put("country", country);
                    // 分布地区（省份）
                    map.put("province", region);
                    // 分布地区（省份）
                    map.put("city", city);
                    // 启动次数
                    map.put("startCount", startCount);
                    // appid
                    map.put("appid", appid);
                    
                    listMap.add(map);
                    
                    // 新增数据到mysql库
                    UvUserAreaDistribution uvUserAreaDistribution = new UvUserAreaDistribution();
                    uvUserAreaDistribution.setAppid(appid);
                    uvUserAreaDistribution.setStartCount(startCount);
                    uvUserAreaDistribution.setCountry(country);
                    uvUserAreaDistribution.setProvince(region);
                    uvUserAreaDistribution.setCity(city);
                    uvUserAreaDistribution.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
                    uvUserAreaDistributionRepository.save(uvUserAreaDistribution);
                }
                return listMap;
            }
        });
        return resultListMap;
    }
}
