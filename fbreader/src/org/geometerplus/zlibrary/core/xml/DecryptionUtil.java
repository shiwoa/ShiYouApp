package org.geometerplus.zlibrary.core.xml;

public class DecryptionUtil {
    private static final String key = "ffa17e84f4812013esph4a4e1c1b981e";

    public static byte[] getKey() {
        return hexStringToByteArray(key);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] charsToBytesUTFCustom(char[] chars) {
        byte[] b = new byte[chars.length << 1];
        for (int i = 0; i < chars.length; i++) {
            int bpos = i << 1;
            b[bpos] = (byte) ((chars[i] & 0xFF00) >> 8);
            b[bpos + 1] = (byte) (chars[i] & 0x00FF);
        }
        return b;
    }

    /*
     * public static char[] bytesToCharsUTFNIO(byte[] bytes) { CharBuffer
     * cBuffer = ByteBuffer.wrap(bytes).asCharBuffer(); return cBuffer.array();
     * }
     */
    public static char[] bytesToCharsUTFCustom(byte[] bytes) {
        char[] buffer = new char[bytes.length >> 1];
        for (int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            char c = (char) (((bytes[bpos] & 0x00FF) << 8) + (bytes[bpos + 1] & 0x00FF));
            buffer[i] = c;
        }
        return buffer;
    }
}
