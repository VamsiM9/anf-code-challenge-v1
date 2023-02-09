package com.anf.core.services.impl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.services.ContentService;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String STORE_LOCATION = "/var/anf-code-challenge";


	@Override
	public void commitUserDetails(SlingHttpServletRequest request) {
		logger.debug("Inside Service USer Details");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String age = request.getParameter("age");
		ResourceResolver resolver = request.getResourceResolver();
		if (resolver != null) {
			Resource anfRes = resolver.getResource(STORE_LOCATION);
			if (anfRes != null) {
				try {
					Node varResNode = anfRes.adaptTo(Node.class);
					varResNode.setProperty("firstName", firstName);
					varResNode.setProperty("age", age);
					varResNode.setProperty("lastName", lastName);
					resolver.commit();
				} catch (RepositoryException | PersistenceException e) {
					logger.error("Error while saving "+e.getMessage());
				}

			} else {
				Resource varRes = resolver.getResource("/var");
				Node varResNode = varRes.adaptTo(Node.class);
				Node anfNode;
				try {
					anfNode = varResNode.addNode("anf-code-challenge");
					anfNode.setProperty("firstName", firstName);
					anfNode.setProperty("lastName", lastName);
					anfNode.setProperty("age", age);
					resolver.commit();
				} catch (RepositoryException | PersistenceException e) {
					logger.error("Error while saving "+e.getMessage());
				}

			}
		}
	}
}
