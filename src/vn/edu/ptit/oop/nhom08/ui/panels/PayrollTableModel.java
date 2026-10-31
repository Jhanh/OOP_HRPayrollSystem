package vn.edu.ptit.oop.nhom08.ui.panels;

import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.Taxable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PayrollTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "Mã NV", "Họ và tên", "Chức vụ", "Phòng ban", "Tổng lương", "Thuế TNCN", "Thực lĩnh"
    };
    private List<Employee> employees;

    public PayrollTableModel() {
        this.employees = new ArrayList<>();
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = (employees != null) ? employees : new ArrayList<>();
        fireTableDataChanged();
    }

    public int getRowCount() {
        return employees.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

 
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= employees.size()) return null;
        
        Employee emp = employees.get(rowIndex);
        double grossSalary = emp.calculateSalary();
        double tax = (emp instanceof Taxable taxableEmp) ? taxableEmp.calculateTax() : 0.0;
        double netSalary = grossSalary - tax;

        return switch (columnIndex) {
            case 0 -> emp.getId();
            case 1 -> emp.getFullName();
            case 2 -> emp.getClass().getSimpleName();
            case 3 -> emp.getDepartment();
            case 4 -> grossSalary; 
            case 5 -> tax;
            case 6 -> netSalary;
            default -> null;
        };
    }
}
