package vn.edu.ptit.oop.nhom08.util;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import vn.edu.ptit.oop.nhom08.model.Employee;

// Cửa sổ hiển thị biểu đồ thống kê trực quan
public class ChartWindow extends JFrame {

    private JFreeChart barChart;
    private JFreeChart pieChart;

    public ChartWindow(List<Employee> initialEmployees) {
        setTitle("Thống Kê Biểu Đồ - Quản Lý Nhân Sự & Bảng Lương");
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadWindowIcon();
        initUI(initialEmployees);
    }

    private void initUI(List<Employee> employees) {
        barChart = ChartHelper.createSalaryBarChart(employees);
        pieChart = ChartHelper.createEmployeeStructurePieChart(employees);

        ChartPanel barChartPanel = new ChartPanel(barChart);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(barChartPanel);
        mainPanel.add(pieChartPanel);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    // Cập nhật lại biểu đồ khi danh sách nhân viên thay đổi
    public void updateData(List<Employee> updatedEmployees) {
        DefaultCategoryDataset newBarDataset = ChartHelper.createSalaryDataset(updatedEmployees);
        CategoryPlot categoryPlot = barChart.getCategoryPlot();
        categoryPlot.setDataset(newBarDataset);

        DefaultPieDataset<String> newPieDataset = ChartHelper.createEmployeeStructureDataset(updatedEmployees);
        @SuppressWarnings("unchecked")
        PiePlot<String> piePlot = (PiePlot<String>) pieChart.getPlot();
        piePlot.setDataset(newPieDataset);

        repaint();
        revalidate();
    }

    private void loadWindowIcon() {
        ImageIcon icon = loadIcon("/resources/icons/chart.png");
        if (icon == null) {
            icon = loadIcon("/resources/icons/app.png");
        }
        if (icon != null) {
            setIconImage(icon.getImage());
        }
    }

    private static ImageIcon loadIcon(String path) {
        URL url = ChartWindow.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        File file = new File(path.startsWith("/") ? path.substring(1) : path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        return null;
    }
}
