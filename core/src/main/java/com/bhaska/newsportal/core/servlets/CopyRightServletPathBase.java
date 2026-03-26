package com.bhaska.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/copyright",
                "sling.servlet.methods=GET"
        }
)
public class CopyRightServletPathBase extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String resourcePath = request.getParameter("path");

        if (resourcePath == null || resourcePath.isEmpty()) {
            response.getWriter().write("{\"error\":\"Path parameter is missing\"}");
            return;
        }

        Resource resource = request.getResourceResolver().getResource(resourcePath);

        if (resource == null) {
            response.getWriter().write("{\"error\":\"Resource not found\"}");
            return;
        }

        ValueMap properties = resource.getValueMap();

        String componentText = properties.get("component-Text", "");
        String copyrightText = properties.get("copyright-text", "");

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("componentText", componentText)
                .add("copyrightText", copyrightText);

        response.getWriter().write(json.build().toString());
    }
}