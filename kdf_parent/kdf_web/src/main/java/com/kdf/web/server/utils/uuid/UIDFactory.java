package  com.kdf.web.server.utils.uuid;

import java.net.InetAddress;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.crypto.SecureUtil;


public abstract class UIDFactory {
    public static final Logger DEBUG = LoggerFactory.getLogger(UUIDUtil.class);

    /**
     * United Unified Identifier
     */
    public static final String UID_UUID = "UUID";

    /**
     * Current Epoch millis SEED
     */
    protected static final long EPOCH = System.currentTimeMillis();

    /**
     * JVM Hashcode
     */
    protected static final long JVMHASH = Integer.MIN_VALUE;

    /**
     * Epoch has millisecond
     */
    protected static final long MACHINEID = getMachineID();

    /**
     * Random by seed
     */
    protected static final Random M_RANDOM = new Random(EPOCH);

    /**
     * MD5 flag
     */
    private boolean isMd5 = false;

    /**
     * Get Default UIDFactory.
     */
    public static UIDFactory getDefault() {
        return UUID.getInstance();
    }

    /**
     * Get Specified UIDFactory.
     */
    public static UIDFactory getInstance(String uidfactory) throws Exception {
        if (uidfactory.equalsIgnoreCase(UID_UUID)) {
            return UUID.getInstance();
        }

        throw new Exception(uidfactory + " Not Found!");
    }

    /**
     * Get next UID.
     */
    public abstract String getNextUID();

    /**
     * Get current UID.
     */
    public abstract String getUID();

    /**
     * Is MD5 switch ON.
     */
    public boolean isMD5() {
        return isMd5;
    }

    /**
     * Set MD5 output.
     *
     * @param flag MD5 switch
     */
    public void setMD5(boolean flag) {
        isMd5 = flag;
    }

    /**
     * Set current UID.
     */
    public abstract void setUID(String uid) throws Exception;

    /**
     * Return Printable ID String.
     */
    public abstract String toPrintableString();

    /**
     * Convert bytes to MD5 bytes.
     */
    protected static byte[] toMD5(byte[] bytes) {
        return SecureUtil.md5().digest(bytes);
    }

    /**
     * Gets the machineID attribute of the GUID class
     */
    private static long getMachineID() {
        long i = 0;

        try {
            InetAddress inetaddress = InetAddress.getLocalHost();
            byte[] abyte0 = inetaddress.getAddress();

            i = toInt(abyte0);
        } catch (Exception ex) {
         //   LogUtils.info(DEBUG, () -> "",ex);
        	ex.printStackTrace();
        }

        return i;
    }

    /**
     * Convert bytes to int utils.
     */
    private static int toInt(byte[] abyte0) {
        int i = ((abyte0[0] << 24) & 0xff000000) | ((abyte0[1] << 16) & 0xff0000) | ((abyte0[2] << 8) & 0xff00)
                | (abyte0[3] & 0xff);

        return i;
    }

}
