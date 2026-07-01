package com.bhaska.newsportal.core.models;

public class DealCardDetail {
    private String cardTitle;
    private String cardDescription;
    private String cardImage;
    private String cardLink;

    public void setCardLink(String cardLink) {
        this.cardLink = cardLink;
    }

    public String getCardLink() {
        return cardLink;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }
}