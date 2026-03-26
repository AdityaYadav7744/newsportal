package com.bhaska.newsportal.core.servlets;

import com.adobe.cq.wcm.core.components.models.Page;
import com.bhaska.newsportal.core.schedulers.WeeklySiteReportJobConsumer;
import org.apache.jackrabbit.webdav.property.ResourceType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.Provider;
import java.util.Iterator;

@Component(service = Servlet.class)
@SlingServletPaths( value = "/bin/newsportal/getdata")
public class ServletAPIDemo extends SlingAllMethodsServlet {

    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String query="SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([/content]) ";

        ResourceResolver resolver=request.getResourceResolver();
        Iterator<Resource> result = resolver.findResources(Query.JCR_SQL2, query);

        JsonArrayBuilder jsonObj= Json.createArrayBuilder();

        while(result.hasNext()){
            Resource res = result.next();
            Page page = res.adaptTo(Page.class);
            String title = page.getTitle();



        }


    }
}
