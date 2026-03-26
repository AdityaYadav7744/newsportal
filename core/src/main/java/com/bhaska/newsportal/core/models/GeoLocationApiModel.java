package com.bhaska.newsportal.core.models;

import com.bhaska.newsportal.core.service.GeoLocationApiInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GeoLocationApiModel {

    Logger log = LoggerFactory.getLogger(GeoLocationApiModel.class);

    @OSGiService
    private GeoLocationApiInterface geoLocationApi;

    private String region;
    private String city;
    private String country;

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
    @PostConstruct
   public void init()  {

        String apiData = geoLocationApi.getApiData();

        try {
            ObjectMapper mapper =new ObjectMapper();
            LocationResponse locationResponse = mapper.readValue(apiData, LocationResponse.class);

            log.info("Response is : {} ",locationResponse);
            city=locationResponse.getCity();
            region=locationResponse.getRegion();
            country=locationResponse.getCountry();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
