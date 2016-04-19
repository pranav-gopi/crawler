/**
 * 
 */
package org.greenicon.crawler.minion;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.greenicon.crawler.model.ProductState;
import org.greenicon.crawler.model.SiteJson;
import org.greenicon.crawler.model.product.IProduct;
import org.greenicon.crawler.model.product.SiteProduct;
import org.greenicon.crawler.service.cron.Warehouse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

/**
 * My mission is to get into dangerous areas and collects data. 
 * I am the commando
 * 
 */
public class CommandoMinion extends AbstractMinion {

	private static final Logger LOGGER = Logger.getLogger(CommandoMinion.class);

	private IProduct product;

	public CommandoMinion() {
		super();
		setName("Commando");
	}

	public IProduct getProduct() {
		return product;
	}

	public void setProduct(IProduct product) {
		this.product = product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.greenicon.crawler.task.IMinion#doSomething()
	 */
	@Override
	public void doSomething() {
		LOGGER.info("starting operation");
		if (product == null) {
			LOGGER.error("invalid site info. Commando out");
			return;
		}
		if (product instanceof SiteProduct) {
			operationOnSiteProduct((SiteProduct) product);
		}
	}

	private void operationOnSiteProduct(SiteProduct siteProduct) {
		SiteJson siteJson = siteProduct.getSiteJson();
		if (siteJson == null || siteJson.getConfig() == null || siteJson.getProcess() == null) {
			LOGGER.error("invalid site info. Commando out");
			return;
		}
		Elements elements = operationHastaLaVista(siteJson.getConfig().getUrl(), siteJson.getProcess().getRoot());
		if (elements != null) {
			siteProduct.setElements(elements);
			siteProduct.setProductState(ProductState.RESOURCE_READY);
			Warehouse.getInstance().addProduct(siteProduct);
			LOGGER.debug("Operation Success");
		} else {
			LOGGER.debug("Operation Failed");
		}
	}

	private Elements operationHastaLaVista(String url, String selector) {
		if (StringUtils.hasText(url) && StringUtils.hasText(selector)) {
			LOGGER.info("url: " + url + ". selector: " + selector);
			Document document;
			try {
				document = Jsoup.connect(url).get();
				Elements elements = document.select(selector);
				return elements;
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else if (!StringUtils.hasText(url)) {
			LOGGER.info("Code RED. url null. Commando out");
		} else if (!StringUtils.hasText(selector)) {
			LOGGER.info("Code RED. selector null. Commando out");
		}
		return null;
	}

	@Override
	public String toString() {
		return "CommandoMinion [product=" + product + "]";
	}

}
