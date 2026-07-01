package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardDetailsModel  {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private List<String> publicDeals;

    @ValueMapValue
    private List<String> privateDeals;

    private List<DealCard> publicDealCards = new ArrayList<>();
    private List<DealCard> privateDealCards = new ArrayList<>();

    @PostConstruct
    protected void init() {
        populateDealCards(publicDeals, publicDealCards);
        populateDealCards(privateDeals, privateDealCards);
    }

    private void populateDealCards(List<String> dealPaths, List<DealCard> targetList) {
        if (dealPaths == null) return;

        for (String path : dealPaths) {
            String componentPath = path + "/jcr:content/root/container/deals_twotab_compone";

            Resource dealComponentResource = resourceResolver.getResource(componentPath);
            if (dealComponentResource != null) {
                ValueMap properties = dealComponentResource.getValueMap();

                DealCard card = new DealCard();
                card.setTitle(properties.get("cardTitle", String.class));
                card.setDescription(properties.get("cardDescription", String.class));
                card.setImagePath(properties.get("cardImageReference", String.class));

                targetList.add(card);
            }
        }
    }

    public List<DealCard> getPublicDealCards() { return publicDealCards; }
    public List<DealCard> getPrivateDealCards() { return privateDealCards; }

    public static class DealCard {
        private String title;
        private String description;
        private String imagePath;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImagePath() { return imagePath; }
        public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    }
}