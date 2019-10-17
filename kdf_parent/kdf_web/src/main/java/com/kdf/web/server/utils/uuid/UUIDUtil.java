package  com.kdf.web.server.utils.uuid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UUIDUtil {

    public static final Logger DEBUG = LoggerFactory.getLogger(UUIDUtil.class);
    private static UIDFactory uuid = null;

    static {
        try {
            uuid = UIDFactory.getInstance("UUID");
        } catch (Exception unsex) {
            //LogUtils.info(DEBUG, () -> "", unsex);
        	unsex.printStackTrace();
        }
    }

    private UUIDUtil() {
    }

    public static String getUUID() {
        return uuid.getNextUID();
    }
    
    public static void main(String[] args) {
		System.out.println(UUIDUtil.getUUID());
	}
}
