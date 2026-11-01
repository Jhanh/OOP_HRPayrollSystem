package vn.edu.ptit.oop.nhom08;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import vn.edu.ptit.oop.nhom08.ui.MainWindow;

// Điểm khởi chạy chính của ứng dụng Quản lý Nhân sự & Bảng lương (HR Payroll System)
public class Main {

    public static void main(String[] args) {
        // Tắt nạp thư viện Native để tương thích hoàn toàn và loại bỏ cảnh báo trên Java 21+
        System.setProperty("flatlaf.useNativeLibrary", "false");

        // Cài đặt Look and Feel hiện đại FlatLaf
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        // Cấu hình Font Segoe UI cho JFreeChart để khắc phục triệt để lỗi hiển thị tiếng Việt
        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        theme.setExtraLargeFont(new Font("Segoe UI", Font.BOLD, 18)); // Tiêu đề biểu đồ
        theme.setLargeFont(new Font("Segoe UI", Font.BOLD, 14));      // Nhãn trục X, Y
        theme.setRegularFont(new Font("Segoe UI", Font.PLAIN, 12));   // Chú giải & số liệu
        ChartFactory.setChartTheme(theme);

        // Khởi tạo và hiển thị cửa sổ chính trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
