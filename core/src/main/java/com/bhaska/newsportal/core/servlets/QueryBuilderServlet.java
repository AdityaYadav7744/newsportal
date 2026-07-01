package com.bhaska.newsportal.core.servlets;


import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/getdata",
                "sling.servlet.methods=GET"
        }
)
public class QueryBuilderServlet extends SlingAllMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet( SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {

       ResourceResolver resourceResolver= request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        Map <String , String> map=new HashMap<>();
        map.put("type","cq:Page");
        map.put("path","/content/newsportal");
        map.put("p.limit","-1");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for(Hit hit : result.getHits()){
          JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            try {
                Resource resource = hit.getResource();
                Resource child = resource.getChild("jcr:content");
                ValueMap valueMap = child.adaptTo(ValueMap.class);
                objectBuilder.add("template",valueMap.get("cq:template", ""));
                objectBuilder.add("title",valueMap.get("jcr:title",""));
                objectBuilder.add("path",resource.getPath());
                arrayBuilder.add(objectBuilder);

            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(arrayBuilder.build().toString());
    }
}
