package vn.edu.ptit.oop.nhom08.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.TaxCalculator;
import vn.edu.ptit.oop.nhom08.model.Taxable;
import vn.edu.ptit.oop.nhom08.repository.EmployeeRepository;

// Dịch vụ tính toán quỹ lương, thuế, lương thực lĩnh
public class PayrollService {

    private final EmployeeRepository repository;

    public PayrollService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // Lấy toàn bộ danh sách nhân viên
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Tìm nhân viên theo mã
    public Optional<Employee> getEmployeeById(String id) {
        return repository.findById(id);
    }

    // Tính tổng lương (gross) của một nhân viên
    public double calculateGrossSalary(Employee employee) {
        return employee.calculateSalary();
    }

    // Tính thuế TNCN của một nhân viên
    public double calculateTax(Employee employee) {
        return TaxCalculator.calculateTax(employee);
    }

    // Tính lương thực lĩnh (net) của một nhân viên
    public double calculateNetSalary(Employee employee) {
        return TaxCalculator.calculateNetSalary(employee);
    }

    // Tính tổng quỹ lương (gross) toàn công ty
    public double calculateTotalPayroll() {
        return repository.findAll().stream()
                .mapToDouble(Employee::calculateSalary)
                .sum();
    }

    // Tính tổng thuế TNCN toàn công ty
    public double calculateTotalTax() {
        return TaxCalculator.calculateTotalTax(repository.findAll());
    }

    // Tính tổng lương thực lĩnh (net) toàn công ty
    public double calculateTotalNetPayroll() {
        return repository.findAll().stream()
                .mapToDouble(TaxCalculator::calculateNetSalary)
                .sum();
    }

    // Tính quỹ lương theo phòng ban
    public Map<String, Double> calculatePayrollByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::calculateSalary)
                ));
    }

    // Tính quỹ lương theo loại nhân viên
    public Map<String, Double> calculatePayrollByEmployeeType() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::getEmployeeType,
                        Collectors.summingDouble(Employee::calculateSalary)
                ));
    }

    // Đếm số nhân viên theo phòng ban
    public Map<String, Long> countByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));
    }

    // Tìm nhân viên có lương cao nhất
    public Optional<Employee> findHighestPaidEmployee() {
        return repository.findAll().stream()
                .max((a, b) -> Double.compare(a.calculateSalary(), b.calculateSalary()));
    }

    // Tìm nhân viên có lương thấp nhất
    public Optional<Employee> findLowestPaidEmployee() {
        return repository.findAll().stream()
                .min((a, b) -> Double.compare(a.calculateSalary(), b.calculateSalary()));
    }

    // Tính lương trung bình toàn công ty
    public double calculateAverageSalary() {
        List<Employee> employees = repository.findAll();
        if (employees.isEmpty()) {
            return 0.0;
        }
        return calculateTotalPayroll() / employees.size();
    }
}

