package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = HttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DealsModelClass {
    /**
     *
     * **/
    @SlingObject
    private ResourceResolver resourceResolver;
    /**
     *
     * **/
    private List<Page> publicDeals = new ArrayList<>();
    /**
     *
     * **/
    private List<Page> privateDeals = new ArrayList<>();

    /**
     * Returns the user name.
     *
     * @return the user name
     */
    public List<Page> getPublicDeals() {
        return publicDeals;
    }

    /**
     * Returns the user name.
     *
     * @return the user name
     */
    public List<Page> getPrivateDeals() {
        return privateDeals;
    }
    /**
     *
     * **/
    @PostConstruct
    public void init() {
         PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
         Page rootPage = pageManager.getPage("/content/newsportal/deals");
         if (rootPage != null) {
              Iterator<Page> iterator = rootPage.listChildren();
              while (iterator.hasNext()) {
                  Page page = iterator.next();

                  Resource dealsComponent =
                          page.getContentResource()
                                  .getChild("root/container/deals");

                  if (dealsComponent == null) {
                      continue;
                  }

                  boolean state =
                          dealsComponent.getValueMap()
                                  .get("privateDeal", false);

                  if (state) {
                       privateDeals.add(page);
                  } else {
                       publicDeals.add(page);
                  }
              }
         }

    }
}
