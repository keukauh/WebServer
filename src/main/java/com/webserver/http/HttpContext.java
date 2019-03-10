package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 有关HTTP协议规定的内容
 * @author ta
 *
 */
public class HttpContext {
	/**
	 * 状态代码与描述的对应关系
	 * key:状态代码
	 * value:对应的状态描述
	 */
	private static final Map<Integer,String> STATUS_MAPPING = new HashMap<Integer,String>();

	/**
	 * 资源后缀与Content-Type值的对应关系
	 * key:后缀名，如:png
	 * value:Content-Type对应值，如:image/png
	 */
	private static final Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	
	
	static {
		//初始化
		
		//初始化了状态代码与对应描述
		initStatusMapping();
		//初始化了资源后缀与对应Content-Type的值
		initMimeMapping();
	
	}
	/**
	 * 初始化资源后缀与Content-Type值的对应
	 */
	private static void initMimeMapping() {
//		MIME_MAPPING.put("png", "image/png");
//		MIME_MAPPING.put("gif", "image/gif");
//		MIME_MAPPING.put("jpg", "image/jpeg");
//		MIME_MAPPING.put("css", "text/css");
//		MIME_MAPPING.put("html", "text/html");
//		MIME_MAPPING.put("js", "application/javascript");
		/*
		 * 通过解析web.xml文件初始化MIME_MAPPING
		 * 
		 * 1:创建SAXReader
		 * 2:使用SAXReader读取conf目录中的web.xml文件
		 * 3:获取根元素下所有名字<mime-mapping>的子元素
		 * 4:遍历每个<mime-mapping>元素，并将其子元素:
		 *   <extension>中间的文本作为key
		 *   <mime-type>中间的文本作为value
		 *   保存到MIME_MAPPING这个Map中即可。
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimeList = root.elements("mime-mapping");
			for(Element mimeEle : mimeList) {
				String key = mimeEle.elementText("extension");
				String value = mimeEle.elementText("mime-type");
				MIME_MAPPING.put(key, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("size:"+MIME_MAPPING.size());
		
	}
	
	
	/**
	 * 初始化状态代码与对应的描述
	 */
	private static void initStatusMapping() {
		STATUS_MAPPING.put(200, "OK");
		STATUS_MAPPING.put(201, "Created");
		STATUS_MAPPING.put(202, "Accepted");
		STATUS_MAPPING.put(204, "No Content");
		STATUS_MAPPING.put(301, "Moved Permanently");
		STATUS_MAPPING.put(302, "Moved Temporarily");
		STATUS_MAPPING.put(304, "Not Modified");
		STATUS_MAPPING.put(400, "Bad Request");
		STATUS_MAPPING.put(401, "Unauthorized");
		STATUS_MAPPING.put(403, "Forbidden");
		STATUS_MAPPING.put(404, "Not Found");
		STATUS_MAPPING.put(500, "Internal Server Error");
		STATUS_MAPPING.put(501, "Not Implemented");
		STATUS_MAPPING.put(502, "Bad Gateway");
		STATUS_MAPPING.put(503, "Service Unavailable");
	}
	
	/**
	 * 根据状态代码获取对应的状态描述默认值
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode) {
		return STATUS_MAPPING.get(statusCode);
	}
	
	/**
	 * 根据资源后缀名获取对应的Content-Type的值
	 * @param ext 
	 * @return 
	 */
	public static String getMimeType(String ext) {
		return MIME_MAPPING.get(ext);
	}
	
	
	public static void main(String[] args) {
//		String reason = getStatusReason(404);
//		System.out.println(reason);
		
		String fileName = "xxx.c4p";
		//获取文件名中最后一个"."之后第一个字符的位置
		int index = fileName.lastIndexOf(".")+1;
		//截取到文件末尾(截取出后缀)
		String ext = fileName.substring(index);
		System.out.println("后缀:"+ext);
		String contentType = getMimeType(ext);
		System.out.println(contentType);
	}
	
	
	
}










