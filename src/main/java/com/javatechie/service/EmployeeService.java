package com.javatechie.service;

import com.javatechie.entity.Employee;
import com.javatechie.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    public static final String DEFAULT_ROLES = "ROLE_EMPLOYEE";
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee addNewEmployee(Employee employee) {
        employee.setRoles(DEFAULT_ROLES);

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Not Found With ID : " + id));
    }

    public Employee updateEmployeesRoles(Employee employee) {
        Employee emp = getEmployee(employee.getId());
        emp.setRoles(employee.getRoles());
        return employeeRepository.save(emp);
    }
}
