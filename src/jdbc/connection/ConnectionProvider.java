package jdbc.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	public static Connection getConnection() throws SQLException {
		// web.xml에서 지정한 poolName 값인 openapi를 풀 이름으로 사용한 것을 알 수 있음
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:openapi");
	}
}
