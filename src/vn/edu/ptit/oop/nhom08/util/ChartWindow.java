package vn.edu.ptit.oop.nhom02.util;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import vn.edu.ptit.oop.nhom02.model.Employee;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartWindow extends JFrame {

    private JFreeChart barChart;
    private JFreeChart pieChart;

    public ChartWindow(List<Employee> initialEmployees) {
        setTitle("Thống Kê Biểu Đồ - Quản Lý Nhân Sự");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI(initialEmployees);
    }

    private void initUI(List<Employee> employees) {
        
        barChart = ChartHelper.createSalaryBarChart(employees);
        pieChart = ChartHelper.createEmployeeStructurePieChart(employees);
        ChartPanel barChartPanel = new ChartPanel(barChart);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        setLayout(new GridLayout(1, 2, 10, 10));
        add(barChartPanel);
        add(pieChartPanel);
    }

    
    public void updateData(List<Employee> updatedEmployees) {
        
        DefaultCategoryDataset newBarDataset = ChartHelper.createSalaryDataset(updatedEmployees);
        barChart.getCategoryPlot().setDataset(newBarDataset);

       
        DefaultPieDataset<String> newPieDataset = ChartHelper.createEmployeeStructureDataset(updatedEmployees);
        ((org.jfree.chart.plot.PiePlot<String>) pieChart.getPlot()).setDataset(newPieDataset);

       
        repaint();
        revalidate();
    }
}
