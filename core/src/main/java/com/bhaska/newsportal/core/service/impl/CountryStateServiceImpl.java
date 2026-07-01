package com.bhaska.newsportal.core.service.impl;

import com.bhaska.newsportal.core.service.CountryStateService;
import org.osgi.service.component.annotations.Component;

import java.util.*;

@Component(service = CountryStateService.class)
public class CountryStateServiceImpl implements CountryStateService {
    @Override
    public List<String> getStates(String country) {

        try {
            String apiUrl = "https://countriesnow.space/api/v0.1/countries/states=" + country;

        } catch (Exception e) {
            return getFallback(country);
        }

        return Collections.emptyList();
    }
    @Override
    public List<String> getFallback(String country) {

        Map<String, List<String>> map = new HashMap<>();

        map.put("IN", Arrays.asList("Maharashtra", "Karnataka", "Delhi"));
        map.put("US", Arrays.asList("California", "Texas"));

        return map.getOrDefault(country, Collections.emptyList());
    }
}