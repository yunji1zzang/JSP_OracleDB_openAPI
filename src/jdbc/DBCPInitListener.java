package jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class DBCPInitListener implements ServletContextListener {
			
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String poolConfig = sce.getServletContext().getInitParameter("poolConfig");
		Properties prop = new Properties();
		try {
			prop.load(new StringReader(poolConfig));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		loadJDBCDriver(prop);
		initConnectionPool(prop);
	}

	private void loadJDBCDriver(Properties prop) {
		String driverClass = prop.getProperty("jdbcdriver");
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBC Driver", ex);
		}
	}
	
	private void initConnectionPool(Properties prop) {
		try {
			String jdbcUrl = prop.getProperty("jdbcUrl");
			String username = prop.getProperty("dbUser");
			String pw = prop.getProperty("dbPass");
			// 커넥션 풀이 새로운 커넥션을 생성할 때 사용할 커넥션 팩토리(ConnectionFactory)를 생성
			ConnectionFactory connFactory =
						// MySQL 연결에 상요할 jdbcUrl, username, pw를 생성자로 지정
						new DriverManagerConnectionFactory(jdbcUrl, username, pw);
			
			// PoolableConnection을 생성하는 팩토리를 생성
			// DBCP는 커넥션 풀에 커넥션을 보관할 때 PoolableConnection을 사용
			// 이 클래스는 내부적으로 실제 커넥션을 담고 있으며, 커넥션 풀을 관리하는 데 필요한 기능을 추가로 제공
			// 예를들면, close() 메서드를 실행하면 실제 커넥션을 종료하지 않고 풀에 커넥션을 반환
			PoolableConnectionFactory poolableConnFactory =
						new PoolableConnectionFactory(connFactory, null);
			poolableConnFactory.setValidationQuery("select 1");
			// DB 커넥션이 유효한지 여부를 검사할 때 사용할 쿼리를 지정
			// 이것은 특정 시간마다 트랜잭션 DB 세션 재접속을 검증하는 validationQuery를 실행
			// 오라클의 경우 : validationQuery="select 1 from dual" 환경을 세팅
			
			// 커넥션 풀의 설정 정보를 생성
			GenericObjectPoolConfig	poolConfig = new GenericObjectPoolConfig();
			// 유휴 커넥션 검사 주기 (1000L*60L*5L) 설정
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L*60L*5L);
			// 풀에 보관중인 커넥션 유효 검사 설정(true)
			poolConfig.setTestWhileIdle(true);
			// 커넥션 최소 개수 (4) 설정
			poolConfig.setMinIdle(4); 
			// 커넥션 최대 개수 (50) 설정
			poolConfig.setMaxTotal(50); 
			
			// 커넥션 풀을 생성
			// 생성자는 PoolableConnection을 생성할 때 사용할 팩토리(PoolableConnectionFactory)와
			// 커넥션 풀 설정(GenericObjectPoolConfig)을 파라미터로 전달 받음
			GenericObjectPool<PoolableConnection> connectionPool =
						new GenericObjectPool<>(poolableConnFactory, poolConfig);
			// PoolableConnectionFactory에서도  GenericObjectPool에서 생성한 커넥션 풀을 연결한다.
			poolableConnFactory.setPool(connectionPool);

			// 커넥션 풀을 제공하는 JDBC 드라이버를 등록
			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			PoolingDriver driver =
						(PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			// 커넥션 풀 드라이버에 GenericObjectPool에서 생성한 커넥션 풀을 등록한다.
			// 이때, "board"를 커넥션 풀 이름으로 주었는데,
			// 이 경우 프로그램에서 사용하는 JDBC URL은 "jdbc:apache:commons:dbcp:board"가 된다.
			String poolName = prop.getProperty("poolName");
			driver.registerPool(poolName, connectionPool);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}	
}
