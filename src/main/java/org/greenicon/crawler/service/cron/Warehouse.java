package org.greenicon.crawler.service.cron;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.greenicon.crawler.model.ProductState;
import org.greenicon.crawler.model.product.IProduct;

public class Warehouse extends Observable{

	private static final Logger LOGGER = Logger.getLogger(Warehouse.class);
	private final Map<ProductState, BlockingQueue<IProduct>> productQueueMap;
	private static final Warehouse instance = new Warehouse();
	
	private Warehouse() {
		productQueueMap = new HashMap<>();
		for(ProductState productState:ProductState.values()){
			productQueueMap.put(productState, new LinkedBlockingQueue<IProduct>());
		}
	}
	
	public static final Warehouse getInstance(){
		return instance;
	}
	
	public void addProduct(IProduct product){
		LOGGER.debug("Adding new product to Warehouse");
		this.productQueueMap.get(product.getProductState()).add(product);
		setChanged();
		notifyObservers(product.getProductState());
		LOGGER.debug("Notified observers");
	}
	
	public Collection<IProduct> getProducts(ProductState productState) {
		LOGGER.debug("Getting products of state: "+productState);
		BlockingQueue<IProduct> products = productQueueMap.get(productState);
		Set<IProduct> productsSet = new HashSet<>();
		products.drainTo(productsSet);
		LOGGER.debug("returning products. size: "+products.size());
		return productsSet;
		
	}
}
