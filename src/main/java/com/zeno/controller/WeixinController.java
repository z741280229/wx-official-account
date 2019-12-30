package com.zeno.controller;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.zeno.pojo.TextMessage;
import com.zeno.util.CheckUtil;
import com.zeno.util.MessageUtil;
import com.zeno.util.MultipartFileToFile;
import com.zeno.util.WeixinUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-23 14:44
 **/
@RestController
public class WeixinController {


    @Value("${wx-office-account-token}")
    private String token;

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

            if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
                if("1".equals(content)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
                }else if("2".equals(content)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondMenu());
                }else if ("3".equals(content)){
                    message = MessageUtil.initNewsMessage(toUserName,fromUserName);
                }else if("4".equals(content)){
                    message = MessageUtil.initImageMessage(toUserName,fromUserName);
                }else if ("5".equals(content)){
                    message = MessageUtil.initMusicMessage(toUserName,fromUserName);
                } else if("?".equals(content) || "？".equals(content)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }
            }else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){
                String eventType = map.get("Event");
                if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
                    String url = map.get("EventKey");
                    message = MessageUtil.initText(toUserName, fromUserName, url);
                }else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
                    String key = map.get("EventKey");
                    message = MessageUtil.initText(toUserName, fromUserName, key);
                }
            }else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
                String label = map.get("Label");
                message = MessageUtil.initText(toUserName, fromUserName, label);
            }
            //System.out.println(message);
            //out.print(message);
        } catch (IOException | DocumentException  e ) {
            e.printStackTrace();
        }finally {
            out.close();
        }
    }


    /**
     * 上传图片并获取
     * @param multfile
     * @return
     * @throws Exception
     */
    @PostMapping("getPicUrl")
    @ResponseBody
    public String getPicUrl(@RequestParam("file") MultipartFile multfile,@RequestParam("type")String type) throws Exception {
        File file = MultipartFileToFile.multipartFileToFile(multfile);

        String url = WeixinUtil.UPLOAD_URL.replace("ACCESS_TOKEN", token).replace("TYPE",type);
        JSONObject jsonObject = WeixinUtil.upload(file,url);
        String typeName = "media_id";
        if(!"image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObject.getString(typeName);
        MultipartFileToFile.delteTempFile(file);
        return mediaId;
    }


    /**
     * 上传图文消息内的图片获取URL【订阅号与服务号认证后均可用】
     * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param multfile
     * @return
     */
    @PostMapping("media/uploadimg")
    public JSONObject uploading(@RequestParam("file") MultipartFile multfile){
        File file = null;
        try {
            file = MultipartFileToFile.multipartFileToFile(multfile);
            String url = WeixinUtil.replaceToken("https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN", this.token);
            JSONObject jsonObject = WeixinUtil.upload(file, url);
            MultipartFileToFile.delteTempFile(file);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 上传图文消息素材【订阅号与服务号认证后均可用】
     * @param jsonObject
     * @return
     */
    @PostMapping("media/uploadnews")
    public JSONObject uploadNews(@RequestBody JSONObject jsonObject){
        String url = WeixinUtil.replaceToken("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN", token);
        try {
            return WeixinUtil.doPostStr(url, JSONObject.toJSONString(jsonObject));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     * @param jsonObject
     * @return
     */
    @PostMapping("/message/mass/send")
    public JSONObject sendMessage(@RequestBody JSONObject jsonObject){
        String url = WeixinUtil.replaceToken("https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN",token);
        try {
            return  WeixinUtil.doPostStr(url, JSONObject.toJSONString(jsonObject));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
