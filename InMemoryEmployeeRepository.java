package vn.edu.ptit.oop.nhom08.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import vn.edu.ptit.oop.nhom08.model.Employee;

// Cài đặt lưu trữ nhân viên trong bộ nhớ (LinkedHashMap giữ thứ tự thêm)
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<String, Employee> storage = new LinkedHashMap<>();

    @Override
    public void add(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Nhân viên không được null!");
        }
        if (storage.containsKey(employee.getId())) {
            throw new IllegalArgumentException("Mã nhân viên đã tồn tại: " + employee.getId());
        }
        storage.put(employee.getId(), employee);
    }

    @Override
    public Optional<Employee> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        if (department == null) {
            return Collections.emptyList();
        }
        return storage.values().stream()
                .filter(e -> department.equalsIgnoreCase(e.getDepartment()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean update(Employee employee) {
        if (employee == null || !storage.containsKey(employee.getId())) {
            return false;
        }
        storage.put(employee.getId(), employee);
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    @Override
    public int count() {
        return storage.size();
    }
}

