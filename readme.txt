����Tomcatʵ�ֵļ��׷�����WebServer
��װ���� JDK1.8 maven��Ŀ
������jar�� dom4j

|-com.webserver
    |-core���İ�
        |-WebServer ������
        |-ClientHandler ʵ���̣߳����ڴ���ͻ��˽���
          |-ServerContext �йط����������Ϣ
    |-httpЭ�鴦���
        |-EmptyRequestException  �������쳣����ʵ����HttpRequestʱ��Ϊ������ʱ���׳����쳣
        |-HttpContext �й�HTTPЭ��涨������ 
        |-HttpRequest �����ÿ��ʵ�����ڱ�ʾ�ͻ�����������͹�����һ�������������Ϣ��
        |-HttpResponse �����ÿ��ʵ����ʾ����˷��͸��ͻ��˵�һ������HTTP��Ӧ����
    |-servlet����չ����������
        |-HttpServlet ����Servlet�ĳ���
          |-LoginServlet ��¼
          |-RegServlet ע��
          |-UpdateServlet �޸