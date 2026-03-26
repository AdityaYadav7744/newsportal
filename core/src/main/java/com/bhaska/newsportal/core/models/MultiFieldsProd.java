package com.bhaska.newsportal.core.models;



import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Model(adaptables = Resource.class , defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultiFieldsProd implements Multifield{
    @ValueMapValue
    private String prodTitle;
    @ValueMapValue
    private Date productExpiryDate;
    @ValueMapValue
    private String prodPrice;
    @ValueMapValue
    private String prodImg;
    @ValueMapValue
    private String prodColor;
    

    @ValueMapValue
    private Boolean productExpired;

    

    @Override
    public String getProdTitle() {
        return prodTitle;
    }



    @JsonIgnore
    @Override
    public Date getProductExpiryDate() {
        return productExpiryDate;
    }



    @Override
    public String getProdPrice() {
        return prodPrice;
    }



    @Override
    public String getProdImg() {
        return prodImg;
    }



    @Override
    public String getProdColor() {
        return prodColor;
    }



    @Override

    @JsonIgnore
    public Boolean getProductExpired() {
        return productExpired;
    }

    @PostConstruct
    public void inti() {
        if (productExpiryDate == null) {
            productExpired = null; // clearly cover null branch
        } else {
            // future = true, past = false
            Date today = new Date();
            productExpired = productExpiryDate.compareTo(today) > 0;
        }
    }

}
