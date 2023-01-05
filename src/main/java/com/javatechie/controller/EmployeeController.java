package com.javatechie.controller;

import com.javatechie.dto.AuthRequest;
import com.javatechie.dto.AuthResponse;
import com.javatechie.entity.Employee;
import com.javatechie.service.EmployeeService;
import com.javatechie.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Thanks for join Javatechie ! Your initial credential to access our portal has been send over email ";
    }

    @PostMapping("/create")
    public Employee addNewEmployee(@RequestBody Employee employee) {
        return employeeService.addNewEmployee(employee);
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticateAndGenerateToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return new AuthResponse(jwtService.generateToken(authRequest.getUserName()));
        } else {
            throw new UsernameNotFoundException("Authentication failed !! : " + 401);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public Employee changeRole(@RequestBody Employee employee) {
        return employeeService.updateEmployeesRoles(employee);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_MANAGER')")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployee(id);
    }
}
