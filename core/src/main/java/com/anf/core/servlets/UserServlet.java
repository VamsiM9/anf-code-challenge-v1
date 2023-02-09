
package com.anf.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.services.ContentService;


/**
 * @author vamsi mandalapu
 *
 */
@Component(service = { Servlet.class })
@SlingServletPaths(value = "/bin/saveUserDetails")
public class UserServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String AGE_PATH = "/etc/age";

	@Reference
	private ContentService contentService;

	int minAge = 0;
	int maxAge = 0;
	int currentAge = 0;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		logger.debug("Inside doGet of UserServlet");
		String age = req.getParameter("age");
		ResourceResolver resolver = req.getResourceResolver();
		if (resolver != null) {
			Resource ageRes = resolver.getResource(AGE_PATH);
			if (ageRes != null) {
				ValueMap vm = ageRes.getValueMap();
				minAge = Integer.parseInt(vm.get("minAge", StringUtils.EMPTY));
				maxAge = Integer.parseInt(vm.get("maxAge", StringUtils.EMPTY));
				currentAge = Integer.parseInt(age);
				//check the user entered age lies in between the min and max value
				if(currentAge > minAge && currentAge < maxAge) {
					contentService.commitUserDetails(req);
				} else {
					resp.sendError(500);
				}
			}
			
		}

	}
}
