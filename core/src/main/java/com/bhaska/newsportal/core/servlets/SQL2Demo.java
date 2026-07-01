package com.bhaska.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
                "sling.servlet.paths=/bin/sqldemo",
                "sling.servlet.methods=GET"
        }
)
public class SQL2Demo extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        // Set response type
        response.setContentType("application/json");

        // SQL2 Query (optimized)
        String query = "SELECT * FROM [cq:Page] WHERE ISDESCENDANTNODE('/content')";

        ResourceResolver resolver = request.getResourceResolver();
        Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);

        // JSON Array
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        while (result.hasNext()) {
            Resource res = result.next();

            // Access jcr:content node
            Resource contentRes = res.getChild("jcr:content");

            if (contentRes != null) {
                ValueMap map = contentRes.getValueMap();

                // Safe value extraction (no null issue)
                String firstName = map.get("firstName", "");
                String lastName = map.get("lastName", "");
                String title = map.get("jcr:title", "");

                // Build JSON object
                JsonObjectBuilder obj = Json.createObjectBuilder();
                obj.add("firstName", firstName);
                obj.add("lastName", lastName);
                obj.add("title", title);

                // Add to array
                arrayBuilder.add(obj);
            }
        }

        // Final output
        response.getWriter().write(arrayBuilder.build().toString());
    }
}