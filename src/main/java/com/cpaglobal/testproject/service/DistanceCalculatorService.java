package com.cpaglobal.testproject.service;

import com.cpaglobal.testproject.model.Geo;

public interface DistanceCalculatorService {

    double calculateDistance(Geo from, Geo to);

}
