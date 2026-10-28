package vn.edu.ptit.oop.nhom08.model;

import java.util.List;

// Tiện ích tính thuế TNCN lũy tiến từng phần và tiền lương thực lĩnh
public final class TaxCalculator {

    public static final double LEVEL_1_LIMIT = 10_000_000; // Ngưỡng miễn thuế: <= 10 triệu
    public static final double LEVEL_2_LIMIT = 20_000_000; // Cận trên bậc 2: 20 triệu
    public static final double LEVEL_3_LIMIT = 40_000_000; // Cận trên bậc 3: 40 triệu

    public static final double RATE_LEVEL_1 = 0.00; // Thuế suất bậc 1: 0%
    public static final double RATE_LEVEL_2 = 0.05; // Thuế suất bậc 2: 5%
    public static final double RATE_LEVEL_3 = 0.10; // Thuế suất bậc 3: 10%
    public static final double RATE_LEVEL_4 = 0.20; // Thuế suất bậc 4: 20%

    // Thuế tối đa tích lũy tại các mốc bậc 2 và bậc 3
    private static final double MAX_TAX_LEVEL_2 = (LEVEL_2_LIMIT - LEVEL_1_LIMIT) * RATE_LEVEL_2; // 500.000 VNĐ
    private static final double MAX_TAX_LEVEL_3 = MAX_TAX_LEVEL_2 + (LEVEL_3_LIMIT - LEVEL_2_LIMIT) * RATE_LEVEL_3; // 2.500.000 VNĐ

    private TaxCalculator() {
    }

    // Tính thuế TNCN lũy tiến từ tổng thu nhập chịu thuế
    public static double calculateTax(double income) {
        if (income < 0) {
            throw new IllegalArgumentException("Thu nhập chịu thuế không được nhỏ hơn 0!");
        }
        if (income <= LEVEL_1_LIMIT) {
            return 0.0;
        } else if (income <= LEVEL_2_LIMIT) {
            return (income - LEVEL_1_LIMIT) * RATE_LEVEL_2;
        } else if (income <= LEVEL_3_LIMIT) {
            return MAX_TAX_LEVEL_2 + (income - LEVEL_2_LIMIT) * RATE_LEVEL_3;
        } else {
            return MAX_TAX_LEVEL_3 + (income - LEVEL_3_LIMIT) * RATE_LEVEL_4;
        }
    }

    // Tính thuế TNCN cho nhân viên: chỉ tính nếu thuộc diện Taxable, còn lại miễn thuế (0 VNĐ)
    public static double calculateTax(Employee employee) {
        if (employee instanceof Taxable) {
            return ((Taxable) employee).calculateTax();
        }
        return 0.0;
    }

    // Tính lương thực lĩnh cho nhân viên (Net Salary = Gross Salary - Tax)
    public static double calculateNetSalary(Employee employee) {
        if (employee == null) {
            return 0.0;
        }
        return Math.max(0.0, employee.calculateSalary() - calculateTax(employee));
    }

    // Tính lương thực lĩnh từ tổng lương và thuế
    public static double calculateNetSalary(double grossSalary, double tax) {
        return Math.max(0.0, grossSalary - tax);
    }

    // Tính tổng thuế cho danh sách nhân viên
    public static double calculateTotalTax(List<Employee> employees) {
        if (employees == null) {
            return 0.0;
        }
        double total = 0.0;
        for (Employee e : employees) {
            total += calculateTax(e);
        }
        return total;
    }

    // Tính tổng thuế cho danh sách các đối tượng cài đặt giao diện Taxable
    public static double calculateTotalTaxFromTaxable(List<? extends Taxable> taxables) {
        if (taxables == null) {
            return 0.0;
        }
        double total = 0.0;
        for (Taxable t : taxables) {
            if (t != null) {
                total += t.calculateTax();
            }
        }
        return total;
    }

    // Kiểm tra nhân viên có thuộc diện chịu thuế TNCN hay không
    public static boolean isTaxable(Employee employee) {
        return employee instanceof Taxable;
    }

    // Xác định bậc thuế (1, 2, 3, 4)
    public static int getTaxBracket(double income) {
        if (income <= LEVEL_1_LIMIT) {
            return 1;
        } else if (income <= LEVEL_2_LIMIT) {
            return 2;
        } else if (income <= LEVEL_3_LIMIT) {
            return 3;
        } else {
            return 4;
        }
    }

    // Mô tả bậc thuế phục vụ hiển thị chi tiết hoặc xuất phiếu lương
    public static String getTaxBracketDescription(double income) {
        int bracket = getTaxBracket(income);
        switch (bracket) {
            case 1:
                return "Bậc 1 (0% - Đến 10.000.000 VNĐ)";
            case 2:
                return "Bậc 2 (5% - Trên 10.000.000 đến 20.000.000 VNĐ)";
            case 3:
                return "Bậc 3 (10% - Trên 20.000.000 đến 40.000.000 VNĐ)";
            case 4:
                return "Bậc 4 (20% - Trên 40.000.000 VNĐ)";
            default:
                return "";
        }
    }
}
