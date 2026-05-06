package com.example.employeeapi.service;

import com.example.employeeapi.dto.EmployeeDTO;
import com.example.employeeapi.exception.DuplicateResourceException;
import com.example.employeeapi.exception.ResourceNotFoundException;
import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        logger.info("Creating employee with email: {}", employeeDTO.getEmail());

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            logger.warn("Email already in use: {}", employeeDTO.getEmail());
            throw new DuplicateResourceException("Email '" + employeeDTO.getEmail() + "' is already registered.");
        }

        Employee employee = mapToEntity(employeeDTO);
        Employee saved = employeeRepository.save(employee);

        logger.info("Employee created successfully with ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        logger.debug("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Found {} employees", employees.size());
        return employees.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        logger.debug("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        logger.info("Updating employee with ID: {}", id);

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Check if the new email belongs to another employee
        if (employeeRepository.existsByEmailAndIdNot(employeeDTO.getEmail(), id)) {
            logger.warn("Email '{}' already used by another employee", employeeDTO.getEmail());
            throw new DuplicateResourceException("Email '" + employeeDTO.getEmail() + "' is already registered.");
        }

        existing.setName(employeeDTO.getName());
        existing.setEmail(employeeDTO.getEmail());
        existing.setDepartment(employeeDTO.getDepartment());
        existing.setSalary(employeeDTO.getSalary());

        Employee updated = employeeRepository.save(existing);
        logger.info("Employee updated successfully with ID: {}", updated.getId());
        return mapToDTO(updated);
    }

    @Override
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with ID: {}", id);

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", "id", id);
        }

        employeeRepository.deleteById(id);
        logger.info("Employee with ID {} deleted successfully", id);
    }

    // ─── Mapping Helpers ─────────────────────────────────────────────────────

    private EmployeeDTO mapToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .build();
    }

    private Employee mapToEntity(EmployeeDTO dto) {
        return Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .salary(dto.getSalary())
                .build();
    }
}
