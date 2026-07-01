package com.bhaska.newsportal.core.service.impl;

import com.bhaska.newsportal.core.service.BlogMigrationService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(service = BlogMigrationService.class)
public class BlogMigrationServiceImpl implements BlogMigrationService {

    private static final String TEMPLATE_PATH =
            "/conf/newsportal/settings/wcm/templates/page-content";

    private static final String PARENT_PAGE_PATH =
            "/content/newsportal/us/en/homePage";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void blogMigrationSerive(Set<String> urls) {

        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(
                ResourceResolverFactory.SUBSERVICE,
                "newsportal-subservice");

        try (ResourceResolver resolver =
                     resourceResolverFactory.getServiceResourceResolver(authInfo)) {

            PageManager pageManager =
                    resolver.adaptTo(PageManager.class);

            for (String pageUrl : urls) {

                try {

                    String html = fetchHtml(pageUrl);

                    if (html == null || html.isEmpty()) {
                        continue;
                    }

                    String title = extractTitle(html);
                    String description = extractMetaDescription(html);
                    String body = extractBody(html);
                    String image= extractImage(html);


                    String pageName = getPageName(pageUrl);

                    Page page = pageManager.getPage(
                            PARENT_PAGE_PATH + "/" + pageName);

                    if (page == null) {

                        page = pageManager.create(
                                PARENT_PAGE_PATH,
                                pageName,
                                TEMPLATE_PATH,
                                title
                        );
                    }

                    if (page != null) {

                        ModifiableValueMap properties =
                                page.getContentResource()
                                        .adaptTo(ModifiableValueMap.class);

                        if (properties != null) {

                            properties.put("jcr:title", title);
                            properties.put("jcr:description", description);
                            properties.put("sourceUrl", pageUrl);
                            properties.put("bodyHtml", body);
                            properties.put("image",image);
                        }
                    }
                    if (page != null) {

                        Resource contentResource = page.getContentResource();

                        Resource container =
                                contentResource.getChild("root/container/container");

                        if (container != null) {

                            Map<String, Object> componentProps = new HashMap<>();

                            componentProps.put(
                                    "sling:resourceType",
                                    "newsportal/components/migration-display-component");

                            resolver.create(
                                    container,
                                    "migratedcontent",
                                    componentProps);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            resolver.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractImage(String html) {

        Pattern pattern = Pattern.compile(
                "<img[^>]+src=[\"']([^\"']+)[\"']",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private String fetchHtml(String url) {

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response =
                         client.execute(request)) {

                return EntityUtils.toString(
                        response.getEntity(),
                        "UTF-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String extractTitle(String html) {

        Pattern pattern = Pattern.compile(
                "<title>(.*?)</title>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }

    private String extractMetaDescription(String html) {

        Pattern pattern = Pattern.compile(
                "<meta[^>]*name=[\"']description[\"'][^>]*content=[\"'](.*?)[\"'][^>]*>",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }

    private String extractBody(String html) {

        Pattern pattern = Pattern.compile(
                "<body[^>]*>(.*?)</body>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private String getPageName(String url) {

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        String pageName =
                url.substring(url.lastIndexOf("/") + 1);
        return pageName
                .replace(".html", "")
                .toLowerCase();
    }
}