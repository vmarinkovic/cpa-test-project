package com.cpaglobal.testproject.service;

import com.cpaglobal.testproject.exception.EmployeeDataException;
import com.cpaglobal.testproject.exception.EmployeeNotFoundException;
import com.cpaglobal.testproject.exception.MissingParameterException;
import com.cpaglobal.testproject.model.Address;
import com.cpaglobal.testproject.model.Company;
import com.cpaglobal.testproject.model.Employee;
import com.cpaglobal.testproject.model.Geo;
import com.cpaglobal.testproject.service.impl.DistanceCalculatorServiceImpl;
import com.cpaglobal.testproject.service.impl.EmployeeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {EmployeeServiceImpl.class, DistanceCalculatorServiceImpl.class})
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeDataFetchService employeeDataFetchService;

    private List<Employee> employees;

    private List<Employee> buildEmployeeList() {

        List<Employee> employees = new ArrayList<>();

        Company companyApple = Company.builder().name("Apple").build();
        Company companyGoogle = Company.builder().name("Google").build();

        employees.add(Employee.builder().id(1).name("Steve")
                .address(Address.builder().geo(new Geo(1, 1)).build())
                .company(companyApple).build());
        employees.add(Employee.builder().id(2).name("Larry")
                .address(Address.builder().geo(new Geo(2, 2)).build())
                .company(companyGoogle).build());
        employees.add(Employee.builder().id(3).name("Tim")
                .address(Address.builder().geo(new Geo(3, 3)).build())
                .company(companyApple).build());
        employees.add(Employee.builder().id(4).name("Satya")
                .address(Address.builder().geo(new Geo(4, 4)).build())
                .company(companyGoogle).build());

        return employees;
    }

    @BeforeEach
    public void setUp() {
        employees = buildEmployeeList();
    }

    @Test
    public void getTwoEmployeesForCompany() {

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        List<Employee> employeesForCompany = employeeService.getAllEmployeesForCompany("Apple");

        assertEquals(2, employeesForCompany.size());
    }

    @Test
    public void getNoEmployeesForCompany() {

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        List<Employee> employeesForCompany = employeeService.getAllEmployeesForCompany("Microsoft");

        assertEquals(0, employeesForCompany.size());
    }

    @Test
    public void getTwoNearestEmployeesToSomeone() {

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        List<Employee> twoNearestEmployees = employeeService.getTwoNearestEmployeesTo("Larry");

        assertThat(twoNearestEmployees).hasSize(2)
                .extracting(Employee::getName)
                .containsExactlyInAnyOrder("Steve", "Tim");
    }

    @Test
    public void getTwoNearestEmployeesToSomeoneWithAdditionalEmployeeInTheList() {

        employees.add(Employee.builder().id(5).name("Sergey")
                .address(Address.builder().geo(new Geo(1.5, 1.5)).build())
                .company(Company.builder().name("Google").build()).build());

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        List<Employee> twoNearestEmployees = employeeService.getTwoNearestEmployeesTo("Larry");

        assertThat(twoNearestEmployees).hasSize(2)
                .extracting(Employee::getName)
                .containsExactlyInAnyOrder("Sergey", "Tim");
    }

    @Test
    public void throwExceptionWhenEmployeeNameIsNull() {

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        MissingParameterException exception = assertThrows(MissingParameterException.class, () -> {
            employeeService.getTwoNearestEmployeesTo(null);
        });

        assertEquals("employeeName must not be null or blank.", exception.getMessage());
    }

    @Test
    public void throwExceptionWhenEmployeeNameIsBlank() {

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        MissingParameterException exception = assertThrows(MissingParameterException.class, () -> {
            employeeService.getTwoNearestEmployeesTo(StringUtils.EMPTY);
        });

        assertEquals("employeeName must not be null or blank.", exception.getMessage());
    }

    @Test
    public void throwExceptionWhenThereAreLessThanThreeEmployees() {

        List<Employee> twoEmployees = employees.subList(0, 2);

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(twoEmployees);

        EmployeeDataException exception = assertThrows(EmployeeDataException.class, () -> {
            employeeService.getTwoNearestEmployeesTo("Larry");
        });

        assertEquals("There must be at least 3 employees in order to search for the two nearest ones.",
                exception.getMessage());
    }

    @Test
    public void throwExceptionWhenEmployeeDoesNotExist() {

        when(employeeDataFetchService.fetchEmployeeData()).thenReturn(employees);

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getTwoNearestEmployeesTo("John");
        });

        assertEquals("Employee with the name of John has not been found.", exception.getMessage());
    }
}
