package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductItemDetails {

    @ValueMapValue
    private String productTitle;

    @ValueMapValue
    private String productExpiry; // CHANGED

    @ValueMapValue
    private int productprice;

    @ValueMapValue
    private String productImage;

    @ValueMapValue
    private String productColor;

    @ValueMapValue
    private String productTag;

    private boolean expired;

    public boolean isExpired() {
        return expired;
    }

    public String getProductTag() {
        return productTag;
    }

    public String getProductColor() {
        return productColor;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getProductprice() {
        return productprice;
    }

    public String getProductExpiry() {
        return productExpiry;
    }

    public String getProductTitle() {
        return productTitle;
    }

    @PostConstruct
    public void init() {
        try {
            if (productExpiry != null) {
                Date expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(productExpiry);
                Date today = new Date();
                expired = expiryDate.before(today);
            }
        } catch (Exception e) {
            expired = false;
        }
    }
}