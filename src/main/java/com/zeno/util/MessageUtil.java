package com.zeno.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.thoughtworks.xstream.XStream;
import com.zeno.pojo.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * 消息封装类
 * @author Stephen
 *
 */
public class MessageUtil {

	//文本消息类型
	public static final String MESSAGE_TEXT = "text";

	public static final String MESSAGE_NEWS = "news";
	//图片消息
	public static final String MESSAGE_IMAGE = "image";
	//语音消息
	public static final String MESSAGE_VOICE = "voice";
	//英语消息
	public static final String MESSAGE_MUSIC = "music";
	//视频消息
	public static final String MESSAGE_VIDEO = "video";
	//链接消息
	public static final String MESSAGE_LINK = "link";
	//地理位置消息
	public static final String MESSAGE_LOCATION = "location";
	//事件推送
	public static final String MESSAGE_EVNET = "event";
	//关注
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	//取消关注
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	//菜单点击
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";

	public static final String MESSAGE_SCANCODE= "scancode_push";

	/**
	 * xml转为map集合
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);

		Element root = doc.getRootElement();

		List<Element> list = root.elements();

		for(Element e : list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}


	/**
	 * 将文本消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 组装文本消息
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(System.currentTimeMillis());
		text.setContent(content);
		return textMessageToXml(text);
	}


	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
		sb.append("1、公司介绍\n");
		sb.append("2、产品介绍\n");
		sb.append("回复？调出此菜单。");
		return sb.toString();
	}

	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("公司主要经营...产品，提供...服务，经营理念是...");
		return sb.toString();
	}

	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("本产品主要优势是...,针对不同人群有什么....");
		return sb.toString();
	}


}
