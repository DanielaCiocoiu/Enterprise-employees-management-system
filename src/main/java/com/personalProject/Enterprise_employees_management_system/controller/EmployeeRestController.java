package com.personalProject.Enterprise_employees_management_system.controller;


import com.personalProject.Enterprise_employees_management_system.domain.Departament;
import com.personalProject.Enterprise_employees_management_system.domain.Employee;
import com.personalProject.Enterprise_employees_management_system.error.EmplyeeNotFoundException;
import com.personalProject.Enterprise_employees_management_system.service.employee.EmployeeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // for security stuff
    @GetMapping("/welcome")
    public String welcome(Authentication authentication) {
        return "Welcome,  " + authentication.getName() + "!";
    }

    @GetMapping("/anyOtherEndpoint")
    public String anyOtherEndpoint() {
        return "Denied";
    }



    @GetMapping("/employees")
    @Cacheable(value = "cacheEmployees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);
        if ((theEmployee == null) || (employeeId < 0)) {
            throw new EmplyeeNotFoundException("Employee id not found - " + employeeId);
        }
        return theEmployee;
    }

    //http://localhost:8080/api?departament=HR
    @GetMapping
    public List<Employee> getEmployeeDepartamentWithRequestParam(@RequestParam(value = "departament") Departament departament) {

        return employeeService.getEmployeeByDepartament(departament);
    }

    @GetMapping("/{name}")
    public List<Employee> getEmployeeDepartament(@PathVariable(name = "name") Departament departament) {
        return employeeService.getEmployeeByDepartament(departament);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody Employee theEmployee) {
        theEmployee.setId(0);
        employeeService.save(theEmployee);
        return theEmployee;
    }

    @PutMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        employeeService.save(theEmployee);
        return theEmployee;
    }

    @DeleteMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteById(employeeId);
        return "Deleted employee id - " + employeeId;
    }

}
