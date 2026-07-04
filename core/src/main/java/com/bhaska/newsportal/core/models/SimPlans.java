package com.bhaska.newsportal.core.models;


public class SimPlans {

    private String planName;
    private String uuid;
    private String price;
    private String validityDays;
    private String planDescription;

    public SimPlans(String planName, String uuid, String price, String validityDays, String planDescription) {
        this.planName = planName;
        this.uuid = uuid;
        this.price = price;
        this.validityDays = validityDays;
        this.planDescription = planDescription;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setValidityDays(String validityDays) {
        this.validityDays = validityDays;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPrice() {
        return price;
    }

    public String getValidityDays() {
        return validityDays;
    }

    public String getPlanDescription() {
        return planDescription;
    }
}
