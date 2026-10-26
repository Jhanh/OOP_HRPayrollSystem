package vn.edu.ptit.oop.nhom08.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Tiện ích định dạng tiền tệ VND (dùng dấu chấm ngăn cách hàng nghìn: 15.000.000)
public final class CurrencyFormatUtil {

    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(new Local("vi", "VN"));
    private static final String PATTERN = "#,##0";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        SYMBOLS.setGroupingSeparator('.');//vd: 15.000.000(mười lăm triệu)
        SYMBOLS.setDecimalSeparator(',');//vd: 15.000.000,15(mười lăm triệu và 15 xu)
    }

    private CurrencyFormatUtil() {
        // Lớp tiện ích, không cho tạo đối tượng
    }

    // Ví dụ: 15000000 -> "15.000.000"
    public static String format(double amount) {
        // DecimalFormat không an toàn đa luồng nên tạo mới mỗi lần gọi
        return new DecimalFormat(PATTERN, SYMBOLS).format(amount);
    }

    // Ví dụ: 15000000 -> "15.000.000 VNĐ"
    public static String formatWithUnit(double amount) {
        return format(amount) + " VNĐ";
    }

    // Ví dụ: 2022-03-15 -> "15/03/2022"
    public static String formatDate(LocalDate date) {
        return (date == null) ? "" : date.format(DATE_FORMAT);
    }

    // Chuyển chuỗi "15.000.000" hoặc "15.000.000 VNĐ" về số. Ném NumberFormatException nếu sai định dạng.
    public static double parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Chuỗi tiền tệ rỗng");//vd: " " || "null" ->"Chuỗi tiền tệ rỗng"
        }
        String cleaned = text.replace("VNĐ", "").replace("VND", "").trim();
        //vd: 15.000.000 VND -> 15000000.0
        //replace phân biệt được chữ hoa và chữ thường nên "vnd" -> lỗi
        try {
            return new DecimalFormat(PATTERN, SYMBOLS).parse(cleaned).doubleValue();
        } catch (ParseException e) {
            throw new NumberFormatException("Không đọc được số tiền: " + text);
            //vd: "abc" -> "Không đọc được số tiền: abc"
        }
    }
}
