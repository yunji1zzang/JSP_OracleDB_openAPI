package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 모든 명령어 핸들러가 동일 인터페이스를 상속받아 구현하는 형태를 사용
// 이때, 사용할 공통 인터페이스가 CommandHandler 인터페이스
// CommandHandler 인터페이스를 정의
public interface CommandHandler {

	// 모든 명령어 핸들러 클래스가 공통으로 구현해야 하는 process() 메서드를 선언
	// 명령어 핸들러 클래스는 process() 메서드를 이용해서 알맞은 로직 코드를 구현하고 결과를 보여줄 JSP URI를 리턴합니다.
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
