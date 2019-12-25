package com.zeno.test;

import com.zeno.pojo.AccessToken;
import com.zeno.util.WeixinUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-24 15:16
 **/
public class WeixinTest {
    public static void main(String[] args) {
        AccessToken token = null;
        try {
            token = WeixinUtil.getAccessToken();
            System.out.println("票据"+token.getToken());
            System.out.println("有效时间"+token.getExpiresIn());

            String path = "C:/Users/Ethan/Desktop/30-1P613192038.jpg";
            String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
            System.out.println(mediaId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
