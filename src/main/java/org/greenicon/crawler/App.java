package org.greenicon.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.greenicon.crawler.model.SiteJson;
import org.greenicon.crawler.util.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App 
{
	private String name;
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "App [name=" + name + "]";
	}

	public static void main( String[] args ) throws IOException
    {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/appContext.xml");
        System.out.println(applicationContext.getBean(App.class));
        String path = Config.getValue("app.json.path");
        System.out.println("path: "+path);
        
        File[] files = getJsonFiles(path);
        
        List<SiteJson> siteJsons = getSiteJsons(files);
        
        for(SiteJson siteJson:siteJsons){
        	String url = siteJson.getConfig().getUrl();
        	String selector = siteJson.getProcess().getRoot();
        	Document document = Jsoup.connect(url).get();
        	Elements elements = document.select(selector);
        	System.out.println(elements.size());
        	Map<String, Map<String, String>> tables = siteJson.getCreate();
        	for(Entry<String, Map<String, String>> tableEntry:tables.entrySet()){
        		String tableName = tableEntry.getKey();
        		System.out.println("Table: "+tableName);
        		Map<String, String> columnDefenitions = tableEntry.getValue();
        		Collection<Map<String, String>> rows = new ArrayList<Map<String,String>>();
            	for(Element element: elements){
            		Map<String, String> row = new HashMap<String, String>();
            		Iterator<String> columnIterator = columnDefenitions.keySet().iterator();
            		while(columnIterator.hasNext()){
            			String column = columnIterator.next();
                		row.put(column, element.select(columnDefenitions.get(column)).text());
            		}
            		rows.add(row);
            	}
            	for(Map<String, String> row : rows){
            		System.out.println("\t"+row);
            	}
        	}
        	
        }
        
        applicationContext.close();
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
