package vn.edu.ptit.oop.nhom08.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// Hộp thoại hiển thị thông tin giới thiệu đề tài và danh sách thành viên Nhóm 08
public class AboutDialog extends JDialog {

    public AboutDialog() {
        this(null);
    }

    public AboutDialog(Frame parent) {
        super(parent, "Thông tin ứng dụng - Nhóm 08", true);
        initUI();
    }

    private void initUI() {
        setSize(580, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setResizable(false);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Hỗ trợ phím ESC để đóng hộp thoại
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // Tiêu đề, logo và thông tin môn học
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        ImageIcon icon = loadIcon("/resources/icons/app.png");
        if (icon != null) {
            headerPanel.add(new JLabel(icon), BorderLayout.WEST);
        }

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ & BẢNG LƯƠNG");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel subLabel = new JLabel("Bài tập lớn Lập trình hướng đối tượng (INT1332) - PTIT");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(Color.GRAY);

        JLabel infoLabel = new JLabel("Chủ đề số 02 • Nhóm 08 • Java SE 17 • FlatLaf • JFreeChart");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(infoLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    // Bảng danh sách 10 thành viên và vai trò
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        String[] columns = {"STT", "MSSV", "Họ và tên", "Nhiệm vụ chính"};
        Object[][] data = {
            {"1", "N24DCAT040", "Nguyễn Quốc Khánh", "Trưởng nhóm, Git & Khung sườn chính"},
            {"2", "N24DCAT063", "Trần Vinh Quang", "Lớp cha Employee & Ngoại lệ hệ thống"},
            {"3", "N24DCAT059", "Nguyễn Vũ Hoàng Phúc", "Hợp đồng FullTime, PartTime & Intern"},
            {"4", "N24DCAT024", "Phan Trọng Hạnh", "Lớp Manager & Nghiệp vụ tính thuế TNCN"},
            {"5", "N24DCAT019", "Hoàng Bá Duy", "Tầng Repository & Dịch vụ PayrollService"},
            {"6", "N24DCAT035", "Ngô Tiến Huy", "Tiện ích Format & Bảng dữ liệu nhân sự"},
            {"7", "N24DCAT041", "Bùi Hải Khôi", "Bảng lương & Tùy biến PayrollCellRenderer"},
            {"8", "N24DCAT061", "Nguyễn Ngọc Quân", "Hộp thoại Form nhập liệu EmployeeFormDialog"},
            {"9", "N24DCAT039", "Nguyễn Nam Khánh", "Biểu đồ thống kê JFreeChart & ChartWindow"},
            {"10", "N24DCAT036", "Trần Minh Huy", "Tính năng BONUS: Xuất phiếu lương & Sắp xếp"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(1).setPreferredWidth(95);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(240);

        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return centerPanel;
    }

    // Nút đóng hộp thoại
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(closeButton);
        bottomPanel.add(closeButton);
        return bottomPanel;
    }

    // Nạp icon an toàn từ resources hoặc đường dẫn tương đối
    private static ImageIcon loadIcon(String path) {
        URL url = AboutDialog.class.getResource(path);
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
