package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom02.exception.InvalidEmployeeDataException;

public class FullTimeEmployee extends Employee {
    private double salaryFactor; // He so luong
    private double bonus;        // Tien thuong

    public FullTimeEmployee(String id, String fullName, String department, double baseSalary, LocalDate hireDate,
                            double salaryFactor, double bonus) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalary, hireDate);
        setSalaryFactor(salaryFactor);
        setBonus(bonus);
    }

    public FullTimeEmployee(String id, String fullName, String department, String baseSalaryStr, String hireDateStr,
                            double salaryFactor, double bonus) throws InvalidEmployeeDataException {
        super(id, fullName, department, baseSalaryStr, hireDateStr);
        setSalaryFactor(salaryFactor);
        setBonus(bonus);
    }

    public double getSalaryFactor() {
        return salaryFactor;
    }

    public void setSalaryFactor(double salaryFactor) throws InvalidEmployeeDataException {
        if (salaryFactor < 1.0) {
            throw new InvalidEmployeeDataException("He so luong phai toi thieu tu 1.0 tro len!");
        }
        this.salaryFactor = salaryFactor;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) throws InvalidEmployeeDataException {
        if (bonus < 0) {
            throw new InvalidEmployeeDataException("Tien thuong khong duoc nho hon 0!");
        }
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return (getBaseSalary() * salaryFactor) + bonus;
    }

    @Override
    public String getTypeName() {
        return "Full-time";
    }
}
