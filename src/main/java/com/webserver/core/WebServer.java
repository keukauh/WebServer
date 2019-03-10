package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer主类
 * @author ta
 *
 */
public class WebServer {
	private ServerSocket server;
	//线程池
	private ExecutorService threadPool;
	/**
	 * 构造方法，初始化使用
	 */
	public WebServer() {
		try {
			server = new ServerSocket(8088);
			threadPool = Executors.newFixedThreadPool(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 服务端开始工作的方法
	 */
	public void start() {
		try {
			while(true) {
				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
				System.out.println("一个客户端连接了!");
				//启动一个线程处理该客户端交互
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
				
//				Thread t = new Thread(handler);
//				t.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}









