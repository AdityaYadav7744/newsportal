package com.bhaska.newsportal.core.servlets;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/page-data/servlet",

        }
)
public class SQL2 extends SlingAllMethodsServlet {
  private String query="SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content])\n" +
          "AND s.[jcr:content/cq:template]='/conf/newsportal/settings/wcm/templates/page-content'\n" +
          "AND s.[jcr:content/jcr:title]=\"News Portal\"";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolverReolver = request.getResourceResolver();
        Iterator<Resource> resources = resolverReolver.findResources(query, Query.JCR_SQL2);
        JsonArrayBuilder jsonArrayBuilder= Json.createArrayBuilder();
        if(resources!=null){
            while (resources.hasNext()){
                Resource chieldResource = resources.next();
                Page page = chieldResource.adaptTo(Page.class);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("jcr:title",page.getTitle());
                objectBuilder.add("path",page.getPath());
                objectBuilder.add("cq:template", page.getTemplate().toString());
                jsonArrayBuilder.add(objectBuilder);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(jsonArrayBuilder.build().toString());
    }
}
