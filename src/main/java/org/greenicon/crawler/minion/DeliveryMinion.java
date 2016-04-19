package org.greenicon.crawler.minion;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.greenicon.crawler.model.product.IProduct;
import org.greenicon.crawler.model.product.SiteProduct;

/**
 * It not who I am underneath, but what I do that defines me :D
 * 
 *
 */
public class DeliveryMinion extends AbstractMinion {

	private static final Logger LOGGER = Logger.getLogger(DeliveryMinion.class);
	
	private IProduct product;
	
	public DeliveryMinion() {
		setName("DeliveryMinion");
	}
	
	@Override
	public void doSomething() {
		if(product instanceof SiteProduct){
			deliverSiteProduct((SiteProduct)product);
		}
	}

	private void deliverSiteProduct(SiteProduct product) {
		if(product!=null && product.getTablesAndRows()!=null){
			for(Entry<String, Collection<Map<String, String>>> tableEntry: product.getTablesAndRows().entrySet()){
				LOGGER.info("Table name: "+tableEntry.getKey());
				LOGGER.info("Rows: ");
				for(Map<String, String> row: tableEntry.getValue()){
					LOGGER.info(row);
				}
			}
			LOGGER.info("To be delivered");
		}
	}

	public IProduct getProduct() {
		return product;
	}

	public void setProduct(IProduct product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "DeliveryMinion [product=" + product + "]";
	}
	
}
