/**
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:09:06
 */
package com.tfzzh.log.jar;

import java.lang.Thread.State;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tfzzh.log.jar.bean.BaseLogBean;
import com.tfzzh.log.jar.dao.LogDAO;
import com.tfzzh.log.jar.tools.LogConstants;

/**
 * 日志处理池
 * 
 * @author Xu Weijie
 * @datetime 2017年8月26日_下午4:09:06
 */
public class LogPool implements Runnable {

	/**
	 * 是否在运行中
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:24:12
	 */
	private boolean isRun = false;

	/**
	 * 与数据库操作对象
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午8:29:03
	 */
	private final LogDAO ld = new LogDAO();

	/**
	 * 可以被放入日志消息的队列
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:24:09
	 */
	private List<BaseLogBean> logList = new LinkedList<>();

	/**
	 * 准备入库的日志列表
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:29:50
	 */
	private List<BaseLogBean> readyLogList = null;

	/**
	 * 最后被记录时间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:40:05
	 */
	private long lastSaveTime = Long.MAX_VALUE;

	/**
	 * 运行中线程
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:42:41
	 */
	private Thread runThr = null;

	/**
	 * 锁
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:33:55
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:24:48
	 */
	public LogPool() {
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:40:42
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.isRun = true;
		this.lastSaveTime = System.currentTimeMillis();
		this.runThr = Thread.currentThread();
		while (this.isRun) {
			if (null != this.readyLogList) {
				this.toDatabase();
			} else if ((System.currentTimeMillis() - this.lastSaveTime) > LogConstants.LOG_MAX_SAVE_INTERVAL) {
				// 已经达到了 最大间隔时间
				this.toNewList();
				this.toDatabase();
			}
			try {
				Thread.sleep(LogConstants.LOG_POOL_CYCLE_INTERVAL);
			} catch (final Exception e) {
			}
		}
		// 进行最后一次入库处理
		this.toNewList();
		this.toDatabase();
		System.exit(0);
	}

	/**
	 * 进行数据入库操作<br />
	 * 因为只有一个线程调用点<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:47:59
	 */
	private void toDatabase() {
		if (null != this.readyLogList) {
			if (this.readyLogList.size() > 0) {
				// 暂时不跟进具体被插入的条目数
				// 暂定认为，如果不能被插入的
				this.ld.insertDatas(this.readyLogList);
				this.readyLogList = null;
				this.lastSaveTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * 处理为一个新的日志队列
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:36:15
	 */
	private void toNewList() {
		if (this.lock.tryLock()) {
			try {
				if (null == this.readyLogList) {
					this.readyLogList = this.logList;
					this.logList = new LinkedList<>();
				}
			} finally {
				this.lock.unlock();
			}
		}
	}

	/**
	 * 增加一条日志
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:38:43
	 * @param bean 日志信息
	 * @return true，增加成功；
	 */
	public boolean addLog(final BaseLogBean bean) {
		if (this.isRun) {
			if (null == bean) {
				return false;
			}
			synchronized (this.logList) {
				this.logList.add(bean);
				if (this.logList.size() > LogConstants.LOG_LIST_MAX_SIZE) {
					this.toNewList();
					this.weekup();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 进行线程唤醒
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午4:59:02
	 */
	private void weekup() {
		// 判定线程状态
		if (null != this.runThr) {
			if (State.TIMED_WAITING == this.runThr.getState()) {
				// 因中断状态，所以激活
				try {
					this.runThr.interrupt();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 进入结束阶段
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月26日_下午5:41:58
	 */
	public void toStop() {
		if (this.isRun) {
			this.isRun = false;
			// 判定线程状态
		}
	}
}
