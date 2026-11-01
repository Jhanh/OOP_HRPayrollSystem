package vn.edu.ptit.oop.nhom08.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.util.ChartHelper.DeptSummary;

// Cửa sổ hiển thị biểu đồ thống kê trực quan với tooltip và thanh trạng thái xem nhanh khi rê chuột
public class ChartWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_HINT =
            "💡 Rê chuột vào từng cột hoặc lát cắt biểu đồ để xem nhanh tổng lương Gross, thuế TNCN và thực lĩnh Net.";

    private JFreeChart barChart;
    private JFreeChart pieChart;
    private final Map<String, DeptSummary> currentSummaries = new HashMap<>();
    private JLabel lblStatus;

    public ChartWindow(List<Employee> initialEmployees) {
        setTitle("Thống Kê Biểu Đồ - Quản Lý Nhân Sự & Bảng Lương");
        setSize(1150, 620);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadWindowIcon();
        initUI(initialEmployees);
    }

    private void initUI(List<Employee> employees) {
        currentSummaries.clear();
        currentSummaries.putAll(ChartHelper.calculateDeptSummaries(employees));

        barChart = ChartHelper.createSalaryBarChart(employees, currentSummaries);
        pieChart = ChartHelper.createEmployeeStructurePieChart(employees);

        ChartPanel barChartPanel = new ChartPanel(barChart);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);

        // Lắng nghe sự kiện rê chuột trên biểu đồ cột: cập nhật nhanh thanh thông tin dưới đáy
        barChartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                // Không mở cửa sổ mới khi bấm để giao diện luôn liền mạch và tập trung góc nhìn tổng quát
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                if (event.getEntity() instanceof CategoryItemEntity) {
                    CategoryItemEntity entity = (CategoryItemEntity) event.getEntity();
                    String dept = entity.getColumnKey().toString();
                    DeptSummary s = currentSummaries.get(dept);
                    if (s != null) {
                        lblStatus.setText(String.format("📊 [%s] Nhân sự: %d người | Quỹ lương Gross: %s | Thuế TNCN: %s | Thực lĩnh (NET): %s",
                                s.getDepartment(), s.getCount(),
                                CurrencyFormatUtil.formatWithUnit(s.getGross()),
                                CurrencyFormatUtil.formatWithUnit(s.getTax()),
                                CurrencyFormatUtil.formatWithUnit(s.getNet())));
                        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        lblStatus.setForeground(new Color(0x007396));
                    }
                } else {
                    resetStatusHint();
                }
            }
        });

        // Lắng nghe sự kiện rê chuột trên biểu đồ tròn: hiển thị cơ cấu tỷ lệ nhân sự
        pieChartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                // Không mở cửa sổ mới khi bấm
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                if (event.getEntity() instanceof PieSectionEntity) {
                    PieSectionEntity entity = (PieSectionEntity) event.getEntity();
                    String sectionKey = String.valueOf(entity.getSectionKey());
                    Number val = entity.getDataset().getValue(entity.getSectionIndex());
                    lblStatus.setText(String.format("📊 Cơ cấu nhân sự: [%s] gồm %d người",
                            sectionKey, val != null ? val.intValue() : 0));
                    lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    lblStatus.setForeground(new Color(0x007396));
                } else {
                    resetStatusHint();
                }
            }
        });

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(barChartPanel);
        mainPanel.add(pieChartPanel);

        // Thanh ghi chú trạng thái xem nhanh dưới đáy cửa sổ
        lblStatus = new JLabel(DEFAULT_HINT, JLabel.CENTER);
        resetStatusHint();
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE2E8F0)),
                BorderFactory.createEmptyBorder(8, 12, 10, 12)
        ));

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }

    private void resetStatusHint() {
        if (lblStatus != null) {
            lblStatus.setText(DEFAULT_HINT);
            lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblStatus.setForeground(new Color(0x475569));
        }
    }

    // Cập nhật lại biểu đồ khi danh sách nhân viên thay đổi
    public void updateData(List<Employee> updatedEmployees) {
        currentSummaries.clear();
        currentSummaries.putAll(ChartHelper.calculateDeptSummaries(updatedEmployees));

        DefaultCategoryDataset newBarDataset = ChartHelper.createSalaryDataset(updatedEmployees);
        CategoryPlot categoryPlot = barChart.getCategoryPlot();
        categoryPlot.setDataset(newBarDataset);

        DefaultPieDataset<String> newPieDataset = ChartHelper.createEmployeeStructureDataset(updatedEmployees);
        @SuppressWarnings("unchecked")
        PiePlot<String> piePlot = (PiePlot<String>) pieChart.getPlot();
        piePlot.setDataset(newPieDataset);

        resetStatusHint();
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
