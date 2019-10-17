package com.kdf.etl.utils.ua;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @title: Device.java 
 * @package com.kdf.etl.utils.ua 
 * @description: user_agent解析类
 * @author: 、T
 * @date: 2019年10月12日 下午5:21:33 
 * @version: V1.0
 */
@SuppressWarnings("serial")
public class Device implements Serializable {
    private String client;
    private String version;
    private String engine;
    private String os;
    private String osVersion;
    private String device;

    public Device() {}

    /**
     * 
     * @title: Device   
     * @description: 构造方法内直接解析user_agent
     * @param: @param userAgent
     * @param: @param allMatchers
     * @param: @throws NoSuchMethodException
     * @param: @throws InvocationTargetException
     * @param: @throws IllegalAccessException  
     * @throws:
     */
    public Device(String userAgent, Map<String, List<Map<String, String>>> allMatchers) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields()) {
//            Map<String, List<Map<String, String>>> allMatchers = uaMatcher.getMatchers();
            List<Map<String, String>> fieldMatchers = allMatchers.get(field.getName()) == null ? new ArrayList<>() : allMatchers.get(field.getName());
            String relVal = null;
            Method setter = this.getClass().getMethod("set" + StringHelper.toUpperCaseFirst(field.getName()), String.class);
            for (Map<String, String> matcher : fieldMatchers) {
                String value = matcher.get("name");
                String reg = matcher.get("matcher");
                String func = matcher.get("func");
                String mapping = matcher.get("mapping");
                boolean needMapping = mapping != null;
                Map<String, String> mappings = new HashMap<>();
                if (mapping != null) {
                    String[] split = mapping.split(",");
                    for (String tmpm : split) {
                        String[] split1 = tmpm.split(":");
                        mappings.put(split1[0], split1[0]);
                    }
                }
                Method method;
                Object rst;
                if (reg.contains(",,")) {
                    method = String.class.getMethod(func, String.class, String.class);
                    String[] regx = reg.split(",,");
                    rst = method.invoke(userAgent, regx[0], regx[1]);
                    if (rst != userAgent) {
                        relVal = needMapping ? mappings.get((String) rst) : (String) rst;
                        break;
                    }
                }else {
                    method = String.class.getMethod(func, String.class);
                    String[] regx = reg.split("\\|\\|");
                    for(String r : regx) {
                        if ((int) method.invoke(userAgent, r) > -1) {
                            relVal = value;
                            break;
                        }
                    }
                }
            }
            setter.invoke(this, relVal == null ? "UnKnown" + StringHelper.toUpperCaseFirst(field.getName()) : relVal);
        }
        this.device = this.device.contains("UnKnown") ? "PC" : this.device;
    }

    public String getClient() {
        return client;
    }

    public String getVersion() {
        return version;
    }

    public String getEngine() {
        return engine;
    }

    public String getOs() {
        return os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public int hashCode() {
        int result = client != null ? client.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (engine != null ? engine.hashCode() : 0);
        result = 31 * result + (os != null ? os.hashCode() : 0);
        result = 31 * result + (osVersion != null ? osVersion.hashCode() : 0);
        result = 31 * result + (device != null ? device.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Device that = (Device) obj;

        if (client != null ? !client.equals(that.client) : that.client != null)
            return false;
        if (version != null ? !version.equals(that.version) : that.version != null)
            return false;
        if (engine != null ? !engine.equals(that.engine) : that.engine != null)
            return false;
        if (os != null ? !os.equals(that.os) : that.os != null)
            return false;
        if (osVersion != null ? !osVersion.equals(that.osVersion) : that.osVersion != null)
            return false;
        return device != null ? device.equals(that.device) : that.device == null;
    }

    @Override
    public String toString() {
        return "{client=" + this.client + ",version=" + this.version +
                ",engine=" + this.engine + ",os=" + this.os + ",osVersion=" +
                this.osVersion + ",device=" + this.device + "}";
    }
}