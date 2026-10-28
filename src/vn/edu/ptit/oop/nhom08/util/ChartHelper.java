package vn.edu.ptit.oop.nhom08.util;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import vn.edu.ptit.oop.nhom08.model.Employee;

// Tiện ích khởi tạo biểu đồ thống kê JFreeChart
public final class ChartHelper {

    private ChartHelper() {
    }

    // Tạo biểu đồ cột phân bổ quỹ lương theo từng phòng ban
    public static JFreeChart createSalaryBarChart(List<Employee> employees) {
        DefaultCategoryDataset dataset = createSalaryDataset(employees);
        return ChartFactory.createBarChart(
                "Quỹ Lương Theo Phòng Ban",
                "Phòng Ban",
                "Tổng lương (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
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
