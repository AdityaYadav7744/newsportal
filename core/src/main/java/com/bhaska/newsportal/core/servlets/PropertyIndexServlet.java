package com.bhaska.newsportal.core.servlets;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
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
                "sling.servlet.paths=/bin/content/newsportal",
                "sling.servlet.methods=GET"
        }
)
public class PropertyIndexServlet extends SlingAllMethodsServlet {
    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String category = request.getParameter("category");
        String author = request.getParameter("author");
         JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        if(category!=null && author!=null){

            String query="SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE(s, [/content/newsportal])" +
                    " AND (s.[jcr:content/category]='" + category + "' OR s.[jcr:content/author]='" + author + "')";
            try {
                ResourceResolver resolverResolver = request.getResourceResolver();
                Iterator<Resource> resources = resolverResolver.findResources(query, "JCR-SQL2");
                while(resources.hasNext()){
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    Resource next = resources.next();
                    objectBuilder.add("path" ,next.getPath());
                    objectBuilder.add("name" ,next.getName());
                    arrayBuilder.add(objectBuilder);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(arrayBuilder.build().toString());
    }
}
