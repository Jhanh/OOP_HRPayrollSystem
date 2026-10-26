package vn.edu.ptit.oop.nhom02.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import vn.edu.ptit.oop.nhom02.exception.InvalidEmployeeDataException;


// Lop truu tuong Employee
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

    public Employee(String id, String fullName, String department, String baseSalaryStr, String hireDateStr) 
            throws InvalidEmployeeDataException {
        setId(id);
        setFullName(fullName);
        setDepartment(department);
        setBaseSalary(baseSalaryStr);
        setHireDate(hireDateStr);
    }

    public abstract double calculateSalary();

    // loai nhan vien(Full-time, Part-time, Intern, Manager)
    public abstract String getTypeName();

    public final int getSeniorityYears() {
        if (hireDate == null) {
            return 0;
        }
        return Period.between(hireDate, LocalDate.now()).getYears();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws InvalidEmployeeDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Ma nhan vien khong duoc de trong!");
        }
        String cleanId = id.trim();
        if (!cleanId.matches("^[a-zA-Z0-9_-]+$")) {
            throw new InvalidEmployeeDataException("Ma nhan vien chi duoc chua chu cai, chu so, dau gach ngang (-) hoac gach duoi (_)!");
        }
        this.id = cleanId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) throws InvalidEmployeeDataException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Ho ten nhan vien khong duoc de trong!");
        }
        String cleanName = fullName.trim();
        if (!cleanName.matches("^[\\p{L}\\s]+$")) {
            throw new InvalidEmployeeDataException("Ho ten nhan vien chi duoc chua chu cai va khoang trang, khong chua so hay ky tu đac biet!");
        }
        this.fullName = cleanName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) throws InvalidEmployeeDataException {
        if (department == null || department.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Phong ban khong duoc de trong!");
        }
        this.department = department.trim();
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) throws InvalidEmployeeDataException {
        if (baseSalary < 0) {
            throw new InvalidEmployeeDataException("Luong co ban khong duoc nho hon 0!");
        }
        this.baseSalary = baseSalary;
    }

    public void setBaseSalary(String baseSalaryStr) throws InvalidEmployeeDataException {
        if (baseSalaryStr == null || baseSalaryStr.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Luong co ban khong duoc de trong!");
        }
        try {
            double salary = Double.parseDouble(baseSalaryStr.trim());
            setBaseSalary(salary);
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Luong co ban phai la so hop le, khong chua ki tu chu!");
        }
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) throws InvalidEmployeeDataException {
        if (hireDate == null) {
            throw new InvalidEmployeeDataException("Ngay vao lam khong duoc de trong!");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new InvalidEmployeeDataException("Ngay vao lam khong duoc lon hon ngay hien tai!");
        }
        this.hireDate = hireDate;
    }

    public void setHireDate(String hireDateStr) throws InvalidEmployeeDataException {
        if (hireDateStr == null || hireDateStr.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Ngay vao lam khong duoc de trong!");
        }
        try {
            LocalDate date = LocalDate.parse(hireDateStr.trim());
            setHireDate(date);
        } catch (DateTimeParseException e) {
            throw new InvalidEmployeeDataException("Ngay vao lam khong dung dinh dang (YYYY-MM-DD) hoac ngay khong ton tai!");
        }
    }

    @Override
    public String toString() {
        return "Ma NV: " + id + " | Ten: " + fullName + " | Loai: " + getTypeName() + " | Phong: " + department;
    }
}
