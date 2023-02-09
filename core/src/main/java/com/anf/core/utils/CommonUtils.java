package com.anf.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author vamsi mandalapu
 *
 */
public class CommonUtils {
	
	private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);
	private static final String DEFAULT_SUBSERVICE_USERNAME = "anfServiceUser";

	public static ResourceResolver getResourceResolver(ResourceResolverFactory resolverFactory, String userName) {
		log.debug("Fetching ResourceResolver for the user ::{}", userName);
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isBlank(userName)) {
			userName = DEFAULT_SUBSERVICE_USERNAME;
		}
		params.put(ResourceResolverFactory.SUBSERVICE, userName);
		try {
			if (null != resolverFactory) {
				return resolverFactory.getServiceResourceResolver(params);
			}
		} catch (LoginException e) {
			log.error("Unable to fetch ResoureResolver ::{}", e.getMessage());
		}
		return null;
	}

}





