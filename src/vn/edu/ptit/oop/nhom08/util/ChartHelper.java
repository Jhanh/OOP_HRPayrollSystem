package vn.edu.ptit.oop.nhom02.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import vn.edu.ptit.oop.nhom02.model.Employee;
import vn.edu.ptit.oop.nhom02.model.FullTimeEmployee;
import vn.edu.ptit.oop.nhom02.model.Intern;
import vn.edu.ptit.oop.nhom02.model.Manager;
import vn.edu.ptit.oop.nhom02.model.PartTimeEmployee;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartHelper {

    public static JFreeChart createSalaryBarChart(List<Employee> employees) {
        DefaultCategoryDataset dataset = createSalaryDataset(employees);
        return ChartFactory.createBarChart(
                "Quỹ Lương Theo Phòng Ban", 
                "Phòng Ban",              
                "Lương (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }


    public static JFreeChart createEmployeeStructurePieChart(List<Employee> employees) {
        DefaultPieDataset<String> dataset = createEmployeeStructureDataset(employees);
        return ChartFactory.createPieChart(
                "Cơ Cấu Loại Nhân Viên", 
                dataset,
                true, true, false
        );
    }


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

    public static DefaultPieDataset<String> createEmployeeStructureDataset(List<Employee> employees) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        if (employees == null || employees.isEmpty()) {
            return dataset;
        }

        long managerCount = 0;
        long fullTimeCount = 0;
        long partTimeCount = 0;
        long internCount = 0;

        for (Employee emp : employees) {
            if (emp instanceof Manager) {
                managerCount++;
            } else if (emp instanceof FullTimeEmployee) {
                fullTimeCount++;
            } else if (emp instanceof PartTimeEmployee) {
                partTimeCount++;
            } else if (emp instanceof Intern) {
                internCount++;
            }
        }

        if (managerCount > 0) dataset.setValue("Manager", managerCount);
        if (fullTimeCount > 0) dataset.setValue("Full-Time", fullTimeCount);
        if (partTimeCount > 0) dataset.setValue("Part-Time", partTimeCount);
        if (internCount > 0) dataset.setValue("Intern", internCount);

        return dataset;
    }
}
