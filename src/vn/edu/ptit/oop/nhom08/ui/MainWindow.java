package vn.edu.ptit.oop.nhom08.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import vn.edu.ptit.oop.nhom08.common.BaseWindow;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.repository.EmployeeRepository;
import vn.edu.ptit.oop.nhom08.repository.InMemoryEmployeeRepository;
import vn.edu.ptit.oop.nhom08.service.PayrollService;
import vn.edu.ptit.oop.nhom08.ui.dialogs.AboutDialog;
import vn.edu.ptit.oop.nhom08.ui.dialogs.EmployeeFormDialog;
import vn.edu.ptit.oop.nhom08.ui.panels.EmployeeTableModel;
import vn.edu.ptit.oop.nhom08.ui.panels.PayrollCellRenderer;
import vn.edu.ptit.oop.nhom08.ui.panels.PayrollTableModel;
import vn.edu.ptit.oop.nhom08.util.ChartWindow;
import vn.edu.ptit.oop.nhom08.util.EmployeeComparator;
import vn.edu.ptit.oop.nhom08.util.PayslipExporter;

// Cửa sổ giao diện chính của hệ thống kế thừa BaseWindow (Layered Architecture)
public class MainWindow extends BaseWindow {

    private static final String ALL_DEPTS = "Tất cả phòng ban";

    // Không khởi tạo tại nơi khai báo để tránh bẫy BaseWindow (trọng tâm phản biện)
    private EmployeeRepository repository;
    private PayrollService payrollService;

    private JTabbedPane tabbedPane;
    private JTable employeeTable;
    private EmployeeTableModel employeeTableModel;
    private JTable payrollTable;
    private PayrollTableModel payrollTableModel;

    private JComboBox<String> cboDepartmentFilter;
    private JTextField txtSearch;
    private JComboBox<String> cboSortCriteria;
    private JButton btnSortOrder;
    private boolean isSortAscending = true;
    private ChartWindow chartWindow;

    public MainWindow() {
        super("Hệ Thống Quản Lý Nhân Sự & Bảng Lương - HR Payroll System Enterprise", 1250, 720);
    }

