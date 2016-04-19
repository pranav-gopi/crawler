package org.greenicon.crawler.service.cron;

import org.greenicon.common.exception.ServiceException;
import org.greenicon.crawler.minion.IMinion;

public interface BaseScheduler {

	public void registerJob(IMinion baseTask, String cronExpression) throws ServiceException;
}
