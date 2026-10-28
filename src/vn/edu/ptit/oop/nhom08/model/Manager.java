package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

// Quản lý: kế thừa 2 tầng (Manager -> FullTimeEmployee -> Employee), có thêm phụ cấp trách nhiệm
public class Manager extends FullTimeEmployee {

    public static final double ALLOWANCE_PER_PERSON = 500_000; // Phụ cấp trách nhiệm / người quản lý (VNĐ)
    private int managedCount; // Số người quản lý (>= 0)

    public Manager(String id, String fullName, String department, double baseSalary, LocalDate hireDate,
                   double salaryFactor, double bonus, int managedCount) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalary, hireDate, salaryFactor, bonus);
        setManagedCount(managedCount);
    }

    public Manager(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                   double salaryFactor, double bonus, int managedCount) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalaryStr, hireDateStr, salaryFactor, bonus);
        setManagedCount(managedCount);
    }

    public Manager(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                   String salaryFactorStr, String bonusStr, String managedCountStr) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalaryStr, hireDateStr, salaryFactorStr, bonusStr);
        setManagedCount(parseManagedCount(managedCountStr));
    }

    private static int parseManagedCount(String managedCountStr) throws InvalidEmployeeDataException {
        try {
            if (managedCountStr == null || managedCountStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Số người quản lý không được để trống!");
            }
            return Integer.parseInt(managedCountStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Số người quản lý phải là số nguyên hợp lệ!");
        }
    }

    public int getManagedCount() {
        return managedCount;
    }

    public void setManagedCount(int managedCount) throws InvalidEmployeeDataException {
        if (managedCount < 0) {
            throw new InvalidEmployeeDataException("Số người quản lý không được nhỏ hơn 0!");
        }
        this.managedCount = managedCount;
    }

    // Phụ cấp trách nhiệm = số người quản lý * 500.000 VNĐ
    public double getResponsibilityAllowance() {
        return managedCount * ALLOWANCE_PER_PERSON;
    }

    // Đa hình: Lương = Lương FullTime (super) + Phụ cấp trách nhiệm
    @Override
    public double calculateSalary() {
        return super.calculateSalary() + getResponsibilityAllowance();
    }

    // Thuế TNCN tính trên tổng lương đã bao gồm phụ cấp trách nhiệm
    @Override
    public double calculateTax() {
        return TaxCalculator.calculateTax(calculateSalary());
    }

    @Override
    public String getEmployeeType() {
        return "Quản lý";
    }

    @Override
    public String getTypeName() {
        return "Manager";
    }
}
