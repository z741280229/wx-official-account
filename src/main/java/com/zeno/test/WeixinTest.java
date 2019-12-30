package com.zeno.test;

import com.alibaba.fastjson.JSONObject;
import com.zeno.pojo.AccessToken;
import com.zeno.pojo.OpenId;
import com.zeno.pojo.OpenIds;
import com.zeno.push.MessagePush;
import com.zeno.util.WeixinUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-24 15:16
 **/
public class WeixinTest {
    public static void main(String[] args) {
        AccessToken token = new AccessToken();
        try {
            token = WeixinUtil.getAccessToken();

            token.setToken("28_R3yU39WNUDLUK0adUgsAuyKVgTGdEeqbneBZu-P5PhU2d6UbUNli2kCQSXPR3RltosGVUmN0j4u0W6a9TtrLFGedW-OobFYAnrv4_W6II13OU3LUdqjbTqBT6JHkkN6HMRYMTGk3vUOe_99zYKBdABAHTK");
            token.setExpiresIn(7200);

            /*System.out.println("票据"+token.getToken());
            System.out.println("有效时间"+token.getExpiresIn());*/



           /* String path = "C:/Users/Ethan/Desktop/51b156b1b8aeb.jpg";
            String mediaId = WeixinUtil.upload(path, token.getToken(), "image");
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


           /*int result = WeixinUtil.deleteMenu(token.getToken());
           if (result == 0){
               System.out.println("菜单删除成功");
           }else {
               System.out.println("菜单删除失败");
           }*/

            /*JSONObject userList = WeixinUtil.getUserList(token.getToken(),null);
            System.out.println(userList);*/

            /*JSONObject jsonObject = WeixinUtil.getUserInfoByOpenId(token.getToken(), "o8nzI0XTOHUXIiQsVdcVhw40XKp4");
            System.out.println(jsonObject);*/

           /* List<OpenId> openIds = new ArrayList<>();
            OpenId openId1 = new OpenId("o8nzI0XTOHUXIiQsVdcVhw40XKp4","zh_CN");
            OpenId openId2 = new OpenId("o8nzI0QsSm5FqaB9j5JFC7GIGN9s","zh_CN");
            OpenId openId3 = new OpenId("o8nzI0RdBgleleG_I16CD-1R1OR0","zh_CN");
            OpenId openId4 = new OpenId("o8nzI0UX5odoaFnaJ_kjXopmIEHA","zh_CN");
            OpenId openId5 = new OpenId("o8nzI0fg4QqcsBUg00vofp4JTBhI","zh_CN");
            openIds.add(openId1);
            openIds.add(openId2);
            openIds.add(openId3);
            openIds.add(openId4);
            openIds.add(openId5);
            OpenIds openIdsList = new OpenIds();
            openIdsList.setUser_list(openIds);
            String ops = JSONObject.toJSONString(openIdsList);
            JSONObject userList = WeixinUtil.getUsersInfo(token.getToken(), ops);
            System.out.println(userList);*/

            /*String path = "C:/Users/Ethan/Desktop/30-1P613192038.jpg";
            String url = MessagePush.getPicUrl(path,token.getToken(),"thumb");
            System.out.println(url);*/


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
