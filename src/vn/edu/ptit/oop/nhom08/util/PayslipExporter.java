package vn.edu.ptit.oop.nhom08.util;

public class PayslipExporter {
    
}
package vn.edu.ptit.oop.nhom08.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import vn.edu.ptit.oop.nhom08.model.Employee;

/**
 * Xuất phiếu lương ra file HTML hoặc TXT (BONUS).
 */
public class PayslipExporter {

    private static final NumberFormat MONEY = NumberFormat.getInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PayslipExporter() { }

    private static String money(double v) {
        return MONEY.format(Math.round(v)) + " VNĐ";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /** Xuất ra file TXT. */
    public static void exportToTxt(Employee e, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            w.write("==============================================");
            w.newLine();
            w.write("              PHIẾU LƯƠNG NHÂN VIÊN");
            w.newLine();
            w.write("==============================================");
            w.newLine();
            w.write("Ngày xuất   : " + LocalDate.now().format(DATE)); w.newLine();
            w.write("Mã NV       : " + e.getId());                    w.newLine();
            w.write("Họ tên      : " + e.getFullName());              w.newLine();
            w.write("Phòng ban   : " + e.getDepartment());            w.newLine();
            w.write("Loại NV     : " + e.getClass().getSimpleName()); w.newLine();
            w.write("----------------------------------------------"); w.newLine();
            w.write("Lương thực nhận: " + money(e.calculateSalary())); w.newLine();
            w.write("=============================================="); w.newLine();
        }
    }

    /** Xuất ra file HTML. */
    public static void exportToHtml(Employee e, String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"vi\">\n<head>\n<meta charset=\"UTF-8\">\n")
          .append("<title>Phiếu lương - ").append(esc(e.getFullName())).append("</title>\n")
          .append("<style>")
          .append("body{font-family:Arial,sans-serif;max-width:560px;margin:30px auto;}")
          .append("h2{text-align:center;color:#1a4d8f;}")
          .append("table{width:100%;border-collapse:collapse;}")
          .append("td{padding:8px;border:1px solid #ccc;}")
          .append("td:first-child{background:#f0f4fa;font-weight:bold;width:40%;}")
          .append(".total td{background:#fff3cd;font-weight:bold;}")
          .append("</style>\n</head>\n<body>\n")
          .append("<h2>PHIẾU LƯƠNG NHÂN VIÊN</h2>\n<table>\n");
        row(sb, "Ngày xuất", LocalDate.now().format(DATE));
        row(sb, "Mã nhân viên", e.getId());
        row(sb, "Họ tên", e.getFullName());
        row(sb, "Phòng ban", e.getDepartment());
        row(sb, "Loại nhân viên", e.getClass().getSimpleName());
        sb.append("<tr class=\"total\"><td>Lương thực nhận</td><td>")
          .append(money(e.calculateSalary())).append("</td></tr>\n")
          .append("</table>\n</body>\n</html>");

        Files.write(Paths.get(filePath), sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void row(StringBuilder sb, String label, String value) {
        sb.append("<tr><td>").append(esc(label)).append("</td><td>")
          .append(esc(value)).append("</td></tr>\n");
    }

    /** Tự chọn định dạng theo đuôi file (.html hoặc .txt). */
    public static void export(Employee e, String filePath) throws IOException {
        if (filePath.toLowerCase().endsWith(".html")) {
            exportToHtml(e, filePath);
        } else {
            exportToTxt(e, filePath);
        }
    }
}
