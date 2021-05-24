package com.cpaglobal.testproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private int id;

    private String name;

    private String username;

    private String email;

    private Address address;

    private String phone;

    private String website;

    private Company company;
}
