package vn.edu.ptit.oop.nhom08.ui.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.PartTimeEmployee;
import vn.edu.ptit.oop.nhom08.util.CurrencyFormatUtil;

// Mô hình bảng hiển thị danh sách hồ sơ nhân sự (Tab 1)
public class EmployeeTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
        "STT", "Mã NV", "Họ và tên", "Phòng ban", "Loại nhân viên", "Lương cơ bản", "Ngày vào làm", "Thâm niên"
    };

    private List<Employee> employees = new ArrayList<>();

    public EmployeeTableModel() {
    }

    public EmployeeTableModel(List<Employee> employees) {
        setEmployees(employees);
    }

    // Cập nhật toàn bộ danh sách nhân viên và làm mới bảng
    public void setEmployees(List<Employee> employees) {
        this.employees = (employees != null) ? new ArrayList<>(employees) : new ArrayList<>();
        fireTableDataChanged();
    }

    // Trả về danh sách bất biến để bảo vệ collection nội bộ (Encapsulation)
    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }

    // Lấy nhân viên tại vị trí dòng được chọn
    public Employee getEmployeeAt(int row) {
        if (row < 0 || row >= employees.size()) {
            return null;
        }
        return employees.get(row);
    }

    // Thêm nhân viên mới vào bảng
    public void addEmployee(Employee e) {
        if (e != null) {
            employees.add(e);
            fireTableRowsInserted(employees.size() - 1, employees.size() - 1);
        }
    }

    // Cập nhật thông tin nhân viên tại dòng chỉ định
    public void updateEmployee(int row, Employee e) {
        if (row >= 0 && row < employees.size() && e != null) {
            employees.set(row, e);
            fireTableRowsUpdated(row, row);
        }
    }

    // Xóa nhân viên khỏi bảng
    public void removeEmployee(int row) {
        if (row >= 0 && row < employees.size()) {
            employees.remove(row);
            fireTableRowsDeleted(row, row);
        }
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
                // Đa hình: mỗi lớp con tự trả về loại nhân viên tương ứng
                return e.getEmployeeType();
            case 5:
                if (e instanceof PartTimeEmployee) {
                    return CurrencyFormatUtil.format(e.getBaseSalary()) + " / giờ";
                }
                return CurrencyFormatUtil.format(e.getBaseSalary());
            case 6:
                return CurrencyFormatUtil.formatDate(e.getHireDate());
            case 7:
                return e.getSeniorityYears() + " năm";
            default:
                return "";
        }
    }
}
