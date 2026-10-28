package vn.edu.ptit.oop.nhom08.model;

import java.util.List;

public final class TaxCalculator {

    // Cận trên của từng bậc (bậc cuối không có cận trên)
    private static final double[] BRACKET_LIMITS = {10_000_000, 20_000_000, 40_000_000};
    // Thuế suất tương ứng 4 bậc
    private static final double[] RATES = {0.0, 0.05, 0.10, 0.20};

    // Lớp tiện ích: không cho tạo đối tượng
    private TaxCalculator() {
    }

    /** Tính thuế lũy tiến từ tổng thu nhập tháng. */
    public static double calculateTax(double income) {
        if (income < 0) {
            throw new IllegalArgumentException("Thu nhập không được âm");
        }
        double tax = 0;
        double lower = 0;
        for (int i = 0; i < RATES.length; i++) {
            double upper = (i < BRACKET_LIMITS.length) ? BRACKET_LIMITS[i] : Double.MAX_VALUE;
            if (income > lower) {
                tax += (Math.min(income, upper) - lower) * RATES[i];
            }
            lower = upper;
        }
        return tax;
    }

    /** Tổng thuế của nhiều đối tượng chịu thuế (đa hình qua interface Taxable). */
    public static double calculateTotalTax(List<? extends Taxable> list) {
        double total = 0;
        for (Taxable t : list) {
            total += t.calculateTax();
        }
        return total;
    }
}

