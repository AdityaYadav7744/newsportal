package com.bhaska.newsportal.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/lib/queryBuilder",
                "sling.servlet.methods=GET"
        }
)
public class QueryBuilderDemo extends SlingAllMethodsServlet {

    @Reference
    QueryBuilder queryBuilder;

    @Override
    protected void doGet( SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
        Session session = resolver.adaptTo(Session.class);

        Map<String, String> predicate=new HashedMap<>();
        predicate.put("type","cq:page");
        predicate.put("path","/content");
        predicate.put("limit","100");

        Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
        SearchResult result = query.getResult();

    }
}
