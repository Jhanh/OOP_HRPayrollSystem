package vn.edu.ptit.oop.nhom08.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import vn.edu.ptit.oop.nhom08.exception.InvalidEmployeeDataException;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.FullTimeEmployee;
import vn.edu.ptit.oop.nhom08.model.Intern;
import vn.edu.ptit.oop.nhom08.model.Manager;
import vn.edu.ptit.oop.nhom08.model.PartTimeEmployee;
import vn.edu.ptit.oop.nhom08.util.CurrencyFormatUtil;

// Hộp thoại thêm mới hoặc chỉnh sửa hồ sơ nhân viên dùng CardLayout và JSpinner
public class EmployeeFormDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private enum EmployeeType {
        FULL_TIME, PART_TIME, INTERN, MANAGER
    }

    private static final class FieldException extends InvalidEmployeeDataException {
        private static final long serialVersionUID = 1L;
        private final transient JComponent field;

        FieldException(JComponent field, String message) {
            super(message);
            this.field = field;
        }

        FieldException(JComponent field, String message, Throwable cause) {
            super(message, cause);
            this.field = field;
        }

        JComponent getField() {
            return field;
        }
    }

    @FunctionalInterface
    private interface ModelCheck {
        void run() throws InvalidEmployeeDataException;
    }

    private static final int MAX_ID_LENGTH = 20;
    private static final int MAX_NAME_LENGTH = 100;
    private static final double MAX_MONEY = 1_000_000_000_000.0;
    private static final double MAX_SALARY_FACTOR = 10.0;
    private static final double MAX_WORKING_HOURS = 744.0;
    private static final int MAX_MANAGED_COUNT = 500;

    private static final String[] DEFAULT_DEPARTMENTS =
            {"Nhân sự", "Kế toán", "Kinh doanh", "Marketing", "Công nghệ thông tin", "Chăm sóc khách hàng"};

    private static final String LBL_BASE_DEFAULT = "Lương cơ bản (*):";
    private static final String LBL_BASE_HOURLY = "Lương theo giờ (*):";
    private static final String LBL_BASE_INTERN = "Trợ cấp cố định (*):";

    private static final String CARD_FULL_TIME = "FULL_TIME";
    private static final String CARD_PART_TIME = "PART_TIME";
    private static final String CARD_INTERN = "INTERN";
    private static final String CARD_MANAGER = "MANAGER";

    private static final Color ERROR_COLOR = new Color(0xD32F2F);

    private static final DateTimeFormatter DATE_SHOW_FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_DMY_FMT =
            DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_YMD_FMT =
            DateTimeFormatter.ofPattern("uuuu/M/d").withResolverStyle(ResolverStyle.STRICT);

    private final JComboBox<EmployeeType> cboType = new JComboBox<>(EmployeeType.values());
    private final JTextField txtId = new JTextField(18);
    private final JTextField txtName = new JTextField(18);
    private final JComboBox<String> cboDepartment = new JComboBox<>();
    private final JLabel lblBaseSalary = new JLabel(LBL_BASE_DEFAULT);
    private final JTextField txtBaseSalary = new JTextField(18);
    private final JTextField txtHireDate = new JTextField(18);

    private final JSpinner spnFtFactor = newDecimalSpinner(1.0, 1.0, MAX_SALARY_FACTOR, 0.1);
    private final JTextField txtFtBonus = new JTextField(18);

    private final JSpinner spnPtHours = newDecimalSpinner(0.0, 0.0, MAX_WORKING_HOURS, 0.5);

    private final JSpinner spnMgFactor = newDecimalSpinner(1.0, 1.0, MAX_SALARY_FACTOR, 0.1);
    private final JTextField txtMgBonus = new JTextField(18);
    private final JSpinner spnMgManaged = newIntSpinner(0, 0, MAX_MANAGED_COUNT);

    private final JLabel lblError = new JLabel(" ");
    private JComponent errorField;
    private Border errorOriginalBorder;

    private final Map<EmployeeType, String> typeLabels = new EnumMap<>(EmployeeType.class);
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final Employee editingEmployee;
    private final Predicate<String> idExistsChecker;
    private final Consumer<String> departmentAdder;

    private Employee result;
    private boolean confirmed;

    public EmployeeFormDialog(Frame owner) {
        this(owner, null, null, null, null);
    }

    public EmployeeFormDialog(Frame owner, Employee employeeToEdit, Predicate<String> idExistsChecker) {
        this(owner, employeeToEdit, idExistsChecker, null, null);
    }

    public EmployeeFormDialog(Frame owner, Employee employeeToEdit, Predicate<String> idExistsChecker,
                              Collection<String> departments) {
        this(owner, employeeToEdit, idExistsChecker, departments, null);
    }

    public EmployeeFormDialog(Frame owner, Employee employeeToEdit, Predicate<String> idExistsChecker,
                              Collection<String> departments, Consumer<String> departmentAdder) {
        super(owner, employeeToEdit == null ? "Thêm nhân viên mới" : "Sửa thông tin nhân viên", true);
        this.editingEmployee = employeeToEdit;
        this.idExistsChecker = idExistsChecker;
        this.departmentAdder = departmentAdder;

        for (EmployeeType t : EmployeeType.values()) {
            typeLabels.put(t, createProbe(t).getEmployeeType());
        }
        fillDepartments(departments);

        loadWindowIcon();
        initComponents();
        registerEvents();

        if (editingEmployee != null) {
            populateForm(editingEmployee);
        } else {
            txtHireDate.setText(LocalDate.now().format(DATE_SHOW_FMT));
            onTypeChanged();
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(owner);
    }

    private void fillDepartments(Collection<String> departments) {
        TreeSet<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (departments != null) {
            for (String d : departments) {
                if (d != null && !d.trim().isEmpty()) {
                    names.add(d.trim());
                }
            }
        }
        if (names.isEmpty()) {
            names.addAll(Arrays.asList(DEFAULT_DEPARTMENTS));
        }
        if (editingEmployee != null && editingEmployee.getDepartment() != null) {
            names.add(editingEmployee.getDepartment());
        }
        cboDepartment.setModel(new DefaultComboBoxModel<>(names.toArray(new String[0])));
    }

    private String canonicalDepartment(String typed) {
        for (int i = 0; i < cboDepartment.getItemCount(); i++) {
            String existing = cboDepartment.getItemAt(i);
            if (existing.equalsIgnoreCase(typed)) {
                return existing;
            }
        }
        return typed;
    }

    // Mở hộp thoại nhập nhanh phòng ban mới ngay trong form thêm/sửa nhân sự
    private void promptAddNewDepartment() {
        String newDept = JOptionPane.showInputDialog(this,
                "Nhập tên phòng ban mới cần thêm:",
                "Thêm phòng ban mới",
                JOptionPane.QUESTION_MESSAGE);
        if (newDept != null) {
            newDept = newDept.trim();
            if (newDept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên phòng ban không được để trống!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cboDepartment.getModel();
            boolean exists = false;
            for (int i = 0; i < model.getSize(); i++) {
                if (model.getElementAt(i).equalsIgnoreCase(newDept)) {
                    cboDepartment.setSelectedIndex(i);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                model.addElement(newDept);
                cboDepartment.setSelectedItem(newDept);
            }
            if (departmentAdder != null) {
                departmentAdder.accept(newDept);
            }
        }
    }

    private void initComponents() {
        cboType.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Object shown = value instanceof EmployeeType ? typeLabels.get(value) : value;
                return super.getListCellRendererComponent(list, shown, index, isSelected, cellHasFocus);
            }
        });

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));

        JPanel common = new JPanel(new GridBagLayout());
        common.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));
        cboDepartment.setEditable(true);

        JPanel deptPanel = new JPanel(new BorderLayout(5, 0));
        deptPanel.add(cboDepartment, BorderLayout.CENTER);
        JButton btnAddDept = new JButton("+");
        btnAddDept.setPreferredSize(new Dimension(36, 26));
        btnAddDept.setToolTipText("Thêm phòng ban mới vào danh mục");
        btnAddDept.addActionListener(e -> promptAddNewDepartment());
        deptPanel.add(btnAddDept, BorderLayout.EAST);

        int r = 0;
        addRow(common, r++, "Loại nhân viên:", cboType);
        addRow(common, r++, "Mã nhân viên (*):", txtId);
        addRow(common, r++, "Họ và tên (*):", txtName);
        addRow(common, r++, "Phòng ban (*):", deptPanel);
        addRow(common, r++, lblBaseSalary, txtBaseSalary);
        addRow(common, r++, "Ngày vào làm (dd/MM/yyyy) (*):", txtHireDate);

        cardPanel.setBorder(BorderFactory.createTitledBorder("Thông tin theo loại nhân viên"));
        cardPanel.add(buildFullTimeCard(), CARD_FULL_TIME);
        cardPanel.add(buildPartTimeCard(), CARD_PART_TIME);
        cardPanel.add(buildInternCard(), CARD_INTERN);
        cardPanel.add(buildManagerCard(), CARD_MANAGER);

        lblError.setForeground(ERROR_COLOR);
        lblError.setPreferredSize(new Dimension(420, 36));
        lblError.setVerticalAlignment(JLabel.TOP);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> onCancel());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.add(btnSave);
        buttons.add(btnCancel);
        getRootPane().setDefaultButton(btnSave);

        JPanel south = new JPanel(new BorderLayout(0, 6));
        south.add(lblError, BorderLayout.NORTH);
        south.add(buttons, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.add(common, BorderLayout.NORTH);
        center.add(cardPanel, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildFullTimeCard() {
        JPanel p = newCard();
        addRow(p, 0, "Hệ số lương:", spnFtFactor);
        addRow(p, 1, "Phụ cấp:", txtFtBonus);
        return p;
    }

    private JPanel buildPartTimeCard() {
        JPanel p = newCard();
        addRow(p, 0, "Số giờ làm trong tháng:", spnPtHours);
        return p;
    }

    private JPanel buildInternCard() {
        return newCard();
    }

    private JPanel buildManagerCard() {
        JPanel p = newCard();
        addRow(p, 0, "Hệ số lương:", spnMgFactor);
        addRow(p, 1, "Phụ cấp:", txtMgBonus);
        addRow(p, 2, "Số người quản lý:", spnMgManaged);
        return p;
    }

    private static JPanel newCard() {
        return new JPanel(new GridBagLayout());
    }

    private static JSpinner newDecimalSpinner(double init, double min, double max, double step) {
        return newNumberSpinner(new SpinnerNumberModel(init, min, max, step), false, "0.0#");
    }

    private static JSpinner newIntSpinner(int init, int min, int max) {
        return newNumberSpinner(new SpinnerNumberModel(init, min, max, 1), true, "0");
    }

    private static JSpinner newNumberSpinner(SpinnerNumberModel model, boolean integer, String pattern) {
        JSpinner spinner = new JSpinner(model);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, pattern);
        spinner.setEditor(editor);
        JFormattedTextField textField = editor.getTextField();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        textField.setFormatterFactory(new DefaultFormatterFactory(
                new StrictNumberFormatter(model, integer, new DecimalFormat(pattern, symbols))));
        textField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        return spinner;
    }

    private static final class StrictNumberFormatter extends NumberFormatter {
        private static final long serialVersionUID = 1L;
        private final transient SpinnerNumberModel model;
        private final boolean integer;

        StrictNumberFormatter(SpinnerNumberModel model, boolean integer, NumberFormat format) {
            super(format);
            this.model = model;
            this.integer = integer;
            setValueClass(integer ? Integer.class : Double.class);
            setAllowsInvalid(true);
        }

        @Override
        public Comparable<?> getMinimum() {
            return model.getMinimum();
        }

        @Override
        public Comparable<?> getMaximum() {
            return model.getMaximum();
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            String t = (text == null) ? "" : text.trim().replace(',', '.');
            if (!t.matches(integer ? "^-?\\d+$" : "^-?\\d+(\\.\\d+)?$")) {
                throw new ParseException("Không phải số hợp lệ: " + text, 0);
            }
            Number value;
            try {
                value = integer ? (Number) Integer.valueOf(t) : (Number) Double.valueOf(t);
            } catch (NumberFormatException ex) {
                throw new ParseException("Số quá lớn: " + text, 0);
            }
            double v = value.doubleValue();
            if (v < ((Number) model.getMinimum()).doubleValue() || v > ((Number) model.getMaximum()).doubleValue()) {
                throw new ParseException("Ngoài phạm vi cho phép: " + text, 0);
            }
            return value;
        }
    }

    private static void addRow(JPanel panel, int row, String label, JComponent field) {
        addRow(panel, row, new JLabel(label), field);
    }

    private static void addRow(JPanel panel, int row, JLabel label, JComponent field) {
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0;
        lc.gridy = row;
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(4, 6, 4, 10);
        panel.add(label, lc);

        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx = 1;
        fc.gridy = row;
        fc.weightx = 1.0;
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.insets = new Insets(4, 0, 4, 6);
        panel.add(field, fc);
        field.setPreferredSize(new Dimension(220, field.getPreferredSize().height));
    }

    private void registerEvents() {
        cboType.addActionListener(e -> onTypeChanged());

        KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        JComponent[] inputs = {txtId, txtName, cboDepartment, txtBaseSalary, txtHireDate,
                txtFtBonus, txtMgBonus, spnFtFactor, spnPtHours, spnMgFactor, spnMgManaged};
        for (JComponent input : inputs) {
            JComponent textPart = focusTargetOf(input);
            if (textPart instanceof JTextComponent) {
                ((JTextComponent) textPart).getDocument().addDocumentListener(new ClearOnEdit(input));
            }
        }
    }

    private final class ClearOnEdit implements DocumentListener {
        private final JComponent owner;

        ClearOnEdit(JComponent owner) {
            this.owner = owner;
        }

        private void changed() {
            if (errorField == owner) {
                clearError();
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            changed();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changed();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            changed();
        }
    }

    private void onTypeChanged() {
        clearError();
        EmployeeType type = (EmployeeType) cboType.getSelectedItem();
        switch (type) {
            case PART_TIME:
                lblBaseSalary.setText(LBL_BASE_HOURLY);
                cardLayout.show(cardPanel, CARD_PART_TIME);
                break;
            case INTERN:
                lblBaseSalary.setText(LBL_BASE_INTERN);
                cardLayout.show(cardPanel, CARD_INTERN);
                break;
            case MANAGER:
                lblBaseSalary.setText(LBL_BASE_DEFAULT);
                cardLayout.show(cardPanel, CARD_MANAGER);
                break;
            case FULL_TIME:
            default:
                lblBaseSalary.setText(LBL_BASE_DEFAULT);
                cardLayout.show(cardPanel, CARD_FULL_TIME);
                break;
        }
    }

    private void onCancel() {
        confirmed = false;
        result = null;
        dispose();
    }

    private void onSave() {
        clearError();
        try {
            result = buildEmployee();
            confirmed = true;
            dispose();
        } catch (FieldException ex) {
            showError(ex.getField(), ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
        } catch (InvalidEmployeeDataException ex) {
            showError(null, ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Không thể lưu nhân viên: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showError(JComponent field, String message) {
        clearError();
        lblError.setText("<html><body style='width:400px'>⚠ " + escapeHtml(message) + "</body></html>");
        if (field == null) {
            return;
        }
        errorField = field;
        errorOriginalBorder = field.getBorder();
        field.setBorder(BorderFactory.createLineBorder(ERROR_COLOR, 2));

        JComponent target = focusTargetOf(field);
        target.requestFocusInWindow();
        if (target instanceof JTextComponent) {
            ((JTextComponent) target).selectAll();
        }
    }

    private void clearError() {
        lblError.setText(" ");
        if (errorField != null) {
            errorField.setBorder(errorOriginalBorder);
            errorField = null;
            errorOriginalBorder = null;
        }
    }

    private static JComponent focusTargetOf(JComponent c) {
        if (c instanceof JSpinner) {
            JComponent editor = ((JSpinner) c).getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                return ((JSpinner.DefaultEditor) editor).getTextField();
            }
        }
        if (c instanceof JComboBox && ((JComboBox<?>) c).isEditable()) {
            Component editorComp = ((JComboBox<?>) c).getEditor().getEditorComponent();
            if (editorComp instanceof JComponent) {
                return (JComponent) editorComp;
            }
        }
        return c;
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static void check(JComponent field, ModelCheck check) throws FieldException {
        try {
            check.run();
        } catch (InvalidEmployeeDataException ex) {
            throw new FieldException(field, ex.getMessage(), ex);
        }
    }

    private static Employee createProbe(EmployeeType type) {
        LocalDate date = LocalDate.now().minusDays(1);
        try {
            switch (type) {
                case PART_TIME:
                    return new PartTimeEmployee("PROBE", "Probe", "Probe", 1.0, date, 1.0);
                case INTERN:
                    return new Intern("PROBE", "Probe", "Probe", 1.0, date);
                case MANAGER:
                    return new Manager("PROBE", "Probe", "Probe", 1.0, date, 1.0, 1.0, 1);
                case FULL_TIME:
                default:
                    return new FullTimeEmployee("PROBE", "Probe", "Probe", 1.0, date, 1.0, 1.0);
            }
        } catch (InvalidEmployeeDataException ex) {
            throw new IllegalStateException("Lỗi khởi tạo mẫu kiểm tra: " + ex.getMessage(), ex);
        }
    }

    private Employee buildEmployee() throws InvalidEmployeeDataException {
        EmployeeType type = (EmployeeType) cboType.getSelectedItem();
        Employee probe = createProbe(type);

        String id = editingEmployee != null ? editingEmployee.getId() : txtId.getText().trim();
        if (editingEmployee == null) {
            if (id.length() > MAX_ID_LENGTH) {
                throw new FieldException(txtId, "Mã nhân viên tối đa " + MAX_ID_LENGTH + " ký tự.");
            }
            check(txtId, () -> probe.setId(id));
            if (idExistsChecker != null && idExistsChecker.test(id)) {
                throw new FieldException(txtId, "Mã nhân viên \"" + id + "\" đã tồn tại!");
            }
        }

        String name = txtName.getText().trim().replaceAll("\\s+", " ");
        if (name.length() > MAX_NAME_LENGTH) {
            throw new FieldException(txtName, "Họ tên tối đa " + MAX_NAME_LENGTH + " ký tự.");
        }
        check(txtName, () -> probe.setFullName(name));

        Object deptItem = cboDepartment.getEditor().getItem();
        String department = canonicalDepartment(
                (deptItem == null ? "" : deptItem.toString()).trim().replaceAll("\\s+", " "));
        check(cboDepartment, () -> probe.setDepartment(department));
        if (departmentAdder != null && department != null && !department.isEmpty()) {
            departmentAdder.accept(department);
        }

        String baseLabel = (type == EmployeeType.PART_TIME) ? "Lương theo giờ"
                : (type == EmployeeType.INTERN) ? "Trợ cấp cố định" : "Lương cơ bản";
        double baseSalary = parseMoney(txtBaseSalary, baseLabel, false);
        check(txtBaseSalary, () -> {
            if (probe instanceof PartTimeEmployee) {
                ((PartTimeEmployee) probe).setHourlyRate(baseSalary);
            } else if (probe instanceof Intern) {
                ((Intern) probe).setAllowance(baseSalary);
            } else {
                probe.setBaseSalary(baseSalary);
            }
        });

        LocalDate hireDate = parseHireDate();
        check(txtHireDate, () -> probe.setHireDate(hireDate));

        switch (type) {
            case PART_TIME: {
                double hours = readDoubleSpinner(spnPtHours, "Số giờ làm");
                PartTimeEmployee pp = (PartTimeEmployee) probe;
                check(spnPtHours, () -> pp.setWorkingHours(hours));
                return new PartTimeEmployee(id, name, department, baseSalary, hireDate, hours);
            }
            case INTERN:
                return new Intern(id, name, department, baseSalary, hireDate);
            case MANAGER: {
                double factor = readDoubleSpinner(spnMgFactor, "Hệ số lương");
                double bonus = parseMoney(txtMgBonus, "Phụ cấp", true);
                int managed = readIntSpinner(spnMgManaged, "Số người quản lý");
                Manager mp = (Manager) probe;
                check(spnMgFactor, () -> mp.setSalaryFactor(factor));
                check(txtMgBonus, () -> mp.setBonus(bonus));
                check(spnMgManaged, () -> mp.setManagedCount(managed));
                return new Manager(id, name, department, baseSalary, hireDate, factor, bonus, managed);
            }
            case FULL_TIME:
            default: {
                double factor = readDoubleSpinner(spnFtFactor, "Hệ số lương");
                double bonus = parseMoney(txtFtBonus, "Phụ cấp", true);
                FullTimeEmployee fp = (FullTimeEmployee) probe;
                check(spnFtFactor, () -> fp.setSalaryFactor(factor));
                check(txtFtBonus, () -> fp.setBonus(bonus));
                return new FullTimeEmployee(id, name, department, baseSalary, hireDate, factor, bonus);
            }
        }
    }

    private LocalDate parseHireDate() throws FieldException {
        String text = txtHireDate.getText().trim().replace('-', '/').replace('.', '/');
        if (text.isEmpty()) {
            throw new FieldException(txtHireDate, "Ngày vào làm không được để trống!");
        }
        try {
            return text.matches("^\\d{4}/.*")
                    ? LocalDate.parse(text, DATE_YMD_FMT)
                    : LocalDate.parse(text, DATE_DMY_FMT);
        } catch (DateTimeParseException ex) {
            throw new FieldException(txtHireDate,
                    "Ngày vào làm sai định dạng dd/MM/yyyy hoặc ngày không tồn tại (ví dụ: 15/08/2023).", ex);
        }
    }

    private double parseMoney(JTextField field, String fieldName, boolean emptyAsZero) throws FieldException {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            if (emptyAsZero) {
                return 0.0;
            }
            throw new FieldException(field, fieldName + " không được để trống!");
        }
        double value;
        try {
            value = CurrencyFormatUtil.parse(text);
        } catch (Exception ex) {
            try {
                value = Double.parseDouble(text.replace(",", "."));
            } catch (NumberFormatException nfe) {
                throw new FieldException(field, fieldName + " phải là số hợp lệ (ví dụ: 12000000 hoặc 12.000.000).", ex);
            }
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new FieldException(field, fieldName + " phải là số hợp lệ.");
        }
        if (value > MAX_MONEY) {
            throw new FieldException(field, fieldName + " quá lớn (tối đa " + (long) MAX_MONEY + " VNĐ).");
        }
        return value;
    }

    private int readIntSpinner(JSpinner spinner, String fieldName) throws FieldException {
        commitSpinner(spinner, fieldName);
        return ((Number) spinner.getValue()).intValue();
    }

    private double readDoubleSpinner(JSpinner spinner, String fieldName) throws FieldException {
        commitSpinner(spinner, fieldName);
        double v = ((Number) spinner.getValue()).doubleValue();
        return Math.round(v * 100.0) / 100.0;
    }

    private void commitSpinner(JSpinner spinner, String fieldName) throws FieldException {
        try {
            spinner.commitEdit();
        } catch (ParseException ex) {
            SpinnerNumberModel m = (SpinnerNumberModel) spinner.getModel();
            throw new FieldException(spinner,
                    fieldName + " phải là số trong khoảng " + m.getMinimum() + " đến " + m.getMaximum() + ".", ex);
        }
    }

    private static void setSpinnerSafely(JSpinner spinner, double value) {
        SpinnerNumberModel m = (SpinnerNumberModel) spinner.getModel();
        boolean isInt = m.getValue() instanceof Integer;
        Number val = isInt ? (Number) Integer.valueOf((int) Math.round(value)) : (Number) Double.valueOf(value);
        if (value > ((Number) m.getMaximum()).doubleValue()) {
            m.setMaximum((Comparable<?>) val);
        }
        if (value < ((Number) m.getMinimum()).doubleValue()) {
            m.setMinimum((Comparable<?>) val);
        }
        m.setValue(val);
    }

    private void populateForm(Employee e) {
        txtId.setText(e.getId());
        txtId.setEnabled(false);
        txtName.setText(e.getFullName());
        cboDepartment.setSelectedItem(e.getDepartment());
        txtBaseSalary.setText(plainNumber(e.getBaseSalary()));
        txtHireDate.setText(e.getHireDate().format(DATE_SHOW_FMT));

        if (e instanceof Manager) {
            Manager m = (Manager) e;
            cboType.setSelectedItem(EmployeeType.MANAGER);
            setSpinnerSafely(spnMgFactor, m.getSalaryFactor());
            txtMgBonus.setText(plainNumber(m.getBonus()));
            setSpinnerSafely(spnMgManaged, m.getManagedCount());
        } else if (e instanceof FullTimeEmployee) {
            FullTimeEmployee f = (FullTimeEmployee) e;
            cboType.setSelectedItem(EmployeeType.FULL_TIME);
            setSpinnerSafely(spnFtFactor, f.getSalaryFactor());
            txtFtBonus.setText(plainNumber(f.getBonus()));
        } else if (e instanceof PartTimeEmployee) {
            PartTimeEmployee p = (PartTimeEmployee) e;
            cboType.setSelectedItem(EmployeeType.PART_TIME);
            setSpinnerSafely(spnPtHours, p.getWorkingHours());
        } else if (e instanceof Intern) {
            cboType.setSelectedItem(EmployeeType.INTERN);
        }
        cboType.setEnabled(false);
        onTypeChanged();
    }

    private static String plainNumber(double value) {
        return (value == Math.rint(value)) ? String.valueOf((long) value) : String.valueOf(value);
    }

    private void loadWindowIcon() {
        String iconPath = (editingEmployee == null) ? "/resources/icons/add.png" : "/resources/icons/edit.png";
        ImageIcon icon = loadIcon(iconPath);
        if (icon == null) {
            icon = loadIcon("/resources/icons/app.png");
        }
        if (icon != null) {
            setIconImage(icon.getImage());
        }
    }

    private static ImageIcon loadIcon(String path) {
        URL url = EmployeeFormDialog.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        File file = new File(path.startsWith("/") ? path.substring(1) : path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        return null;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Employee getResult() {
        return result;
    }
}
