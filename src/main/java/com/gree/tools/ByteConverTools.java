package com.gree.tools;

public class ByteConverTools {
    /**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes,int index) {
        int value = 0;
        for(int i = index; i < 4+index; i++) {
            int shift= (3 + index - i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }
    public static int byteArrayToShort(byte[] bytes,int index) {
        int value = 0;
        for(int i = index; i < 2+index; i++) {
            int shift= (1 + index - i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }
    public static byte[] intToByteArray(int val){
        byte[] b = new byte[4];
        b[0] = (byte)(val & 0xff);
        b[1] = (byte)((val >> 8) & 0xff);
        b[2] = (byte)((val >> 16) & 0xff);
        b[3] = (byte)((val >> 24) & 0xff);
        return b;
    }
    public static byte boolenArrayToByte(boolean[] array){
        if(array != null && array.length > 0) {
            byte b = 0;
            for(int i=0;i<=7;i++) {
                if(array[i]){
                    int nn=(1<<(7-i));
                    b += nn;
                }
            }
            return b;
        }
        return 0;
    }

    public static boolean[] ByteToArrayBoolen(byte b){
        boolean[] array = new boolean[4];
        for (int i = 3; i >= 0; i--) {
            //对于byte的每bit进行判定
            array[i] = (b & 8) == 8;
            //判定byte的最后一位是否为1，若为1，则是true；否则是false
            b = (byte) (b << 1);
            //将byte右移一位
        }
        return array;
    }
}
