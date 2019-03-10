package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ���ڴ���ע��ҵ��
 * @author ta
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		/*
		 * ע������:
		 * 1:ͨ��request��ȡ�û��ύ��ע����Ϣ
		 * 2:���û���Ϣд���ļ�user.dat��
		 * 3:����response��Ӧ��ע����ҳ��
		 */
		System.out.println("RegServlet:��ʼע��...");
		//1
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(
			request.getParameter("age")
		);
		System.out.println(username+","+password+","+nickname+","+age);
		/*
		 * 2
		 * �����û��������룬�ǳ�Ϊ�ַ���������Ϊ����
		 * �������ÿ����¼ռ100�ֽ�
		 * �û��������룬�ǳƸ�վ32�ֽڣ�����̶�4�ֽڡ�
		 * 
		 */
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","rw")
		){
			raf.seek(raf.length());		
			//д�û���
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			//д����
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			//д�ǳ�
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			//д����
			raf.writeInt(age);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//3��Ӧ�û�ע��ɹ�
		forward("myweb/reg_success.html", request, response);
		System.out.println("RegServlet:ע�����!");
	}
}







