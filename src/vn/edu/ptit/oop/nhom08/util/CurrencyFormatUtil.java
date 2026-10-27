package vn.edu.ptit.oop.nhom08.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

// Tiện ích định dạng tiền tệ VNĐ và ngày tháng chuẩn Việt Nam
public final class CurrencyFormatUtil {

    private static final String PATTERN = "#,##0";
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.forLanguageTag("vi-VN"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        SYMBOLS.setGroupingSeparator('.');
        SYMBOLS.setDecimalSeparator(',');
    }

    private CurrencyFormatUtil() {
        // Lớp tiện ích tĩnh, ngăn khởi tạo đối tượng
    }

    // Định dạng số tiền thành chuỗi (ví dụ: 15000000 -> "15.000.000")
    public static String format(double amount) {
        return new DecimalFormat(PATTERN, SYMBOLS).format(amount);
    }

    // Định dạng số tiền kèm đơn vị (ví dụ: 15000000 -> "15.000.000 VNĐ")
    public static String formatWithUnit(double amount) {
        return format(amount) + " VNĐ";
    }

    // Chuyển đổi chuỗi tiền tệ về số thực double (ví dụ: "15.000.000 VNĐ" -> 15000000.0)
    public static double parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Chuỗi tiền tệ không được để trống!");
        }
        // Xóa đơn vị tiền tệ VNĐ/VND (không phân biệt hoa thường) và khoảng trắng
        String cleaned = text.replaceAll("(?i)\\s*vn[đd]", "").trim();
        try {
            return new DecimalFormat(PATTERN, SYMBOLS).parse(cleaned).doubleValue();
        } catch (ParseException e) {
            // Thử parse thông thường nếu chuỗi không có dấu chấm phân cách
            try {
                return Double.parseDouble(cleaned.replace(",", "."));
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Không đọc được số tiền: " + text);
            }
        }
    }

    // Định dạng ngày LocalDate sang chuỗi (ví dụ: 2022-03-15 -> "15/03/2022")
    public static String formatDate(LocalDate date) {
        return (date == null) ? "" : date.format(DATE_FORMATTER);
    }

    // Chuyển chuỗi "dd/MM/yyyy" hoặc "yyyy-MM-dd" về LocalDate
    public static LocalDate parseDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        String trimmed = text.trim();
        try {
            return LocalDate.parse(trimmed, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(trimmed);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Ngày không đúng định dạng (dd/MM/yyyy hoặc YYYY-MM-DD): " + text);
            }
        }
    }
}
