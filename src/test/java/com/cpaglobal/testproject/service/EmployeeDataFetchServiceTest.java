package com.cpaglobal.testproject.service;

import com.cpaglobal.testproject.exception.EmployeeDataException;
import com.cpaglobal.testproject.model.Employee;
import com.cpaglobal.testproject.service.impl.EmployeeDataFetchServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = EmployeeDataFetchServiceImpl.class)
public class EmployeeDataFetchServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeDataFetchService employeeDataFetchService;

    @Test
    public void fetchEmployeeData() {

        Employee[] employeesArray = new Employee[]{
                Employee.builder().name("Steve").build(),
                Employee.builder().name("Larry").build(),
                Employee.builder().name("Satya").build()
        };

        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(employeesArray);

        List<Employee> employees = employeeDataFetchService.fetchEmployeeData();

        assertEquals(3, employees.size());

        assertEquals("Steve", employees.get(0).getName());
        assertEquals("Larry", employees.get(1).getName());
        assertEquals("Satya", employees.get(2).getName());
    }

    @Test
    public void throwExceptionWhenNoDataIsFetched() {

        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(new Employee[]{});

        EmployeeDataException exception = assertThrows(EmployeeDataException.class, () -> {
            employeeDataFetchService.fetchEmployeeData();
        });

        assertEquals("No employee data fetched.", exception.getMessage());
    }
}
