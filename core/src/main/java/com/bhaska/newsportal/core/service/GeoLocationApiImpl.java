package com.bhaska.newsportal.core.service;

import org.apache.http.client.fluent.Request;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = GeoLocationApiInterface.class)
public class GeoLocationApiImpl implements GeoLocationApiInterface {

    private static final Logger log = LoggerFactory.getLogger(GeoLocationApiImpl.class);

    private static final String API = "https://ipinfo.io/json";

    @Override
    public String getApiData() {

        try {
            return Request.Get(API)
                    .connectTimeout(10000)
                    .socketTimeout(10000)
                    .execute()
                    .returnContent()
                    .asString();

        } catch (Exception e) {
            log.error("Error while calling Geo Location API", e);
        }

        // Return empty JSON instead of null (safe handling)
        return "{}";
    }
}