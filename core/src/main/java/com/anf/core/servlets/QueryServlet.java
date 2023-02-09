package com.anf.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.JcrPropertyPredicateEvaluator;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.foundation.List;

/**
 * @author vamsi mandalapu
 *
 */
@Component(service = { Servlet.class })
@SlingServletPaths(value = "/bin/fetchResults")
public class QueryServlet extends SlingSafeMethodsServlet {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	private QueryBuilder queryBuilder;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		String queryUsing = StringUtils.isNotBlank(req.getParameter("queryUsing")) ? req.getParameter("queryUsing")
				: "querybuilder";
		resp.setContentType("text/plain");
		if (queryUsing.equals("querybuilder")) {
			resp.getWriter().write(fetchResultsUsingQueryBuilder(req));
		} else if (queryUsing.equals("xpath")) {
			resp.getWriter().write(fetchResultsUsingXpath(req));
		}

	}

	private String fetchResultsUsingXpath(SlingHttpServletRequest req) {

		Session session = req.getResourceResolver().adaptTo(Session.class);
		String queryStatement = "/jcr:root/content/anf-code-challenge/us/en//element(*, cq:Page)\r\n" + "[\r\n"
				+ "(jcr:content/@anfCodeChallenge)\r\n" + "]\r\n" + "order by jcr:content/@jcr:created";
		QueryManager qm;
		StringBuilder sb = new StringBuilder();
		try {
			qm = session.getWorkspace().getQueryManager();
			javax.jcr.query.Query q = qm.createQuery(queryStatement, javax.jcr.query.Query.XPATH);
			QueryResult result = q.execute();
			RowIterator itr = result.getRows();
			int i = 0;
			while (itr.hasNext() && i<10) {
				Row row = itr.nextRow();
				sb.append("Page Xpath: " + row.getPath() + "\n");
				i++;
			}
			return sb.toString();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	private String fetchResultsUsingQueryBuilder(SlingHttpServletRequest req) {
		Map<String, String> params = new HashMap<>();
		params.put("path", "/content/anf-code-challenge/us/en");
		params.put("type", "cq:Page");
		params.put("p.limit", "10");
		params.put("1_property.operation", JcrPropertyPredicateEvaluator.OP_EXISTS);
		params.put("1_property", "jcr:content/anfCodeChallenge");
		params.put("orderby", "@jcr:content/jcr:created");
		params.put("orderby.sort", "asc");
		try {
			Query query = queryBuilder.createQuery(PredicateGroup.create(params),
					req.getResourceResolver().adaptTo(Session.class));
			logger.debug("suggestions query::{}", query.toString());

			SearchResult result = query.getResult();
			logger.debug("SuggestionProviderImpl result ::{}", result);

			StringBuilder responseString = new StringBuilder();
			for (Hit hit : result.getHits()) {
				responseString.append("Path QB : " + hit.getPath() + "\n");
			}

			return responseString.toString();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;

	}

}
