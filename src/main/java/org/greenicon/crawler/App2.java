package org.greenicon.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.greenicon.common.exception.ServiceException;
import org.greenicon.crawler.minion.CommandoMinion;
import org.greenicon.crawler.minion.SupervisorMinion;
import org.greenicon.crawler.model.ProductState;
import org.greenicon.crawler.model.SiteJson;
import org.greenicon.crawler.model.product.SiteProduct;
import org.greenicon.crawler.service.cron.BaseScheduler;
import org.greenicon.crawler.service.cron.Warehouse;
import org.greenicon.crawler.util.Config;
import org.greenicon.crawler.util.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Pranav
 *
 */
public class App2 {

	private static final Logger LOGGER = Logger.getLogger(App2.class);
	
	private static ApplicationContext applicationContext;
	
	public static void main(String[] args) throws ServiceException {
		ClassPathXmlApplicationContext classPathApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/appContext.xml");
		applicationContext = classPathApplicationContext;
		
		String path = Config.getValue("app.json.path");
        File[] files = getJsonFiles(path);
        List<SiteJson> siteJsons = getSiteJsons(files);
        
        for(SiteJson siteJson:siteJsons){
        	processSiteJson(siteJson);
        }
        LOGGER.info("exiting app");
	}
	
	private static void processSiteJson(SiteJson siteJson) {
		if(!validateSiteJson(siteJson))
			return;
		BaseScheduler commandoScheduler = applicationContext.getBean("commandoScheduler", BaseScheduler.class);
		TaskExecutor executor = applicationContext.getBean("executor", TaskExecutor.class);
		
		Warehouse warehouse = Warehouse.getInstance();
		
		SupervisorMinion supervisorMinion = new SupervisorMinion();
		executor.execute(supervisorMinion);
		
		supervisorMinion.setLabourUnion(executor);
		supervisorMinion.goToWarehouse(warehouse);
		
		CommandoMinion commandoMinion = new CommandoMinion();
		commandoMinion.setName("Commando");
		
		SiteProduct siteProduct = new SiteProduct();
		siteProduct.setProductState(ProductState.INITIALIZED);
		siteProduct.setSiteJson(siteJson);
		commandoMinion.setProduct(siteProduct);
		
		try {
			commandoScheduler.registerJob(commandoMinion, siteJson.getConfig().getCronExpression());
		} catch (ServiceException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private static boolean validateSiteJson(SiteJson siteJson) {
		if(siteJson == null || siteJson.getConfig() == null || siteJson.getProcess() == null || siteJson.getCreate() == null){
			LOGGER.info("invalid siteJson");
			return false;
		}
		return true;
	}

	private static List<SiteJson> getSiteJsons(File[] files) {
		List<SiteJson> list = new ArrayList<SiteJson>();
		if(files!=null){
        	for(File file:files){
        		BufferedReader bufferedReader = null;
        		try {
        			bufferedReader = new BufferedReader(new FileReader(file));
            		String line = null;
            		StringBuilder stringBuilder = new StringBuilder();
					while((line =bufferedReader.readLine()) != null){
						stringBuilder.append(line);
					}
					
					String json = stringBuilder.toString();
					System.out.println(json);
					
					SiteJson siteJson = new ObjectMapper().readValue(json.getBytes(), SiteJson.class);
					System.out.println(siteJson);
					list.add(siteJson);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        	}
        }
		return list;
	}

	private static File[] getJsonFiles(String path) {
		File dir = new File(path);
        File[] files = null;
        if(dir!=null && dir.isDirectory()){
        	 files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					File file = new File(dir.getAbsolutePath()+File.separatorChar+name);
					if(file.isFile() && name.endsWith("json"))
						return true;
					return false;
				}
			});
        }
        return files;
	}
	
}
