package com.cpaglobal.testproject.service;

import com.cpaglobal.testproject.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployeesForCompany(String companyName);

    List<Employee> getTwoNearestEmployeesTo(String employeeName);

}
