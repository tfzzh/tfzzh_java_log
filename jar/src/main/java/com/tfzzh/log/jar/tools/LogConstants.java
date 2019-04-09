/**
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:41:25
 */
package com.tfzzh.log.jar.tools;

import com.tfzzh.core.annotation.PropertiesFile;
import com.tfzzh.core.annotation.PropertiesValue;
import com.tfzzh.tools.BaseConstants;

/**
 * 日志相关常量信息
 * 
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:41:25
 */
@PropertiesFile("Log")
public class LogConstants extends BaseConstants {

	static {
		new LogConstants("log_message");
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:11:29
	 * @param bundleName 资源名
	 */
	public LogConstants(final String bundleName) {
		super(bundleName);
	}

	/**
	 * 日志预存队列的最大长度
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:13:58
	 */
	@PropertiesValue
	public static int LOG_LIST_MAX_SIZE;

	/**
	 * 日志的记录间隔时间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:14:43
	 */
	@PropertiesValue
	public static int LOG_MAX_SAVE_INTERVAL;

	/**
	 * 数据池循环间隔时间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午8:39:30
	 */
	@PropertiesValue
	public static int LOG_POOL_CYCLE_INTERVAL;

	/**
	 * 数据库连接用URL
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:20:17
	 */
	@PropertiesValue
	public static String DATABASE_URL;

	/**
	 * 数据库连接用帐户
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:20:21
	 */
	@PropertiesValue
	public static String DATABASE_USER;

	/**
	 * 数据库连接用密码
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:20:21
	 */
	@PropertiesValue
	public static String DATABASE_PASS;

	/**
	 * 数据库驱动类
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:20:20
	 */
	@PropertiesValue
	public static String DATABASE_DRIVER;

	/**
	 * 数据库是否使用unicde的方式连接
	 *
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:41:41
	 */
	@PropertiesValue
	public static String DATABASE_USE_UNICODE;

	/**
	 * 数据库字符编码
	 *
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:41:42
	 */
	@PropertiesValue
	public static String DATABASE_CHARACTER_ENCODING;

	/**
	 * 数据库空闲断线时间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:20:19
	 */
	@PropertiesValue
	public static String DATABASE_CONNECTION_TIMEOUT;

	/**
	 * 数据库创建连接超时时间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午6:20:19
	 */
	@PropertiesValue
	public static String DATABASE_SOCKET_TIMEOUT;
}
