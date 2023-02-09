package com.anf.core.listeners;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.utils.CommonUtils;

/**
 * 
 * @author vamsi mandalapu
 *
 */

@Component(service = EventHandler.class, immediate = true, property = {
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED" })
@ServiceDescription("Listens when page is created")
public class PageCreateListener implements EventHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	ResourceResolverFactory resolverFactory;

	public void handleEvent(final Event event) {
		String path = event.getProperty(SlingConstants.PROPERTY_PATH).toString();

		if (path.startsWith("/content/anf-code-challenge/us/en") && path.endsWith("jcr:content")) {
			logger.debug("Path ::{}", path);
			ResourceResolver resolver = CommonUtils.getResourceResolver(resolverFactory, "anfService");
			try {
				Resource pageContentResource = resolver.getResource(path);
				logger.debug("resource checking ::{} and resolver checking ::{}", pageContentResource == null,
						resolver == null);
				if (pageContentResource != null) {
					logger.debug("Content Resource Exists ::{}", pageContentResource.getPath());
					// getting page content resource's valuemap.
					ValueMap vm = pageContentResource.getValueMap();
					vm.put("pageCreated", true);
					resolver.commit();
				}
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

}
