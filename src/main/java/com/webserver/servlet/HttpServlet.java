package com.webserver.servlet;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ����Servlet�ĳ���
 * @author ta
 *
 */
public abstract class HttpServlet {
	/**
	 * ҵ������
	 * @param request
	 * @param response
	 */
	public abstract void service(HttpRequest request,HttpResponse response);
	
	/**
	 * ��תָ��·����Ӧ����Դ
	 * ʵ�ʸ÷�������(Tomcat)��ת�����У�����ͨ��request��ȡ
	 * @param path
	 * @param request
	 * @param response
	 */
	public void forward(String path,HttpRequest request,HttpResponse response) {
		File file = new File("webapps/"+path);
		response.setEntity(file);
	}
}









