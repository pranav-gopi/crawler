package org.greenicon.crawler.minion;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.greenicon.crawler.model.ProductState;
import org.greenicon.crawler.model.product.IProduct;
import org.greenicon.crawler.service.cron.Warehouse;
import org.greenicon.crawler.util.TaskExecutor;


/**
 * I just manages minions. ;)
 * 
 *
 */
public class SupervisorMinion extends AbstractMinion implements Observer {

	private static final Logger LOGGER = Logger.getLogger(SupervisorMinion.class);
	
	private TaskExecutor labourUnion;
	
	private final Queue<IProduct> products;
	private final Object _lock = new Object();

	public SupervisorMinion() {
		products = new ConcurrentLinkedQueue<>();
		setName("Supervisor");
	}

	@Override
	public void doSomething() {
		while (true) {
			while (!products.isEmpty()) {
				IProduct product = products.poll();
				switch (product.getProductState()) {
					case RESOURCE_READY:
						System.out.println("give to scientist");
						ScientistMinion scientistMinion = new ScientistMinion();
						scientistMinion.setProduct(product);
						labourUnion.execute(scientistMinion);
						break;
					case PROCESSED:
						System.out.println("give for delivery");
						DeliveryMinion deliveryMinion = new DeliveryMinion();
						deliveryMinion.setProduct(product);
						labourUnion.execute(deliveryMinion);
						break;
					default:
						LOGGER.info("State not identified");
				}

			}
			try {
				synchronized (_lock) {
					_lock.wait();
				}
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void goToWarehouse(Warehouse warehouse) {
		warehouse.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Warehouse && arg instanceof ProductState) {
			Warehouse warehouse = (Warehouse) o;
			ProductState productState = (ProductState) arg;
			Collection<IProduct> products = warehouse.getProducts(productState);
			this.products.addAll(products);
			try {
				synchronized (_lock) {
					_lock.notify();
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

		}
	}

	public TaskExecutor getLabourUnion() {
		return labourUnion;
	}

	public void setLabourUnion(TaskExecutor labourUnion) {
		this.labourUnion = labourUnion;
	}

	
}
