package com.cpaglobal.testproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;

    private String suite;

    private String city;

    private String zipcode;

    private Geo geo;
}
