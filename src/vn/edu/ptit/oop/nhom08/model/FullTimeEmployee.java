package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

// Nhân viên toàn thời gian (hưởng lương cơ bản * hệ số + thưởng, chịu thuế TNCN)
public class FullTimeEmployee extends Employee implements Taxable {
    private double salaryFactor; // Hệ số lương (>= 1.0)
    private double bonus;        // Tiền thưởng/phụ cấp (>= 0)

    public FullTimeEmployee(String id, String fullName, String department, double baseSalary, LocalDate hireDate,
                            double salaryFactor, double bonus) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalary, hireDate);
        setSalaryFactor(salaryFactor);
        setBonus(bonus);
    }

    public FullTimeEmployee(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                            double salaryFactor, double bonus) throws InvalidEmployeeDataException {
        this(id, fullName, department, parseBaseSalary(baseSalaryStr), parseHireDate(hireDateStr), salaryFactor, bonus);
    }

    public FullTimeEmployee(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                            String salaryFactorStr, String bonusStr) throws InvalidEmployeeDataException {
        this(id, fullName, department, parseBaseSalary(baseSalaryStr), parseHireDate(hireDateStr),
                parseSalaryFactor(salaryFactorStr), parseBonus(bonusStr));
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

    private static double parseSalaryFactor(String salaryFactorStr) throws InvalidEmployeeDataException {
        try {
            if (salaryFactorStr == null || salaryFactorStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Hệ số lương không được để trống!");
            }
            return Double.parseDouble(salaryFactorStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Hệ số lương không đúng định dạng số!");
        }
    }

    private static double parseBonus(String bonusStr) throws InvalidEmployeeDataException {
        try {
            if (bonusStr == null || bonusStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Tiền thưởng/phụ cấp không được để trống!");
            }
            return Double.parseDouble(bonusStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Tiền thưởng/phụ cấp không đúng định dạng số!");
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

    @Override
    public double calculateTax() {
        return TaxCalculator.calculateTax(calculateSalary());
    }

    @Override
    public String getEmployeeType() {
        return "Toàn thời gian";
    }

    public String getTypeName() {
        return "Full-time";
    }
}
