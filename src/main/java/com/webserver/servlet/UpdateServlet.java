package com.webserver.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
/**
 * �޸�����
 * @author ta
 *
 */
public class UpdateServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("UpdateServlet:��ʼ�޸�����!");
		//��ȡ�û�����
		String username = request.getParameter("username");
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		//�޸�����
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","rw")
		){
			boolean update = false;//�Ƿ��޸ĳɹ�
			for(int i=0;i<raf.length()/100;i++) {
				//�ƶ�ָ�뵽������¼��ʼλ��
				raf.seek(i*100);
				
				//��ȡ�û���
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data,"UTF-8").trim();
				//�ҵ����û�
				if(name.equals(username)) {
					//������
					raf.read(data);
					String pwd = new String(data,"UTF-8").trim();
					if(pwd.equals(oldpwd)) {
						//��������
						//1�Ƚ�ָ���ƶ�������λ��
						raf.seek(i*100+32);
						//2����д����
						data = newpwd.getBytes("UTF-8");
						data = Arrays.copyOf(data, 32);
						raf.write(data);
						update = true;
					}
					break;
				}	
			}
		
			if(update) {
				forward("myweb/update_success.html", request, response);
			}else {
				forward("myweb/update_fail.html", request, response);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}








