package com.bhaska.newsportal.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
/**
 * This model class is use for the showing the cards on child pages like
 * **/
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DealCardModel {

    @ValueMapValue(name = "selectedDeal")
    private String selectedDeal;

    @ValueMapValue(name = "cardTitle")
    private String cardTitle;

    @ValueMapValue(name = "cardDescription")
    private String cardDescription;

    @ValueMapValue(name = "cardImageReference")
    private String cardImageReference;

    @ValueMapValue(name = "cardImageName")
    private String cardImageName;


    public String getSelectedDeal() {
        return selectedDeal;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getCardImageReference() {
        return cardImageReference;
    }

    public String getCardImageName() {
        return cardImageName;
    }
}