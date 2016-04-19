package org.greenicon.crawler.util;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class TaskExecutor extends ThreadPoolTaskExecutor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5282518701516972479L;
	
	private static final Logger LOGGER = Logger.getLogger(TaskExecutor.class);
	
	private static final int MAX_WAIT_TIME = 5;
	private static final int MAX_RETRY = 50;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	
	public void initThreadPool() {
		getThreadPoolExecutor().setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void destroyThreadPool() {
		LOGGER.info("Going to destroy thread pool");
		shutdown();
		int count = 0;
		try {
			while (count < MAX_RETRY && (!getThreadPoolExecutor().isTerminated()
					|| !getThreadPoolExecutor().awaitTermination(MAX_WAIT_TIME, TIME_UNIT))) {
				count++;
			}
			if (count == MAX_RETRY) {
				List<Runnable> runnables = getThreadPoolExecutor().shutdownNow();
				for (Runnable runnable : runnables) {
					logger.error("Thread with id " + runnable.hashCode() + " was not completed.");
				}
			}
		} catch (IllegalStateException e) {
			logger.error("Error is : ", e);
		} catch (InterruptedException e) {
			logger.error("Error is : ", e);
		}
		logger.info("All threads are finished.");
	}

	
}
