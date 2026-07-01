package com.bhaska.newsportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import com.day.cq.replication.ReplicationStatus;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/customSitemap",
                "sling.servlet.methods=GET"
        }
)
public class CustomSiteMap extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/xml");

        PrintWriter writer = response.getWriter();

        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        ResourceResolver resolver = request.getResourceResolver();
        Resource root = resolver.getResource("/content/newsportal/us/en");
        if (root != null) {
            traverse(root, writer);
        }
        writer.println("</urlset>");
    }

    private void traverse(Resource resource, PrintWriter writer) {

        for (Resource child : resource.getChildren()) {
            if ("cq:Page".equals(child.getValueMap().get("jcr:primaryType", ""))) {
                Resource content = child.getChild("jcr:content");
                if (content != null) {
                    ReplicationStatus repStatus = content.adaptTo(ReplicationStatus.class);
                    if (repStatus != null && repStatus.isActivated()) {
                        writer.println("<url>");
                        writer.println("<loc>http://localhost:4502" + child.getPath() + ".html</loc>");
                        writer.println("</url>");
                    }
                }
                traverse(child, writer);
            }
        }
    }
}