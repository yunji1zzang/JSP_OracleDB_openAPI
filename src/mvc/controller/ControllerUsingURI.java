package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import mvc.command.NullHandler;

// 설정 파일을 이용해서 핸들러 객체를 생성하는 기능을 구현하는 컨트롤러 서블릿 코딩
public class ControllerUsingURI extends HttpServlet {

	// <커맨드, 핸들러인스턴스> 매핑 정보 저장
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<String, CommandHandler>();

	@Override
	public void init() throws ServletException {
		// configFile 초기화 파라미터 값을 읽어옴
		String configFile = getInitParameter("configFile");
		// Properties 객체를 참조하는 객체 참조 변수 prop에 Properties를 생성
		Properties prop = new Properties();
		// 설정 파일 경로를 지정
		String configFilePath = getServletContext().getRealPath(configFile);
		
		// 설정 파일로부터 매핑 정보를 읽어와서 Properties 객체에 저장
		// Properties는 목록(이름, 값)을 갖는 클래스로서 프로퍼티 이름을 커맨드 이름으로 사용하고 값을 클래스 이름으로 사용
		try (FileReader fr = new FileReader(configFilePath)){
			prop.load(fr);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		// Properties에 저장된 각 프로퍼티의 키에 대해 처리 작업을 반복
		Iterator keyIter = prop.keySet().iterator();
		while (keyIter.hasNext()) {
			// 프로퍼티 이름을 커맨드 이름으로 사용
			String command = (String) keyIter.next();
			// 커맨드 이름에 해당하는 핸들러 클래스 이름(handlerClassName)을 Properties에서 구함
			String handlerClassName = prop.getProperty(command);
			try {
				// 핸들러 클래스 이름(handlerClassName)을 이용해서 Class 객체를 구함
				Class<?> handlerClass = Class.forName(handlerClassName);
				// 앞서 구했던 handlerClass로부터 핸들러 객체를 생성
				// 즉, 앞서 구했던 핸들러 클래스 이름(handlerClassName)에 해당하는 클래스의 객체를 생성
				CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance();
				// commandHandlerMap에 (커맨드, 핸들러객체) 매핑 정보를 저장
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new ServletException(e);
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 클라이언트가 요청한 명령어를 구함
		// 예제에서는 URI 파라미터값을 명령어로 사용
		String command = request.getRequestURI();
		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
		}

		// commandHandlerMap에서 요청을 처리할 핸들러 객체를 구함
		// cmd 파라미터 값을 키로 사용
		CommandHandler handler = commandHandlerMap.get(command);
		// 명령어에 해당하는 핸들러 객체가 존재하지 않을 경우, NullHandler를 사용
		if (handler == null) {
			// NullHandler 클래스는 404 에러를 응답으로 전송하는 핸들러 클래스
			handler = new NullHandler();
		}
		
		String viewPage = null;
		try {
			// 앞서 핸들러 객체의 process() 메서드를 호출해서 요청을 처리하고, 결과로 보여줄 뷰 페이지 경로를 리턴값으로 전달
			// 핸들러 인스턴스인 handler의 process() 메서드는  클라이언트의 요청을 알맞게 처리한 후,
			// 뷰 페이지에 보여줄 결과값을 request(또는 session)의 속성에 저장
			viewPage = handler.process(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		// viewPage가 null이 아닌 경우 핸들러 인스턴스가 리턴한 뷰 페이지 viewPage로 이동
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
}
