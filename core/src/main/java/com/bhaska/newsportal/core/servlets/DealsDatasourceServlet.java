package com.bhaska.newsportal.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.resourceTypes=newsportal/datasource/deals",
                "sling.servlet.methods=GET"
        }
)
public class PrivateDealsDatasourceServlet extends SlingAllMethodsServlet {

    private static final String DEALS_ROOT = "/content/newsportal/deals";

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {

        ResourceResolver resolver =
                request.getResourceResolver();
        

        List<Resource> options = new ArrayList<>();

        Resource dealsRoot =
                resolver.getResource(DEALS_ROOT);

        if (dealsRoot == null) {
            request.setAttribute(
                    DataSource.class.getName(),
                    new SimpleDataSource(options.iterator()));
            return;
        }

        Iterator<Resource> children = dealsRoot.listChildren();

        while (children.hasNext()) {

            Resource child = children.next();
            Page dealPage = child.adaptTo(Page.class);

            if (dealPage == null) {
                continue;
            }

            Resource dealsComponent = dealPage.getContentResource().getChild("root/container/deals");
            if (dealsComponent == null) {
                continue;
            }
            boolean privateDeal = dealsComponent.getValueMap().get("privateDeals", false);


            if (privateDeal) {
                Map<String, Object> map = new HashMap<>();
                map.put("text", dealPage.getTitle());
                map.put("value", dealPage.getPath());
                ValueMap vm = new ValueMapDecorator(map);
                options.add(  new ValueMapResource(
                                resolver,
                                new ResourceMetadata(),
                                "nt:unstructured",
                                vm));
            }
        }
        DataSource ds = new SimpleDataSource(options.iterator());

        request.setAttribute(
                DataSource.class.getName(),
                ds);
    }
}