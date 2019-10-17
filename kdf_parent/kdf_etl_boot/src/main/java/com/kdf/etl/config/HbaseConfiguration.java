package com.kdf.etl.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
public class HbaseConfiguration {

	@Value("${hbase.zookeeper.property.clientPort}")
    private String clientPort;
 
    @Value("${hbase.zookeeper.quorum}")
    private String quorum;
 
    @Value("${zookeeper.znode.parent}")
    private String znodeParent;
 
    @Bean
    public HbaseTemplate getHbaseTemplate() {
    	System.err.println("22222222222222222222222222");
        org.apache.hadoop.conf.Configuration configuration = org.apache.hadoop.hbase.HBaseConfiguration.create();
//        configuration = org.apache.hadoop.hbase.HBaseConfiguration.create();
//        configuration.set("hbase.zookeeper.property.clientPort", clientPort);//Hbase中zookeeper的端口号;默认是2181
        configuration.set("hbase.zookeeper.quorum", quorum);//hadoop的地址,这里需要在系统的host文件配置如:hadoop1,hadoop2,host文件中未:192.168.0.1 hadoop1  192.168.0.2 hadoop2
        configuration.set("zookeeper.znode.parent", znodeParent);
//        configuration.set("hbase.client.scanner.timeout.period", "10000");
 
 
//        File workaround = new File(".");
//        System.getProperties().put("hadoop.home.dir",
//                workaround.getAbsolutePath());
////        new File("./bin").mkdirs();
//        new File(".".concat(File.separator).concat("bin")).mkdirs();
//        try {
//            new File(".".concat(File.separator).concat("bin").concat(File.separator).concat("winutils.exe")).createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
 
        HbaseTemplate ht = new HbaseTemplate(configuration);
        return ht;
    }
}
