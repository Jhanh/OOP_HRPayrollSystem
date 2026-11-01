package vn.edu.ptit.oop.nhom08.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import vn.edu.ptit.oop.nhom08.model.Employee;
import vn.edu.ptit.oop.nhom08.model.FullTimeEmployee;
import vn.edu.ptit.oop.nhom08.model.Intern;
import vn.edu.ptit.oop.nhom08.model.Manager;
import vn.edu.ptit.oop.nhom08.model.PartTimeEmployee;

// Cài đặt kho lưu trữ nhân sự trong bộ nhớ RAM kết hợp tệp CSV lưu trữ lâu dài
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private static final String CSV_PATH = "data/employees.csv";
    private static final String CSV_HEADER = "TYPE,ID,FULL_NAME,DEPARTMENT,BASE_SALARY,HIRE_DATE,PARAM1,PARAM2,PARAM3";
    private static final String[] DEFAULT_DEPARTMENTS = {
        "Nhân sự", "Kế toán", "Kinh doanh", "Marketing", "Công nghệ thông tin", "Chăm sóc khách hàng"
    };

    private final Map<String, Employee> storage = new LinkedHashMap<>();
    private final Set<String> customDepartments = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private boolean hasUnsavedChanges = false;

    public InMemoryEmployeeRepository() {
        loadFromCsv();
    }

    // Đọc toàn bộ danh sách nhân sự từ tệp data/employees.csv vào bộ nhớ RAM
    public synchronized boolean loadFromCsv() {
        return loadFromCsv(Path.of(CSV_PATH));
    }

    // Đọc dữ liệu nhân sự từ đường dẫn tệp CSV bất kỳ vào bộ nhớ RAM
    public synchronized boolean loadFromCsv(Path path) {
        if (path == null) {
            return false;
        }
        if (!Files.exists(path)) {
            if (CSV_PATH.equals(path.toString().replace('\\', '/'))) {
                try {
                    if (path.getParent() != null) {
                        Files.createDirectories(path.getParent());
                    }
                    StringBuilder initContent = new StringBuilder(CSV_HEADER).append(System.lineSeparator());
                    for (String dept : DEFAULT_DEPARTMENTS) {
                        initContent.append("#DEPT,").append(dept).append(System.lineSeparator());
                    }
                    Files.writeString(path, initContent.toString(), StandardCharsets.UTF_8);
                } catch (IOException ignored) {
                }
                storage.clear();
                customDepartments.clear();
                Collections.addAll(customDepartments, DEFAULT_DEPARTMENTS);
                hasUnsavedChanges = false;
                return true;
            }
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            Map<String, Employee> loaded = new LinkedHashMap<>();
            Set<String> loadedDepts = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1).trim();
                }
                if (line.isEmpty() || line.startsWith("TYPE,")) {
                    continue;
                }
                if (line.startsWith("#DEPT,") || line.startsWith("#DEPARTMENT,")) {
                    List<String> deptParts = parseCsvLine(line);
                    if (deptParts.size() > 1 && !deptParts.get(1).trim().isEmpty()) {
                        loadedDepts.add(deptParts.get(1).trim());
                    }
                    continue;
                }
                if (line.startsWith("#")) {
                    continue;
                }

                List<String> partsList = parseCsvLine(line);
                if (partsList.size() < 6) {
                    continue;
                }
                String[] parts = partsList.toArray(new String[0]);

                String type = parts[0].trim().toUpperCase(Locale.ROOT);
                String id = parts[1].trim();
                String fullName = parts[2].trim();
                String department = parts[3].trim();
                double baseSalary = Double.parseDouble(parts[4].trim());
                LocalDate hireDate = LocalDate.parse(parts[5].trim());

                try {
                    Employee emp = null;
                    switch (type) {
                        case "MANAGER": {
                            double factor = parts.length > 6 && !parts[6].trim().isEmpty() ? Double.parseDouble(parts[6].trim()) : 1.0;
                            double bonus = parts.length > 7 && !parts[7].trim().isEmpty() ? Double.parseDouble(parts[7].trim()) : 0.0;
                            int managed = parts.length > 8 && !parts[8].trim().isEmpty() ? Integer.parseInt(parts[8].trim()) : 0;
                            emp = new Manager(id, fullName, department, baseSalary, hireDate, factor, bonus, managed);
                            break;
                        }
                        case "FULL_TIME":
                        case "FULLTIME": {
                            double factor = parts.length > 6 && !parts[6].trim().isEmpty() ? Double.parseDouble(parts[6].trim()) : 1.0;
                            double bonus = parts.length > 7 && !parts[7].trim().isEmpty() ? Double.parseDouble(parts[7].trim()) : 0.0;
                            emp = new FullTimeEmployee(id, fullName, department, baseSalary, hireDate, factor, bonus);
                            break;
                        }
                        case "PART_TIME":
                        case "PARTTIME": {
                            double hours = parts.length > 6 && !parts[6].trim().isEmpty() ? Double.parseDouble(parts[6].trim()) : 0.0;
                            emp = new PartTimeEmployee(id, fullName, department, baseSalary, hireDate, hours);
                            break;
                        }
                        case "INTERN": {
                            emp = new Intern(id, fullName, department, baseSalary, hireDate);
                            break;
                        }
                    }

                    if (emp != null) {
                        loaded.put(emp.getId().toUpperCase(Locale.ROOT), emp);
                        if (emp.getDepartment() != null && !emp.getDepartment().trim().isEmpty()) {
                            loadedDepts.add(emp.getDepartment().trim());
                        }
                    }
                } catch (Exception ex) {
                    // Bỏ qua dòng dữ liệu bị lỗi cú pháp trong file
                }
            }

            // Nạp dữ liệu từ file vào RAM (kể cả khi file rỗng/0 nhân viên do người dùng đã xóa hết)
            storage.clear();
            storage.putAll(loaded);
            customDepartments.clear();
            customDepartments.addAll(loadedDepts);
            hasUnsavedChanges = false;
            return true;
        } catch (IOException e) {
            // Lỗi đọc file
        }
        return false;
    }

    // Ghi đồng bộ dữ liệu hiện tại trong RAM ra tệp data/employees.csv
    public synchronized boolean saveToCsv() {
        return saveToCsv(Path.of(CSV_PATH));
    }

    // Ghi dữ liệu hiện tại trong RAM ra một đường dẫn tệp CSV bất kỳ
    public synchronized boolean saveToCsv(Path path) {
        if (path == null) return false;
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write(CSV_HEADER);
                writer.newLine();

                for (String dept : customDepartments) {
                    writer.write("#DEPT," + dept);
                    writer.newLine();
                }

                for (Employee e : storage.values()) {
                    String line = formatEmployeeToCsv(e);
                    if (line != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Cảnh báo: Không thể lưu dữ liệu ra file " + path + ": " + e.getMessage());
            return false;
        }
    }

    private String formatEmployeeToCsv(Employee e) {
        if (e instanceof Manager) {
            Manager m = (Manager) e;
            return String.format(Locale.ROOT, "MANAGER,%s,%s,%s,%d,%s,%.2f,%d,%d",
                    escapeCsv(m.getId()), escapeCsv(m.getFullName()), escapeCsv(m.getDepartment()),
                    (long) m.getBaseSalary(), m.getHireDate(), m.getSalaryFactor(),
                    (long) m.getBonus(), m.getManagedCount());
        } else if (e instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) e;
            return String.format(Locale.ROOT, "FULL_TIME,%s,%s,%s,%d,%s,%.2f,%d,0",
                    escapeCsv(ft.getId()), escapeCsv(ft.getFullName()), escapeCsv(ft.getDepartment()),
                    (long) ft.getBaseSalary(), ft.getHireDate(), ft.getSalaryFactor(),
                    (long) ft.getBonus());
        } else if (e instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) e;
            return String.format(Locale.ROOT, "PART_TIME,%s,%s,%s,%d,%s,%.1f,0,0",
                    escapeCsv(pt.getId()), escapeCsv(pt.getFullName()), escapeCsv(pt.getDepartment()),
                    (long) pt.getHourlyRate(), pt.getHireDate(), pt.getWorkingHours());
        } else if (e instanceof Intern) {
            Intern in = (Intern) e;
            return String.format(Locale.ROOT, "INTERN,%s,%s,%s,%d,%s,0,0,0",
                    escapeCsv(in.getId()), escapeCsv(in.getFullName()), escapeCsv(in.getDepartment()),
                    (long) in.getAllowance(), in.getHireDate());
        }
        return null;
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        String trimmed = s.trim();
        if (trimmed.contains(",") || trimmed.contains("\"")) {
            return "\"" + trimmed.replace("\"", "\"\"") + "\"";
        }
        return trimmed;
    }

    // Tách dòng CSV chuẩn RFC 4180: hỗ trợ dấu nháy kép, dấu phẩy bên trong nháy và dấu nháy thoát ""
    private static List<String> parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        if (line == null) {
            return tokens;
        }
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++; // Bỏ qua dấu nháy kép escape ""
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens;
    }

    @Override
    public synchronized boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    @Override
    public synchronized void saveChanges() {
        saveToCsv();
        hasUnsavedChanges = false;
    }

    @Override
    public synchronized boolean reloadFromStorage() {
        boolean ok = loadFromCsv();
        hasUnsavedChanges = false;
        return ok;
    }

    @Override
    public synchronized boolean backupToFile(File file) {
        if (file == null) return false;
        return saveToCsv(file.toPath());
    }

    @Override
    public synchronized boolean restoreFromFile(File file) {
        if (file == null || !file.exists()) return false;
        boolean ok = loadFromCsv(file.toPath());
        if (ok) {
            hasUnsavedChanges = true; // Dữ liệu phục hồi từ tệp sao lưu được tải vào RAM
        }
        return ok;
    }

    @Override
    public synchronized List<Employee> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    @Override
    public synchronized Optional<Employee> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id.trim().toUpperCase(Locale.ROOT)));
    }

    // Thao tác Thêm/Sửa chỉ thay đổi trên RAM để bạn thử nghiệm, khi bấm Lưu mới chốt vào file
    @Override
    public synchronized boolean save(Employee employee) {
        if (employee == null || employee.getId() == null) {
            return false;
        }
        storage.put(employee.getId().trim().toUpperCase(Locale.ROOT), employee);
        hasUnsavedChanges = true;
        return true;
    }

    // Thao tác Xóa chỉ thay đổi trên RAM, khi bấm Lưu mới chốt vào file
    @Override
    public synchronized boolean deleteById(String id) {
        if (id == null) return false;
        boolean removed = storage.remove(id.trim().toUpperCase(Locale.ROOT)) != null;
        if (removed) {
            hasUnsavedChanges = true;
        }
        return removed;
    }

    @Override
    public synchronized boolean existsById(String id) {
        if (id == null) return false;
        return storage.containsKey(id.trim().toUpperCase(Locale.ROOT));
    }

    @Override
    public synchronized List<Employee> findByDepartment(String department) {
        if (department == null || department.trim().isEmpty() || "Tất cả".equalsIgnoreCase(department.trim())) {
            return findAll();
        }
        String cleanDept = department.trim();
        return storage.values().stream()
                .filter(e -> cleanDept.equalsIgnoreCase(e.getDepartment()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public synchronized List<String> getAllDepartments() {
        TreeSet<String> departments = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        departments.addAll(customDepartments);
        for (Employee e : storage.values()) {
            if (e.getDepartment() != null && !e.getDepartment().trim().isEmpty()) {
                departments.add(e.getDepartment().trim());
            }
        }
        return new ArrayList<>(departments);
    }

    @Override
    public synchronized void addDepartment(String department) {
        if (department != null && !department.trim().isEmpty()) {
            customDepartments.add(department.trim());
            hasUnsavedChanges = true;
        }
    }

    @Override
    public synchronized int count() {
        return storage.size();
    }

    @Override
    public synchronized void clear() {
        storage.clear();
        customDepartments.clear();
        hasUnsavedChanges = true;
    }
}
