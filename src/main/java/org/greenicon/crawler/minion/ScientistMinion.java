package org.greenicon.crawler.minion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.greenicon.crawler.model.ProductState;
import org.greenicon.crawler.model.SiteJson;
import org.greenicon.crawler.model.product.IProduct;
import org.greenicon.crawler.model.product.SiteProduct;
import org.greenicon.crawler.service.cron.Warehouse;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * If u want to get some r&d done. Then m the guy
 * 
 *
 */
public class ScientistMinion extends AbstractMinion {

	private static final Logger LOGGER = Logger.getLogger(ScientistMinion.class);
	
	private IProduct product;

	public ScientistMinion() {
		setName("Scientist");
	}
	
	@Override
	public void doSomething() {
		if (product instanceof SiteProduct) {
			SiteProduct siteProduct = doSomeResearchOnSite((SiteProduct) product);
			if(siteProduct!=null){
				siteProduct.setProductState(ProductState.PROCESSED);
				LOGGER.error("Research success");
				Warehouse.getInstance().addProduct(siteProduct);
			}else{
				LOGGER.error("Research failed");
			}
		}

	}

	private SiteProduct doSomeResearchOnSite(SiteProduct siteProduct) {
		SiteJson siteJson = siteProduct.getSiteJson();
		Elements elements = siteProduct.getElements();
		
		if(siteJson == null){
			LOGGER.error("Invalid siteJson");
			return null;
		}else if(elements == null || elements.isEmpty()){
			LOGGER.error("Invalid elements");
			return null;
		}
		
		Map<String, Map<String, String>> tables = siteJson.getCreate();
		
		for (Entry<String, Map<String, String>> tableEntry : tables.entrySet()) {
			String tableName = tableEntry.getKey();
			System.out.println("Table: " + tableName);
			Map<String, String> columnDefenitions = tableEntry.getValue();
			Collection<Map<String, String>> rows = new ArrayList<Map<String, String>>();
			for (Element element : elements) {
				Map<String, String> row = new HashMap<String, String>();
				Iterator<String> columnIterator = columnDefenitions.keySet().iterator();
				while (columnIterator.hasNext()) {
					String column = columnIterator.next();
					row.put(column, element.select(columnDefenitions.get(column)).text());
				}
				rows.add(row);
			}
			siteProduct.addTablesAndRows(tableName, rows);
		}
		return siteProduct;
	}

	public IProduct getProduct() {
		return product;
	}

	public void setProduct(IProduct product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ScientistMinion [product=" + product + "]";
	}

}
