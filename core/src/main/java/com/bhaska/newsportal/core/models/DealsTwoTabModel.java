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
public class DealsTwoTabModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private List<String> publicDeals;

    @ValueMapValue
    private List<String> privateDeals;
    private String cardLink;
    private List<DealCardDetail> publicDealCards = new ArrayList<>();
    private List<DealCardDetail> privateDealCards = new ArrayList<>();

    private static final String LIVE_ROOT = "/content/newsportal/us/en/deals";

    @PostConstruct
    protected void init() {
        mapIdsToLiveContent(publicDeals, publicDealCards);
        mapIdsToLiveContent(privateDeals, privateDealCards);
    }

    // ... Inside DealsTwoTabModel.java ...

    private void mapIdsToLiveContent(List<String> selectedPaths, List<DealCardDetail> finalCardList) {
        if (selectedPaths == null) {
            return;
        }

        for (String configPath : selectedPaths) {
            String pageId = configPath.substring(configPath.lastIndexOf("/") + 1);

            if ("golden".equals(pageId)) {
                pageId = "golde";
            }

            String liveComponentPath = LIVE_ROOT + "/" + pageId + "/jcr:content/root/container/container/deals_cards";

            Resource cardResource = resourceResolver.getResource(liveComponentPath);
            if (cardResource != null) {
                ValueMap properties = cardResource.getValueMap();

                DealCardDetail card = new DealCardDetail();
                card.setCardTitle(properties.get("cardTitle", String.class));
                card.setCardDescription(properties.get("cardDescription", String.class));
                card.setCardImage(properties.get("cardImageReference", String.class));
                card.setCardLink(LIVE_ROOT + "/" + pageId + ".html");
                finalCardList.add(card);
            }
        }
    }

    public void setCardLink(String cardLink) { this.cardLink = cardLink; }
    public String getCardLink() { return cardLink; }
    public List<DealCardDetail> getPublicDealCards() {
        return publicDealCards;
    }

    public List<DealCardDetail> getPrivateDealCards() {
        return privateDealCards;
    }
}