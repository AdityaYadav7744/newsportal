package com.bhaska.newsportal.core.service;

import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(service = ApiService.class, immediate = true)
@Designate(ocd = ApiConfig.class)
public class ApiServiceImpl implements ApiService {

    private static final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);

    private String apiUrl;
    private boolean enableApi;

    @Activate
    @Modified
    protected void activate(ApiConfig config) {

        this.apiUrl = config.apiUrl();
        this.enableApi = config.enableApi();

        log.info("API URL : {}", apiUrl);
        log.info("API Enabled : {}", enableApi);

        if(enableApi){
            fetchApiData();
        }
    }

    @Override
    public void fetchApiData() {

        try {

            log.info("Calling API : {}", apiUrl);

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();

            log.info("Response Status Code : {}", statusCode);

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            log.info("API Response : {}", response.toString());

        } catch (Exception e) {
        }

    }
}