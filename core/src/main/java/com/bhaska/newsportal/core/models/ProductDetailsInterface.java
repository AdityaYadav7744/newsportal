package com.bhaska.newsportal.core.models;

import java.util.List;

public interface ProductDetailsInterface {
    public String getTitle();
    public String getDescription();
    public String getStatus();
    public String getCategory();
    public boolean isLoadFromCF();
    public List<ProductItemDetails> getProducts();
}
