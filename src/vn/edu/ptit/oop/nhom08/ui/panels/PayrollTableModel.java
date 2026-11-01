package vn.edu.ptit.oop.nhom08.ui.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.TaxCalculator;

// Mô hình bảng tính lương chi tiết có dòng tổng cộng ở cuối bảng
public class PayrollTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
        "STT", "Mã NV", "Họ và tên", "Phòng ban", "Loại nhân viên", "Tổng lương", "Thuế TNCN", "Thực lĩnh"
    };

    private List<Employee> employees = new ArrayList<>();

    public PayrollTableModel() {
    }

    public PayrollTableModel(List<Employee> employees) {
        setEmployees(employees);
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = (employees != null) ? new ArrayList<>(employees) : new ArrayList<>();
        fireTableDataChanged();
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }

    public boolean isSummaryRow(int rowIndex) {
        return !employees.isEmpty() && rowIndex == employees.size();
    }

    public Employee getEmployeeAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= employees.size()) {
            return null;
        }
        return employees.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return employees.isEmpty() ? 0 : employees.size() + 1; // +1 dòng tổng cộng
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return Integer.class;
        if (columnIndex >= 5 && columnIndex <= 7) return Double.class;
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Dòng tổng cộng cuối bảng
        if (isSummaryRow(rowIndex)) {
            switch (columnIndex) {
                case 0: return "";
                case 1: return "TỔNG CỘNG";
                case 2: return employees.size() + " nhân viên";
                case 3: return "";
                case 4: return "";
                case 5: return calculateTotalGross();
                case 6: return calculateTotalTax();
                case 7: return calculateTotalNet();
                default: return "";
            }
        }

        Employee e = employees.get(rowIndex);
        switch (columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return e.getId();
            case 2: return e.getFullName();
            case 3: return e.getDepartment();
            case 4: return e.getEmployeeType();
            case 5: return e.calculateSalary();
            case 6: return TaxCalculator.calculateTax(e);
            case 7: return TaxCalculator.calculateNetSalary(e);
            default: return "";
        }
    }

    private double calculateTotalGross() {
        return employees.stream().mapToDouble(Employee::calculateSalary).sum();
    }

    private double calculateTotalTax() {
        return TaxCalculator.calculateTotalTax(employees);
    }

    private double calculateTotalNet() {
        return employees.stream().mapToDouble(TaxCalculator::calculateNetSalary).sum();
    }
}