    @Override
    protected void initUI() {
        // Cài đặt đóng cửa sổ an toàn để bắt sự kiện thoát và xác nhận lưu thay đổi
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleAppExit();
            }
        });

        // Khởi tạo tầng dữ liệu và nghiệp vụ bên trong initUI()
        repository = new InMemoryEmployeeRepository();
        payrollService = new PayrollService(repository);

        setupMenus();
        setupToolBarButtons();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel employeeTab = createEmployeeTab();
        JPanel payrollTab = createPayrollTab();

        tabbedPane.addTab("Hồ Sơ Nhân Sự", employeeTab);
        tabbedPane.addTab("Bảng Tính Lương & Thuế TNCN", payrollTab);

        tabbedPane.addChangeListener(e -> refreshData());

        add(tabbedPane, BorderLayout.CENTER);
        setupShortcuts();
        refreshData();
    }

    private void setupMenus() {
        fileMenu.removeAll();

        // Menu File chuyên trách các thao tác lưu trữ, tệp tin và sao lưu
        JMenuItem saveItem = new JMenuItem("Lưu (Save)");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveDataToFile());
        fileMenu.add(saveItem);

        JMenuItem reloadItem = new JMenuItem("Tải lại (Reload)");
        reloadItem.addActionListener(e -> reloadDataFromFile());
        fileMenu.add(reloadItem);

        fileMenu.addSeparator();

        JMenuItem backupItem = new JMenuItem("Sao lưu (Backup)...");
        backupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        backupItem.addActionListener(e -> openBackupDialog());
        fileMenu.add(backupItem);

        JMenuItem restoreItem = new JMenuItem("Khôi phục (Restore)...");
        restoreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        restoreItem.addActionListener(e -> openRestoreDialog());
        fileMenu.add(restoreItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Thoát (Exit)");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> handleAppExit());
        fileMenu.add(exitItem);

        JMenuItem chartItem = new JMenuItem("Biểu đồ thống kê...");
        chartItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        chartItem.addActionListener(e -> openChartWindow());
        viewMenu.add(chartItem);

        JMenuItem refreshItem = new JMenuItem("Làm mới");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        refreshItem.addActionListener(e -> refreshData());
        viewMenu.add(refreshItem);
    }

    private void setupToolBarButtons() {
        addToolButton("/resources/icons/add.png", "Thêm nhân viên", e -> openAddDialog());
        addToolButton("/resources/icons/edit.png", "Sửa nhân viên", e -> openEditDialog());
        addToolButton("/resources/icons/delete.png", "Xóa nhân viên", e -> deleteSelectedEmployee());
        toolBar.addSeparator();
        addToolButton("/resources/icons/chart.png", "Biểu đồ thống kê", e -> openChartWindow());
        addToolButton("/resources/icons/export.png", "Xuất phiếu lương", e -> openExportPayslipDialog());
    }

    private void setupShortcuts() {
        // Phím tắt Thêm nhân viên: Ctrl + N
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "shortcutAddEmployee"
        );
        getRootPane().getActionMap().put("shortcutAddEmployee", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddDialog();
            }
        });

        // Phím tắt Chỉnh sửa nhân viên: Ctrl + E
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "shortcutEditEmployee"
        );
        getRootPane().getActionMap().put("shortcutEditEmployee", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditDialog();
            }
        });

        // Phím tắt Xóa nhân viên: Ctrl + D (toàn cửa sổ) hoặc phím Delete (khi đang chọn trên bảng)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), "shortcutDeleteEmployee"
        );
        getRootPane().getActionMap().put("shortcutDeleteEmployee", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedEmployee();
            }
        });

        if (employeeTable != null) {
            employeeTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "shortcutTableDeleteEmployee"
            );
            employeeTable.getActionMap().put("shortcutTableDeleteEmployee", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteSelectedEmployee();
                }
            });
        }
    }

    private JPanel createEmployeeTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thanh công cụ lọc, tìm kiếm và sắp xếp
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc và tìm kiếm nhân sự"));

        cboDepartmentFilter = new JComboBox<>();
        cboDepartmentFilter.setPreferredSize(new Dimension(160, 28));
        cboDepartmentFilter.addActionListener(e -> applyFilterAndSort());

        JButton btnAddDept = new JButton("+ PB");
        btnAddDept.setPreferredSize(new Dimension(65, 28));
        btnAddDept.setToolTipText("Bổ sung phòng ban mới vào hệ thống");
        btnAddDept.addActionListener(e -> onAddNewDepartment());

        txtSearch = new JTextField(12);
        txtSearch.setPreferredSize(new Dimension(140, 28));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilterAndSort(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilterAndSort(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilterAndSort(); }
        });

        cboSortCriteria = new JComboBox<>(new String[] {
            "Mã nhân viên",
            "Họ và tên",
            "Mức lương",
            "Thâm niên",
            "Phòng ban",
            "Loại nhân viên"
        });
        cboSortCriteria.setPreferredSize(new Dimension(145, 28));
        cboSortCriteria.addActionListener(e -> applyFilterAndSort());

        btnSortOrder = new JButton("▲ Tăng dần");
        btnSortOrder.setPreferredSize(new Dimension(105, 28));
        btnSortOrder.setToolTipText("Nhấp để đảo ngược thứ tự sắp xếp (Tăng dần / Giảm dần)");
        btnSortOrder.addActionListener(e -> {
            isSortAscending = !isSortAscending;
            btnSortOrder.setText(isSortAscending ? "▲ Tăng dần" : "▼ Giảm dần");
            applyFilterAndSort();
        });

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setPreferredSize(new Dimension(85, 28));
        btnRefresh.setToolTipText("Làm mới dữ liệu và đặt lại bộ lọc (F5)");
        btnRefresh.addActionListener(e -> {
            cboDepartmentFilter.setSelectedIndex(0);
            txtSearch.setText("");
            cboSortCriteria.setSelectedIndex(0);
            isSortAscending = true;
            btnSortOrder.setText("▲ Tăng dần");
            refreshData();
        });

        filterPanel.add(new JLabel("Phòng ban:"));
        filterPanel.add(cboDepartmentFilter);
        filterPanel.add(btnAddDept);
        filterPanel.add(new JLabel("Tìm theo tên:"));
        filterPanel.add(txtSearch);
        filterPanel.add(new JLabel("Sắp xếp:"));
        filterPanel.add(cboSortCriteria);
        filterPanel.add(btnSortOrder);
        filterPanel.add(btnRefresh);

        // Bảng danh sách nhân sự
        employeeTableModel = new EmployeeTableModel();
        employeeTable = new JTable(employeeTableModel);
        employeeTable.setRowHeight(25);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setShowGrid(true);
        employeeTable.setFillsViewportHeight(true);
        configureEmployeeTable();

        // Nhấp đúp chuột để mở hộp thoại Sửa
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openEditDialog();
                }
            }
        });

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPayrollTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thanh tiêu đề bảng lương
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        LocalDate now = LocalDate.now();
        JLabel lblTitle = new JLabel(String.format("Bảng kê thu nhập & Thuế TNCN lũy tiến - Kỳ tính lương Tháng %02d/%d",
                now.getMonthValue(), now.getYear()));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnExport = new JButton("Xuất phiếu lương");
        btnExport.addActionListener(e -> openExportPayslipDialog());

        JButton btnExportCompany = new JButton("Xuất bảng lương toàn cty");
        btnExportCompany.addActionListener(e -> openExportCompanyPayrollDialog());

        JButton btnViewChart = new JButton("Xem biểu đồ quỹ lương");
        btnViewChart.addActionListener(e -> openChartWindow());

        topBar.add(lblTitle);
        topBar.add(btnExport);
        topBar.add(btnExportCompany);
        topBar.add(btnViewChart);

        // Bảng tính lương
        payrollTableModel = new PayrollTableModel();
        payrollTable = new JTable(payrollTableModel);
        payrollTable.setRowHeight(25);
        payrollTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        payrollTable.setShowGrid(true);
        payrollTable.setFillsViewportHeight(true);

        // Áp dụng PayrollCellRenderer cho toàn bộ các cột
        PayrollCellRenderer renderer = new PayrollCellRenderer();
        for (int i = 0; i < payrollTable.getColumnCount(); i++) {
            payrollTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        configurePayrollTable();

        // Nhấp đúp để xuất phiếu lương cá nhân
        payrollTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openExportPayslipDialog();
                }
            }
        });

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(payrollTable), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // CẤU HÌNH ĐỘ RỘNG CỘT VÀ CĂN LỀ DỮ LIỆU BẢNG (DỄ DÀNG TÙY BIẾN KÍCH THƯỚC)
    // =========================================================================

    /**
     * Cấu hình độ rộng cột và căn lề cho Bảng Hồ sơ Nhân sự (Tab 1).
     * Bạn có thể điều chỉnh mảng columnWidths để thay đổi độ rộng từng cột theo ý muốn.
     */
    private void configureEmployeeTable() {
        // Thứ tự các cột: 0: STT | 1: Mã NV | 2: Họ và tên | 3: Phòng ban | 4: Loại NV | 5: Lương cơ bản | 6: Ngày vào làm | 7: Thâm niên
        int[] columnWidths = {60, 90, 200, 130, 150, 150, 135, 105};
        for (int i = 0; i < columnWidths.length && i < employeeTable.getColumnCount(); i++) {
            TableColumn col = employeeTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(columnWidths[i]);
            if (i == 0) { // Cột STT: giữ gọn gàng, không bị giãn nở quá mức
                col.setMinWidth(40);
                col.setMaxWidth(65);
            }
        }

        // Tạo Renderer căn lề với hiệu ứng xen kẽ dòng (Zebra stripes)
        class CustomAlignRenderer extends DefaultTableCellRenderer {
            private final int alignment;
            public CustomAlignRenderer(int alignment) {
                this.alignment = alignment;
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(alignment);
                if (!isSelected) {
                    c.setBackground((row % 2 == 0) ? Color.WHITE : new Color(248, 250, 252));
                }
                return c;
            }
        }

        CustomAlignRenderer centerRenderer = new CustomAlignRenderer(SwingConstants.CENTER);
        CustomAlignRenderer leftRenderer = new CustomAlignRenderer(SwingConstants.LEFT);

        // Gán căn lề:
        employeeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT: Căn giữa
        employeeTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Mã NV: Căn giữa
        employeeTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);   // Họ và tên: Căn trái
        employeeTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);   // Phòng ban: Căn trái
        employeeTable.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);   // Loại nhân viên: Căn trái
        employeeTable.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);   // Lương cơ bản: Căn trái
        employeeTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Ngày vào làm: Căn giữa
        employeeTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Thâm niên: Căn giữa
    }

    /**
     * Cấu hình độ rộng cột cho Bảng Tính lương & Thuế TNCN (Tab 2).
     * Bạn có thể điều chỉnh mảng columnWidths để thay đổi độ rộng từng cột theo ý muốn.
     */
    private void configurePayrollTable() {
        // Thứ tự các cột: 0: STT | 1: Mã NV | 2: Họ và tên | 3: Phòng ban | 4: Loại NV | 5: Tổng lương | 6: Thuế TNCN | 7: Thực lĩnh
        int[] columnWidths = {60, 90, 200, 130, 150, 150, 135, 105};
        for (int i = 0; i < columnWidths.length && i < payrollTable.getColumnCount(); i++) {
            TableColumn col = payrollTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(columnWidths[i]);
            if (i == 0) { // Cột STT: giữ gọn gàng
                col.setMinWidth(40);
                col.setMaxWidth(65);
            }
        }
    }

    // Mở hộp thoại Thêm nhân viên mới
    private void openAddDialog() {
        EmployeeFormDialog dialog = new EmployeeFormDialog(this, null, repository::existsById,
                repository.getAllDepartments(), repository::addDepartment);
        dialog.setVisible(true);

        if (dialog.isConfirmed() && dialog.getResult() != null) {
            Employee newEmp = dialog.getResult();
            repository.save(newEmp);
            refreshData();
            updateChartIfOpen();
            setStatus("Đã thêm: " + newEmp.getFullName() + " [" + newEmp.getId() + "]");
        }
    }

    // Mở hộp thoại Sửa nhân viên đã chọn
    private void openEditDialog() {
        Employee selected = getSelectedEmployee();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EmployeeFormDialog dialog = new EmployeeFormDialog(this, selected, null,
                repository.getAllDepartments(), repository::addDepartment);
        dialog.setVisible(true);

        if (dialog.isConfirmed() && dialog.getResult() != null) {
            Employee updated = dialog.getResult();
            repository.save(updated);
            refreshData();
            updateChartIfOpen();
            setStatus("Đã cập nhật: " + updated.getFullName() + " [" + updated.getId() + "]");
        }
    }

    // Xóa nhân viên đã chọn
    private void deleteSelectedEmployee() {
        Employee selected = getSelectedEmployee();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                String.format("Bạn có chắc muốn xóa nhân viên [%s] %s?",
                        selected.getId(), selected.getFullName()),
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            repository.deleteById(selected.getId());
            refreshData();
            updateChartIfOpen();
            setStatus("Đã xóa: " + selected.getFullName());
        }
    }

    // Mở cửa sổ biểu đồ phân tích JFreeChart
    private void openChartWindow() {
        List<Employee> currentEmployees = repository.findAll();
        if (chartWindow == null || !chartWindow.isDisplayable()) {
            chartWindow = new ChartWindow(currentEmployees);
            chartWindow.setVisible(true);
        } else {
            chartWindow.updateData(currentEmployees);
            chartWindow.toFront();
        }
    }

    private void updateChartIfOpen() {
        if (chartWindow != null && chartWindow.isDisplayable()) {
            chartWindow.updateData(repository.findAll());
        }
    }

    // Lấy thư mục mặc định an toàn cho JFileChooser (ưu tiên data/, fallback về thư mục dự án)
    private File getDefaultChooserDirectory() {
        File dataDir = new File("data");
        if (dataDir.exists() && dataDir.isDirectory()) {
            return dataDir;
        }
        return new File(".");
    }

    // Xuất phiếu lương cá nhân (BONUS)
    private void openExportPayslipDialog() {
        Employee selected = getSelectedEmployee();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xuất phiếu lương!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser(getDefaultChooserDirectory());
        chooser.setDialogTitle("Xuất phiếu lương - " + selected.getFullName());
        chooser.setSelectedFile(new File("payslip_" + selected.getId() + ".html"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Trang web HTML (*.html)", "html", "htm"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Tệp văn bản (*.txt)", "txt"));

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                PayslipExporter.export(selected, file.getAbsolutePath());
                setStatus("Đã xuất phiếu lương: " + file.getName());

                int openNow = JOptionPane.showConfirmDialog(this,
                        "Xuất phiếu lương thành công. Mở tệp ngay?",
                        "Xuất phiếu lương",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);

                if (openNow == JOptionPane.YES_OPTION && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu tệp: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Xuất bảng lương toàn công ty ra tệp HTML hoặc CSV
    private void openExportCompanyPayrollDialog() {
        List<Employee> allEmployees = repository.findAll();
        if (allEmployees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu nhân viên để xuất bảng lương!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser(getDefaultChooserDirectory());
        chooser.setDialogTitle("Xuất bảng lương toàn công ty");
        LocalDate now = LocalDate.now();
        String defaultName = String.format("bang_luong_toan_cong_ty_%02d_%d.html", now.getMonthValue(), now.getYear());
        chooser.setSelectedFile(new File(defaultName));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Báo cáo HTML (*.html)", "html", "htm"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Bảng tính Excel CSV (*.csv)", "csv"));

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String pathStr = file.getAbsolutePath();
            if (!pathStr.toLowerCase().endsWith(".html") && !pathStr.toLowerCase().endsWith(".htm")
                    && !pathStr.toLowerCase().endsWith(".csv")) {
                if (chooser.getFileFilter() != null && chooser.getFileFilter().getDescription().contains("CSV")) {
                    pathStr += ".csv";
                } else {
                    pathStr += ".html";
                }
                file = new File(pathStr);
            }

            try {
                PayslipExporter.exportCompanyPayroll(allEmployees, file.getAbsolutePath());
                setStatus("Đã xuất bảng lương: " + file.getName());

                int openNow = JOptionPane.showConfirmDialog(this,
                        "Đã xuất bảng lương toàn công ty thành công!\nBạn có muốn mở tệp ngay không?",
                        "Xuất bảng lương",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);

                if (openNow == JOptionPane.YES_OPTION && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất bảng lương: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Lưu các thay đổi hiện tại từ bộ nhớ RAM vào tệp dữ liệu CSV chính (data/employees.csv)
    private void saveDataToFile() {
        try {
            repository.saveChanges();
            refreshData();
            JOptionPane.showMessageDialog(this, "Đã lưu dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Nạp lại dữ liệu từ tệp CSV chính, hủy bỏ mọi thay đổi nháp chưa lưu trong RAM
    private void reloadDataFromFile() {
        if (repository.hasUnsavedChanges()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Dữ liệu chưa lưu sẽ bị hủy. Bạn có muốn tải lại?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        boolean ok = repository.reloadFromStorage();
        if (ok) {
            refreshData();
            updateChartIfOpen();
            JOptionPane.showMessageDialog(this, "Đã tải lại dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tệp dữ liệu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Tạo bản sao lưu dự phòng độc lập ra một tệp CSV riêng (Backup to file)
    private void openBackupDialog() {
        JFileChooser chooser = new JFileChooser(getDefaultChooserDirectory());
        chooser.setDialogTitle("Sao lưu dữ liệu");
        String defaultName = "backup_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        chooser.setSelectedFile(new File(defaultName));
        chooser.setFileFilter(new FileNameExtensionFilter("Tệp dữ liệu CSV (*.csv)", "csv"));

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            boolean success = repository.backupToFile(file);
            if (success) {
                setStatus("Đã sao lưu: " + file.getName());
                JOptionPane.showMessageDialog(this,
                        "Sao lưu dữ liệu thành công:\n" + file.getName(),
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể ghi bản sao lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Khôi phục dữ liệu từ một tệp sao lưu bất kỳ (Restore from file)
    private void openRestoreDialog() {
        if (repository.hasUnsavedChanges()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Dữ liệu chưa lưu sẽ bị ghi đè. Bạn có muốn tiếp tục?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        JFileChooser chooser = new JFileChooser(getDefaultChooserDirectory());
        chooser.setDialogTitle("Khôi phục dữ liệu từ tệp sao lưu");
        chooser.setFileFilter(new FileNameExtensionFilter("Tệp dữ liệu CSV (*.csv)", "csv"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            boolean success = repository.restoreFromFile(file);
            if (success) {
                refreshData();
                updateChartIfOpen();
                setStatus("Đã khôi phục: " + file.getName());

                int saveMain = JOptionPane.showConfirmDialog(this,
                        "Đã khôi phục " + repository.count() + " nhân sự từ tệp [" + file.getName() + "].\n"
                                + "Bạn có muốn lưu đè vào dữ liệu chính không?",
                        "Đồng bộ dữ liệu",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (saveMain == JOptionPane.YES_OPTION) {
                    repository.saveChanges();
                    refreshData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tệp sao lưu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Xử lý đóng ứng dụng an toàn và kiểm tra dữ liệu chưa lưu
    private void handleAppExit() {
        if (repository.hasUnsavedChanges()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Dữ liệu chưa được lưu. Bạn có muốn lưu trước khi thoát?",
                    "Xác nhận thoát",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                repository.saveChanges();
                dispose();
                System.exit(0);
            } else if (choice == JOptionPane.NO_OPTION) {
                dispose();
                System.exit(0);
            }
            // CANCEL hoặc đóng dialog: tiếp tục làm việc, không thoát
        } else {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn thoát chương trình?",
                    "Thoát",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        }
    }

    // Lấy đối tượng nhân sự được chọn trên tab đang hoạt động
    private Employee getSelectedEmployee() {
        int tabIndex = tabbedPane.getSelectedIndex();
        if (tabIndex == 0) {
            int row = employeeTable.getSelectedRow();
            return (row >= 0) ? employeeTableModel.getEmployeeAt(row) : null;
        } else {
            int row = payrollTable.getSelectedRow();
            return (row >= 0) ? payrollTableModel.getEmployeeAt(row) : null;
        }
    }

    // Áp dụng bộ lọc và sắp xếp dữ liệu
    private void applyFilterAndSort() {
        String selectedDept = (String) cboDepartmentFilter.getSelectedItem();
        String keyword = (txtSearch != null) ? txtSearch.getText().trim().toLowerCase(Locale.ROOT) : "";
        int sortIndex = (cboSortCriteria != null) ? cboSortCriteria.getSelectedIndex() : 0;

        List<Employee> list = new ArrayList<>(repository.findAll());

        // Lọc theo phòng ban
        if (selectedDept != null && !ALL_DEPTS.equals(selectedDept)) {
            list = list.stream()
                    .filter(e -> selectedDept.equalsIgnoreCase(e.getDepartment()))
                    .collect(Collectors.toList());
        }

        // Tìm kiếm theo Họ và tên
        if (!keyword.isEmpty()) {
            list = list.stream()
                    .filter(e -> e.getFullName() != null && e.getFullName().toLowerCase(Locale.ROOT).contains(keyword))
                    .collect(Collectors.toList());
        }

        // Sắp xếp đa tiêu chí linh hoạt và hỗ trợ đảo ngược thứ tự (Tăng dần / Giảm dần)
        Comparator<Employee> comp;
        switch (sortIndex) {
            case 1: comp = EmployeeComparator.byName(); break;
            case 2: comp = EmployeeComparator.bySalary(); break;
            case 3: comp = EmployeeComparator.bySeniority(); break;
            case 4: comp = EmployeeComparator.byDepartment(); break;
            case 5: comp = EmployeeComparator.byEmployeeType(); break;
            case 0:
            default:
                comp = EmployeeComparator.byId();
                break;
        }

        if (!isSortAscending) {
            comp = comp.reversed();
        }
        list.sort(comp);

        employeeTableModel.setEmployees(list);
        payrollTableModel.setEmployees(list);
    }

    // Thêm một phòng ban mới vào hệ thống
    private void onAddNewDepartment() {
        String deptName = JOptionPane.showInputDialog(this,
                "Nhập tên phòng ban mới cần thêm vào hệ thống:",
                "Thêm phòng ban mới",
                JOptionPane.QUESTION_MESSAGE);
        if (deptName != null) {
            deptName = deptName.trim();
            if (deptName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên phòng ban không được để trống!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            repository.addDepartment(deptName);
            updateDepartmentFilterOptions();
            cboDepartmentFilter.setSelectedItem(deptName);
            applyFilterAndSort();
            setStatus("Đã thêm: " + deptName);
        }
    }

    // Làm mới toàn bộ dữ liệu giao diện và thanh trạng thái
    public void refreshData() {
        updateDepartmentFilterOptions();
        applyFilterAndSort();
        String syncStatus = repository.hasUnsavedChanges() ? " | [Chưa lưu *]" : " | [Đã lưu]";
        setStatus(payrollService.getStatusBarSummary() + syncStatus);
    }

    private void updateDepartmentFilterOptions() {
        String currentSelection = (String) cboDepartmentFilter.getSelectedItem();
        List<String> depts = new ArrayList<>();
        depts.add(ALL_DEPTS);
        depts.addAll(repository.getAllDepartments());

        cboDepartmentFilter.setModel(new DefaultComboBoxModel<>(depts.toArray(new String[0])));
        if (currentSelection != null && depts.contains(currentSelection)) {
            cboDepartmentFilter.setSelectedItem(currentSelection);
        } else {
            cboDepartmentFilter.setSelectedIndex(0);
        }
    }

    @Override
    protected void showAbout() {
        new AboutDialog(this).setVisible(true);
    }
}
