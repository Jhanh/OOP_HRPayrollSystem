package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

// Lớp trừu tượng cha cho tất cả loại nhân viên
public abstract class Employee {
    private String id;
    private String fullName;
    private String department;
    private double baseSalary;
    private LocalDate hireDate;

    // Khởi tạo đối tượng với kiểu dữ liệu chuẩn (double, LocalDate)
    public Employee(String id, String fullName, String department, double baseSalary, LocalDate hireDate) 
            throws InvalidEmployeeDataException {
        setId(id);
        setFullName(fullName);
        setDepartment(department);
        setBaseSalary(baseSalary);
        setHireDate(hireDate);
    }

    // Khởi tạo tiện ích hỗ trợ nạp dữ liệu từ chuỗi (Form/CSV)
    public Employee(String id, String fullName, String department, String baseSalaryStr, String hireDateStr) 
            throws InvalidEmployeeDataException {
        setId(id);
        setFullName(fullName);
        setDepartment(department);
        setBaseSalary(baseSalaryStr);
        setHireDate(hireDateStr);
    }

    // Phương thức trừu tượng tính lương theo từng loại nhân viên
    public abstract double calculateSalary();

    // Phương thức trừu tượng trả về loại nhân viên
    public abstract String getEmployeeType();

    // Bí danh getTypeName để tương thích nếu các component gọi theo cách này
    public String getTypeName() {
        return getEmployeeType();
    }

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
        String cleanId = id.trim();
        if (!cleanId.matches("^[a-zA-Z0-9_-]+$")) {
            throw new InvalidEmployeeDataException("Mã nhân viên chỉ được chứa chữ cái, chữ số, dấu gạch ngang (-) hoặc gạch dưới (_)!");
        }
        this.id = cleanId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) throws InvalidEmployeeDataException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Họ và tên nhân viên không được để trống!");
        }
        String cleanName = fullName.trim();
        if (!cleanName.matches("^[\\p{L}\\s]+$")) {
            throw new InvalidEmployeeDataException("Họ tên nhân viên chỉ được chứa chữ cái và khoảng trắng, không chứa số hay ký tự đặc biệt!");
        }
        this.fullName = cleanName;
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
            throw new InvalidEmployeeDataException("Lương cơ bản không được nhỏ hơn 0!");
        }
        this.baseSalary = baseSalary;
    }

    public void setBaseSalary(String baseSalaryStr) throws InvalidEmployeeDataException {
        if (baseSalaryStr == null || baseSalaryStr.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Lương cơ bản không được để trống!");
        }
        try {
            setBaseSalary(Double.parseDouble(baseSalaryStr.trim()));
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Lương cơ bản phải là số hợp lệ, không chứa ký tự chữ!");
        }
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) throws InvalidEmployeeDataException {
        if (hireDate == null) {
            throw new InvalidEmployeeDataException("Ngày vào làm không được để trống!");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new InvalidEmployeeDataException("Ngày vào làm không được lớn hơn ngày hiện tại!");
        }
        this.hireDate = hireDate;
    }

    public void setHireDate(String hireDateStr) throws InvalidEmployeeDataException {
        if (hireDateStr == null || hireDateStr.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Ngày vào làm không được để trống!");
        }
        try {
            setHireDate(LocalDate.parse(hireDateStr.trim()));
        } catch (DateTimeParseException e) {
            throw new InvalidEmployeeDataException("Ngày vào làm không đúng định dạng (YYYY-MM-DD) hoặc ngày không tồn tại!");
        }
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
