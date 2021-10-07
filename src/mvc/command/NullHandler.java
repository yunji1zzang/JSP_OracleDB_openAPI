package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;

// NullHandler 클래스는 404 에러를 응답으로 전송하는 핸들러 클래스
public class NullHandler implements CommandHandler {
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// SC_NOT_FOUND : 404 에러에 해당
		res.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}
}
