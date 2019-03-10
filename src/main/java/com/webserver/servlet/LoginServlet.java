package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * �����¼ҵ��
 * @author ta
 *
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		System.out.println("��ʼ��¼...");
		//1��ȡ�û���¼��Ϣ
		String username = request.getParameter("username");
		String password = request.getParameter("password");   
		
		//2��¼��֤
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","r");
		){
			//Ĭ�ϱ�ʾ��¼ʧ��
			boolean check = false;
			
			for(int i=0;i<raf.length()/100;i++) {
				//�ֽ�ָ���ƶ���������¼�Ŀ�ʼλ��
				raf.seek(i*100);
				//��ȡ�û���
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data,"UTF-8").trim();
				//�Ƿ�Ϊ���û�
				if(name.equals(username)) {
					//��ȡ����
					raf.read(data);
					String pwd = new String(data,"UTF-8").trim();
					if(pwd.equals(password)) {
						//��¼�ɹ�
						forward("myweb/login_success.html", request, response);
						check = true;
					}
					break;
				}
			}
			//�ж��Ƿ��¼�ɹ�
			if(!check) {
				forward("myweb/login_fail.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("��¼���!");
	}
}







