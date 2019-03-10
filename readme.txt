基于Tomcat实现的简易服务器WebServer
安装环境 JDK1.8 maven项目
第三方jar包 dom4j

|-com.webserver
    |-core核心包
        |-WebServer 启动类
        |-ClientHandler 实现线程，用于处理客户端交互
          |-ServerContext 有关服务端配置信息
    |-http协议处理包
        |-EmptyRequestException  空请求异常，当实例化HttpRequest时若为空请求时会抛出该异常
        |-HttpContext 有关HTTP协议规定的内容 
        |-HttpRequest 该类的每个实例用于表示客户端浏览器发送过来的一个具体的请求信息。
        |-HttpResponse 该类的每个实例表示服务端发送给客户端的一个具体HTTP响应内容
    |-servlet包扩展服务器功能
        |-HttpServlet 所有Servlet的超类
          |-LoginServlet 登录
          |-RegServlet 注册
          |-UpdateServlet 修改