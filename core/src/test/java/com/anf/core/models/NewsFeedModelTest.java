package com.anf.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author vamsi mandalapu
 *
 */
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class NewsFeedModelTest {

	public static final String PRODUCTS_LIST = "/var/commerce/products/anf-code-challenge/newsData";

	AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	@BeforeEach
	public void setUp() {
		context.addModelsForClasses("com.anf.core.models");
		context.load().json("/products.json", "/var");
	}

	@Test
	@Disabled
	void doTest() {
		Resource res = context.resourceResolver().getResource(PRODUCTS_LIST);
		context.currentResource(res);
		NewsFeedModel model = context.request().adaptTo(NewsFeedModel.class);
		assertNotNull(model.newsList);
	}

}
