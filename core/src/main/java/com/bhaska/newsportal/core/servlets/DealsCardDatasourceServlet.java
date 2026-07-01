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
import java.util.*;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.resourceTypes=newsportal/cards/datasource/deals",
                "sling.servlet.methods=GET"
        }
)
public class DealsCardDatasourceServlet extends SlingAllMethodsServlet {
    private static final String DEALS_ROOT = "/content/newsportal/deals";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        ResourceResolver resolver = request.getResourceResolver();
        List<Resource> options = new ArrayList<>();
        Resource root = resolver.getResource(DEALS_ROOT);
        if (root == null) {
            return;
        }

        Iterator<Resource> iterator = root.listChildren();

        while (iterator.hasNext()) {

            Resource child = iterator.next();
            Page page = child.adaptTo(Page.class);

            if (page == null) {
                continue;
            }

            addOption(options, resolver, page);
        }

        DataSource dataSource = new SimpleDataSource(options.iterator());
        request.setAttribute(DataSource.class.getName(), dataSource);
    }

    private void addOption(List<Resource> options, ResourceResolver resolver, Page page) {

        Map<String, Object> map = new HashMap<>();
        map.put("text", page.getTitle());
        map.put("value", page.getPath());

        ValueMap valueMap = new ValueMapDecorator(map);

        options.add(new ValueMapResource(
                resolver,
                new ResourceMetadata(),
                "nt:unstructured",
                valueMap
        ));
    }
}
