package com.cpaglobal.testproject.controller;

import com.cpaglobal.testproject.model.Employee;
import com.cpaglobal.testproject.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/company/{companyName}")
    public List<Employee> getAllEmployeesForCompany(@PathVariable String companyName) {
        return employeeService.getAllEmployeesForCompany(companyName);
    }

    @GetMapping("/nearest/{employeeName}")
    public List<Employee> getTwoNearestEmployeesTo(@PathVariable String employeeName) {
        return employeeService.getTwoNearestEmployeesTo(employeeName);
    }
}
