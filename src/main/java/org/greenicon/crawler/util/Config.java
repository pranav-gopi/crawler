package org.greenicon.crawler.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Config implements ApplicationContextAware{

	private static Properties appProperties;
	
	public static String getValue(String key){
		return getValue(key, null);
	}
	
	public static String getValue(String key, String defaultVal){
		String val = defaultVal;
		if(appProperties != null && appProperties.containsKey(key))
			val = appProperties.getProperty(key);
		return val;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appProperties = applicationContext.getBean("appProperties", Properties.class);
		
	}

}
