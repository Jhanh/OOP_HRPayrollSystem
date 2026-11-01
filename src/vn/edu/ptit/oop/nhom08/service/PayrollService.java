package vn.edu.ptit.oop.nhom08.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.TaxCalculator;
import vn.edu.ptit.oop.nhom08.repository.EmployeeRepository;
import vn.edu.ptit.oop.nhom08.util.CurrencyFormatUtil;

// Dịch vụ tính toán tổng quỹ lương, thuế TNCN và thống kê theo phòng ban
public class PayrollService {

    private final EmployeeRepository repository;

    public PayrollService(EmployeeRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("EmployeeRepository không được null!");
        }
        this.repository = repository;
    }

    // Tính tổng quỹ lương (Gross) của toàn bộ nhân viên
    public double calculateTotalPayroll() {
        return repository.findAll().stream()
                .mapToDouble(Employee::calculateSalary)
                .sum();
    }

    // Tính tổng thuế TNCN của toàn bộ nhân viên
    public double calculateTotalTax() {
        return TaxCalculator.calculateTotalTax(repository.findAll());
    }

    // Tính tổng lương thực lĩnh (Net) của toàn bộ nhân viên
    public double calculateTotalNetSalary() {
        return repository.findAll().stream()
                .mapToDouble(TaxCalculator::calculateNetSalary)
                .sum();
    }

    // Phân bổ quỹ lương theo phòng ban dùng Java Stream groupingBy
    public Map<String, Double> calculatePayrollByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::calculateSalary)
                ));
    }

    // Thống kê số lượng nhân viên theo phòng ban
    public Map<String, Long> countEmployeesByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));
    }

    // Thống kê số lượng nhân viên theo loại hợp đồng (Đa hình)
    public Map<String, Long> countEmployeesByType() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::getEmployeeType,
                        Collectors.counting()
                ));
    }

    // Lấy tổng số lượng nhân sự
    public int getTotalEmployeeCount() {
        return repository.count();
    }

    // Tính mức lương trung bình trong doanh nghiệp
    public double calculateAverageSalary() {
        List<Employee> list = repository.findAll();
        if (list.isEmpty()) return 0.0;
        return calculateTotalPayroll() / list.size();
    }

    // Tìm nhân viên có mức lương cao nhất
    public Optional<Employee> findHighestPaidEmployee() {
        return repository.findAll().stream()
                .max(Comparator.comparingDouble(Employee::calculateSalary));
    }

    // Định dạng thông báo tóm tắt hiển thị trên Status Bar
    public String getStatusBarSummary() {
        int totalEmp = getTotalEmployeeCount();
        String totalGross = CurrencyFormatUtil.formatWithUnit(calculateTotalPayroll());
        String totalNet = CurrencyFormatUtil.formatWithUnit(calculateTotalNetSalary());
        return String.format(" Sẵn sàng | Nhân viên: %d | Quỹ lương: %s | Thực lĩnh: %s",
                totalEmp, totalGross, totalNet);
    }
}
