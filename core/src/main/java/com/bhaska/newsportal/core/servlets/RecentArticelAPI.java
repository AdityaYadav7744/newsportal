package com.bhaska.newsportal.core.servlets;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/newsportal/ad",
                "sling.servlet.methods=GET",
                "sling.servlet.methods=POST"
        }
)
public class RecentArticelAPI extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = resolver.getResource("/content/newsportal/my-articals");

        JsonObjectBuilder json = Json.createObjectBuilder();

        if (resource != null) {

            ValueMap valueMap = resource.getValueMap();

            String resourceType = valueMap.get("sling:resourceType", String.class);
            String type = valueMap.get("type", String.class);
            String limit = valueMap.get("limit", String.class);

            json.add("sling:resourceType", resourceType != null ? resourceType : "");
            json.add("type", type != null ? type : "");
            json.add("limit", limit != null ? limit : "");

        } else {
            json.add("error", "Resource not found");
        }

        response.getWriter().write(json.build().toString());
    }

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response)
            throws ServletException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = resolver.getResource("/content/newsportal/my-articals");

        String userId = request.getParameter("userId");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");

        if (resource != null && userId != null) {

            Map<String, Object> map = new HashedMap<>();
            map.put("email", email);
            map.put("firstName", firstName);
            map.put("lastName", lastName);
            map.put("password", password);

            resolver.create(resource, userId, map);
            resolver.commit();

            response.getWriter().write("Data Save Successfully");

        } else {
            response.getWriter().write("Resource or userId missing");
        }
    }
}