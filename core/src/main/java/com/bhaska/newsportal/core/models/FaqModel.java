package com.bhaska.newsportal.core.models.impl;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables ={  Resource.class, HttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FaqModel {

    @SlingObject
    private Resource resource;
    @ValueMapValue
    private boolean disableLazyLoading;
    @ValueMapValue
    private String cardType;
    @ValueMapValue
    private boolean showAsCarousel;
    @ValueMapValue
    private String rootPath;
    @ValueMapValue
    private  String clickAction;

    private List<ManualCards> cards=new ArrayList<>();

    @PostConstruct
    private void init(){
        if (cardType.equalsIgnoreCase("manual")){
            readManualCard();

        } else if (cardType.equalsIgnoreCase("dynamic")) {
        }
    }

    private void readManualCard(){
         Resource children = resource.getChild("manualCards");
         if(children==null){
             return;
         }
         for (Resource item : children.getChildren()){
             ValueMap vm = item.getValueMap();
             cards.add(new ManualCards(
                     vm.get("cardTitle", ""),
                     vm.get("cardImage", ""),
                     vm.get("shortDescription", ""),
                     vm.get("fullDescription", ""),
                     vm.get("ctaText", ""),
                     vm.get("ctaLink", "")
             ));

         }
    }

    public List<ManualCards> getCards() {
        return cards;
    }

    public Resource getResource() {
        return resource;
    }

    public boolean isDisableLazyLoading() {
        return disableLazyLoading;
    }

    public String getCardType() {
        return cardType;
    }

    public boolean isShowAsCarousel() {
        return showAsCarousel;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getClickAction() {
        return clickAction;
    }


}
