package com.kdf.etl.contoller;

import com.beust.jcommander.internal.Maps;
import com.kdf.etl.service.PvUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

@Slf4j
@RestController
public class PvUrlController {

    @Autowired
    private PvUrlService pvUrlService;

    @GetMapping("/testlwl")
    public void testHive() {
        pvUrlService.hiveToMysql("");
    }
}
