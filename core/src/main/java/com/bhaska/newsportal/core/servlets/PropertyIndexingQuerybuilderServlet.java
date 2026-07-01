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
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
                "sling.servlet.paths=/bin/propertyindex",
                "sling.servlet.methods=GET"
        }
)

public class PropertyIndexingQuerybuilderServlet extends SlingAllMethodsServlet {


    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet( SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {


         String category = request.getParameter("category");
         String admin = request.getParameter("author");

        ResourceResolver resolver = request.getResourceResolver();
         Session session = resolver.adaptTo(Session.class);
         JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {
            Map<String , String> map=new HashMap<>();
            map.put("type","cq:Page");
            map.put("path","/content/newsportal");
            map.put("group.p.or","true");
            map.put("group.1_property","jcr:content/category");
            map.put("group.1_property.value",category);

            map.put("group.2_property","jcr:content/author");
            map.put("group.2_property.value",admin);
            Query query = queryBuilder.createQuery(new PredicateGroup().create(map), session);
            SearchResult result = query.getResult();

            for(Hit hit : result.getHits()) {
                 JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                Resource resource = hit.getResource();
                objectBuilder.add("path" , resource.getPath());
                arrayBuilder.add(objectBuilder);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        response.setContentType("application/json");
        response.getWriter().write(arrayBuilder.build().toString());
    }
}
