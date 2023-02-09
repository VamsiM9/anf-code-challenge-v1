package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * @author vamsi mandalapu
 *
 */
public interface ContentService {
	void commitUserDetails(SlingHttpServletRequest request);
}
