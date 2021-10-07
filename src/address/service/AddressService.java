package address.service;

import java.sql.Connection;
import java.sql.SQLException;

import address.dao.AddressDAO;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
//주소 입력 기능을 제공하는 AddressService 클래스의 코딩 구현
public class AddressService {
	private AddressDAO addressDAO = new AddressDAO();
	
	public void insert(String basic, String detail) {
		Connection conn =null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			// 주소 데이터 테이블에 삽입
			addressDAO.insertAddress(conn, basic, detail);	
			//성공시 커밋
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
