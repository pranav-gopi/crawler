package org.greenicon.crawler.model.product;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.greenicon.crawler.model.ProductState;
import org.greenicon.crawler.model.SiteJson;
import org.jsoup.select.Elements;

public class SiteProduct implements IProduct{

	private SiteJson siteJson;
	private Elements elements;
	private Map<String, Collection<Map<String, String>>> tablesAndRows;
	
	
	private ProductState productState;

	
	public SiteProduct() {
		productState = ProductState.INITIALIZED;
	}
	
	public Elements getElements() {
		return elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}
	
	public void setProductState(ProductState productState) {
		this.productState = productState;
	}

	public SiteJson getSiteJson() {
		return siteJson;
	}

	public void setSiteJson(SiteJson siteJson) {
		this.siteJson = siteJson;
	}
	


	public Map<String, Collection<Map<String, String>>> getTablesAndRows() {
		return Collections.unmodifiableMap(tablesAndRows);
	}

	public void addTablesAndRows(String tableName, Collection<Map<String, String>> rows) {
		if(tablesAndRows == null){
			synchronized (this) {
				if(tablesAndRows == null){
					tablesAndRows = new HashMap<>();
				}
			}
		}
		if(tablesAndRows.containsKey(tableName))
			tablesAndRows.get(tableName).addAll(rows);
		else
			tablesAndRows.put(tableName, rows);
		
	}

	@Override
	public String toString() {
		return "SiteProduct [siteJson=" + siteJson + ", elements=" + elements + ", tablesAndRows=" + tablesAndRows
				+ ", productState=" + productState + "]";
	}

	@Override
	public ProductState getProductState() {
		return productState;
	}
	
}
