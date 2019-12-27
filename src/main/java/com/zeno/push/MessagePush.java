package com.zeno.push;

import com.zeno.util.WeixinUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @program: wx-official-account
 * @description:消息推送
 * @author: Zeno
 * @create: 2019-12-27 13:53
 **/
public class MessagePush {

    private static final String PIC_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";

    /**
     * 获取图片路径
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws IOException
     */
    public static String getPicUrl(String filePath, String accessToken,String type) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
       return null;
    }


}
