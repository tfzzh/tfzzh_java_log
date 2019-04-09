/**
 * @author 许纬杰：tfzzh@qq.com
 */
package com.tfzzh.log.jar.log.log.model.bean;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tfzzh.core.annotation.DontShow;
import com.tfzzh.log.jar.bean.BaseLogBean;
import com.tfzzh.model.dao.annotation.DataField;
import com.tfzzh.model.dao.annotation.DataTable;
import com.tfzzh.model.dao.annotation.IdField;
import com.tfzzh.model.dao.annotation.KeyIndex;
import com.tfzzh.model.dao.annotation.KeyIndexs;
import com.tfzzh.model.dao.tools.IdFieldEnum;
import com.tfzzh.model.dao.tools.KeyIndexEnum;

/**
 * 登陆日志数据实体Bean
 * 
 * @author 许纬杰
 * @dateTime 2017-08-26
 */
@DataTable(tableName = "login_log", desc = "登陆日志表")
public final class LoginLogBean extends BaseLogBean {

	/**
	 * @author 许纬杰：tfzzh@qq.com
	 */
	private static final long serialVersionUID = 29801L;

	/**
	 * 对象ID
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 */
	public static final Long OBJECT_ID = 29801L;

	/**
	 * insertSql的头部
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 */
	private static final String insertSqlHead = "INSERT INTO `";

	/**
	 * insertSql的主干
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 */
	private static final String insertSqlBody = "` (`uid`,`pf_open_id`,`open_id`,`game_code`,`pf_code`,`is_new`,`log_time`) VALUES (?,?,?,?,?,?,?);";

	/**
	 * 版本号
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 */
	private int verId = 0;

	/**
	 * 自增ID
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@KeyIndexs({ @KeyIndex(value = KeyIndexEnum.Unique, name = "id", count = 1, index = 1), @KeyIndex(value = KeyIndexEnum.Primary, name = "primary", count = 1, index = 1) })
	@IdField(IdFieldEnum.Increment)
	@DataField(fieldName = "id", fieldType = "int", length = 11, desc = "自增ID", index = 1)
	private Integer id;

	/**
	 * 相关用户ID
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@DataField(fieldName = "uid", fieldType = "char", length = 32, desc = "相关用户ID", index = 2)
	private String uid;

	/**
	 * 相关渠道的OpendId
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@KeyIndex(value = KeyIndexEnum.Normal, name = "1", count = 1, index = 1)
	@DataField(fieldName = "pf_open_id", fieldType = "varchar", length = 32, desc = "相关渠道的OpendId", index = 3)
	private String pfOpenId;

	/**
	 * 淘游的OpenId
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@KeyIndexs({ @KeyIndex(value = KeyIndexEnum.Normal, name = "2", count = 1, index = 1), @KeyIndex(value = KeyIndexEnum.Normal, name = "3", count = 2, index = 2), @KeyIndex(value = KeyIndexEnum.Normal, name = "4", count = 2, index = 2) })
	@DataField(fieldName = "open_id", fieldType = "char", length = 32, desc = "淘游的OpenId", index = 4)
	private String openId;

	/**
	 * 游戏Code
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@KeyIndex(value = KeyIndexEnum.Normal, name = "3", count = 2, index = 1)
	@DataField(fieldName = "game_code", fieldType = "char", length = 6, desc = "游戏Code", index = 5)
	private String gameCode;

	/**
	 * 渠道Code
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@KeyIndex(value = KeyIndexEnum.Normal, name = "4", count = 2, index = 1)
	@DataField(fieldName = "pf_code", fieldType = "char", length = 6, desc = "渠道Code", index = 6)
	private String pfCode;

	/**
	 * 是否新增帐号
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@KeyIndex(value = KeyIndexEnum.Normal, name = "5", count = 1, index = 1)
	@DataField(fieldName = "is_new", fieldType = "char", length = 1, desc = "是否新增帐号", index = 7)
	private String isNew;

	/**
	 * 日志时间
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 */
	@DataField(fieldName = "log_time", fieldType = "bigint", desc = "日志时间", index = 8)
	private Long logTime;

	/**
	 * 得到对象Id
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 * @return 对象Id
	 */
	@Override
	public Long getObjectId() {
		return LoginLogBean.OBJECT_ID;
	}

