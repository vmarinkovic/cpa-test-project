package com.cpaglobal.testproject.service.impl;

import com.cpaglobal.testproject.exception.EmployeeDataException;
import com.cpaglobal.testproject.model.Employee;
import com.cpaglobal.testproject.service.EmployeeDataFetchService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeDataFetchServiceImpl implements EmployeeDataFetchService {

    private static final String EMPLOYEE_DATA_URL = "https://jsonplaceholder.typicode.com/users";

    private RestTemplate restTemplate;

    public EmployeeDataFetchServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> fetchEmployeeData() {

        Employee[] employeeData = restTemplate.getForObject(EMPLOYEE_DATA_URL, Employee[].class);

        if (employeeData == null || employeeData.length == 0) {
            throw new EmployeeDataException("No employee data fetched.");
        }

        return List.of(employeeData);
    }
}
