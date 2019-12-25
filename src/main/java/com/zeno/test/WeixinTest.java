package com.zeno.test;

import com.alibaba.fastjson.JSONObject;
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

            /*String path = "C:/Users/Ethan/Desktop/30-1P613192038.jpg";
            String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
            System.out.println(mediaId);*/
            /*String meun = JSONObject.toJSONString(WeixinUtil.initMenu());
            int result = WeixinUtil.createMenu(token.getToken(),meun);
            if (result == 0){
                System.out.println("创建菜单成功");
            }else{
                System.out.println("错误码：" + result);
            }*/
           /*JSONObject jsonObject =  WeixinUtil.queryMenu(token.getToken());
           System.out.println(jsonObject);*/
           int result = WeixinUtil.deleteMenu(token.getToken());
           if (result == 0){
               System.out.println("菜单删除成功");
           }else {
               System.out.println("菜单删除失败");
           }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
