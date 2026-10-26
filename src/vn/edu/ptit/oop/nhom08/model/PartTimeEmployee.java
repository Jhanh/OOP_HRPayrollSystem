package vn.edu.ptit.oop.nhom08.model;

import java.time.LocalDate;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;

// Nhân viên bán thời gian (lương tính theo giờ làm thực tế)
public class PartTimeEmployee extends Employee {
    private double workingHours; // Số giờ làm việc trong tháng (>= 0)

    // Khởi tạo với kiểu dữ liệu chuẩn (double, LocalDate)
    public PartTimeEmployee(String id, String fullName, String department, double hourlyRate, LocalDate hireDate,
                            double workingHours) throws InvalidEmployeeDataException {
        super(id, fullName, department, hourlyRate, hireDate);
        setWorkingHours(workingHours);
    }

    // Khởi tạo tiện ích hỗ trợ nạp dữ liệu từ chuỗi (Form/CSV)
    public PartTimeEmployee(String id, String fullName, String department, String hourlyRateStr, String hireDateStr,
                            double workingHours) throws InvalidEmployeeDataException {
        this(id, fullName, department, parseHourlyRate(hourlyRateStr), parseHireDate(hireDateStr), workingHours);
    }

    // Khởi tạo tiện ích hỗ trợ nạp toàn bộ tham số từ chuỗi
    public PartTimeEmployee(String id, String fullName, String department, String hourlyRateStr, String hireDateStr,
                            String workingHoursStr) throws InvalidEmployeeDataException {
        this(id, fullName, department, parseHourlyRate(hourlyRateStr), parseHireDate(hireDateStr), parseWorkingHours(workingHoursStr));
    }

    private static double parseHourlyRate(String hourlyRateStr) throws InvalidEmployeeDataException {
        try {
            if (hourlyRateStr == null || hourlyRateStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Lương theo giờ không được để trống!");
            }
            return Double.parseDouble(hourlyRateStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Lương theo giờ không đúng định dạng số!");
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

    private static double parseWorkingHours(String workingHoursStr) throws InvalidEmployeeDataException {
        try {
            if (workingHoursStr == null || workingHoursStr.trim().isEmpty()) {
                throw new InvalidEmployeeDataException("Số giờ làm việc không được để trống!");
            }
            return Double.parseDouble(workingHoursStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException("Số giờ làm việc không đúng định dạng số!");
        }
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) throws InvalidEmployeeDataException {
        if (workingHours < 0) {
            throw new InvalidEmployeeDataException("Số giờ làm việc không được nhỏ hơn 0!");
        }
        this.workingHours = workingHours;
    }

    // Bí danh để tương thích trực tiếp với khái niệm lương theo giờ
    public double getHourlyRate() {
        return getBaseSalary();
    }

    public void setHourlyRate(double hourlyRate) throws InvalidEmployeeDataException {
        setBaseSalary(hourlyRate);
    }

    @Override
    public double calculateSalary() {
        // Lương bán thời gian = Lương mỗi giờ (baseSalary) * Số giờ làm việc
        return getBaseSalary() * workingHours;
    }

    @Override
    public String getEmployeeType() {
        return "Bán thời gian";
    }

    // Tương thích thêm getTypeName nếu có component gọi
    public String getTypeName() {
        return "Part-time";
    }
}
