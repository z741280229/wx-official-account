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

	public static final String MESSAGE_TEXT = "text";

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
}
