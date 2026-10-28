package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

/**
 * Quản lý: kế thừa FullTimeEmployee (kế thừa 2 tầng: Manager -> FullTimeEmployee -> Employee).
 * Có thêm phụ cấp trách nhiệm tính theo số người quản lý.
 * Thuế TNCN được kế thừa từ FullTimeEmployee (Taxable), tính trên tổng lương đã gồm phụ cấp trách nhiệm.
 */
public class Manager extends FullTimeEmployee {

    /** Phụ cấp trách nhiệm cho mỗi người được quản lý (VNĐ). */
    private static final double ALLOWANCE_PER_PERSON = 500_000;

    private int managedCount; // Số người quản lý (>= 0)

    // Khởi tạo với kiểu dữ liệu chuẩn (double, LocalDate)
    public Manager(String id, String fullName, String department, double baseSalary, LocalDate hireDate,
                   double salaryFactor, double bonus, int managedCount) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalary, hireDate, salaryFactor, bonus);
        setManagedCount(managedCount);
    }

    // Khởi tạo tiện ích hỗ trợ nạp dữ liệu từ chuỗi (Form/CSV)
    public Manager(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                   double salaryFactor, double bonus, int managedCount) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalaryStr, hireDateStr, salaryFactor, bonus);
        setManagedCount(managedCount);
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

    /** Phụ cấp trách nhiệm = số người quản lý x 500.000 VNĐ. */
    public double getResponsibilityAllowance() {
        return managedCount * ALLOWANCE_PER_PERSON;
    }

    /** Đa hình: lương = lương FullTime (super) + phụ cấp trách nhiệm. */
    @Override
    public double calculateSalary() {
        return super.calculateSalary() + getResponsibilityAllowance();
    }

    @Override
    public String getEmployeeType() {
        return "Quản lý";
    }

    // FullTimeEmployee đã override getTypeName() trả "Full-time", nên Manager phải override lại
    @Override
    public String getTypeName() {
        return "Manager";
    }
}
