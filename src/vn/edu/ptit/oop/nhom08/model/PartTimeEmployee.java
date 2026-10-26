package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

public class PartTimeEmployee extends Employee {
    private double workingHours; // So gio lam viec trong thang

    public PartTimeEmployee(String id, String fullName, String department, double hourlyRate, LocalDate hireDate,
                            double workingHours) throws InvalidEmployeeDataException {
        super(id, fullName, department, hourlyRate, hireDate);
        setWorkingHours(workingHours);
    }

    public PartTimeEmployee(String id, String fullName, String department, String hourlyRateStr, String hireDateStr,
                            double workingHours) throws InvalidEmployeeDataException {
        super(id, fullName, department, hourlyRateStr, hireDateStr);
        setWorkingHours(workingHours);
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) throws InvalidEmployeeDataException {
        if (workingHours < 0) {
            throw new InvalidEmployeeDataException("So gio lam viec khong duoc nho hon 0!");
        }
        this.workingHours = workingHours;
    }

    @Override
    public double calculateSalary() {
        // baseSalary o day dai dien cho muc luong theo gio (hourlyRate)
        return getBaseSalary() * workingHours;
    }

    @Override
    public String getTypeName() {
        return "Part-time";
    }
}
