package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = {SlingHttpServletRequest.class , Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class OpenGraphModel {

    @ValueMapValue
    private String ogTitle;

    @ValueMapValue
    private String ogDescription;

    @ValueMapValue
    private String ogImage;

    @ValueMapValue
    private String ogType;

    @ValueMapValue
    private String metaTitle;

    @ValueMapValue
    private String metaDescription;

    @ScriptVariable
    private Page currentPage;

    public String getOgTitle() {

        if (StringUtils.isNotBlank(ogTitle)) {
            return ogTitle;
        }

        return metaTitle != null
                ? metaTitle
                : currentPage.getTitle();
    }


    public String getOgDescription() {

        if (StringUtils.isNotBlank(ogDescription)) {
            return ogDescription;
        }

        return metaDescription;
    }

    public String getOgImage() {
        return ogImage;
    }


    public String getOgType() {

        return StringUtils.isNotBlank(ogType)
                ? ogType
                : "website";
    }
}