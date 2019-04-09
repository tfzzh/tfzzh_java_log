/**
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:38:15
 */
package com.tfzzh.log.jar.dao;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tfzzh.log.CoreLog;
import com.tfzzh.log.jar.bean.BaseLogBean;
import com.tfzzh.log.jar.tools.ConnectionInfo;
import com.tfzzh.log.jar.tools.ConnectionTools;
import com.tfzzh.model.dao.tools.AlterTypeEnum;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.EntityTool;

/**
 * 这里直接作为DAO的实际逻辑处理对象
 * 
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:38:15
 */
public class LogDAO {

	/**
	 * 已经被验证过得表名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:54:34
	 */
	private final Map<String, Object> tns = new HashMap<>();

	/**
	 * 被记录的日志类
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午6:34:44
	 */
	private final Map<Class<? extends BaseLogBean>, Object> tcs = new HashMap<>();

	/**
	 * 创建新表或更新表结构
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午7:28:39
	 * @param conn 所用连接信息
	 * @param e 目标表对象
	 * @return -1，未成功；<br />
	 *         0，没有任何变化；<br />
	 *         1，创建的新表；<br />
	 *         2，更新的内容；<br />
	 */
	private <E extends BaseLogBean> int createOrEditTable(final ConnectionInfo conn, final E e) {
		final long l = System.currentTimeMillis();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int back = -1;
		// final String backS = null;
		String sql = null;
		try {
			final DatabaseMetaData dmd = conn.getMetaData();
			final String tableName = e.getTableName();
			rs = dmd.getTables(null, null, tableName, new String[] { "TABLE" });
			final EntityInfoBean<E> eib = EntityTool.getInstance().getEntityInfo(e.getObjectId());
			// 对字段的处理，先增加，再修改，再移除
			// 对索引的处理，先删除，再增加
			// 整体，字段增加，字段修改，索引删除，字段移除，索引增加
			if (rs.next()) {
				// 关闭之前的关联
				rs.close();
				final StringBuilder sb = new StringBuilder(200);
				sb.append("ALTER TABLE `").append(tableName).append("` ");
				// 需要被移除的字段
				final List<String> dropFields = new LinkedList<>();
				boolean isFirst = true;
				{ // 字段的控制
					// 从新关联字段
					rs = dmd.getColumns(null, null, tableName, null);
					// 一定是copy的
					final Map<String, EntityInfoBean<E>.FieldInfoBean> fis = eib.getTpFiledsCopy();
					final Map<String, EntityInfoBean<E>.FieldInfoBean> efs = new LinkedHashMap<>(fis.size());
					EntityInfoBean<E>.FieldInfoBean fi;
					String fieldName;
					String behindField = null;
					while (rs.next()) {
						// 认为此处的字段列表是有序的，正常就是有序的
						// 有表，进入对表字段及索引的逐个比较
						fieldName = rs.getString("COLUMN_NAME");
						// 1.TABLE_CAT String => table catalog (may be null)
						// 2.TABLE_SCHEM String => table schema (may be null)
						// 3.TABLE_NAME String => table name
						// 4.COLUMN_NAME String => column name
						// 5.DATA_TYPE int => SQL type from java.sql.Types
						// 6.TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
						// 7.COLUMN_SIZE int => column size.
						// 8.BUFFER_LENGTH is not used.
						// 9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
						// 10.NUM_PREC_RADIX int => Radix (typically either 10 or 2)
						// 11.NULLABLE int => is NULL allowed. ◦ columnNoNulls - might not allow NULL values
						// ◦ columnNullable - definitely allows NULL values
						// ◦ columnNullableUnknown - nullability unknown
						//
						// 12.REMARKS String => comment describing column (may be null)
						// 13.COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
						// 14.SQL_DATA_TYPE int => unused
						// 15.SQL_DATETIME_SUB int => unused
						// 16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
						// 17.ORDINAL_POSITION int => index of column in table (starting at 1)
						// 18.IS_NULLABLE String => ISO rules are used to determine the nullability for a column. ◦ YES --- if the column can include NULLs
						// ◦ NO --- if the column cannot include NULLs
						// ◦ empty string --- if the nullability for the column is unknown
						//
						// 19.SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
						// 20.SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
						// 21.SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
						// 22.SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
						// 23.GENERATED COLUMNIS_AUTOINCREMENT String => Indicates whether this column is auto incremented ◦ YES --- if the column is auto incremented
						// ◦ NO --- if the column is not auto incremented
						// ◦ empty string --- if it cannot be determined whether the column is auto incremented
						//
						// 24.IS_GENERATEDCOLUMN String => Indicates whether this is a generated column ◦ YES --- if this a generated column
						// ◦ NO --- if this not a generated column
						// ◦ empty string --- if it cannot be determined whether this is a generated column
						// rs.getInt("DATA_TYPE") 有点乱，不可用
						// rs.getInt("NUM_PREC_RADIX") 进制10或2
						// rs.getString("IS_GENERATEDCOLUMN") 是否生成的列
						// rs.getString("IS_NULLABLE")同rs.getInt("NULLABLE")：Yes-1；No-0；
						// rs.getInt("CHAR_OCTET_LENGTH")
						// System.out.println(fieldName + " -- " + rs.getString("TYPE_NAME") + " -- " + rs.getInt("COLUMN_SIZE") + " -- " + rs.getInt("DECIMAL_DIGITS") + " -- " + rs.getInt("NULLABLE") + " -- " + rs.getString("COLUMN_DEF") + " -- " + rs.getString("IS_AUTOINCREMENT") + " -- " + rs.getString("REMARKS") + " -- " + rs.getInt("ORDINAL_POSITION"));
						// 需要比较的，名字为主，类型，长度，非空，无符号，自增，默认值，注释
						if (null != (fi = fis.remove(fieldName))) {
							// 因为已经存在，需要进行数据交验，是否与之前相同
							if (!fi.validate(rs.getString("TYPE_NAME").toUpperCase(), rs.getInt("COLUMN_SIZE"), rs.getInt("NULLABLE") == 1, "YES".equals(rs.getString("IS_AUTOINCREMENT")), rs.getInt("DECIMAL_DIGITS"), rs.getString("COLUMN_DEF"), rs.getString("REMARKS"), behindField)) {
								efs.put(fieldName, fi);
							}
							behindField = fieldName;
						} else {
							// 因为已经不存在，而需要移除，记录进列表中
							dropFields.add(fieldName);
						}
					}
					rs.close();
					if (fis.size() > 0) {
						// 此列表一定是有序的，不会有前置字段相关问题
						// 需要再被增加的字段
						for (final EntityInfoBean<E>.FieldInfoBean f : fis.values()) {
							isFirst = this.changeTrace(sb, f.getBeforeFieldName(), efs, isFirst);
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							f.getAddAlter(sb);
						}
					}
					if (efs.size() > 0) {
						Iterator<EntityInfoBean<E>.FieldInfoBean> it = efs.values().iterator();
						EntityInfoBean<E>.FieldInfoBean f;
						int size = 0;
						// 有修改的字段
						while (it.hasNext()) {
							f = it.next();
							// 得到就移除
							it.remove();
							size = efs.size();
							isFirst = this.changeTrace(sb, f.getBeforeFieldName(), efs, isFirst);
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							f.getChangeAlter(sb);
							if (size != efs.size()) {
								it = efs.values().iterator();
							}
						}
					}
				}
				final Map<String, EntityInfoBean<E>.IndexInfoBean> iis = eib.getIndexsCopy();
				EntityInfoBean<E>.IndexInfoBean priKey = null;
				{ // 索引的设置
					rs = dmd.getIndexInfo(null, null, tableName, false, false);
					// 直接从索引队列中移除，如果存在，并增加进“安全队列”
					// 如果不存在于队列，判定是否存在于“安全队列”，如果存在不管，如果不存在加入到要“移除队列（附带去重功能的）”
					// 如果是主键，如果不存在于索引队列，则直接进入“移除队列”
					// 如果是主键，存在于索引队列（第一次），比较字段数量（省略说明），比较同位置字段名，如果相同，设定位置为null，如果不同，直接加入到“移除队列”
					// 如果是主键，不存在于索引队列，判定，如果存在“移除队列”跳过，是否存在“安全队列”，存在，进行与以上相同判断，及相同操作，不存在于“安全队列”，增加进“移除队列”
					EntityInfoBean<E>.IndexInfoBean ii;
					// 安全队列
					final Set<String> sas = new HashSet<>();
					// 移除队列
					final Set<String> rms = new HashSet<>();
					String indexName;
					final String pkn = "PRIMARY";
					final EntityInfoBean<E>.FieldInfoBean[] pfs = eib.getPrimaryFieldsCopy();
					while (rs.next()) {
						// 仅比较名字
						// System.out.println(rs.getString("INDEX_NAME") + " -- " + rs.getString("COLUMN_NAME") + " -- " + rs.getBoolean("NON_UNIQUE") + " -- " + rs.getString("ASC_OR_DESC") + " -- " + rs.getShort("TYPE") + " -- " + rs.getInt("CARDINALITY") + " -- " + rs.getShort("PAGES") + " -- " + rs.getShort("ORDINAL_POSITION"));
						indexName = rs.getString("INDEX_NAME").toUpperCase();
						if (null == (ii = iis.remove(indexName))) {
							if (!sas.contains(indexName)) {
								if (rms.add(indexName)) {
									// 需要被移除的
									if (isFirst) {
										isFirst = false;
									} else {
										sb.append(',');
									}
									AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
								}
							} else if (indexName.equals(pkn)) {
								// 主键处理逻辑，与下重复
								final int ind = rs.getShort("ORDINAL_POSITION");
								if (ind > pfs.length) {
									if (rms.add(indexName)) {
										// 因为长度一定不同，直接移除
										if (isFirst) {
											isFirst = false;
										} else {
											sb.append(',');
										}
										AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
									}
								} else {
									final String fn = rs.getString("COLUMN_NAME");
									if (fn.equals(pfs[ind - 1].getDataFieldName())) {
										// 与目标位置的
										pfs[ind - 1] = null;
									} else {
										if (rms.add(indexName)) {
											if (isFirst) {
												isFirst = false;
											} else {
												sb.append(',');
											}
											AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
										}
									}
								}
							}
						} else if (indexName.equalsIgnoreCase(pkn)) {
							priKey = ii;
							if (rms.contains(pkn)) {
								// 已经是需要被移除状态，直接下一个
								continue;
							}
							if (!sas.contains(pkn)) {
								// 还未存在于安全中，则增加
								sas.add(pkn);
							}
							if (null == pfs) {
								// 因为新版本不存在主键了
								if (rms.add(pkn)) {
									if (isFirst) {
										isFirst = false;
									} else {
										sb.append(',');
									}
									AlterTypeEnum.Drop.getAlterIndex(sb, pkn, null);
								}
								continue;
							}
							// 设置注解目标位置为null
							final int ind = rs.getShort("ORDINAL_POSITION");
							if (ind > pfs.length) {
								// 因为长度一定不同，直接移除
								if (rms.add(indexName)) {
									if (isFirst) {
										isFirst = false;
									} else {
										sb.append(',');
									}
									AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
								}
							} else {
								final String fn = rs.getString("COLUMN_NAME");
								if (fn.equals(pfs[ind - 1].getDataFieldName())) {
									// 与目标位置的
									pfs[ind - 1] = null;
								} else {
									if (rms.add(indexName)) {
										if (isFirst) {
											isFirst = false;
										} else {
											sb.append(',');
										}
										AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
									}
								}
							}
						} else {
							sas.add(indexName);
						}
					}
					if (sas.contains(pkn)) {
						// 如果不存在主键相关，则进一步确认
						if (!rms.contains(pkn)) {
							for (final EntityInfoBean<E>.FieldInfoBean fi : pfs) {
								if (null != fi) {
									if (rms.add(pkn)) {
										// 主键不同，增加
										if (isFirst) {
											isFirst = false;
										} else {
											sb.append(',');
										}
										AlterTypeEnum.Drop.getAlterIndex(sb, pkn, null);
									}
									break;
								}
							}
							// 以为没有被移除，所以不需要增加
							priKey = null;
						}
					}
				}
				{ // 处理需要被移除的字段
					for (final String fn : dropFields) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(',');
						}
						AlterTypeEnum.Drop.getAlterField(sb, fn, null, null);
					}
				}
				{ // 处理需要被增加的索引
					for (final EntityInfoBean<E>.IndexInfoBean ii : iis.values()) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(',');
						}
						ii.getAddAlter(sb);
					}
					if (null != priKey) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(',');
						}
						priKey.getAddAlter(sb);
					}
				}
				if (isFirst) {
					// 因为没有任何数据修改
					back = 0;
				} else {
					sb.append(';');
					sql = sb.toString();
					// System.out.println("sql >> " + sql);
					ps = conn.prepareStatement(sql);
					ps.execute();
					back = 2;
				}
			} else {
				// 没有目标表，直接创建
				sql = eib.getCreateSQL(tableName);
				// System.out.println("sql >> " + sql);
				ps = conn.prepareStatement(sql);
				ps.execute();
				back = 1;
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (final SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (null != ps) {
				try {
					ps.close();
				} catch (final SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " create table: ", sql, " >");
			}
		}
		return back;
	}

	/**
	 * 修改追溯
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午7:21:32
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param beforeName 前一个字段的字段名
	 * @param efs 修改字段列表
	 * @param isFirst 是否第一个
	 * @return isFirst-是否第一个，用来向下传递
	 */
	private <E extends BaseLogBean> boolean changeTrace(final StringBuilder sb, final String beforeName, final Map<String, EntityInfoBean<E>.FieldInfoBean> efs, boolean isFirst) {
		EntityInfoBean<E>.FieldInfoBean f;
		if (null != (f = efs.remove(beforeName))) {
			isFirst = this.changeTrace(sb, f.getBeforeFieldName(), efs, isFirst);
		} else {
			return isFirst;
		}
		// 如果存在，优先处理
		if (isFirst) {
			isFirst = false;
		} else {
			sb.append(',');
		}
		f.getChangeAlter(sb);
		return isFirst;
	}

	/**
	 * 批量插入数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:05:07
	 * @param el 数据列表
	 */
	public <E extends BaseLogBean> void insertDatas(final List<E> el) {
		final long l = System.currentTimeMillis();
		final Map<String, List<E>> eml = new HashMap<>();
		final ConnectionInfo conn = ConnectionTools.getConnection();
		List<E> tel;
		for (final E e : el) {
			if (!this.tns.containsKey(e.getTableName())) {
				// 不存在，进行一次创建或修改
				if (!this.tcs.containsKey(e.getClass())) {
					EntityTool.getInstance().initEntityInfo(e.getClass(), null);
					this.tcs.put(e.getClass(), new Object());
				}
				this.createOrEditTable(conn, e);
			}
			tel = eml.get(e.getTableName());
			if (null == tel) {
				tel = new LinkedList<>();
				eml.put(e.getTableName(), tel);
			}
			tel.add(e);
		}
		E te;
		for (final Entry<String, List<E>> ent : eml.entrySet()) {
			tel = ent.getValue();
			te = tel.get(0);
			// 创建ps
			try (PreparedStatement ps = conn.prepareStatement(te.getInsertSql())) {
				for (final E e : tel) {
					e.putAttrToPreparedStatement(ps);
					ps.addBatch();
				}
				// 批量提交数据
				ps.executeBatch();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
		CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("use[").append(System.currentTimeMillis() - l).append("] >> insert with tables[").append(eml.size()).append("] sum datas[").append(el.size()).append("]").toString());
	}
}
