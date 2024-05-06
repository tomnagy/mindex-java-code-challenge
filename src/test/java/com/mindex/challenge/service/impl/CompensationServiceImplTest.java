package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;
    private String employeeIDCompensationUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        employeeIDCompensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        BigDecimal testSalary = new BigDecimal("123.45");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d-MMM-yyyy");
        LocalDate testEffectiveDate = LocalDate.parse("01-Jan-2024", df);

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(testSalary);
        testCompensation.setEffectiveDate(testEffectiveDate);

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation.getCompensationId());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read by compensation id checks
        Compensation readByCompensationId = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getCompensationId()).getBody();

        assertEquals(createdCompensation.getCompensationId(), readByCompensationId.getCompensationId());
        assertCompensationEquivalence(createdCompensation, readByCompensationId);

        // Read by employee id checks
        Compensation readByEmployeeId = restTemplate.getForEntity(employeeIDCompensationUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();

        assertEquals(createdCompensation.getCompensationId(), readByEmployeeId.getCompensationId());
        assertCompensationEquivalence(createdCompensation, readByEmployeeId);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
