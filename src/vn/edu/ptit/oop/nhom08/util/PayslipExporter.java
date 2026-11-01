package vn.edu.ptit.oop.nhom08.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.FullTimeEmployee;
import vn.edu.ptit.oop.nhom08.model.Intern;
import vn.edu.ptit.oop.nhom08.model.Manager;
import vn.edu.ptit.oop.nhom08.model.PartTimeEmployee;
import vn.edu.ptit.oop.nhom08.model.TaxCalculator;

// Tiện ích xuất phiếu lương cá nhân và bảng lương toàn công ty ra file HTML/CSV/TXT (BONUS)
public final class PayslipExporter {

    private PayslipExporter() {
    }

    // Xuất phiếu lương dạng văn bản thuần (.txt)
    public static void exportToTxt(Employee e, String filePath) throws IOException {
        double grossSalary = e.calculateSalary();
        double tax = TaxCalculator.calculateTax(e);
        double netSalary = TaxCalculator.calculateNetSalary(e);

        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write("======================================================================"); writer.newLine();
            writer.write("                HR PAYROLL SYSTEM - ENTERPRISE EDITION");                 writer.newLine();
            writer.write("                    PHIẾU LƯƠNG NHÂN VIÊN CHI TIẾT");                     writer.newLine();
            writer.write("======================================================================"); writer.newLine();
            writer.write("Ngày xuất phiếu  : " + CurrencyFormatUtil.formatDate(LocalDate.now()));  writer.newLine();
            writer.write("Mã nhân viên     : " + e.getId());                                       writer.newLine();
            writer.write("Họ và tên        : " + e.getFullName());                                 writer.newLine();
            writer.write("Phòng ban        : " + e.getDepartment());                               writer.newLine();
            writer.write("Loại hợp đồng    : " + e.getEmployeeType());                             writer.newLine();
            writer.write("Ngày vào làm     : " + CurrencyFormatUtil.formatDate(e.getHireDate()));  writer.newLine();
            writer.write("----------------------------------------------------------------------"); writer.newLine();
            writer.write("CHI TIẾT LƯƠNG & THUẾ:");                                                writer.newLine();

            if (e instanceof Manager) {
                Manager m = (Manager) e;
                writer.write(" - Lương cơ bản       : " + CurrencyFormatUtil.formatWithUnit(m.getBaseSalary())); writer.newLine();
                writer.write(" - Hệ số lương        : " + m.getSalaryFactor());                                  writer.newLine();
                writer.write(" - Phụ cấp            : " + CurrencyFormatUtil.formatWithUnit(m.getBonus()));      writer.newLine();
                writer.write(" - Số người quản lý   : " + m.getManagedCount() + " người");                        writer.newLine();
                writer.write(" - Phụ cấp trách nhiệm: " + CurrencyFormatUtil.formatWithUnit(m.getResponsibilityAllowance())); writer.newLine();
            } else if (e instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) e;
                writer.write(" - Lương cơ bản       : " + CurrencyFormatUtil.formatWithUnit(ft.getBaseSalary())); writer.newLine();
                writer.write(" - Hệ số lương        : " + ft.getSalaryFactor());                                  writer.newLine();
                writer.write(" - Phụ cấp            : " + CurrencyFormatUtil.formatWithUnit(ft.getBonus()));      writer.newLine();
            } else if (e instanceof PartTimeEmployee) {
                PartTimeEmployee pt = (PartTimeEmployee) e;
                writer.write(" - Lương theo giờ     : " + CurrencyFormatUtil.formatWithUnit(pt.getHourlyRate()) + " / giờ"); writer.newLine();
                writer.write(" - Số giờ làm việc    : " + pt.getWorkingHours() + " giờ");                         writer.newLine();
            } else if (e instanceof Intern) {
                Intern in = (Intern) e;
                writer.write(" - Mức trợ cấp cố định: " + CurrencyFormatUtil.formatWithUnit(in.getAllowance())); writer.newLine();
            }

