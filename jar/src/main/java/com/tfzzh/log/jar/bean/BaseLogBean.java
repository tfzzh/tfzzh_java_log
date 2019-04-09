/**
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:11:57
 */
package com.tfzzh.log.jar.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bson.Document;

import com.tfzzh.model.bean.BaseDataBean;

/**
 * 基础日志数据对象
 * 
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:11:57
 */
public abstract class BaseLogBean extends BaseDataBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午3:36:28
	 */
	private static final long serialVersionUID = -4593225343115097529L;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午8:52:56
	 */
	protected String tableName;

	/**
	 * 设置表名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午8:04:51
	 * @param tableName 表名
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 得到对应表名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:53:39
	 * @return 对应表名
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * 得到新增数据用SQL
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:57:59
	 * @return 新增数据用SQL
	 */
	public abstract String getInsertSql();

	/**
	 * 将属性值放入到ps中
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午8:16:27
	 * @param ps 目标ps
	 * @throws SQLException 抛
	 */
	public abstract void putAttrToPreparedStatement(PreparedStatement ps) throws SQLException;

	/**
	 * 得到主键的值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午3:32:03
	 * @return key的值
	 * @see com.tfzzh.model.bean.BaseDataBean#getKeyValue()
	 */
	@Override
	public Object getKeyValue() {
		return null;
	}

	/**
	 * 将数据库中数据放入到对象<br />
	 * 针对一般数据库用方法<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午3:32:04
	 * @param rs 数据库结果
	 * @param index 字段对应索引位
	 * @see com.tfzzh.model.bean.BaseDataBean#putResultData(java.sql.ResultSet, int[])
	 */
	@Override
	public void putResultData(final ResultSet rs, final int[] index) {
	}

	/**
	 * 将Mongo库中数据放入到对象<br />
	 * 针对一般MongoDb库用方法<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午3:32:06
	 * @param doc mongo库结果
	 * @see com.tfzzh.model.bean.BaseDataBean#putDocumentData(org.bson.Document)
	 */
	@Override
	public void putDocumentData(final Document doc) {
	}
}
