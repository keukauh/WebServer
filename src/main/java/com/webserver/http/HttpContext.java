package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * �й�HTTPЭ��涨������
 * @author ta
 *
 */
public class HttpContext {
	/**
	 * ״̬�����������Ķ�Ӧ��ϵ
	 * key:״̬����
	 * value:��Ӧ��״̬����
	 */
	private static final Map<Integer,String> STATUS_MAPPING = new HashMap<Integer,String>();

	/**
	 * ��Դ��׺��Content-Typeֵ�Ķ�Ӧ��ϵ
	 * key:��׺������:png
	 * value:Content-Type��Ӧֵ����:image/png
	 */
	private static final Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	
	
	static {
		//��ʼ��
		
		//��ʼ����״̬�������Ӧ����
		initStatusMapping();
		//��ʼ������Դ��׺���ӦContent-Type��ֵ
		initMimeMapping();
	
	}
	/**
	 * ��ʼ����Դ��׺��Content-Typeֵ�Ķ�Ӧ
	 */
	private static void initMimeMapping() {
//		MIME_MAPPING.put("png", "image/png");
//		MIME_MAPPING.put("gif", "image/gif");
//		MIME_MAPPING.put("jpg", "image/jpeg");
//		MIME_MAPPING.put("css", "text/css");
//		MIME_MAPPING.put("html", "text/html");
//		MIME_MAPPING.put("js", "application/javascript");
		/*
		 * ͨ������web.xml�ļ���ʼ��MIME_MAPPING
		 * 
		 * 1:����SAXReader
		 * 2:ʹ��SAXReader��ȡconfĿ¼�е�web.xml�ļ�
		 * 3:��ȡ��Ԫ������������<mime-mapping>����Ԫ��
		 * 4:����ÿ��<mime-mapping>Ԫ�أ���������Ԫ��:
		 *   <extension>�м���ı���Ϊkey
		 *   <mime-type>�м���ı���Ϊvalue
		 *   ���浽MIME_MAPPING���Map�м��ɡ�
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
	 * ��ʼ��״̬�������Ӧ������
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
	 * ����״̬�����ȡ��Ӧ��״̬����Ĭ��ֵ
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode) {
		return STATUS_MAPPING.get(statusCode);
	}
	
	/**
	 * ������Դ��׺����ȡ��Ӧ��Content-Type��ֵ
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
		//��ȡ�ļ��������һ��"."֮���һ���ַ���λ��
		int index = fileName.lastIndexOf(".")+1;
		//��ȡ���ļ�ĩβ(��ȡ����׺)
		String ext = fileName.substring(index);
		System.out.println("��׺:"+ext);
		String contentType = getMimeType(ext);
		System.out.println(contentType);
	}
	
	
	
}










