/**
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午6:08:14
 */
package com.tfzzh.log.jar.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 连接工具
 * 
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午6:08:14
 */
public class ConnectionTools {

	/**
	 * 得到连接信息
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:12:20
	 * @return 得到连接
	 */
	public static ConnectionInfo getConnection() {
		try {
			Class.forName(LogConstants.DATABASE_DRIVER);
		} catch (final ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		final Properties pps = new Properties();
		pps.put("user", LogConstants.DATABASE_USER);
		pps.put("password", LogConstants.DATABASE_PASS);
		pps.put("useUnicode", LogConstants.DATABASE_USE_UNICODE);
		pps.put("characterEncoding", LogConstants.DATABASE_CHARACTER_ENCODING);
		pps.put("connectTimeout", LogConstants.DATABASE_CONNECTION_TIMEOUT);
		pps.put("socketTimeout", LogConstants.DATABASE_SOCKET_TIMEOUT);
		try {
			final Connection conn = DriverManager.getConnection(LogConstants.DATABASE_URL, pps);
			return new ConnectionInfo(conn);
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
