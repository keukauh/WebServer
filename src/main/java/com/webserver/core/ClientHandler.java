package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;

/**
 * 用于处理客户端交互
 * @author ta
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		System.out.println("ClientHandler:开始处理请求");	
		try {
			/*
			 * ClientHandler处理客户端请求大致做三件事:
			 * 1:准备工作
			 * 2:处理请求
			 * 3:发送响应
			 */
			/*
			 * 1.1 解析请求的过程
			 * 实例化HttpRequest的同时，将Socket传入，以便
			 * HttpRequest可以根据Socket获取输入流，读取客户
			 * 端发送过来的请求内容。
			 * 
			 * 1.2创建响应对象
			 */
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			
			/*
			 * 2 处理请求的过程
			 * 获取请求中的请求路径(requestURI)
			 * 请求可能存在两种:
			 * 请求静态资源:
			 * 使用requestURI从保存所有网络应用的webapps目录下
			 * 寻找对应资源。
			 * 
			 * 请求某个业务:
			 * 根据requestURI具体的值来分析其请求的是哪个业
			 * 务，从而调用对应的业务处理类来完成该业务的处理。
			 * 
			 * 
			 */
			String url = request.getRequestURI();
			/*
			 * 根据请求判断是否为业务
			 */
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null) {
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
				
			}else {
				File file = new File("webapps"+url);
				if(file.exists()) {
					System.out.println("该资源已找到！");
					response.setEntity(file);			
				}else {
					System.out.println("该资源不存在!404!");
					//响应404状态代码以及404页面
					File notFoundPage = new File("webapps/root/404.html");
					response.setStatusCode(404);
					response.setEntity(notFoundPage);
				}
			}	
				
			/*
			 * 3 响应客户端
			 */
			response.flush();
			
		} catch(EmptyRequestException e) {
			System.out.println("空请求.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//与客户端断开连接
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
	}

}


