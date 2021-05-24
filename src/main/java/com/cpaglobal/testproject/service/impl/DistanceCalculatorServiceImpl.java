package com.cpaglobal.testproject.service.impl;

import com.cpaglobal.testproject.model.Geo;
import com.cpaglobal.testproject.service.DistanceCalculatorService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DistanceCalculatorServiceImpl implements DistanceCalculatorService {

    @Override
    public double calculateDistance(Geo from, Geo to) {

        Objects.requireNonNull(from, "\"from\" geo coordinates must not be null");
        Objects.requireNonNull(to, "\"to\" geo coordinates must not be null");

        return Math.sqrt(Math.pow(to.getLat() - from.getLat(), 2) + Math.pow((to.getLng() - from.getLng()), 2));
    }
}
