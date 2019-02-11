package tsp.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptSome {
    public static String encryptMd5(String value) {
        String result = DigestUtils.md5Hex(value);
        return result;
    }
}
