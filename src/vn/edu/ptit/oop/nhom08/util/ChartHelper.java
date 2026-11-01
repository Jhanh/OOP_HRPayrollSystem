package vn.edu.ptit.oop.nhom08.util;

import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.TaxCalculator;

// Tiện ích khởi tạo biểu đồ thống kê JFreeChart
public final class ChartHelper {

    // DTO tóm tắt quỹ lương và thuế theo phòng ban
    public static final class DeptSummary {
        private final String department;
        private final int count;
        private final double gross;
        private final double tax;
        private final double net;

        public DeptSummary(String department, int count, double gross, double tax, double net) {
            this.department = department;
            this.count = count;
            this.gross = gross;
            this.tax = tax;
            this.net = net;
        }

        public String getDepartment() { return department; }
        public int getCount() { return count; }
        public double getGross() { return gross; }
        public double getTax() { return tax; }
        public double getNet() { return net; }
    }

    private ChartHelper() {
    }

    // Tính toán tổng hợp số liệu lương và thuế theo từng phòng ban
    public static Map<String, DeptSummary> calculateDeptSummaries(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, List<Employee>> byDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        Map<String, DeptSummary> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Employee>> entry : byDept.entrySet()) {
            String dept = entry.getKey();
            List<Employee> list = entry.getValue();
            int count = list.size();
            double gross = list.stream().mapToDouble(Employee::calculateSalary).sum();
            double tax = list.stream().mapToDouble(TaxCalculator::calculateTax).sum();
            double net = gross - tax;
            result.put(dept, new DeptSummary(dept, count, gross, tax, net));
        }
        return result;
    }

    // Tạo biểu đồ cột phân bổ quỹ lương theo từng phòng ban kèm hỗ trợ ghi chú chi tiết
    public static JFreeChart createSalaryBarChart(List<Employee> employees, Map<String, DeptSummary> summariesRef) {
        DefaultCategoryDataset dataset = createSalaryDataset(employees);
        JFreeChart chart = ChartFactory.createBarChart(
                "Quỹ Lương Theo Phòng Ban",
                "Phòng Ban",
                "Tổng lương",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        // 1. Trục hoành: Xoay nhãn phòng ban 45 độ để hiển thị đầy đủ, không bị cắt thành "..." khi có nhiều cột
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setMaximumCategoryLabelLines(2);
        domainAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));

        // 2. Trục tung: Định dạng tiền tệ dễ đọc (ví dụ 10.000.000 thay vì 1E7)
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        DecimalFormat vnFormat = new DecimalFormat("#,##0");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("vi-VN"));
        symbols.setGroupingSeparator('.');
        vnFormat.setDecimalFormatSymbols(symbols);
        rangeAxis.setNumberFormatOverride(vnFormat);

        // 3. Tooltip hiển thị đầy đủ thông tin lương Gross, Thuế TNCN và Thực lĩnh Net khi rê chuột vào cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultToolTipGenerator((catDataset, row, column) -> {
            Comparable<?> colKey = catDataset.getColumnKey(column);
            if (colKey == null) return null;
            String dept = colKey.toString();
            DeptSummary s = (summariesRef != null) ? summariesRef.get(dept) : null;
            if (s == null) return dept;
            return String.format(
                "<html><div style='padding:3px;font-family:Segoe UI;'>"
                + "<b>Phòng ban:</b> %s (%d nhân sự)<br>"
                + "<b>Lương Gross:</b> %s<br>"
                + "<b>Thuế TNCN:</b> %s<br>"
                + "<b>Thực lĩnh (Net):</b> %s"
                + "</div></html>",
                s.getDepartment(), s.getCount(),
                CurrencyFormatUtil.formatWithUnit(s.getGross()),
                CurrencyFormatUtil.formatWithUnit(s.getTax()),
                CurrencyFormatUtil.formatWithUnit(s.getNet())
            );
        });

        return chart;
    }

    // Nạp chồng phương thức mặc định
    public static JFreeChart createSalaryBarChart(List<Employee> employees) {
        return createSalaryBarChart(employees, calculateDeptSummaries(employees));
    }

    // Tạo biểu đồ tròn cơ cấu tỷ lệ nhân sự theo loại hợp đồng
    public static JFreeChart createEmployeeStructurePieChart(List<Employee> employees) {
        DefaultPieDataset<String> dataset = createEmployeeStructureDataset(employees);
        JFreeChart chart = ChartFactory.createPieChart(
                "Cơ Cấu Nhân Sự Theo Loại Hình",
                dataset,
                true, true, false
        );

        // Hiển thị nhãn chi tiết gồm tên, số lượng và tỷ lệ phần trăm
        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.0%")
        ));
        return chart;
    }

    // Tạo dataset quỹ lương từ danh sách nhân viên
    public static DefaultCategoryDataset createSalaryDataset(List<Employee> employees) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (employees == null || employees.isEmpty()) {
            return dataset;
        }

        Map<String, Double> salaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::calculateSalary)
                ));

        salaryByDept.forEach((dept, totalSalary) -> {
            dataset.addValue(totalSalary, "Quỹ lương", dept);
        });

        return dataset;
    }

    // Tạo dataset cơ cấu nhân sự đa hình qua getEmployeeType()
    public static DefaultPieDataset<String> createEmployeeStructureDataset(List<Employee> employees) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        if (employees == null || employees.isEmpty()) {
            return dataset;
        }

        Map<String, Long> countByType = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getEmployeeType,
                        Collectors.counting()
                ));

        countByType.forEach(dataset::setValue);
        return dataset;
    }
}
