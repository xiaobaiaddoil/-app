package com.example.myapplication.http;


import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public  class BuildPath {
    //翻译的url
    private static final String YOUDAO_URL1 = "https://openapi.youdao.com/api";

    private static final String APP_KEY1= "445a0bda7d402deb";

    private static final String APP_SECRET1 = "xFAOaefbiP2JTsPvMjS3O4rllYupVHSK";

    //语音合成url
    private static final String YOUDAO_URL2 = "https://openapi.youdao.com/ttsapi";
    private static final String APP_KEY2 = "6388f812b74739c4";
    private static final String APP_SECRET2 = "AHwYDrmsqeDlTUYKzpOZU3bNSxf04Pvh";

    //英语发音的地址，到时候使用其来 下载和加载
    private static final String voicePath = "http://dict.youdao.com/dictvoice?audio=";
    private static final String mp3 = ".mp3";
    private static final String usvoicepath = "http://media.shanbay.com/audio/us/";
    private static final String ukvoicepath ="http://media.shanbay.com/audio/uk/";
    //做加载时的页面  可以
    private static final String todayEnglish= "https://apiv3.shanbay.com/weapps/dailyquote/quote/?date=";


    public static String getVoicePath() {
        return voicePath;
    }
    public static String getUkvoicepath(String word){
        return ukvoicepath+word+mp3;
    }
    public static String getUsvoicepath(String word){
        return usvoicepath+word+mp3;
    }
    public static String getTodayEnglish(){
        return todayEnglish+getDateString();
    }
    public static String getTodayEnglishpath(){
        return todayEnglish;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getwordpath(String word, String from, String to){
        String wenhao = "?";
        String and = "&";

        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String salt = String.valueOf(System.currentTimeMillis());
        String signStr = APP_KEY1 + truncate(word) + salt + curtime + APP_SECRET1;
        String sign = getDigestType(signStr,"SHA-256");

        String path = "";
        path=YOUDAO_URL1+"?";
        path +="q="+word+and+"from="+from+and+"to="+to+and;
        path +="appKey="+APP_KEY1+and+"salt="+salt+and;
        path +="sign="+sign+and+"signType="+"v3"+and;
        path +="curtime="+curtime;
        return path;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getvociepath(String word, String langType){
        String salt = String.valueOf(System.currentTimeMillis());
        String signStr = APP_KEY2 + word + salt + APP_SECRET2;
        String sign = getDigestType(signStr,"MD5");
        String wenhao = "?";
        String and = "&";
        String path = "";
        path=YOUDAO_URL2+"?";
        path += "q="+word+and+"langType="+langType+and+"appKey="+APP_KEY2+and;
        path += path+"salt="+salt+and;
        path += path+"sign="+sign;
        return path;
    }
    public static String getDateString(){
        Date d = new Date(System.currentTimeMillis());
        String date = (String) DateFormat.format("yyyy-MM-dd", d);
        return date;
    }
    public static String getDateStringCalendar(){
        Date d = new Date(System.currentTimeMillis());
        String date_=getDateString();
        String date = date_.replace("-",".");
        return date;
    }

    /***
     * 获取日期的指定格式的Date对象
     * @return
     */
    public static Date getDate(){
        Date d = new Date(System.currentTimeMillis());
        Date date = (Date)DateFormat.format("yy-MM-dd/HH:mm:ss",d);
        return date;
    }
    /**
     * 生成加密字段
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getDigestType(String string, String type) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance(type);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    /**
     *
     * @param result 音频字节流
     * @param file 存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        }catch (Exception e){
            Log.i("error",e.toString());
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
