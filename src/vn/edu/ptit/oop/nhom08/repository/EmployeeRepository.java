package vn.edu.ptit.oop.nhom08.repository;

import java.io.File;
import java.util.List;
import java.util.Optional;
import vn.edu.ptit.oop.nhom08.model.Employee;

// Giao diện tầng lưu trữ dữ liệu nhân sự (CRUD)
public interface EmployeeRepository {

    // Lấy toàn bộ danh sách nhân sự
    List<Employee> findAll();

    // Tìm kiếm nhân sự theo mã nhân viên
    Optional<Employee> findById(String id);

    // Lưu nhân viên (thêm mới nếu chưa có, cập nhật nếu đã tồn tại)
    boolean save(Employee employee);

    // Xóa nhân viên theo mã
    boolean deleteById(String id);

    // Kiểm tra mã nhân viên đã tồn tại chưa
    boolean existsById(String id);

    // Lọc danh sách nhân viên theo phòng ban
    List<Employee> findByDepartment(String department);

    // Lấy danh sách tất cả các phòng ban hiện có
    List<String> getAllDepartments();

    // Thêm một phòng ban mới vào hệ thống
    void addDepartment(String department);

    // Đếm tổng số lượng nhân sự
    int count();

    // Xóa toàn bộ dữ liệu trên RAM
    void clear();

    // Kiểm tra xem có thay đổi trên RAM mà chưa được lưu vào tệp dữ liệu không
    boolean hasUnsavedChanges();

    // Lưu chốt toàn bộ dữ liệu hiện tại trên RAM vào tệp data/employees.csv (Backup / Save)
    void saveChanges();

    // Tải lại dữ liệu từ tệp data/employees.csv vào RAM, hủy bỏ các thay đổi nháp
    boolean reloadFromStorage();

    // Tạo bản sao lưu toàn bộ dữ liệu ra tệp CSV bất kỳ
    boolean backupToFile(File file);

    // Khôi phục dữ liệu từ tệp CSV sao lưu bất kỳ vào bộ nhớ RAM
    boolean restoreFromFile(File file);
}
