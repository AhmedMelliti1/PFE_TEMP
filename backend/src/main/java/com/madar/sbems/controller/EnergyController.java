package com.madar.sbems.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.madar.sbems.service.FeatureEngineeringService;


import com.madar.sbems.entity.PredictionRequest;
import com.madar.sbems.service.MlService;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final MlService mlService;

    public EnergyController(MlService mlService) {
        this.mlService = mlService;
    }

    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestBody PredictionRequest request) {
        return ResponseEntity.ok(mlService.getPrediction(request));
    }
    @Autowired
    private FeatureEngineeringService featureService;

    @PostMapping("/predict-auto")
    public ResponseEntity<?> predictAuto() {

        PredictionRequest request = featureService.generateFeatures();

        return ResponseEntity.ok(mlService.getPrediction(request));
    }
}
