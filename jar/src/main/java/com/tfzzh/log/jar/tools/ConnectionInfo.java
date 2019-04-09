/**
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午6:48:48
 */
package com.tfzzh.log.jar.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tfzzh.model.exception.SqlBatchException;

/**
 * 与数据库连接信息
 * 
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午6:48:48
 */
public class ConnectionInfo implements AutoCloseable {

	/**
	 * 连接信息
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:49:00
	 */
	private final Connection conn;

	/**
	 * 是否自动提交，与批量操作有关，缓存类属性
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:49:16
	 */
	private boolean isAutoCommit;

	/**
	 * 已经积累的批量数据数量<br />
	 * 自动提交时，此值为0<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:49:17
	 */
	private int batchCount = 0;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:50:04
	 * @param conn 有效的连接信息
	 */
	protected ConnectionInfo(final Connection conn) {
		this.conn = conn;
	}

	/**
	 * 得到表结构
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:50:28
	 * @return 表结构
	 * @throws SQLException 抛
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.conn.getMetaData();
	}

	/**
	 * 得到数据库操作用Ps对象，单条的及时数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:50:51
	 * @param sql 目标sql
	 * @return 编制的声明
	 * @throws SQLException 抛
	 */
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		if (this.batchCount != 0) {
			// 该种情况是不允许的
			throw new SqlBatchException(sql, this.batchCount);
		}
		if (!this.isAutoCommit) {
			this.isAutoCommit = true;
			this.conn.setAutoCommit(true);
		}
		return this.conn.prepareStatement(sql);
	}

	/**
	 * 提交批量作业
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午7:07:45
	 * @throws SQLException 抛
	 */
	public void commit() throws SQLException {
		if (!this.isAutoCommit) {
			this.conn.commit();
			this.batchCount = 0;
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午7:09:16
	 */
	@Override
	public void close() {
		try {
			this.conn.isClosed();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
