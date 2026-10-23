package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

public class Intern extends Employee {

    public Intern(String id, String fullName, String department, double allowance, LocalDate hireDate) 
            throws InvalidEmployeeDataException {
        super(id, fullName, department, allowance, hireDate);
    }

    public Intern(String id, String fullName, String department, String allowanceStr, String hireDateStr) 
            throws InvalidEmployeeDataException {
        super(id, fullName, department, allowanceStr, hireDateStr);
    }

    @Override
    public double calculateSalary() {
        // baseSalary dai dien cho khoan tro cap co dinh (allowance) cua thuc tap sinh
        return getBaseSalary();
    }

    @Override
    public String getTypeName() {
        return "Intern";
    }
}
