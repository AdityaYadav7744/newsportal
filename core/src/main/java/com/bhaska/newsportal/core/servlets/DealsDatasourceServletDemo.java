package com.bhaska.newsportal.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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
public class DealsDatasourceServletDemo extends SlingSafeMethodsServlet {
    private static final String DEALS_ROOT = "/content/newsportal/deals";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
        List<Resource> options = new ArrayList<>();

        Resource resource = request.getResource().getChild("datasource");

        if (resource == null) {
            return;
        }

        String dealType = resource.getValueMap().get("type", String.class);

        Resource dealResource = resolver.getResource(DEALS_ROOT);

        if (dealResource != null) {

            Iterator<Resource> iterator = dealResource.listChildren();

            while (iterator.hasNext()) {

                Page page = iterator.next().adaptTo(Page.class);

                if (page == null) {
                    continue;
                }

                Resource dealComponent =
                        page.getContentResource().getChild("root/container/deals");

                if (dealComponent == null) {
                    continue;
                }

                boolean privateDeal =
                        dealComponent.getValueMap().get("privateDeals", false);

                if ("private".equals(dealType) && privateDeal) {
                    addOption(options, resolver, page);
                }

                if ("public".equals(dealType) && !privateDeal) {
                    addOption(options, resolver, page);
                }
            }
        }

        DataSource dataSource = new SimpleDataSource(options.iterator());
        request.setAttribute(DataSource.class.getName(), dataSource);
    }

    private void addOption(List<Resource> options, ResourceResolver resolver, Page page) {

        Map<String, Object> map = new HashMap<>();
        map.put("text", page.getTitle());
        map.put("value", page.getPath());
        ValueMap valueMap = new ValueMapDecorator(map);
        options.add(
                new ValueMapResource(
                        resolver,
                        new ResourceMetadata(),
                        "nt:unstructured",
                        valueMap
                )
        );
    }

}