	/**
	 * 得到版本号
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 * @return the verId
	 */
	public int getVerId() {
		return this.verId;
	}

	/**
	 * 设置版本号
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 * @param verId the verId to set
	 */
	public void setVerId(final int verId) {
		this.verId = verId;
	}

	/**
	 * 得到自增ID
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * 设置自增ID
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param id the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * 得到相关用户ID
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the uid
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * 设置相关用户ID
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param uid the uid to set
	 */
	public void setUid(final String uid) {
		this.uid = uid;
	}

	/**
	 * 得到相关渠道的OpendId
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the pfOpenId
	 */
	public String getPfOpenId() {
		return this.pfOpenId;
	}

	/**
	 * 设置相关渠道的OpendId
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param pfOpenId the pfOpenId to set
	 */
	public void setPfOpenId(final String pfOpenId) {
		this.pfOpenId = pfOpenId;
	}

	/**
	 * 得到淘游的OpenId
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the openId
	 */
	public String getOpenId() {
		return this.openId;
	}

	/**
	 * 设置淘游的OpenId
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param openId the openId to set
	 */
	public void setOpenId(final String openId) {
		this.openId = openId;
	}

	/**
	 * 得到游戏Code
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the gameCode
	 */
	public String getGameCode() {
		return this.gameCode;
	}

	/**
	 * 设置游戏Code
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param gameCode the gameCode to set
	 */
	public void setGameCode(final String gameCode) {
		this.gameCode = gameCode;
	}

	/**
	 * 得到渠道Code
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the pfCode
	 */
	public String getPfCode() {
		return this.pfCode;
	}

	/**
	 * 设置渠道Code
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param pfCode the pfCode to set
	 */
	public void setPfCode(final String pfCode) {
		this.pfCode = pfCode;
	}

	/**
	 * 得到是否新增帐号
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the isNew
	 */
	public String getIsNew() {
		return this.isNew;
	}

	/**
	 * 设置是否新增帐号
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param isNew the isNew to set
	 */
	public void setIsNew(final String isNew) {
		this.isNew = isNew;
	}

	/**
	 * 得到日志时间
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return the logTime
	 */
	public Long getLogTime() {
		return this.logTime;
	}

	/**
	 * 设置日志时间
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @param logTime the logTime to set
	 */
	public void setLogTime(final Long logTime) {
		this.logTime = logTime;
	}

	/**
	 * 得到表名称
	 * 
	 * @author 许纬杰
	 * @dateTime 2017-08-26
	 * @return 所相关表名称
	 */
	@DontShow
	@Override
	public String getTableName() {
		if (null == super.tableName) {
			return "login_log";
		} else {
			return super.tableName;
		}
	}

	/**
	 * 得到自增字段的类型
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 * @return Integer类型字段
	 * @see com.tfzzh.model.bean.BaseEntityBean#getIncrementKeyType()
	 */
	@Override
	public Class<Integer> getIncrementKeyType() {
		return Integer.class;
	}

	/**
	 * 得到新增数据用SQL
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 * @return 新增数据用SQL
	 * @see com.tfzzh.log.jar.bean.BaseLogBean#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		final String tn = this.getTableName();
		final StringBuilder sb = new StringBuilder(LoginLogBean.insertSqlHead.length() + tn.length() + LoginLogBean.insertSqlBody.length());
		sb.append(LoginLogBean.insertSqlHead);
		sb.append(tn);
		sb.append(LoginLogBean.insertSqlBody);
		return sb.toString();
	}

	/**
	 * 将属性值放入到ps中
	 * 
	 * @author 许纬杰：tfzzh@qq.com
	 * @param ps 目标ps
	 * @throws SQLException 抛
	 * @see com.tfzzh.log.jar.bean.BaseLogBean#putAttrToPreparedStatement(java.sql.PreparedStatement)
	 */
	@Override
	public void putAttrToPreparedStatement(final PreparedStatement ps) throws SQLException {
		int i = 1;
		ps.setString(i++, this.uid);
		ps.setString(i++, this.pfOpenId);
		ps.setString(i++, this.openId);
		ps.setString(i++, this.gameCode);
		ps.setString(i++, this.pfCode);
		ps.setString(i++, this.isNew);
		ps.setLong(i++, this.logTime);
	}
}
