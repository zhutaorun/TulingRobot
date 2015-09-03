package com.zhutaorun.tulingrobot;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zhutaorun on 15/9/2.
 */
public class HttpUtil {

    public static int TXT = 100000;   //文本
    public static int TRAIN= 305000;   //列车
    public static int WEB = 200000;   //网址类数据
    public static int NEWS = 302000;   //新闻
    public static int NOTES = 308000;   //菜谱视频小说
    public static int KEY_LENGTH_ERROR = 40001;   //key的长度错误
    public static int REQ_EMPTY = 40002;   //请求内容为空
    public static int KEY_ERROR = 40003;   //key错误或者账号未激活
    public static int REQUEST_FREQUENCY = 40004;   //当天请求次数已用完
    public static int NO_ACTION = 40005;   //暂不支持该功能
    public static int SERVER_UPGRAGE = 40006;   //服务器升级
    public static int SERVER_DATA_ERROR = 40007;   //服务器数据格式异常

    public static String API_HOST = "http://www.tuling123.com/openapi/api";
    public static String API_KEY  = "1585396614eb2e72ee9b2e75a2ec1b5f";
    static ByteArrayOutputStream baos;
    static InputStream is;

    public static String getData(final String info){
        String result = "";
        try{
            URL url = new URL(getParam(info));
            //url打开连接对象
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //允许写入数据
            urlConnection.setDoOutput(true);
            //设置请求方式
            urlConnection.setRequestMethod("GET");
            //设置时间
            urlConnection.setReadTimeout(5*1000);
            urlConnection.setConnectTimeout(5*1000);
            //得到输入流
            OutputStream os = urlConnection.getOutputStream();
            //写入服务器端 能以java的基本类型往里写
            DataOutputStream outputStream = new DataOutputStream(os);
            outputStream.flush();
            outputStream.close();
            is = urlConnection.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len =0;
            while ((len = is.read(buffer))!= -1){
                baos.write(buffer,0,len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(baos != null){
                try{
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is !=null){
                try{
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;

    }

    private static String getParam(String info){
        String url = "";
        try{
            url = API_HOST+"?key="+API_KEY + "&info=" + URLEncoder.encode(info, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


}
