package com.bhaska.newsportal.core.workflow;

public class ProductDetails {

    private String ProductName;
    private String ProductDescription;
    private String productPrice;
    private String productImage;

    public String getProductName() {
        return ProductName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public ProductDetails(String productName, String productDescription, String productPrice, String productImage) {
        ProductName = productName;
        ProductDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

}
