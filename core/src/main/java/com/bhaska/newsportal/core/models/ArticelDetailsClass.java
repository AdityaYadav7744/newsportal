package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = ArticleDetailsInterface.class,
        resourceType = "newsportal/components/article-details"
)
public class ArticelDetailsClass implements ArticleDetailsInterface {

    @ValueMapValue
    private String articleTitle;

    @ValueMapValue
    private String articelDesc;

    @ValueMapValue
    private String articleImage;

    @ValueMapValue
    private String articelDate;

    @Override
    public String getArticleTitle() {
        return articleTitle;
    }

    @Override
    public String getArticelDesc() {
        return articelDesc;
    }

    @Override
    public String getArticleImage() {
        return articleImage;
    }

    @Override
    public String getArticelDate() {
        return articelDate;
    }
}