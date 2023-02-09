package com.anf.core.models;

/**
 * @author vamsi mandalapu
 *
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author vamsi mandalapu
 *
 */
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class NewsFeedModel {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String PRODUCTS_PATH = "/var/commerce/products/anf-code-challenge/newsData";

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	Resource resource;

	String date;

	List<NewsObject> newsList = new ArrayList<>();

	@PostConstruct
	protected void init() {
		logger.debug("Inside Model ::");
		ResourceResolver resolver = request.getResourceResolver();
		Resource products = resolver.getResource(PRODUCTS_PATH);
		logger.debug("products resource ::{}", products == null);
		if (products != null) {
			logger.debug("Products Path ::{}", products.getPath());
			Iterator<Resource> itr = products.listChildren();
			while (itr.hasNext()) {
				Resource res = itr.next();
				NewsObject obj = new NewsObject();
				ValueMap vm = res.getValueMap();
				obj.setAuthor(vm.get("author", String.class));
				obj.setContent(vm.get("content", String.class));
				obj.setDescription(vm.get("description", String.class));
				obj.setTitle(vm.get("title", String.class));
				obj.setUrl(vm.get("url", String.class));
				obj.setUrlImage(vm.get("urlImage", String.class));
				newsList.add(obj);
			}
		}
	}

	public List<NewsObject> getNewsList() {
		return newsList;
	}

	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(new Date());
	}
}

