package vn.edu.ptit.oop.nhom08.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

// Hộp thoại hiển thị thông tin ứng dụng tinh gọn, trực quan
public class AboutDialog extends JDialog {

    public AboutDialog() {
        this(null);
    }

    public AboutDialog(Frame parent) {
        super(parent, "Thông tin ứng dụng", true);
        initUI();
    }

    private void initUI() {
        setSize(480, 310);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setResizable(false);

        loadWindowIcon();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 15, 24));

        // 1. Phần tiêu đề chính giữa
        JLabel lblTitle = new JLabel("HR Payroll System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Hệ thống Quản lý Nhân sự & Bảng lương", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSubtitle.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(4));
        contentPanel.add(lblSubtitle);
        contentPanel.add(Box.createVerticalStrut(14));

        // 2. Đường kẻ phân cách mỏng
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(14));

        // 3. Bảng thông tin dạng Key - Value
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setAlignmentX(CENTER_ALIGNMENT);

        addInfoRow(infoPanel, 0, "Môn học:", "Lập trình hướng đối tượng (INT1332)");
        addInfoRow(infoPanel, 1, "Đề tài:", "Đề tài số 02 - Quản lý Nhân sự & Bảng lương");
        addInfoRow(infoPanel, 2, "Thực hiện:", "Nhóm 08 - PTIT");
        addInfoRow(infoPanel, 3, "Công nghệ:", "Java Swing, JFreeChart 1.5, FlatLaf");
        addInfoRow(infoPanel, 4, "Phiên bản:", "1.0.0 (2026)");

        contentPanel.add(infoPanel);

        // 4. Nút Đóng
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(85, 28));
        btnClose.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnClose);
        bottomPanel.add(btnClose);

        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Hỗ trợ bấm phím ESC để đóng hộp thoại
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void addInfoRow(JPanel panel, int row, String label, String value) {
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0;
        lc.gridy = row;
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(3, 10, 3, 15);

        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblKey, lc);

        GridBagConstraints vc = new GridBagConstraints();
        vc.gridx = 1;
        vc.gridy = row;
        vc.anchor = GridBagConstraints.WEST;
        vc.weightx = 1.0;
        vc.insets = new Insets(3, 0, 3, 10);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblVal, vc);
    }

    private void loadWindowIcon() {
        ImageIcon icon = loadIcon("/resources/icons/app.png");
        if (icon != null) {
            setIconImage(icon.getImage());
        }
    }

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
