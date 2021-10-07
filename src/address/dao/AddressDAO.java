package address.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressDAO {

	// 기본주소, 상세주소 등록
	public void insertAddress(Connection conn, String basic, String detail) 
			throws SQLException {
		System.out.println("AddressDAO.insertAddress()");
		try(PreparedStatement pstmt = conn.prepareStatement(
				"insert into jsp_address values(idx_seq.nextval,?,?)")) {
			pstmt.setString(1, basic);
			pstmt.setString(2, detail);
			
			pstmt.executeUpdate();
		}
	}
}
