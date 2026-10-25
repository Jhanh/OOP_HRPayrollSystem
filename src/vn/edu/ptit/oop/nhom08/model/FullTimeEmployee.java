package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

public class FullTimeEmployee extends Employee implements Taxable {
    private double salaryFactor; // Hệ số lương/thưởng (>= 1.0)
    private double bonus;        // Tiền thưởng / phụ cấp (>= 0)

    // Khởi tạo với kiểu dữ liệu chuẩn (double, LocalDate)
    public FullTimeEmployee(String id, String fullName, String department, double baseSalary, LocalDate hireDate,
                            double salaryFactor, double bonus) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalary, hireDate);
        setSalaryFactor(salaryFactor);
        setBonus(bonus);
    }

    // Khởi tạo tiện ích hỗ trợ nạp dữ liệu từ chuỗi (Form/CSV)
    public FullTimeEmployee(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                            double salaryFactor, double bonus) throws InvalidEmployeeDataException {
        this(id, fullName, department, parseBaseSalary(baseSalaryStr), parseHireDate(hireDateStr), salaryFactor, bonus);
    }

    private static double parseBaseSalary(String baseSalaryStr) throws InvalidEmployeeDataException {
        try {
            if (baseSalaryStr == null || baseSalaryStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Lương cơ bản không được để trống!");
            }
            return Double.parseDouble(baseSalaryStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Lương cơ bản không đúng định dạng số!");
        }
    }

    private static LocalDate parseHireDate(String hireDateStr) throws InvalidEmployeeDataException {
        try {
            if (hireDateStr == null || hireDateStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Ngày vào làm không được để trống!");
            }
            return LocalDate.parse(hireDateStr.trim());
        } catch (Exception e) {
            throw new InvalidEmployeeDataException("Ngày vào làm không đúng định dạng (YYYY-MM-DD)!");
        }
    }

    public double getSalaryFactor() {
        return salaryFactor;
    }

    public void setSalaryFactor(double salaryFactor) throws InvalidEmployeeDataException {
        if (salaryFactor < 1.0) {
            throw new InvalidEmployeeDataException("Hệ số lương phải từ 1.0 trở lên!");
        }
        this.salaryFactor = salaryFactor;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) throws InvalidEmployeeDataException {
        if (bonus < 0) {
            throw new InvalidEmployeeDataException("Tiền thưởng/phụ cấp không được nhỏ hơn 0!");
        }
        this.bonus = bonus;
    }

    // Bí danh getter để tương thích linh hoạt giữa bonusRate và allowance
    public double getBonusRate() {
        return salaryFactor;
    }

    public double getAllowance() {
        return bonus;
    }

    @Override
    public double calculateSalary() {
        return (getBaseSalary() * salaryFactor) + bonus;
    }

    // Triển khai tính thuế TNCN theo biểu lũy tiến cho giao diện Taxable
    public double calculateTax() {
        double income = calculateSalary();
        if (income <= 10_000_000) {
            return 0.0;
        } else if (income <= 20_000_000) {
            return (income - 10_000_000) * 0.05;
        } else if (income <= 40_000_000) {
            return 500_000 + (income - 20_000_000) * 0.10;
        } else {
            return 2_500_000 + (income - 40_000_000) * 0.20;
        }
    }

    @Override
    public String getEmployeeType() {
        return "Toàn thời gian";
    }

    // Tương thích thêm getTypeName nếu có component gọi
    public String getTypeName() {
        return "Full-time";
    }
}
