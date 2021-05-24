package com.cpaglobal.testproject.service.impl;

import com.cpaglobal.testproject.exception.EmployeeDataException;
import com.cpaglobal.testproject.exception.EmployeeNotFoundException;
import com.cpaglobal.testproject.exception.MissingParameterException;
import com.cpaglobal.testproject.model.Employee;
import com.cpaglobal.testproject.service.DistanceCalculatorService;
import com.cpaglobal.testproject.service.EmployeeDataFetchService;
import com.cpaglobal.testproject.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDataFetchService employeeDataFetchService;
    private DistanceCalculatorService distanceCalculatorService;

    public EmployeeServiceImpl(EmployeeDataFetchService employeeDataFetchService,
                               DistanceCalculatorService distanceCalculatorService) {
        this.employeeDataFetchService = employeeDataFetchService;
        this.distanceCalculatorService = distanceCalculatorService;
    }

    @Override
    public List<Employee> getAllEmployeesForCompany(String companyName) {

        if (StringUtils.isBlank(companyName)) {
            throw new MissingParameterException("companyName must not be null or blank.");
        }

        return employeeDataFetchService.fetchEmployeeData()
                .stream()
                .filter(employee -> companyName.equals(employee.getCompany().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> getTwoNearestEmployeesTo(String employeeName) {

        if (StringUtils.isBlank(employeeName)) {
            throw new MissingParameterException("employeeName must not be null or blank.");
        }

        final List<Employee> employees = employeeDataFetchService.fetchEmployeeData();

        if (employees.size() < 3) {
            throw new EmployeeDataException("There must be at least 3 employees in order to search for the two nearest ones.");
        }

        final Employee employee = findEmployeeByName(employees, employeeName);

        return findTwoNearestEmployees(employees, employee);
    }

    private List<Employee> findTwoNearestEmployees(List<Employee> employees, Employee employee) {

        List<Employee> nearestEmployees = new ArrayList<>(2);

        // Considering the first two employees as the nearest, iterate through employees list until
        // the first two employees are added. If the employee we are searching nearest for is within
        // the first two elements, skip it.
        int i = 0;

        while (nearestEmployees.size() < 2) {
            if (!employees.get(i).equals(employee)) {
                nearestEmployees.add(employees.get(i));
            }

            i++;
        }

        // Iterate through the rest of employees and try to find if someone is nearer.
        for (; i < employees.size(); i++) {

            Employee currentEmployee = employees.get(i);

            if (currentEmployee.equals(employee)) {
                continue;
            }

            double currentDistance = calculateDistance(currentEmployee, employee);

            if (currentDistance < calculateDistance(nearestEmployees.get(0), employee)) {
                nearestEmployees.remove(0);
                nearestEmployees.add(currentEmployee);
            } else if (currentDistance < calculateDistance(nearestEmployees.get(1), employee)) {
                nearestEmployees.remove(1);
                nearestEmployees.add(currentEmployee);
            }
        }

        return nearestEmployees;
    }

    private Employee findEmployeeByName(List<Employee> employees, String employeeName) {
        return employees.stream()
                .filter(e -> employeeName.equals(e.getName()))
                .findFirst()
                .orElseThrow(() -> {
                    throw new EmployeeNotFoundException(
                            String.format("Employee with the name of %s has not been found.", employeeName));
                });
    }

    private double calculateDistance(Employee from, Employee to) {
        return distanceCalculatorService.calculateDistance(from.getAddress().getGeo(), to.getAddress().getGeo());
    }
}
