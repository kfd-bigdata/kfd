package com.kdf.etl.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.kdf.etl.bean.PvUrl;
import com.kdf.etl.repository.PvUrlRepository;
import com.kdf.etl.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PvUrlService {

    @Autowired
    private PvUrlRepository pvUrlRepository;

    @Autowired
    private HiveTemplate hiveTemplate;

    /**
     * hive里的数据转存到mysql
     */
    public void hiveToMysql(String yearMonthDayHour){
        List<Map<String, String>> list = getUrlPv(yearMonthDayHour);
        list.forEach(temp ->{
            PvUrl pvUrl = new PvUrl();
            pvUrl.setAppId(temp.get("appId"));
            pvUrl.setPvCount(Integer.parseInt(temp.get("count")));
            pvUrl.setUrl(temp.get("url"));
            pvUrl.setCreateTime(new Date());
            pvUrl.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
            pvUrlRepository.save(pvUrl);
        });
    }

    private List<Map<String, String>> getUrlPv(String yearMonthDayHour) {
        StringBuffer hiveSql = new StringBuffer();
        hiveSql.append("select count(*) as count, appid, url from pv_log_hive_");
        hiveSql.append(yearMonthDayHour);
        hiveSql.append(" group by appid,url");
        if(log.isInfoEnabled()) {
            log.info("getBrowserUv  hiveSql=[{}]", hiveSql.toString());
        }
        List<Map<String, String>> resultList = hiveTemplate.execute(new HiveClientCallback<List<Map<String, String>>>() {
            @Override
            public List<Map<String, String>> doInHive(HiveClient hiveClient) throws Exception {
                List<Map<String, String>> urlList = Lists.newArrayList();
                Connection conn = hiveClient.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(hiveSql.toString());
                while(rs.next()) {
                    String url = rs.getString("url");
                    String count = rs.getString("count");
                    String appid = rs.getString("appid");
                    Map<String, String> map = Maps.newHashMap();
                    map.put("count", count);
                    map.put("url", url);
                    map.put("appId", appid);
                    urlList.add(map);
                }
                return urlList;
            }
        });
        log.info("resultList========={}", resultList);
        return resultList;
    }
}
