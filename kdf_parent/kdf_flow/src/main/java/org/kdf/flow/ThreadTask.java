package org.kdf.flow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @title: ThreadTask.java 
 * @package org.kdf.flow 
 * @description: 线程池
 * @author: 、T
 * @date: 2019年9月29日 下午3:42:51 
 * @version: V1.0
 */
public final class ThreadTask {

	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private ThreadTask() {

	}

	private static ThreadTask threadTask = new ThreadTask();

	public static ThreadTask getInstance() {
		return threadTask;
	}

	/**
	 * 新增任务
	 */
	public void addTask(Runnable runnable) {
		executorService.execute(runnable);
//		executorService.shutdown();
	}

}
