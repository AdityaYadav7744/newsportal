package com.bhaska.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "newsportal/components/copy-rights",
        methods = "GET",
        selectors = "copyright",
        extensions = "json"
)
public class CopyRightResourceBaseServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        Resource resource = request.getResource();
        ValueMap map = resource.getValueMap();

        String componentText = map.get("component-Text", String.class);
        String copyrightText = map.get("copyright-text", String.class);

        JsonObjectBuilder jsonData = Json.createObjectBuilder();

        // ✅ Handle null safely
        if (componentText != null) {
            jsonData.add("componentText", componentText);
        } else {
            jsonData.add("componentText", "");
        }

        if (copyrightText != null) {
            jsonData.add("copyrightText", copyrightText);
        } else {
            jsonData.add("copyrightText", "");
        }

        response.getWriter().write(jsonData.build().toString());
    }
}