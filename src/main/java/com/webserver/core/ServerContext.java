package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ���ඨ���йط����������Ϣ
 * @author ta
 *
 */
public class ServerContext {
	/**
	 * servlet��Ӧ��ϵ
	 * key:����·��
	 * value:��Ӧ��Servlet�����ȫ�޶���
	 */
	private static final Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();

	static {
		//��ʼ��
		initServletMapping();
	}
	/**
	 * ��ʼ���������Ӧ��Servlet����
	 */
	private static void initServletMapping() {
//		SERVLET_MAPPING.put("/myweb/reg", "com.webserver.servlet.RegServlet");
//		SERVLET_MAPPING.put("/myweb/login", "com.webserver.servlet.LoginServlet");
		/*
		 * ����conf/servlets.xml
		 * ����Ԫ��<servlets>�µ�����<servlet>Ԫ��ȡ��
		 * ����ÿ��<servlet>Ԫ���е�����:
		 * url��ֵ��Ϊkey,className��ֵ��Ϊvalue
		 * ���浽SERVLET_MAPPING���Map����ɳ�ʼ��
		 */
		try {
			
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/servlets.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("servlet");
			for(Element servletEle : list) {
				String key = servletEle.attributeValue("url");
				String value = servletEle.attributeValue("className");
				SERVLET_MAPPING.put(key, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * ���������ȡ��Ӧ��ҵ�����������
	 * @param url
	 * @return
	 */
	public static String getServletName(String url) {
		return SERVLET_MAPPING.get(url);
	}
	
	public static void main(String[] args) {
		String className = getServletName("/myweb/reg");
		System.out.println(className);
	}
}








