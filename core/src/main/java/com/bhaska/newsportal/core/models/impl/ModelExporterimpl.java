package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(
        adaptables = Resource.class,
        resourceType = "newsportal/components/article-details",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = "jackson",
        extensions = "json",
        selector = "modelexporter"
)
public class ModelExporter {

    @ValueMapValue
    private String articleTitle;

    @ValueMapValue
    private String articelDesc;

    @ValueMapValue
    private String articleImage;

    @ValueMapValue
    private String articelDate;

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticelDesc() {
        return articelDesc;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public String getArticelDate() {
        return articelDate;
    }
}
