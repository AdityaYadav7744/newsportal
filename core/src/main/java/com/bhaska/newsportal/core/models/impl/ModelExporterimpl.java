package com.bhaska.newsportal.core.models.impl;

import com.bhaska.newsportal.core.models.ModelExporter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(
        adaptables = Resource.class,
        resourceType = "newsportal/components/article-details",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        adapters = ModelExporter.class
)
@Exporter(name = "jackson",
        extensions = "json",
        selector = "model"
)
public class ModelExporterimpl implements ModelExporter {

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
