package org.greenicon.crawler.service.cron;

import org.apache.log4j.Logger;
import org.greenicon.common.exception.ServiceException;
import org.greenicon.crawler.minion.IMinion;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;

@Component(value="commandoScheduler")
public class CronScheduler implements BaseScheduler {

	private static final Logger LOGGER = Logger.getLogger(CronScheduler.class);

	private static final String PRE_ALARMNAME = "ALARM-";
	private static final String MINION_FUNCTION = "doSomething";
	
	@Autowired
	private Scheduler scheduler;

	public void registerJob(IMinion minion, String cronExpression) throws ServiceException {
		LOGGER.info("Setting alarm for minion");
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Minion: " + minion);
		try {
			MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
			jobDetail.setTargetObject(minion);
			jobDetail.setTargetMethod(MINION_FUNCTION);
			jobDetail.setName(PRE_ALARMNAME + minion.getId());
			jobDetail.setConcurrent(true);
			jobDetail.afterPropertiesSet();

			CronTriggerBean cronTrigger = new CronTriggerBean();
			cronTrigger.setBeanName("Cron-" + minion.getId());
			cronTrigger.setCronExpression(cronExpression);
			cronTrigger.afterPropertiesSet();

			scheduler.scheduleJob((JobDetail) jobDetail.getObject(), cronTrigger);

			if(scheduler.isInStandbyMode()){
				scheduler.start();
			}

		} catch (ClassNotFoundException | NoSuchMethodException e) {
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
