package com.zeno.controller;

import com.zeno.pojo.TextMessage;
import com.zeno.util.CheckUtil;
import com.zeno.util.MessageUtil;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-23 14:44
 **/
@RestController
public class WeixinController {

    /**
     * 与微信建立连接
     * @param req
     * @param resp
     */
    @GetMapping("wx")
    public void weixinGet(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入get......");
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            if(CheckUtil.checkSignature(signature, timestamp, nonce)){
                out.print(echostr);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            out.close();
        }
    }

    /**
     * 接受客户端数据，并响应
     * @param req
     * @param resp
     */
    @PostMapping("wx")
    public void weixin(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入post......");
        PrintWriter  out = null;
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            out =  resp.getWriter();
            Map<String, String> map = MessageUtil.xmlToMap(req);

            //请求方（关注公众号账号）
            String fromUserName = map.get("FromUserName");

            //被请求方（公众号服务端）
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            String msgId = map.get("MsgId");

            String message = null;

            if ("text".equals(msgType)){
                TextMessage text = new TextMessage();
                //处理发送，将信息发送给客户端（关注公众号的用户）
                text.setFromUserName(toUserName);
                text.setMsgType(msgType);
                //发送方，公众号服务端
                text.setToUserName(fromUserName);
                text.setCreateTime(System.currentTimeMillis());
                text.setContent(content);
                text.setMsgId(msgId);
                message = MessageUtil.textMessageToXml(text);
            }
            out.print(message);
        } catch (IOException | DocumentException  e ) {
            e.printStackTrace();
        }finally {
            out.close();
        }


    }
}
