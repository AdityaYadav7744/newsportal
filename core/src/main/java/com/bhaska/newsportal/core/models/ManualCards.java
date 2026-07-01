package com.bhaska.newsportal.core.models;

public class ManualCards {
    private String cardTitle;
    private String cardImage;
    private String shortDescription;
    private String fullDescription;
    private String ctaText;
    private String ctaLink;
    public String getCardTitle() {
        return cardTitle;
    }

    public ManualCards(String cardTitle, String cardImage, String shortDescription, String fullDescription, String ctaText, String ctaLink) {
        this.cardTitle = cardTitle;
        this.cardImage = cardImage;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.ctaText = ctaText;
        this.ctaLink = ctaLink;
    }

    public String getCardImage() {
        return cardImage;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getCtaText() {
        return ctaText;
    }

    public String getCtaLink() {
        return ctaLink;
    }
    @Override
    public String toString() {
        return "ManualCards{" +
                "cardTitle='" + cardTitle + '\'' +
                ", cardImage='" + cardImage + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", ctaText='" + ctaText + '\'' +
                ", ctaLink='" + ctaLink + '\'' +
                '}';
    }
}
