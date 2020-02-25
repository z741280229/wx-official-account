package com.zeno.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpResponse;
import com.zeno.menu.Button;
import com.zeno.menu.ClickButton;
import com.zeno.menu.Menu;
import com.zeno.menu.ViewButton;
import com.zeno.pojo.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-24 11:26
 **/
public class WeixinUtil {

    //录入自身账户的ID
    private static final String APPID = "wxfdbea2505f5e3239";

    //录入自身账户的APPSECRET
    private static final String APPSECRET = "ad9ef7b40621562f7a78bbfec267fa8c";

    //获取token路径
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //上传文件路径
    public static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    //创建菜单路径
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //查询菜单路径
    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    //删除菜单路径
    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    //获取用户列表路径
    private static final String QUERY_USERS_LIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

    //获取用户基本信息(单个)
    private static final String QUERY_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    //批量获取用户信息
    private static final String QUERY_BATCHGET_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";

    private static final String CREATE_TAG_URL = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN";





    /**
     * get请求
     * @param url
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doGetStr(String url) throws ParseException, IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.parseObject(result);

        }
        return jsonObject;
    }

    /**
     * POST请求
     * @param url
     * @param outStr
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpost.setEntity(new StringEntity(outStr,"UTF-8"));
        CloseableHttpResponse response = client.execute(httpost);
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    /**
     * 获取accessToken(单日请求上限：2000次)
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static AccessToken getAccessToken() throws ParseException, IOException{
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if(jsonObject != null){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInteger("expires_in"));
        }
        return token;
    }

    /**
     * 文件上传
     * @param reqUrl  文件路径
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public static JSONObject upload(File file,String reqUrl) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        //File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }
        //String url = reqUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
        ///String url = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
        URL urlObj = new URL(reqUrl);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.parseObject(result);
        return jsonObj;
        /*System.out.println(jsonObj);
        String typeName = "media_id";
        if(!"image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;*/
    }

    /**
     * 组装菜单
     * @return
     */
    public static Menu initMenu(){
        Menu menu = new Menu();
        ClickButton button11 = new ClickButton();
        button11.setName("click菜单");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();
        button21.setName("view菜单");
        button21.setType("view");
        button21.setUrl("http://www.imooc.com");

        ClickButton button31 = new ClickButton();
        button31.setName("扫码事件");
        button31.setType("scancode_push");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("地理位置");
        button32.setType("location_select");
        button32.setKey("32");

        Button button = new Button();
        button.setName("菜单");
        button.setSub_button(new Button[]{button31,button32});

        menu.setButton(new Button[]{button11,button21,button});
        return menu;
    }

    /**
     * 创建菜单
     * @param token
     * @param menu
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int createMenu(String token,String menu) throws ParseException, IOException{
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, menu);
        if(jsonObject != null){
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }

    /**
     * 查询菜单
     * @param token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject queryMenu(String token) throws ParseException, IOException{
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        return jsonObject;
    }

    /**
     * 删除菜单
     * @param token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int deleteMenu(String token) throws ParseException, IOException{
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        int result = 0;
        if(jsonObject != null){
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }


    /**
     * 获取用户列表（单日请求上线：500次）
     * 关注者数量超过10000时
     * 当公众号关注者数量超过10000时，可通过填写next_openid的值，从而多次拉取列表的方式来满足需求。
     * 具体而言，就是在调用接口时，将上一次调用得到的返回中的next_openid值，作为下一次调用中的next_openid
     * @param token
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static JSONObject getUserList(String token,String nextOpenid) throws IOException, ParseException {
        String url = null;
        if (nextOpenid == null){
            url = QUERY_USERS_LIST_URL.replace("ACCESS_TOKEN", token).replace("NEXT_OPENID","");
        }else{
            url = QUERY_USERS_LIST_URL.replace("ACCESS_TOKEN", token).replace("NEXT_OPENID",nextOpenid);
        }
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null){
            return jsonObject;
        }else {
            return doGetStr(url);
        }
    }


    /**
     * 获取用户基本信息(单个，单日请求上线：500000次，注：与批量请求共500000次)
     *
     * 开发者可通过OpenID来获取用户基本信息。特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，
     * 可通过获取用户基本信息中的unionid来区分用户的唯一性，因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，
     * 用户的unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。
     * @param token
     * @param openId
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static JSONObject getUserInfoByOpenId(String token,String openId) throws IOException, ParseException {
        String url = QUERY_USER_URL.replace("ACCESS_TOKEN",token).replace("OPENID",openId);
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null){
            return jsonObject;
        }else {
            return doGetStr(url);
        }
    }


    /**
     * 批量获取用户信息（每次最多获取一百条，单日请求上线：500000次，与单个请求共500000次）
     *
     * @param token
     * @param userOpenIds
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static JSONObject getUsersInfo(String token,String userOpenIds) throws IOException, ParseException {
        String url = QUERY_BATCHGET_USER_URL.replace("ACCESS_TOKEN",token);
        JSONObject jsonObject = doPostStr(url, userOpenIds);
        if (jsonObject != null){
            return jsonObject;
        }else {
            return doPostStr(url, userOpenIds);
        }
    }


    /**
     * 替换token
     * @param url
     * @param token
     * @return
     */
    public static String replaceToken(String url,String token){
        return url.replace("ACCESS_TOKEN",token);
    }


}
