package  com.kdf.web.server.utils.uuid;

public class UUID extends UIDFactory {

    protected long mhiTag;

    protected long mloTag;

    protected String muuid = null;

    protected UUID(long highTag, long loTag) {
        mhiTag = highTag;
        mloTag = loTag;
        muuid = toString(this.toByteArray());
    }

    protected UUID() {
        next();
        muuid = toString(this.toByteArray());
    }

    private static byte hiNibble(byte b) {
        return (byte) ((b >> 4) & 0xf);
    }

    private static byte loNibble(byte b) {
        return (byte) (b & 0xf);
    }


    public boolean equals(Object obj) {
        try {
            if (obj == null) {
                return false;
            } else {
                UUID uuid = (UUID) obj;
                boolean flag = (uuid.mhiTag == mhiTag)
                        && (uuid.mloTag == mloTag);

                return flag;
            }
        } catch (ClassCastException cce) {
            return false;
        }
    }


    @Override
    public int hashCode() {
        int result = (int) (mhiTag ^ (mhiTag >>> 32));
        result = 31 * result + (int) (mloTag ^ (mloTag >>> 32));
        return result;
    }

    public String getNextUID() {
        next();

        return muuid;
    }

    public String getUID() {
        return muuid;
    }

    public void setUID(String uidStr) throws Exception {
        long loTag = 0L;
        long hiTag = 0L;
        int len = uidStr.length();

        if (32 != len) {
            throw new Exception("bad string format");
        }

        int i = 0;
        int idx = 0;

        for (; i < 2; i++) {
            loTag = 0L;

            for (int j = 0; j < (len / 2); j++) {
                String s = uidStr.substring(idx++, idx);
                int val = Integer.parseInt(s, 16);

                loTag <<= 4;
                loTag |= val;
            }

            if (i == 0) {
                hiTag = loTag;
            }
        }

        mhiTag = hiTag;
        mloTag = loTag;
        muuid = toString(this.toByteArray());
    }

    public String toPrintableString() {
        byte[] bytes = toByteArray();

        if (16 != bytes.length) {
            return "** Bad UUID Format/Value **";
        }

        StringBuffer buf = new StringBuffer();
        int i;

        for (i = 0; i < 4; i++) {
            buf.append(Integer.toHexString(hiNibble(bytes[i])));
            buf.append(Integer.toHexString(loNibble(bytes[i])));
        }

        while (i < 10) {
            buf.append('-');

            int j = 0;

            while (j < 2) {
                buf.append(Integer.toHexString(hiNibble(bytes[i])));
                buf.append(Integer.toHexString(loNibble(bytes[i++])));
                j++;
            }
        }

        buf.append('-');

        for (; i < 16; i++) {
            buf.append(Integer.toHexString(hiNibble(bytes[i])));
            buf.append(Integer.toHexString(loNibble(bytes[i])));
        }

        return buf.toString();
    }

    public String toString() {
        return muuid;
    }

    protected static UIDFactory getInstance() {
        return new UUID();
    }

    protected static String toString(byte[] bytes) {
        if (16 != bytes.length) {
            return "** Bad UUID Format/Value **";
        }

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < 16; i++) {
            buf.append(Integer.toHexString(hiNibble(bytes[i])));
            buf.append(Integer.toHexString(loNibble(bytes[i])));
        }

        return buf.toString();
    }

    protected void next() {
        mhiTag = (System.currentTimeMillis() + (JVMHASH * 4294967296L))
                ^ MACHINEID;
        mloTag = EPOCH + Math.abs(M_RANDOM.nextLong());
        muuid = toString(this.toByteArray());
    }

    protected byte[] toByteArray() {
        byte[] bytes = new byte[16];
        int idx = 15;
        long val = mloTag;

        for (int i = 0; i < 8; i++) {
            bytes[idx--] = (byte) (int) (val & (long) 255);
            val >>= 8;
        }

        val = mhiTag;

        for (int i = 0; i < 8; i++) {
            bytes[idx--] = (byte) (int) (val & (long) 255);
            val >>= 8;
        }

        if (!this.isMD5()) {
            return bytes;
        } else {
            return toMD5(bytes);
        }
    }
}
