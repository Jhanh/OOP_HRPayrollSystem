package vn.edu.ptit.oop.nhom08.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

// Lớp cửa sổ trừu tượng cơ sở bắt buộc (lớp cha của MainWindow)
public abstract class BaseWindow extends JFrame {
    protected JMenuBar menuBar;
    protected JMenu fileMenu, viewMenu, helpMenu;
    protected JToolBar toolBar;
    protected JPanel statusBar;
    protected JLabel statusLabel;

    public BaseWindow(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadIcon();       // Nạp icon tiêu đề
        initMenuBar();    // Khởi tạo menu (File, View, Help)
        initToolBar();    // Khởi tạo thanh công cụ
        initStatusBar();  // Khởi tạo thanh trạng thái
        initUI();         // Phương thức trừu tượng: lớp con tự cài đặt
    }

    // Lớp con xây dựng nội dung chính và thêm vào BorderLayout.CENTER
    protected abstract void initUI();

    // Hook: lớp con override để hiển thị AboutDialog riêng của nhóm
    protected void showAbout() {
        JOptionPane.showMessageDialog(this, getTitle(), "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // Cập nhật thông điệp trên thanh trạng thái
    protected void setStatus(String msg) {
        statusLabel.setText(" " + msg);
    }

    // Nạp icon an toàn từ resources hoặc đường dẫn file
    protected static ImageIcon loadImageIcon(String path) {
        URL url = BaseWindow.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        // Fallback nạp trực tiếp nếu chạy ngoài môi trường IDE
        File file = new File(path.startsWith("/") ? path.substring(1) : path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        return null;
    }

    // Tiện ích thêm nút có icon vào thanh công cụ
    protected JButton addToolButton(String iconPath, String tooltip, ActionListener action) {
        ImageIcon icon = loadImageIcon(iconPath);
        JButton btn = (icon != null) ? new JButton(icon) : new JButton(tooltip);
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);
        btn.addActionListener(action);
        toolBar.add(btn);
        return btn;
    }

    private void loadIcon() {
        ImageIcon icon = loadImageIcon("/resources/icons/app.png");
        if (icon != null) {
            setIconImage(icon.getImage());
        }
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(exitItem);

        JMenuItem aboutItem = new JMenuItem("About...");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void initToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);
    }

    private void initStatusBar() {
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusLabel = new JLabel(" Sẵn sàng");
        statusBar.add(statusLabel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }
}
