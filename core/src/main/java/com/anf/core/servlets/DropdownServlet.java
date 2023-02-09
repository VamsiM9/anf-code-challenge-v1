package com.anf.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

/**
 * @author vamsi mandalapu
 *
 */
@Component(name = "DropDown Values GET Servlet", service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "anf-code-challenge/components/form/country/dropdown", methods = "GET")
public class DropdownServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		try {
			ResourceResolver resolver = request.getResourceResolver();
			// set fallback
			request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
			logger.debug("Dropdown resource path ::{}", request.getResource().getPath());
			Resource datasource = request.getResource().getChild("datasource");
			ValueMap datasourceVM = ResourceUtil.getValueMap(datasource);
			String optionJosn = datasourceVM.get("options", String.class);
			String jsonStr = null;
			if (optionJosn != null) {
				Resource jsonResource = resolver
						.getResource(optionJosn + "/jcr:content/renditions/original/jcr:content");
				if (!ResourceUtil.isNonExistingResource(jsonResource)) {
					Node cfNode = jsonResource.adaptTo(Node.class);
					InputStream in = cfNode.getProperty("jcr:data").getBinary().getStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					jsonStr = sb.toString();
					reader.close();
				}
				List<Resource> optionResourceList = new ArrayList<Resource>();
				logger.debug("JSON String ::{}", jsonStr);
				JSONObject jsonObj = new JSONObject(jsonStr);
				logger.debug("Json Object ::{}", jsonObj);
				Iterator<String> keys = jsonObj.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					String value = jsonObj.getString(key);
					ValueMap vm = new ValueMapDecorator(new HashMap<>());
					vm.put("value", value);
					vm.put("text", key);
					optionResourceList
							.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
				}

				DataSource ds = new SimpleDataSource(optionResourceList.iterator());
				request.setAttribute(DataSource.class.getName(), ds);

			} else {
				logger.info("options property is missing in datasource node ");
			}

		} catch (Exception e) {
			logger.error("Error occured ::{}", e.getMessage());
		}
	}

}
