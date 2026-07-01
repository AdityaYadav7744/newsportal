package com.bhaska.newsportal.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/sitemap/servlet"
        }
)
public class QueryBUilderDemoServlet extends SlingAllMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
         ResourceResolver resolverResolver = request.getResourceResolver();
         Session session = resolverResolver.adaptTo(Session.class);
         Map<String, String> map=new HashMap<>();
         map.put("path","/content");
         map.put("type","cq:Page");
         Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
         SearchResult result = query.getResult();
        PageManager pageManager=resolverResolver.adaptTo(PageManager.class);

         for (Hit hit :result.getHits()){
             try {
                 Resource resource = hit.getResource();
                  Page page = pageManager.getContainingPage(resource);
                  response.getWriter().write(page.getPath().toString()+"/n");
             } catch (RepositoryException e) {
                 throw new RuntimeException(e);
             }
         }

    }
}
