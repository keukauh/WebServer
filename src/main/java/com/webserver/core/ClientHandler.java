package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;

/**
 * ���ڴ���ͻ��˽���
 * @author ta
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		System.out.println("ClientHandler:��ʼ��������");	
		try {
			/*
			 * ClientHandler����ͻ������������������:
			 * 1:׼������
			 * 2:��������
			 * 3:������Ӧ
			 */
			/*
			 * 1.1 ��������Ĺ���
			 * ʵ����HttpRequest��ͬʱ����Socket���룬�Ա�
			 * HttpRequest���Ը���Socket��ȡ����������ȡ�ͻ�
			 * �˷��͹������������ݡ�
			 * 
			 * 1.2������Ӧ����
			 */
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			
			/*
			 * 2 ��������Ĺ���
			 * ��ȡ�����е�����·��(requestURI)
			 * ������ܴ�������:
			 * ����̬��Դ:
			 * ʹ��requestURI�ӱ�����������Ӧ�õ�webappsĿ¼��
			 * Ѱ�Ҷ�Ӧ��Դ��
			 * 
			 * ����ĳ��ҵ��:
			 * ����requestURI�����ֵ����������������ĸ�ҵ
			 * �񣬴Ӷ����ö�Ӧ��ҵ����������ɸ�ҵ��Ĵ���
			 * 
			 * 
			 */
			String url = request.getRequestURI();
			/*
			 * ���������ж��Ƿ�Ϊҵ��
			 */
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null) {
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
				
			}else {
				File file = new File("webapps"+url);
				if(file.exists()) {
					System.out.println("����Դ���ҵ���");
					response.setEntity(file);			
				}else {
					System.out.println("����Դ������!404!");
					//��Ӧ404״̬�����Լ�404ҳ��
					File notFoundPage = new File("webapps/root/404.html");
					response.setStatusCode(404);
					response.setEntity(notFoundPage);
				}
			}	
				
			/*
			 * 3 ��Ӧ�ͻ���
			 */
			response.flush();
			
		} catch(EmptyRequestException e) {
			System.out.println("������.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//��ͻ��˶Ͽ�����
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
	}

}


