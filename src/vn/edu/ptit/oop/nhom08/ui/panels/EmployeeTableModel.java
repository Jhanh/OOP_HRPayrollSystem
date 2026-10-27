package vn.edu.ptit.oop.nhom08.ui.panels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.util.CurrencyFormatUtil;

// Mô hình dữ liệu cho JTable hiển thị danh sách hồ sơ nhân viên
public class EmployeeTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
        "STT", "Mã NV", "Họ và tên", "Phòng ban", "Loại nhân viên", "Lương cơ bản (VNĐ)", "Ngày vào làm"
    };

    private List<Employee> employees = new ArrayList<>();

    public EmployeeTableModel() {
    }

    public EmployeeTableModel(List<Employee> employees) {
        setEmployees(employees);
    }

    // Thay toàn bộ dữ liệu và làm mới bảng
    public void setEmployees(List<Employee> employees) {
        this.employees = (employees != null) ? new ArrayList<>(employees) : new ArrayList<>();
        fireTableDataChanged();
    }

    // Lấy nhân viên tại dòng đã chọn (dùng cho Sửa / Xóa / Xuất phiếu lương)
    public Employee getEmployeeAt(int row) {
        if (row < 0 || row >= employees.size()) {
            return null;
        }
        return employees.get(row);
    }

    @Override
    public int getRowCount() {
        return employees.size();
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
        return (columnIndex == 0) ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee e = employees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return e.getId();
            case 2:
                return e.getFullName();
            case 3:
                return e.getDepartment();
            case 4:
                // Đa hình: mỗi lớp con tự trả về tên loại của mình, không cần instanceof
                return e.getEmployeeType();
            case 5:
                return CurrencyFormatUtil.format(e.getBaseSalary());
            case 6:
                return CurrencyFormatUtil.formatDate(e.getHireDate());
            default:
                return "";
        }
    }
}
