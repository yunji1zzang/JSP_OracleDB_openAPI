package address.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import address.service.AddressService;
import mvc.command.CommandHandler;
//주소 검색을 처리할 SearchAddressHandler를 구현하려고 한다.
public class SearchAddressHandler implements CommandHandler{
	
	private static final String FORM_VIEW =
			"/WEB-INF/view/addressAPIPopup.jsp";
	
	// 주소입력 기능을 제공하는 addressService 생성
	AddressService addressService = new AddressService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// equalsIgnoreCase : 같은 값 확인할 때, 대소문자 구분을 하지 않는다.
		// equals : 같은 값 확인할 때, 대소문자 구분을 한다.
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req,res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			// 405 응답 코드 전송 (허용되지 않는 메소드 응답)
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// 폼 요청을 처리함
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws IOException {
		System.out.println("SearchAddressHandler.processForm()");
		// 폼을 위한 뷰를 리턴한다.
		return FORM_VIEW;
	}
	
	// 폼 전송 처리한다.
	private String processSubmit(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("SearchAddressHandler.processSubmit()");
		// addressForm.jsp 에서 form 태그로 넘어온 파라미터값을 변수에 저장한다.
		String basic_address = request.getParameter("basic_address");
		String detail_address = request.getParameter("detail_address");
		
		System.out.println("기본주소 : "+basic_address);
		System.out.println("상세주소 : "+detail_address);
		
		// 에러 정보를 담을 맵 객체를 생성하고, "errors" 속성에 저장
		Map<String, Boolean> errors = new HashMap<>();
		// 주소가 유효한지 검사한다.
		request.setAttribute("errors", errors);
		
		// 기본 주소가 빈칸 일 경우 에러발생 
		if(basic_address == null || basic_address.isEmpty()) {
			errors.put("basic_address", Boolean.TRUE);
		}
		// 상세 주소가 빈칸 일 경우 에러발생 
		if(detail_address == null || detail_address.isEmpty()) {
			errors.put("detail_address", Boolean.TRUE);
		}
		
		// 에러가 존재하면 FORM_VIEW 폼을 다시 보여준다.
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		try {
			// 주소입력 메서드 실행
			addressService.insert(basic_address, detail_address);
			return "/WEB-INF/view/addressSuccess.jsp";
			// Exception 예외가 발생하면 익셉션을 처리한다.
		} catch (Exception e) {
			errors.put("AddressFail", Boolean.TRUE);
			// 404 응답 코드 전송(요청한 자원이 존재하지 않음)
			return FORM_VIEW;
		}
	}
}
