package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

// Thực tập sinh (hưởng trợ cấp cố định, không chịu thuế TNCN)
public class Intern extends Employee {

    // Khởi tạo với kiểu dữ liệu chuẩn (double, LocalDate)
    public Intern(String id, String fullName, String department, double allowance, LocalDate hireDate) 
            throws InvalidEmployeeDataException {
        super(id, fullName, department, allowance, hireDate);
    }

    // Khởi tạo tiện ích hỗ trợ nạp dữ liệu từ chuỗi (Form/CSV)
    public Intern(String id, String fullName, String department, String allowanceStr, String hireDateStr) 
            throws InvalidEmployeeDataException {
        this(id, fullName, department, parseAllowance(allowanceStr), parseHireDate(hireDateStr));
    }

    private static double parseAllowance(String allowanceStr) throws InvalidEmployeeDataException {
        try {
            if (allowanceStr == null || allowanceStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Trợ cấp thực tập sinh không được để trống!");
            }
            return Double.parseDouble(allowanceStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Trợ cấp thực tập sinh không đúng định dạng số!");
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

    public double getAllowance() {
        return getBaseSalary();
    }

    public void setAllowance(double allowance) throws InvalidEmployeeDataException {
        setBaseSalary(allowance);
    }

    @Override
    public double calculateSalary() {
        // Lương của thực tập sinh bằng chính khoản trợ cấp cố định (baseSalary)
        return getBaseSalary();
    }

    @Override
    public String getEmployeeType() {
        return "Thực tập sinh";
    }

    // Tương thích thêm getTypeName nếu có component gọi
    public String getTypeName() {
        return "Intern";
    }
}
