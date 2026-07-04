package com.bhaska.newsportal.core.models;



import java.util.List;



import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
@Model(adaptables  = {Resource.class},
        adapters = ProductDetails.class,
        resourceType = "/apps/newsportal/components/productdetailscomponent",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
 )
 @Exporter(name = "jackson",
    extensions = {"json","xml"}
 )
@JsonInclude(
    JsonInclude.Include.NON_NULL
)
public class ProductDetailsComponentImpl implements ProductDetails {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;
    @ValueMapValue
    @Optional
    private Boolean prodStatus;
    @ValueMapValue
    private String category;

    @ChildResource
    private List<MultiFieldsProd> productCard;

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @JsonIgnore
    @Override
    public Boolean getProdStatus() {
        return prodStatus;
    }
    @Override
    public String getCategory() {
        return category;
    }
    
    public List<MultiFieldsProd> getProductCard() {
        return productCard;
    }
   
}


     
