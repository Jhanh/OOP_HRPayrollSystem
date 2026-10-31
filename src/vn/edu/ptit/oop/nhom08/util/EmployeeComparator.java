package vn.edu.ptit.oop.nhom08.util;

public class EmployeeComparator {
    
}
package vn.edu.ptit.oop.nhom08.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import vn.edu.ptit.oop.nhom08.model.Employee;

/**
 * Bộ so sánh nhân viên đa tiêu chí (BONUS).
 * Mặc định: Phòng ban (A-Z) -> Lương (giảm dần) -> Tên (A-Z).
 */
public class EmployeeComparator implements Comparator<Employee> {

    public enum Criteria { DEPARTMENT, SALARY, NAME, ID }

    private static final Collator VN = Collator.getInstance(new Locale("vi", "VN"));

    private final Comparator<Employee> chain;

    /** So sánh mặc định: phòng ban -> lương giảm dần -> tên. */
    public EmployeeComparator() {
        this(Criteria.DEPARTMENT, Criteria.SALARY, Criteria.NAME);
    }

    /** So sánh theo thứ tự tiêu chí tùy chọn (tiêu chí đứng trước ưu tiên hơn). */
    public EmployeeComparator(Criteria... criteria) {
        Comparator<Employee> result = null;
        for (Criteria c : criteria) {
            Comparator<Employee> next = byCriteria(c);
            result = (result == null) ? next : result.thenComparing(next);
        }
        this.chain = (result != null) ? result : byCriteria(Criteria.ID);
    }

    private static Comparator<Employee> byCriteria(Criteria c) {
        switch (c) {
            case DEPARTMENT:
                return (a, b) -> VN.compare(nvl(a.getDepartment()), nvl(b.getDepartment()));
            case SALARY: // giảm dần
                return (a, b) -> Double.compare(b.calculateSalary(), a.calculateSalary());
            case NAME:
                return (a, b) -> VN.compare(nvl(a.getFullName()), nvl(b.getFullName()));
            case ID:
            default:
                return (a, b) -> nvl(a.getId()).compareTo(nvl(b.getId()));
        }
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    @Override
    public int compare(Employee a, Employee b) {
        return chain.compare(a, b);
    }

    /** Đảo ngược thứ tự sắp xếp. */
    public Comparator<Employee> descending() {
        return chain.reversed();
    }
}
