// Copyright 2015 ExtantFuture Inc. All Rights Reserved
package com.example.jiao.myapplication;

import java.util.concurrent.*;

/**
 * 任务中心,含公用线程池
 *
 * @author rambodu, <rambo@extantfuture.com>
 * @date 2015年3月19日 下午10:14:40
 */
public class JobCenter {

	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	public static void submitTask(Runnable task) {
		executor.execute(task);
	}

	public static <T> Future<T> submitTask(Callable<T> task) {
		return executor.submit(task);
	}

	public static void schedule(Runnable command, long delay, TimeUnit unit) {
		scheduledExecutorService.schedule(command, delay, unit);
	}

	public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return scheduledExecutorService.schedule(callable, delay, unit);
	}

	public static ExecutorService getExecutor() {
		return executor;
	}

	public static ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}
}
