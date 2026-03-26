package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = ArticleLandingInterface.class,
        resourceType = "newsportal/components/dynamic-article",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ArticleLandingClass implements ArticleLandingInterface {

    @ValueMapValue
    private String rootPath;

    @ValueMapValue
    private int maxArticles;

    @SlingObject
    private ResourceResolver resolver;

    private List<ArticleDetailsInterface> articles = new ArrayList<>();


    public String getRootPath() {
        return rootPath;
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public List<ArticleDetailsInterface> getArticles() {
        return articles;
    }

    @PostConstruct
    protected void init() {

        if (rootPath == null || resolver == null) {
            return;
        }

        Resource rootResource = resolver.getResource(rootPath);

        if (rootResource == null) {
            return;
        }

        findArticles(rootResource);

        if (maxArticles > 0 && articles.size() > maxArticles) {
            articles = articles.subList(0, maxArticles);
        }
    }

    private void findArticles(Resource resource) {

        if (resource == null) {
            return;
        }

        if (resource.isResourceType("newsportal/components/article-details")) {

            ArticleDetailsInterface article =
                    resource.adaptTo(ArticleDetailsInterface.class);

            if (article != null) {
                articles.add(article);
            }
        }

        for (Resource child : resource.getChildren()) {
            findArticles(child);
        }
    }
}