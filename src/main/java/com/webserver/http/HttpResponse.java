package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * ��Ӧ����
 * �����ÿ��ʵ����ʾ����˷��͸��ͻ��˵�һ������HTTP��Ӧ
 * ����
 * һ��HTTP��Ӧ����������:
 * ״̬�У���Ӧͷ����Ӧ����
 * 
 * @author ta
 *
 */
public class HttpResponse {
	//״̬�������Ϣ����
	//״̬����,Ĭ��Ϊ:200
	private int statusCode = 200;
	//״̬����,Ĭ��Ϊ:"OK"
	private String statusReason = "OK";
	
	//��Ӧͷ�����Ϣ����
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	
	//��Ӧ���������Ϣ����
	//��Ӧ��ʵ���ļ�
	private File entity;
	
	//�����������Ϣ
	private Socket socket;
	private OutputStream out;
	
	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ǰ��Ӧ�������ݰ��ձ�׼��HTTP��Ӧ��ʽ���͸�
	 * �ͻ��ˡ�
	 */
	public void flush() {
		/*
		 * 1:����״̬��
		 * 2:������Ӧͷ
		 * 3:������Ӧ����
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	private void sendStatusLine() {
		System.out.println("HttpResponse:��ʼ����״̬��...");
		try {
			String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);//written CR
			out.write(10);//written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:����״̬�����");
	}
	private void sendHeaders() {
		System.out.println("HttpResponse:��ʼ������Ӧͷ...");
		try {
			//����headers����ÿ����Ӧͷ����
			Set<Entry<String,String>> entrySet
									= headers.entrySet();
			for(Entry<String,String> header : entrySet) {
				String key = header.getKey();
				String value = header.getValue();
				String line = key+": "+value;
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);//written CR
				out.write(10);//written LF
			}
			
			
			//��������һ��CRLF��ʾ��Ӧͷ�������
			out.write(13);//written CR
			out.write(10);//written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:������Ӧͷ���");
	}
	private void sendContent() {
		System.out.println("HttpResponse:��ʼ������Ӧ����...");
		if(entity==null) {
			return;
		}
		try(
			FileInputStream fis = new FileInputStream(entity);	
		){
			int len = -1;
			byte[] data = new byte[1024*10];
			while((len = fis.read(data))!=-1) {
				out.write(data,0,len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:������Ӧ�������");
	}

	public File getEntity() {
		return entity;
	}
	/**
	 * ������Ӧ�����е�ʵ���ļ�
	 * �����ø��ļ���ͬʱ�����Զ����ݸ��ļ��������
	 * ��Ӧͷ:Content-Type��Content-Length
	 * @param entity
	 */
	public void setEntity(File entity) {
		/* 
		 * ���Content-Type
		 * ���ݸ������ļ������ֵĺ�׺����HttpContext
		 * �л�ȡ��Ӧ��Content-Typeֵ
		 */
		String fileName = entity.getName();
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String contentType =HttpContext.getMimeType(ext);
		this.headers.put("Content-Type", contentType);
		
		//���Content-Length
		this.headers.put("Content-Lenghth", entity.length()+"");
		
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * ����״̬����
	 * �����õ�ͬʱ���ڲ�����ݸ�״̬����ȥHttpContext��
	 * ��ȡ�ô����Ӧ��״̬����ֵ���Զ��������á�
	 * ��������ʡȥ�����ÿ������״̬�����Ҫ��������״̬
	 * ���������á�
	 * ������Ҫ���ô���������ò�ͬ��״̬����ֵ������Ͳ���
	 * �ٵ���setStatusReason�����ˡ�
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		//�Զ����ö�Ӧ��״̬����
		this.statusReason 
			= HttpContext.getStatusReason(statusCode);
		
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	/**
	 * ���ָ������Ӧͷ
	 * @param name
	 * @param value
	 */
	public void putHeader(String name,String value) {
		this.headers.put(name, value);
	}
	
	public String getHeader(String name) {
		return this.headers.get(name);
	}
	
	
}









