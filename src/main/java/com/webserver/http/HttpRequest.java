package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * �������
 * �����ÿ��ʵ�����ڱ�ʾ�ͻ�����������͹�����һ������
 * ��������Ϣ��
 * һ���������������:�����У���Ϣͷ����Ϣ����
 * @author ta
 *
 */
public class HttpRequest {
	//�����������Ϣ����
	//����ʽ
	private String method;
	//��Դ·��
	private String url;
	//Э��汾
	private String protocol;
	
	/*
	 * url�е����󲿷�
	 */
	private String requestURI;
	/*
	 * url�еĲ�������
	 */
	private String queryString;
	/*
	 * ���в���
	 * key:������
	 * value:����ֵ
	 * 
	 */
	private Map<String,String> parameters = new HashMap<String,String>();
	
	//��Ϣͷ�����Ϣ����
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	//��Ϣ���������Ϣ����
	
	/*
	 * �Ϳͻ���������ص�����
	 */
	private Socket socket;
	private InputStream in;
	
	/**
	 * ���췽����������ʼ���������
	 * ��ʼ�����ǽ�������Ĺ��̡���������Socket��ȡ
	 * ��������������ȡ�ͻ��˷��͹������������ݣ�������
	 * �������������õ��������Ķ�Ӧ�����ϡ�
	 * @throws EmptyRequestException 
	 * 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			//ͨ��socket��ȡ�����������ڶ�ȡ�ͻ��˷��͵���������
			this.in = socket.getInputStream();
			/*
			 * ��������������Ҫ��������:
			 * 1:��������������
			 * 2:������Ϣͷ����
			 * 3:������Ϣ��������
			 */
			//1
			parseRequestLine();
			//2
			parseHeaders();
			//3
			parseContent();
		} catch(EmptyRequestException e) {
			//�������׸�ClientHandler
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����������
	 * @throws EmptyRequestException 
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("HttpRequest:����������...");
		/*
		 * ��ͨ����������ȡ��һ���ַ���(CRLF��β)����Ϊһ��������
		 * ��һ�����ݾ�������������
		 */
		String line = readLine();
		
		//�Ƿ�Ϊ������
		if("".equals(line)) {
			throw new EmptyRequestException();
		}
		
		
		System.out.println("������:"+line);
		/*
		 * ���������е���������Ϣ:
		 * method url protocl
		 * ��ȡ�����������õ���Ӧ��������
		 * 
		 * GET /index.html HTTP/1.1
		 */
		
		
		String[] data = line.split("\\s");
		method = data[0];
		url = data[1];
		protocol = data[2];
		
		//��һ������URL
		parseURL();
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);
		System.out.println("HttpRequest:�������������");
	}
	/**
	 * ��һ������URL
	 */
	private void parseURL() {
		System.out.println("HttpRequest:��һ������url...");
		//�жϵ�ǰ������url�Ƿ��в���?
		if(url.indexOf("?")!=-1) {
			String[] data = url.split("\\?");
			this.requestURI = data[0];
			//�ж�"?"�����Ƿ���ʵ�ʵĲ�������
			if(data.length>1) {
				this.queryString = data[1];
				try {
					parseParameters(this.queryString);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}else {
			this.requestURI = url;
		}
		
		
		
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		
		System.out.println("HttpRequest:����url���!");
	}
	/**
	 * ��������
	 * @param line
	 * @throws UnsupportedEncodingException 
	 */
	private void parseParameters(String line) throws UnsupportedEncodingException {
		//�ȶԲ����к���"%XX"����ת��
		line = decode(line);	
		//��һ����ֲ���
		String[] paraArr = line.split("&");
		//����ÿһ���������в�ֲ������Ͳ���ֵ
		for(String para : paraArr) {
			//ÿ����������"="���
			String[] arr = para.split("=");
			if(arr.length>1) {
				this.parameters.put(arr[0],arr[1]);
			}else {
				this.parameters.put(arr[0],null);
			}
		}
	}
	
	/**
	 * �Ը����ַ�����"%XX"�����ݽ���
	 * @param line ����%XX���ַ���
	 * @return ��line��%XX�����滻Ϊԭ�Ⲣ����
	 * @throws UnsupportedEncodingException 
	 */
	private String decode(String line) throws UnsupportedEncodingException {
		return URLDecoder.decode(line, "UTF-8");
	}
	
	
	/**
	 * ������Ϣͷ
	 */
	private void parseHeaders() {
		System.out.println("HttpRequest:������Ϣͷ...");
		/*
		 * ʵ��˼·
		 * ������Ϣͷ���ɶ��й��ɵģ��Դ�����Ӧ��ѭ����
		 * ����readLine������ȡÿһ��(ÿһ����Ϣͷ)����
		 * readLine�������ص���һ�����ַ���ʱ��˵��Ӧ��
		 * ������ȡ����CRLF,��ͱ�ʾ������Ϣͷ����ȡ���
		 * �ˣ���ô��Ӧ��ֹͣ��ȡ�����ˡ�
		 * ���������ڶ�ȡÿ�У���:ÿ����Ϣͷ��Ӧ������
		 * ��Ϣͷ����ð�ſո���Ϊ����(��Ϣͷ��ʽΪname: value)
		 * ��һ��Ӧ������Ϣͷ���֣��ڶ���Ϊ��Ϣͷ��ֵ������
		 * �ֱ�������key��value���뵽����headers���map
		 * �С������������վ�����˽�����Ϣͷ�Ĺ�����
		 */
		while(true) {
			String line = readLine();
			if("".equals(line)) {
				break;
			}
			String[] data = line.split(": ");
			headers.put(data[0], data[1]);
		}
		System.out.println("headers:"+headers);
		System.out.println("HttpRequest:������Ϣͷ���");
	}
	/**
	 * ������Ϣ����
	 * @throws IOException 
	 */
	private void parseContent() throws IOException {
		System.out.println("HttpRequest:������Ϣ����...");
		/*
		 * �ж��Ƿ�����Ϣ����:
		 * �鿴��Ϣͷ���Ƿ���Content-Length
		 */
		if(headers.containsKey("Content-Length")) {
			int len = Integer.parseInt(headers.get("Content-Length"));
			byte[] data = new byte[len];
			in.read(data);
			/*
			 * �ж���Ϣ���ĵ���������:
			 * �鿴��ϢͷContent-Type��ֵ
			 */
			String contentType = headers.get("Content-Type");
			//�Ƿ�Ϊform��
			if("application/x-www-form-urlencoded".equals(contentType)) {
				String line = new String(data,"ISO8859-1");
				System.out.println("��������:"+line);
				/*
				 * �������������������뵽����parameters��
				 */
				try {
					parseParameters(line);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		
		
		
		System.out.println("HttpRequest:������Ϣ�������...");
	}
	
	/**
	 * ͨ��������in��ȡһ���ַ�����
	 * ������ȡ�����ַ�������ȡ��CRLFʱֹͣ������֮ǰ
	 * ��ȡ�������ַ���һ���ַ�����ʽ���ء����ص��ַ���
	 * �в���������CRLF��
	 * CR:�س���  ��ӦASC����ֵ13
	 * LF:���з�  ��ӦASC����ֵ10
	 * @return
	 */
	private String readLine() {
		StringBuilder builder = new StringBuilder();
		try {
			int pre = -1;//��¼�ϴζ�ȡ�����ַ�
			int cur = -1;//��¼���ζ�ȡ�����ַ�
			while((cur = in.read())!=-1) {
				//�ж��ϴζ�ȡ���Ƿ�ΪCR�������Ƿ�ΪLF
				if(pre==13&&cur==10) {
					break;
				}
				builder.append((char)cur);
				pre = cur;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString().trim();
	}
	
	
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return this.parameters.get(name);
	}
	
	
	
}













