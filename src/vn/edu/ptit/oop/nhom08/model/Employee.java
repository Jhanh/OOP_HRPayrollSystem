package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

// Lớp trừu tượng cha cho tất cả loại nhân viên
public abstract class Employee {
    private String id;
    private String fullName;
    private String department;
    private double baseSalary;
    private LocalDate hireDate;

    public Employee(String id, String fullName, String department, double baseSalary, LocalDate hireDate)
            throws InvalidEmployeeDataException {
        setId(id);
        setFullName(fullName);
        setDepartment(department);
        setBaseSalary(baseSalary);
        setHireDate(hireDate);
    }

    // Phương thức trừu tượng tính lương theo từng loại nhân viên
    public abstract double calculateSalary();

    // Tên hiển thị loại nhân viên
    public abstract String getEmployeeType();

    // Tính số năm thâm niên (final để cố định công thức)
    public final int getSeniorityYears() {
        if (hireDate == null) {
            return 0;
        }
        int years = Period.between(hireDate, LocalDate.now()).getYears();
        return Math.max(0, years);
    }

    // --- Getters & Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) throws InvalidEmployeeDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Mã nhân viên không được để trống!");
        }
        this.id = id.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) throws InvalidEmployeeDataException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Họ và tên không được để trống!");
        }
        this.fullName = fullName.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) throws InvalidEmployeeDataException {
        if (department == null || department.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Phòng ban không được để trống!");
        }
        this.department = department.trim();
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) throws InvalidEmployeeDataException {
        if (baseSalary < 0) {
            throw new InvalidEmployeeDataException("Lương cơ bản không được âm!");
        }
        this.baseSalary = baseSalary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) throws InvalidEmployeeDataException {
        if (hireDate == null) {
            throw new InvalidEmployeeDataException("Ngày vào làm không được để trống!");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new InvalidEmployeeDataException("Ngày vào làm không thể ở tương lai!");
        }
        this.hireDate = hireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s - Phòng: %s - Lương: %,.0f VNĐ (Thâm niên: %d năm)",
                id, fullName, getEmployeeType(), department, calculateSalary(), getSeniorityYears());
    }
}
