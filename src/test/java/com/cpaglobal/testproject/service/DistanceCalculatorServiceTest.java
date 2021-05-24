package com.cpaglobal.testproject.service;

import com.cpaglobal.testproject.model.Geo;
import com.cpaglobal.testproject.service.impl.DistanceCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistanceCalculatorServiceTest {

    private DistanceCalculatorService distanceCalculatorService = new DistanceCalculatorServiceImpl();

    private static final double DELTA = 0.0001;

    @Test
    public void calculateDistanceWithPositiveCoordinates() {

        Geo source = new Geo(1, 2);
        Geo target = new Geo(3, 4);

        assertEquals(2.828427, distanceCalculatorService.calculateDistance(source, target), DELTA);
    }

    @Test
    public void calculateDistanceWithNegativeCoordinates() {

        Geo source = new Geo(1, -2);
        Geo target = new Geo(-3, 4);

        assertEquals(7.2111103, distanceCalculatorService.calculateDistance(source, target), DELTA);
    }

    @Test
    public void throwExceptionWhenSourceGeoIsNull() {

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            distanceCalculatorService.calculateDistance(null, new Geo(1, 1));
        });

        assertEquals("\"from\" geo coordinates must not be null", exception.getMessage());
    }

    @Test
    public void throwExceptionWhenTargetGeoIsNull() {

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            distanceCalculatorService.calculateDistance(new Geo(1, 1), null);
        });

        assertEquals("\"to\" geo coordinates must not be null", exception.getMessage());
    }
}