            writer.write("----------------------------------------------------------------------"); writer.newLine();
            writer.write("TỔNG LƯƠNG (GROSS): " + CurrencyFormatUtil.formatWithUnit(grossSalary)); writer.newLine();
            writer.write("THUẾ TNCN         : " + CurrencyFormatUtil.formatWithUnit(tax));        writer.newLine();
            writer.write("======================================================================"); writer.newLine();
            writer.write("THỰC LĨNH (NET)   : " + CurrencyFormatUtil.formatWithUnit(netSalary));  writer.newLine();
            writer.write("======================================================================"); writer.newLine();
        }
    }

    // Xuất phiếu lương dạng trang HTML chuyên nghiệp (.html)
    public static void exportToHtml(Employee e, String filePath) throws IOException {
        double grossSalary = e.calculateSalary();
        double tax = TaxCalculator.calculateTax(e);
        double netSalary = TaxCalculator.calculateNetSalary(e);

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"vi\">\n<head>\n<meta charset=\"UTF-8\">\n")
          .append("<title>Phiếu lương - ").append(escapeHtml(e.getFullName())).append("</title>\n")
          .append("<style>\n")
          .append("body { font-family: 'Segoe UI', Arial, sans-serif; background: #f4f6f9; padding: 25px; margin: 0; }\n")
          .append(".card { max-width: 600px; margin: 0 auto; background: #fff; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); padding: 25px; border-top: 5px solid #007396; }\n")
          .append(".header { text-align: center; border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 20px; }\n")
          .append(".header h2 { margin: 0; color: #007396; text-transform: uppercase; font-size: 20px; }\n")
          .append(".header p { margin: 5px 0 0; color: #666; font-size: 13px; }\n")
          .append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 14px; }\n")
          .append("td { padding: 9px 12px; border-bottom: 1px solid #f0f0f0; }\n")
          .append("td.label { color: #555; width: 45%; font-weight: 600; }\n")
          .append("td.val { text-align: right; color: #222; }\n")
          .append(".section-title { font-weight: bold; background: #f8fafc; color: #007396; padding: 8px 12px; }\n")
          .append(".total-row { background: #e8f5e9; font-size: 16px; font-weight: bold; }\n")
          .append(".total-row td { color: #1b5e20; border-top: 2px solid #4caf50; }\n")
          .append(".footer { text-align: center; font-size: 12px; color: #888; margin-top: 15px; }\n")
          .append("</style>\n</head>\n<body>\n")
          .append("<div class=\"card\">\n")
          .append("<div class=\"header\">\n")
          .append("  <h2>Phiếu Lương Cá Nhân</h2>\n")
          .append("  <p>Hệ Thống Quản Lý Nhân Sự & Bảng Lương Doanh Nghiệp (Enterprise Edition)</p>\n")
          .append("  <p>Ngày xuất: ").append(CurrencyFormatUtil.formatDate(LocalDate.now())).append("</p>\n")
          .append("</div>\n")
          .append("<table>\n")
          .append("  <tr><td colspan=\"2\" class=\"section-title\">I. THÔNG TIN NHÂN SỰ</td></tr>\n");

        addHtmlRow(sb, "Mã nhân viên", e.getId());
        addHtmlRow(sb, "Họ và tên", e.getFullName());
        addHtmlRow(sb, "Phòng ban", e.getDepartment());
        addHtmlRow(sb, "Loại hợp đồng", e.getEmployeeType());
        addHtmlRow(sb, "Ngày vào làm", CurrencyFormatUtil.formatDate(e.getHireDate()));

        sb.append("  <tr><td colspan=\"2\" class=\"section-title\">II. THÔNG TIN THU NHẬP & THUẾ</td></tr>\n");
        if (e instanceof Manager) {
            Manager m = (Manager) e;
            addHtmlRow(sb, "Lương cơ bản", CurrencyFormatUtil.formatWithUnit(m.getBaseSalary()));
            addHtmlRow(sb, "Hệ số lương", String.valueOf(m.getSalaryFactor()));
            addHtmlRow(sb, "Phụ cấp", CurrencyFormatUtil.formatWithUnit(m.getBonus()));
            addHtmlRow(sb, "Số người quản lý", m.getManagedCount() + " người");
            addHtmlRow(sb, "Phụ cấp trách nhiệm", CurrencyFormatUtil.formatWithUnit(m.getResponsibilityAllowance()));
        } else if (e instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) e;
            addHtmlRow(sb, "Lương cơ bản", CurrencyFormatUtil.formatWithUnit(ft.getBaseSalary()));
            addHtmlRow(sb, "Hệ số lương", String.valueOf(ft.getSalaryFactor()));
            addHtmlRow(sb, "Phụ cấp", CurrencyFormatUtil.formatWithUnit(ft.getBonus()));
        } else if (e instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) e;
            addHtmlRow(sb, "Lương theo giờ", CurrencyFormatUtil.formatWithUnit(pt.getHourlyRate()) + " / giờ");
            addHtmlRow(sb, "Số giờ làm việc", pt.getWorkingHours() + " giờ");
        } else if (e instanceof Intern) {
            Intern in = (Intern) e;
            addHtmlRow(sb, "Mức trợ cấp cố định", CurrencyFormatUtil.formatWithUnit(in.getAllowance()));
        }

        addHtmlRow(sb, "Tổng lương (Gross)", CurrencyFormatUtil.formatWithUnit(grossSalary));
        addHtmlRow(sb, "Thuế TNCN", CurrencyFormatUtil.formatWithUnit(tax));

        sb.append("  <tr class=\"total-row\">\n")
          .append("    <td class=\"label\">THỰC LĨNH (NET)</td>\n")
          .append("    <td class=\"val\">").append(CurrencyFormatUtil.formatWithUnit(netSalary)).append("</td>\n")
          .append("  </tr>\n")
          .append("</table>\n")
          .append("<div class=\"footer\">Phiếu lương được trích xuất tự động từ hệ thống HR Payroll System.</div>\n")
          .append("</div>\n</body>\n</html>");

        Files.write(Paths.get(filePath), sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void addHtmlRow(StringBuilder sb, String label, String value) {
        sb.append("  <tr><td class=\"label\">").append(escapeHtml(label))
          .append("</td><td class=\"val\">").append(escapeHtml(value)).append("</td></tr>\n");
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // Tự động nhận diện đuôi file (.html hay .txt) để xuất đúng định dạng
    public static void export(Employee e, String filePath) throws IOException {
        if (filePath.toLowerCase().endsWith(".html") || filePath.toLowerCase().endsWith(".htm")) {
            exportToHtml(e, filePath);
        } else {
            exportToTxt(e, filePath);
        }
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }

    // Xuất bảng lương toàn công ty (tự nhận diện định dạng CSV hoặc HTML theo đuôi file)
    public static void exportCompanyPayroll(List<Employee> employees, String filePath) throws IOException {
        if (filePath.toLowerCase().endsWith(".csv")) {
            exportCompanyPayrollToCsv(employees, filePath);
        } else {
            exportCompanyPayrollToHtml(employees, filePath);
        }
    }

    // Xuất bảng lương toàn công ty ra tệp HTML chuyên nghiệp để xem trình duyệt, in ấn hoặc nộp báo cáo
    public static void exportCompanyPayrollToHtml(List<Employee> employees, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        LocalDate now = LocalDate.now();
        String monthYear = String.format("%02d/%d", now.getMonthValue(), now.getYear());

        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(Comparator.comparing(Employee::getDepartment).thenComparing(Employee::getId));

        double totalGross = 0;
        double totalTax = 0;
        double totalNet = 0;

        for (Employee e : sorted) {
            totalGross += e.calculateSalary();
            totalTax += TaxCalculator.calculateTax(e);
            totalNet += TaxCalculator.calculateNetSalary(e);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"vi\">\n<head>\n<meta charset=\"UTF-8\">\n")
          .append("<title>Bảng Lương Toàn Công Ty - Tháng ").append(monthYear).append("</title>\n")
          .append("<style>\n")
          .append("body { font-family: 'Segoe UI', Arial, sans-serif; background: #f1f5f9; padding: 25px; margin: 0; color: #1e293b; }\n")
          .append(".container { max-width: 1150px; margin: 0 auto; background: #fff; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.06); padding: 30px; border-top: 5px solid #007396; }\n")
          .append(".header { text-align: center; border-bottom: 2px solid #e2e8f0; padding-bottom: 18px; margin-bottom: 25px; }\n")
          .append(".header h1 { margin: 0; color: #007396; font-size: 22px; text-transform: uppercase; letter-spacing: 0.5px; }\n")
          .append(".header h2 { margin: 6px 0 0; color: #334155; font-size: 16px; font-weight: 600; }\n")
          .append(".header p { margin: 5px 0 0; color: #64748b; font-size: 13px; }\n")
          .append(".stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 25px; }\n")
          .append(".stat-card { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 6px; padding: 14px; text-align: center; }\n")
          .append(".stat-card .num { font-size: 18px; font-weight: bold; margin-top: 4px; }\n")
          .append(".stat-card .lbl { font-size: 11px; color: #64748b; text-transform: uppercase; font-weight: 600; }\n")
          .append(".text-blue { color: #0284c7; }\n")
          .append(".text-dark { color: #0f172a; }\n")
          .append(".text-orange { color: #ea580c; }\n")
          .append(".text-green { color: #16a34a; }\n")
          .append(".section-title { font-size: 15px; font-weight: bold; color: #007396; margin: 25px 0 10px 0; border-left: 4px solid #007396; padding-left: 10px; text-transform: uppercase; }\n")
          .append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 13px; }\n")
          .append("th { background: #f8fafc; color: #334155; font-weight: 600; text-align: left; padding: 10px 12px; border: 1px solid #cbd5e1; }\n")
          .append("td { padding: 8px 12px; border: 1px solid #e2e8f0; }\n")
          .append("tr:nth-child(even) td { background: #fafafa; }\n")
          .append(".text-right { text-align: right; }\n")
          .append(".text-center { text-align: center; }\n")
          .append(".font-bold { font-weight: bold; }\n")
          .append(".total-row td { background: #e8f5e9 !important; font-weight: bold; color: #166534; border-top: 2px solid #86efac; border-bottom: 2px solid #86efac; font-size: 13px; }\n")
          .append(".signatures { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-top: 40px; text-align: center; page-break-inside: avoid; }\n")
          .append(".sig-block b { font-size: 13px; color: #1e293b; text-transform: uppercase; }\n")
          .append(".sig-block i { font-size: 12px; color: #64748b; }\n")
          .append(".sig-space { height: 75px; }\n")
          .append(".footer { text-align: center; font-size: 11px; color: #94a3b8; margin-top: 25px; border-top: 1px solid #f1f5f9; padding-top: 15px; }\n")
          .append(".btn-print { background: #007396; color: white; border: none; padding: 7px 15px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 600; }\n")
          .append(".btn-print:hover { background: #005a75; }\n")
          .append("@media print {\n")
          .append("  body { background: #fff; padding: 0; }\n")
          .append("  .container { box-shadow: none; border-top: none; padding: 0; max-width: 100%; }\n")
          .append("  .no-print { display: none; }\n")
          .append("  @page { size: A4 landscape; margin: 12mm; }\n")
          .append("}\n")
          .append("</style>\n</head>\n<body>\n")
          .append("<div class=\"container\">\n")
          .append("  <div class=\"no-print\" style=\"text-align: right; margin-bottom: 15px;\">\n")
          .append("    <button onclick=\"window.print()\" class=\"btn-print\">🖨️ In Bảng Lương / Xuất PDF</button>\n")
          .append("  </div>\n")
          .append("  <div class=\"header\">\n")
          .append("    <h1>HR Payroll System Enterprise</h1>\n")
          .append("    <h2>BẢNG THANH TOÁN TIỀN LƯƠNG & THUẾ TNCN TOÀN CÔNG TY</h2>\n")
          .append("    <p>Kỳ tính lương: <b>Tháng ").append(monthYear).append("</b> | Ngày lập: ")
          .append(CurrencyFormatUtil.formatDate(now)).append("</p>\n")
          .append("  </div>\n")
          .append("  <div class=\"stat-grid\">\n")
          .append("    <div class=\"stat-card\"><div class=\"lbl\">Tổng số nhân sự</div><div class=\"num text-dark\">")
          .append(sorted.size()).append(" người</div></div>\n")
          .append("    <div class=\"stat-card\"><div class=\"lbl\">Tổng quỹ lương (Gross)</div><div class=\"num text-blue\">")
          .append(CurrencyFormatUtil.formatWithUnit(totalGross)).append("</div></div>\n")
          .append("    <div class=\"stat-card\"><div class=\"lbl\">Tổng thuế TNCN khấu trừ</div><div class=\"num text-orange\">")
          .append(CurrencyFormatUtil.formatWithUnit(totalTax)).append("</div></div>\n")
          .append("    <div class=\"stat-card\"><div class=\"lbl\">Tổng thực lĩnh (Net)</div><div class=\"num text-green\">")
          .append(CurrencyFormatUtil.formatWithUnit(totalNet)).append("</div></div>\n")
          .append("  </div>\n")
          .append("  <div class=\"section-title\">I. BẢNG KÊ CHI TIẾT LƯƠNG NHÂN SỰ</div>\n")
          .append("  <table>\n")
          .append("    <thead>\n")
          .append("      <tr>\n")
          .append("        <th class=\"text-center\" style=\"width: 45px;\">STT</th>\n")
          .append("        <th class=\"text-center\" style=\"width: 75px;\">Mã NV</th>\n")
          .append("        <th>Họ và tên</th>\n")
          .append("        <th>Phòng ban</th>\n")
          .append("        <th class=\"text-center\">Loại hợp đồng</th>\n")
          .append("        <th class=\"text-right\">Lương cơ bản / Trợ cấp</th>\n")
          .append("        <th class=\"text-right\">Tổng lương (Gross)</th>\n")
          .append("        <th class=\"text-right\">Thuế TNCN</th>\n")
          .append("        <th class=\"text-right\">Thực lĩnh (NET)</th>\n")
          .append("        <th class=\"text-center\" style=\"width: 90px;\">Ký nhận</th>\n")
          .append("      </tr>\n")
          .append("    </thead>\n")
          .append("    <tbody>\n");

        int stt = 1;
        for (Employee e : sorted) {
            double gross = e.calculateSalary();
            double tax = TaxCalculator.calculateTax(e);
            double net = TaxCalculator.calculateNetSalary(e);

            sb.append("      <tr>\n")
              .append("        <td class=\"text-center\">").append(stt++).append("</td>\n")
              .append("        <td class=\"text-center font-bold\">").append(escapeHtml(e.getId())).append("</td>\n")
              .append("        <td>").append(escapeHtml(e.getFullName())).append("</td>\n")
              .append("        <td>").append(escapeHtml(e.getDepartment())).append("</td>\n")
              .append("        <td class=\"text-center\">").append(escapeHtml(e.getEmployeeType())).append("</td>\n")
              .append("        <td class=\"text-right\">").append(CurrencyFormatUtil.formatWithUnit(e.getBaseSalary())).append("</td>\n")
              .append("        <td class=\"text-right font-bold\">").append(CurrencyFormatUtil.formatWithUnit(gross)).append("</td>\n")
              .append("        <td class=\"text-right\">").append(CurrencyFormatUtil.formatWithUnit(tax)).append("</td>\n")
              .append("        <td class=\"text-right font-bold text-green\">").append(CurrencyFormatUtil.formatWithUnit(net)).append("</td>\n")
              .append("        <td class=\"text-center\"></td>\n")
              .append("      </tr>\n");
        }

        sb.append("      <tr class=\"total-row\">\n")
          .append("        <td colspan=\"5\" class=\"text-center font-bold\">TỔNG CỘNG TOÀN CÔNG TY</td>\n")
          .append("        <td class=\"text-right font-bold\">-</td>\n")
          .append("        <td class=\"text-right font-bold\">").append(CurrencyFormatUtil.formatWithUnit(totalGross)).append("</td>\n")
          .append("        <td class=\"text-right font-bold\">").append(CurrencyFormatUtil.formatWithUnit(totalTax)).append("</td>\n")
          .append("        <td class=\"text-right font-bold text-green\">").append(CurrencyFormatUtil.formatWithUnit(totalNet)).append("</td>\n")
          .append("        <td></td>\n")
          .append("      </tr>\n")
          .append("    </tbody>\n")
          .append("  </table>\n")
          .append("  <div class=\"section-title\">II. TỔNG HỢP CHI PHÍ LƯƠNG THEO PHÒNG BAN</div>\n")
          .append("  <table>\n")
          .append("    <thead>\n")
          .append("      <tr>\n")
          .append("        <th class=\"text-center\" style=\"width: 45px;\">STT</th>\n")
          .append("        <th>Phòng ban</th>\n")
          .append("        <th class=\"text-center\" style=\"width: 100px;\">Số nhân sự</th>\n")
          .append("        <th class=\"text-right\">Tổng quỹ lương (Gross)</th>\n")
          .append("        <th class=\"text-right\">Thuế TNCN</th>\n")
          .append("        <th class=\"text-right\">Thực lĩnh (NET)</th>\n")
          .append("        <th class=\"text-right\" style=\"width: 130px;\">Tỷ trọng quỹ lương</th>\n")
          .append("      </tr>\n")
          .append("    </thead>\n")
          .append("    <tbody>\n");

        Map<String, List<Employee>> byDept = sorted.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new, Collectors.toList()));

        int deptStt = 1;
        for (Map.Entry<String, List<Employee>> entry : byDept.entrySet()) {
            String dept = entry.getKey();
            List<Employee> list = entry.getValue();
            double deptGross = list.stream().mapToDouble(Employee::calculateSalary).sum();
            double deptTax = TaxCalculator.calculateTotalTax(list);
            double deptNet = list.stream().mapToDouble(TaxCalculator::calculateNetSalary).sum();
            double pct = totalGross > 0 ? (deptGross / totalGross * 100.0) : 0.0;

            sb.append("      <tr>\n")
              .append("        <td class=\"text-center\">").append(deptStt++).append("</td>\n")
              .append("        <td class=\"font-bold\">").append(escapeHtml(dept)).append("</td>\n")
              .append("        <td class=\"text-center\">").append(list.size()).append(" người</td>\n")
              .append("        <td class=\"text-right font-bold\">").append(CurrencyFormatUtil.formatWithUnit(deptGross)).append("</td>\n")
              .append("        <td class=\"text-right\">").append(CurrencyFormatUtil.formatWithUnit(deptTax)).append("</td>\n")
              .append("        <td class=\"text-right font-bold text-green\">").append(CurrencyFormatUtil.formatWithUnit(deptNet)).append("</td>\n")
              .append("        <td class=\"text-right\">").append(String.format(Locale.US, "%.1f%%", pct)).append("</td>\n")
              .append("      </tr>\n");
        }

        sb.append("      <tr class=\"total-row\">\n")
          .append("        <td colspan=\"2\" class=\"text-center font-bold\">TỔNG CỘNG</td>\n")
          .append("        <td class=\"text-center font-bold\">").append(sorted.size()).append(" người</td>\n")
          .append("        <td class=\"text-right font-bold\">").append(CurrencyFormatUtil.formatWithUnit(totalGross)).append("</td>\n")
          .append("        <td class=\"text-right font-bold\">").append(CurrencyFormatUtil.formatWithUnit(totalTax)).append("</td>\n")
          .append("        <td class=\"text-right font-bold text-green\">").append(CurrencyFormatUtil.formatWithUnit(totalNet)).append("</td>\n")
          .append("        <td class=\"text-right font-bold\">100.0%</td>\n")
          .append("      </tr>\n")
          .append("    </tbody>\n")
          .append("  </table>\n")
          .append("  <div class=\"signatures\">\n")
          .append("    <div class=\"sig-block\">\n")
          .append("      <b>NGƯỜI LẬP BIỂU</b><br><i>(Ký và ghi rõ họ tên)</i>\n")
          .append("      <div class=\"sig-space\"></div>\n")
          .append("    </div>\n")
          .append("    <div class=\"sig-block\">\n")
          .append("      <b>KẾ TOÁN TRƯỞNG</b><br><i>(Ký và ghi rõ họ tên)</i>\n")
          .append("      <div class=\"sig-space\"></div>\n")
          .append("    </div>\n")
          .append("    <div class=\"sig-block\">\n")
          .append("      <b>GIÁM ĐỐC DOANH NGHIỆP</b><br><i>(Ký, đóng dấu & ghi rõ họ tên)</i>\n")
          .append("      <div class=\"sig-space\"></div>\n")
          .append("    </div>\n")
          .append("  </div>\n")
          .append("  <div class=\"footer\">Báo cáo bảng lương được trích xuất tự động từ hệ thống HR Payroll System Enterprise.</div>\n")
          .append("</div>\n</body>\n</html>");

        Files.write(path, sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    // Xuất bảng lương toàn công ty ra tệp Excel CSV (hỗ trợ tiếng Việt UTF-8 BOM)
    public static void exportCompanyPayrollToCsv(List<Employee> employees, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        LocalDate now = LocalDate.now();
        String monthYear = String.format("%02d/%d", now.getMonthValue(), now.getYear());

        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(Comparator.comparing(Employee::getDepartment).thenComparing(Employee::getId));

        double totalGross = 0;
        double totalTax = 0;
        double totalNet = 0;

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write("\uFEFF"); // UTF-8 BOM cho Microsoft Excel
            writer.write("HỆ THỐNG QUẢN LÝ NHÂN SỰ & BẢNG LƯƠNG - HR PAYROLL SYSTEM"); writer.newLine();
            writer.write("BẢNG THANH TOÁN TIỀN LƯƠNG & THUẾ TNCN TOÀN CÔNG TY - THÁNG " + monthYear); writer.newLine();
            writer.write("Ngày lập báo cáo: " + CurrencyFormatUtil.formatDate(now)); writer.newLine();
            writer.write("Tổng số nhân sự: " + sorted.size() + " người"); writer.newLine();
            writer.newLine();

            // Tiêu đề bảng chi tiết
            writer.write("STT,Mã NV,Họ và tên,Phòng ban,Loại hợp đồng,Lương cơ bản/Trợ cấp (VNĐ),Tổng lương Gross (VNĐ),Thuế TNCN (VNĐ),Thực lĩnh NET (VNĐ),Ký nhận"); writer.newLine();

            int stt = 1;
            for (Employee e : sorted) {
                double gross = e.calculateSalary();
                double tax = TaxCalculator.calculateTax(e);
                double net = TaxCalculator.calculateNetSalary(e);
                totalGross += gross;
                totalTax += tax;
                totalNet += net;

                writer.write(String.format(Locale.US, "%d,%s,\"%s\",\"%s\",\"%s\",%.0f,%.0f,%.0f,%.0f,",
                        stt++,
                        e.getId(),
                        escapeCsv(e.getFullName()),
                        escapeCsv(e.getDepartment()),
                        escapeCsv(e.getEmployeeType()),
                        e.getBaseSalary(),
                        gross,
                        tax,
                        net));
                writer.newLine();
            }

            // Dòng tổng cộng
            writer.write(String.format(Locale.US, "TỔNG CỘNG TOÀN CÔNG TY,,,,,,%.0f,%.0f,%.0f,",
                    totalGross, totalTax, totalNet));
            writer.newLine();
            writer.newLine();

            // Tổng hợp theo phòng ban
            writer.write("TỔNG HỢP CHI PHÍ LƯƠNG THEO PHÒNG BAN"); writer.newLine();
            writer.write("STT,Phòng ban,Số nhân sự,Tổng lương Gross (VNĐ),Thuế TNCN (VNĐ),Thực lĩnh NET (VNĐ),Tỷ trọng (%)"); writer.newLine();

            Map<String, List<Employee>> byDept = sorted.stream()
                    .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new, Collectors.toList()));

            int deptStt = 1;
            for (Map.Entry<String, List<Employee>> entry : byDept.entrySet()) {
                String dept = entry.getKey();
                List<Employee> list = entry.getValue();
                double deptGross = list.stream().mapToDouble(Employee::calculateSalary).sum();
                double deptTax = TaxCalculator.calculateTotalTax(list);
                double deptNet = list.stream().mapToDouble(TaxCalculator::calculateNetSalary).sum();
                double pct = totalGross > 0 ? (deptGross / totalGross * 100.0) : 0.0;

                writer.write(String.format(Locale.US, "%d,\"%s\",%d,%.0f,%.0f,%.0f,%.1f%%",
                        deptStt++,
                        escapeCsv(dept),
                        list.size(),
                        deptGross,
                        deptTax,
                        deptNet,
                        pct));
                writer.newLine();
            }

            writer.write(String.format(Locale.US, "TỔNG CỘNG,,%d,%.0f,%.0f,%.0f,100.0%%",
                    sorted.size(), totalGross, totalTax, totalNet));
            writer.newLine();
        }
    }
}
