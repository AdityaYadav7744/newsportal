package com.bhaska.newsportal.core.models;

import java.util.Date;

public interface Multifield {
    String getProdTitle();
    Date getProductExpiryDate();
    String getProdPrice();
    String getProdImg();
    String getProdColor();
    Boolean getProductExpired();

}
