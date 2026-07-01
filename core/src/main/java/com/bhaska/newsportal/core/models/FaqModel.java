package com.bhaska.newsportal.core.models;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = {  Resource.class, HttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FaqModel {
    /**
     * Getting Logger Object.
     */
   private static final Logger LOG = LoggerFactory.getLogger(FaqModel.class);
    /**
     * Getting Resource.
     */
    @SlingObject
    private Resource resource;
    /**
     * Getting disableLazyLoading.
     */
    @ValueMapValue
    private boolean disableLazyLoading;
    /**
     * Getting cardType.
     */
    @ValueMapValue
    private String cardType;
    /**
     * Getting showAsCarousel.
     */
    @ValueMapValue
    private boolean showAsCarousel;
    /**
     * Getting cardCount.
     */
    @ValueMapValue
    private int cardCount;
    /**
     * Getting rootPath.
     */
    @ValueMapValue
    private String rootPath;
    /**
     * Getting clickAction.
     */
    @ValueMapValue
    private  String clickAction;
    /**
     * Getting cards.
     */
    private List<ManualCards> cards = new ArrayList<>();
    /**
     * Method will execute after all variable initallization.
     */
    @PostConstruct
    private void init() {
        LOG.info("inside initMethod");
        if (cardType.equalsIgnoreCase("manual")) {
            readManualCard();
            LOG.info("readManualCard method called");
        } else if (cardType.equalsIgnoreCase("dynamic")) {
            readDynamicCards();
            LOG.info("readDynamicCards method called");
        }
    }
    /**
     * @return it will read the composite multifield.
     */
    private void readManualCard() {
         Resource children = resource.getChild("manualCards");
         if (children == null) {
             return;
         }
         for (Resource item : children.getChildren()) {
             ValueMap vm = item.getValueMap();
             cards.add(new ManualCards(
                     vm.get("cardTitle", ""),
                     vm.get("cardImage", ""),
                     vm.get("shortDescription", ""),
                     vm.get("fullDescription", ""),
                     vm.get("ctaText", ""),
                     vm.get("ctaLink", "") + ".html"
             ));
         }
    }
    /**
     * It will read the child pages.
     */
    public void readDynamicCards() {
        PageManager pageManager = resource.getResourceResolver()
                .adaptTo(PageManager.class);
        if (pageManager == null) {
            return;
        }
         Page page = pageManager.getPage(rootPath);
        if (page == null) {
            return;
        }
         Iterator<Page> iterator = page.listChildren();
        while (iterator.hasNext()) {
             Page childPage = iterator.next();
             ValueMap vm = childPage.getProperties();
             cards.add(new ManualCards(
                     vm.get("jcr:title", ""),
                     vm.get("cardImage", ""),
                     vm.get("shortDescription", ""),
                     vm.get("fullDescription", ""),
                     vm.get("ctaText", ""),
                     vm.get("ctaLink", "") + ".html"
             ));
        }
    }
    /**
     * @return the Cards.
     */
    public List<ManualCards> getCards() {
        LOG.info("This is the data present in List {}", cards);
        return cards;
    }
    /**
     * @return the Resource.
     */
    public Resource getResource() {
        return resource;
    }
    /**
     * @return the is disableLoading is clicked or not.
     */
    public boolean isDisableLazyLoading() {
        return disableLazyLoading;
    }
    /**
     * @return the ClickAction.
     */
    public String getCardType() {
        return cardType;
    }
    /**
     * @return the CheckBox is clicked or not.
     */
    public boolean isShowAsCarousel() {
        return showAsCarousel;
    }
    /**
     * @return the CardCount.
     */
    public int getCardCount() {
        return cardCount;
    }
    /**
     * @return the RootPath.
     */
    public String getRootPath() {
        return rootPath;
    }
    /**
     * @return the ClickAction.
     */
    public String getClickAction() {
        return clickAction;
    }
}
