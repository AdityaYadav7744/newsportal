package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCardModel {

    @ValueMapValue
    private String productTitle;

    @ValueMapValue
    private String productPrice;

    @ValueMapValue
    private String productColor;

    @ValueMapValue
    private String productImage;

    @ValueMapValue
    private String productExpiry;

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductColor() {
        return productColor;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductExpiry() {
        return productExpiry;
    }
}