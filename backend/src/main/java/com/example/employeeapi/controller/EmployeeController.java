package com.example.employeeapi.controller;

import com.example.employeeapi.dto.ApiResponse;
import com.example.employeeapi.dto.EmployeeDTO;
import com.example.employeeapi.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ─── POST /api/employees ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        logger.info("POST /api/employees - Creating employee: {}", employeeDTO.getName());
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    // ─── GET /api/employees ───────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        logger.info("GET /api/employees - Fetching all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(
                ApiResponse.success("Employees retrieved successfully", employees));
    }

    // ─── GET /api/employees/{id} ──────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        logger.info("GET /api/employees/{} - Fetching employee by ID", id);
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Employee retrieved successfully", employee));
    }

    // ─── PUT /api/employees/{id} ──────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        logger.info("PUT /api/employees/{} - Updating employee", id);
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(
                ApiResponse.success("Employee updated successfully", updated));
    }

    // ─── DELETE /api/employees/{id} ───────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        logger.info("DELETE /api/employees/{} - Deleting employee", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(
                ApiResponse.success("Employee deleted successfully"));
    }
}
