package com.madar.sbems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.madar.sbems.entity.PredictionRequest;

@Service
public class MlService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public Object getPrediction(PredictionRequest request) {

        System.out.println("Calling ML service at: " + mlServiceUrl);

        Object response = restTemplate.postForObject(
                mlServiceUrl,
                request,
                Object.class
        );

        System.out.println("Response received from ML service");

        return response;
    }
}