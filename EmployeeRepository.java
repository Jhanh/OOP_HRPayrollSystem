package vn.edu.ptit.oop.nhom08.repository;

import java.util.List;
import java.util.Optional;
import vn.edu.ptit.oop.nhom08.model.Employee;

// Interface định nghĩa các thao tác CRUD trên kho dữ liệu nhân viên
public interface EmployeeRepository {

    void add(Employee employee);

    Optional<Employee> findById(String id);

    List<Employee> findAll();

    List<Employee> findByDepartment(String department);

    boolean update(Employee employee);

    boolean deleteById(String id);

    boolean existsById(String id);

    int count();
}

