package vn.edu.ptit.oop.nhom08.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import vn.edu.ptit.oop.nhom08.model.Employee;

// Bộ so sánh nhân viên đa tiêu chí bằng Comparator.comparing(...).thenComparing(...) (BONUS)
public class EmployeeComparator implements Comparator<Employee> {

    public enum Criteria {
        DEPARTMENT,
        EMPLOYEE_TYPE,
        SALARY_DESC,
        SALARY_ASC,
        NAME,
        ID,
        SENIORITY_DESC
    }

    private static final Collator VN_COLLATOR = Collator.getInstance(Locale.forLanguageTag("vi-VN"));

    private final Comparator<Employee> comparatorChain;

    // So sánh mặc định: Phòng ban (A-Z) -> Lương giảm dần -> Họ tên (A-Z)
    public EmployeeComparator() {
        this(Criteria.DEPARTMENT, Criteria.SALARY_DESC, Criteria.NAME);
    }

    // So sánh tùy biến theo danh sách tiêu chí ưu tiên giảm dần
    public EmployeeComparator(Criteria... criteriaList) {
        Comparator<Employee> chain = null;
        if (criteriaList != null && criteriaList.length > 0) {
            for (Criteria c : criteriaList) {
                Comparator<Employee> comp = createComparator(c);
                chain = (chain == null) ? comp : chain.thenComparing(comp);
            }
        }
        this.comparatorChain = (chain != null) ? chain : createComparator(Criteria.ID);
    }

    private static Comparator<Employee> createComparator(Criteria criteria) {
        switch (criteria) {
            case DEPARTMENT:
                return Comparator.comparing(e -> safeString(e.getDepartment()), VN_COLLATOR);
            case EMPLOYEE_TYPE:
                return Comparator.comparing(e -> safeString(e.getEmployeeType()), VN_COLLATOR);
            case SALARY_DESC:
                return Comparator.comparingDouble(Employee::calculateSalary).reversed();
            case SALARY_ASC:
                return Comparator.comparingDouble(Employee::calculateSalary);
            case NAME:
                return byName();
            case SENIORITY_DESC:
                return Comparator.comparingInt(Employee::getSeniorityYears).reversed();
            case ID:
            default:
                return Comparator.comparing(e -> safeString(e.getId()));
        }
    }

    private static String safeString(String s) {
        return (s == null) ? "" : s;
    }

    @Override
    public int compare(Employee o1, Employee o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        return comparatorChain.compare(o1, o2);
    }

    // Bộ so sánh chuẩn: Phòng ban -> Lương giảm dần -> Họ tên (chuẩn Việt Nam)
    public static Comparator<Employee> defaultChain() {
        return Comparator.comparing((Employee e) -> safeString(e.getDepartment()), VN_COLLATOR)
                .thenComparing(Comparator.comparingDouble(Employee::calculateSalary).reversed())
                .thenComparing(byName());
    }

    // Bộ so sánh theo lương giảm dần -> Thâm niên giảm dần
    public static Comparator<Employee> bySalaryDescending() {
        return Comparator.comparingDouble(Employee::calculateSalary).reversed()
                .thenComparing(Comparator.comparingInt(Employee::getSeniorityYears).reversed());
    }

    // Bộ so sánh theo mã nhân viên (A-Z)
    public static Comparator<Employee> byId() {
        return Comparator.comparing(e -> safeString(e.getId()));
    }

    // Trích xuất Tên chính (từ cuối cùng) chuẩn tiếng Việt
    public static String getFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String trimmed = fullName.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace >= 0) {
            return trimmed.substring(lastSpace + 1);
        }
        return trimmed;
    }

    // Trích xuất Họ và Tên đệm (phần trước từ cuối cùng) chuẩn tiếng Việt
    public static String getLastNameAndMiddle(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String trimmed = fullName.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace >= 0) {
            return trimmed.substring(0, lastSpace);
        }
        return "";
    }

    // Bộ so sánh theo họ tên chuẩn Việt Nam: Ưu tiên Tên (A-Z) -> Trùng Tên so sánh Họ & Tên đệm
    public static Comparator<Employee> byName() {
        return Comparator.comparing((Employee e) -> getFirstName(e.getFullName()), VN_COLLATOR)
                .thenComparing((Employee e) -> getLastNameAndMiddle(e.getFullName()), VN_COLLATOR);
    }

    // Bộ so sánh theo mức lương (Thấp đến Cao)
    public static Comparator<Employee> bySalary() {
        return Comparator.comparingDouble(Employee::calculateSalary);
    }

    // Bộ so sánh theo thâm niên (Ít năm đến Nhiều năm)
    public static Comparator<Employee> bySeniority() {
        return Comparator.comparingInt(Employee::getSeniorityYears);
    }

    // Bộ so sánh theo phòng ban tiếng Việt (A-Z)
    public static Comparator<Employee> byDepartment() {
        return Comparator.comparing((Employee e) -> safeString(e.getDepartment()), VN_COLLATOR)
                .thenComparing(e -> safeString(e.getId()));
    }

    // Bộ so sánh theo loại nhân viên tiếng Việt (A-Z)
    public static Comparator<Employee> byEmployeeType() {
        return Comparator.comparing((Employee e) -> safeString(e.getEmployeeType()), VN_COLLATOR)
                .thenComparing(e -> safeString(e.getId()));
    }
}
