# OOP_Nhom08_HRPayrollSystem - Hệ Thống Quản Lý Nhân Sự & Bảng Lương

> **BÀI TẬP LỚN MÔN LẬP TRÌNH HƯỚNG ĐỐI TƯỢNG (INT1332)**  
> **HỌC VIỆN CÔNG NGHỆ BƯU CHÍNH VIỄN THÔNG (PTIT)**  
> **Nhóm thực hiện:** Nhóm 08 (Gồm 10 thành viên)  
> **Chủ đề lựa chọn (Đề số 02):** Quản lý Nhân sự & Bảng lương (*HR Payroll System*)  
> **Công nghệ sử dụng:** Java SE 17+ (LTS) | Java Swing GUI | JFreeChart 1.5.4 | FlatLaf 3.6.2  

---

## 📑 MỤC LỤC
1. [Giới thiệu tổng quan & Định hướng dự án](#1-giới-thiệu-tổng-quan--định-hướng-dự-án)
2. [Yêu cầu môi trường & Hướng dẫn thiết lập](#2-yêu-cầu-môi-trường--hướng-dẫn-thiết-lập)
3. [Cấu trúc dự án & Chi tiết 24 File `.java` chuẩn mực](#3-cấu-trúc-dự-án--chi-tiết-24-file-java-chuẩn-mực)
4. [Ánh xạ 4 Nguyên lý OOP cốt lõi vào mã nguồn](#4-ánh-xạ-4-nguyên-lý-oop-cốt-lõi-vào-mã-nguồn)
5. [Quy tắc nghiệp vụ tính Lương & Bảng tính Thuế TNCN](#5-quy-tắc-nghiệp-vụ-tính-lương--bảng-tính-thuế-tncn)
6. [Lưu ý kỹ thuật sống còn (Trọng tâm phản biện)](#6-lưu-ý-kỹ-thuật-sống-còn-trọng-tâm-phản-biện)
7. [Bảng phân công nhiệm vụ cho 10 thành viên](#7-bảng-phân-công-nhiệm-vụ-cho-10-thành-viên)
8. [Bảng kiểm thử mẫu (Test Cases)](#8-bảng-kiểm-thử-mẫu-test-cases)
9. [Quy chuẩn nộp sản phẩm & Quản trị Git](#9-quy-chuẩn-nộp-sản-phẩm--quản-trị-git)

---

## 1. Giới thiệu tổng quan & Định hướng dự án

Hệ thống **Quản lý Nhân sự & Bảng lương (HR Payroll System)** phục vụ quản trị toàn diện thông tin nhân sự với đa dạng các loại hợp đồng lao động: *Nhân viên toàn thời gian (Full-time)*, *Nhân viên bán thời gian (Part-time)*, *Thực tập sinh (Intern)*, và *Trưởng phòng/Quản lý (Manager)*. Hệ thống tự động hóa:
* Tính toán chi tiết tiền lương, các khoản phụ cấp trách nhiệm, hệ số thưởng và trợ cấp.
* Tính thuế thu nhập cá nhân (TNCN) theo biểu lũy tiến từng phần minh bạch.
* Thống kê và trực quan hóa phân bổ quỹ lương theo phòng ban cùng cơ cấu nhân sự bằng biểu đồ **JFreeChart**.
* Xuất phiếu lương cá nhân chuyên nghiệp (định dạng HTML/TXT) và hỗ trợ sắp xếp bảng dữ liệu đa tiêu chí linh hoạt.
---

## 2. Yêu cầu môi trường & Hướng dẫn thiết lập

### 2.1 Môi trường yêu cầu
* **JDK:** Java Development Kit từ phiên bản 17 LTS trở lên (tương thích hoàn hảo với Java 17, Java 21, Java 25).
* **IDE hỗ trợ:** Eclipse IDE, Visual Studio Code (Java Extension Pack), hoặc IntelliJ IDEA.
* **Thư viện bên ngoài (Đặt sẵn trong thư mục `lib/`):**
  * `lib/jfreechart-1.5.4.jar`: Thư viện vẽ biểu đồ phân tích thống kê (BarChart & PieChart).
  * `lib/flatlaf-3.6.2.jar`: Look and Feel hiện đại, tối ưu giao diện phẳng và hiển thị High-DPI.

---

### 2.2 Hướng dẫn cấu hình thư viện `lib/` trong các IDE

#### A. Cấu hình trên Visual Studio Code
Trong thư mục `.vscode/settings.json`, các thành viên cần đảm bảo khai báo đường dẫn thư viện ngoài như sau:
```json
{
    "java.project.referencedLibraries": [
        "lib/*.jar"
    ]
}
```
*(Nếu thiếu cấu hình này, VS Code sẽ báo lỗi không tìm thấy package của JFreeChart và FlatLaf).*

#### B. Cấu hình trên Eclipse IDE:
1. Chuột phải vào project `OOP_Nhom08_HRPayrollSystem` › chọn **Properties**.
2. Chọn **Java Build Path** › chuyển sang tab **Libraries**.
3. Chọn mục **Classpath** › bấm nút **Add External JARs...**.
4. Trỏ tới thư mục `lib/` trong project, chọn cả 2 file `flatlaf-3.6.2.jar` và `jfreechart-1.5.4.jar` › bấm **Apply and Close**.

#### C. Biên dịch và thực thi bằng dòng lệnh (Terminal / PowerShell):
```powershell
# 1. Chuyển vào thư mục gốc của project
cd d:\OOP_Nhom08_HRPayrollSystem

# 2. Biên dịch toàn bộ mã nguồn vào thư mục bin/
javac -encoding UTF-8 -cp "lib/*" -d bin src/vn/edu/ptit/oop/nhom08/model/*.java src/vn/edu/ptit/oop/nhom08/exception/*.java src/vn/edu/ptit/oop/nhom08/repository/*.java src/vn/edu/ptit/oop/nhom08/service/*.java src/vn/edu/ptit/oop/nhom08/util/*.java src/vn/edu/ptit/oop/nhom08/common/*.java src/vn/edu/ptit/oop/nhom08/ui/panels/*.java src/vn/edu/ptit/oop/nhom08/ui/dialogs/*.java src/vn/edu/ptit/oop/nhom08/ui/*.java src/vn/edu/ptit/oop/nhom08/*.java

# 3. Khởi chạy ứng dụng
java -cp "bin;lib/*" vn.edu.ptit.oop.nhom08.Main
```

---

## 3. Cấu trúc dự án & Chi tiết 24 File `.java` chuẩn mực

Dự án được nhóm thiết kế tinh gọn, bám sát kiến trúc phân tầng (Layered Architecture) với đúng **24 file `.java`**, bảo đảm tính rõ ràng, tách bạch trách nhiệm:

```
OOP_Nhom08_HRPayrollSystem/
├── .gitignore                          # Quy chuẩn các file tạm và thư mục rác bị loại bỏ
├── .vscode/
│   └── settings.json                   # Cấu hình môi trường VS Code
├── README.md                           # Tài liệu dự án đầy đủ cho cả nhóm
├── caythumuc.txt                       # Cây thư mục của toàn bộ dự án
├── docs/                               # Tài liệu báo cáo, sơ đồ UML & ảnh chụp giao diện
│   ├── screenshots/                    # Ảnh chụp màn hình chức năng chạy thực tế
│   ├── UML_ClassDiagram.drawio         # File nguồn vẽ thiết kế lớp
│   └── UML_ClassDiagram.png            # File ảnh sơ đồ lớp nhúng vào báo cáo
├── lib/                                # Các thư viện JAR mở rộng
│   ├── flatlaf-3.6.2.jar
│   └── jfreechart-1.5.4.jar
├── resources/
│   └── icons/                          # Bộ 6 icon chuẩn kích thước 32x32 PNG trong suốt
│       ├── add.png                     # Nút Thêm nhân viên
│       ├── app.png                     # Biểu tượng cửa sổ ứng dụng (Title bar icon)
│       ├── chart.png                   # Nút Mở cửa sổ biểu đồ phân tích
│       ├── delete.png                  # Nút Xóa nhân viên
│       ├── edit.png                    # Nút Cập nhật/Sửa nhân viên
│       └── export.png                  # Nút Xuất phiếu lương (Payslip)
└── src/
    └── vn/edu/ptit/oop/nhom08/
        ├── Main.java                   # [File 01] Điểm vào chương trình, khởi động Swing EDT
        ├── common/
        │   └── BaseWindow.java         # [File 02] Lớp trừu tượng bắt buộc của MainWindow
        ├── exception/
        │   └── InvalidEmployeeDataException.java # [File 03] Ngoại lệ kiểm tra dữ liệu nhập liệu
        ├── model/
        │   ├── Employee.java           # [File 04] Lớp trừu tượng cha chứa thông tin nhân sự cốt lõi
        │   ├── FullTimeEmployee.java   # [File 05] Lớp con: Nhân viên toàn thời gian (implements Taxable)
        │   ├── Intern.java             # [File 06] Lớp con: Thực tập sinh (Không implement Taxable)
        │   ├── Manager.java            # [File 07] Lớp con kế thừa 2 tầng từ FullTimeEmployee
        │   ├── PartTimeEmployee.java   # [File 08] Lớp con: Nhân viên bán thời gian (tính theo giờ)
        │   ├── TaxCalculator.java      # [File 09] Lớp nghiệp vụ tính toán thuế TNCN theo bậc lũy tiến
        │   └── Taxable.java            # [File 10] Interface định nghĩa hành vi tính thuế
        ├── repository/
        │   ├── EmployeeRepository.java # [File 11] Interface tầng lưu trữ (CRUD nhân sự)
        │   └── InMemoryEmployeeRepository.java # [File 12] Cài đặt lưu trữ bộ nhớ sẵn >= 15 nhân viên mẫu
        ├── service/
        │   └── PayrollService.java     # [File 13] Dịch vụ tính toán quỹ lương, gom nhóm theo phòng ban
        ├── ui/
        │   ├── MainWindow.java         # [File 14] Cửa sổ chính chứa JTabbedPane (Nhân viên & Bảng lương)
        │   ├── dialogs/
        │   │   ├── AboutDialog.java    # [File 15] Hộp thoại hiển thị thông tin nhóm 8 & đề tài
        │   │   └── EmployeeFormDialog.java # [File 16] Hộp thoại Thêm/Sửa dùng CardLayout & JSpinner
        │   └── panels/
        │       ├── EmployeeTableModel.java # [File 17] TableModel hiển thị danh sách hồ sơ nhân viên
        │       ├── PayrollCellRenderer.java # [File 18] Renderer căn lề, định dạng tiền và tô màu bảng
        │       └── PayrollTableModel.java  # [File 19] TableModel hiển thị chi tiết tiền lương và dòng tổng
        └── util/
            ├── ChartHelper.java        # [File 20] Tiện ích tạo BarChart và PieChart bằng JFreeChart
            ├── ChartWindow.java        # [File 21] Cửa sổ hiển thị biểu đồ (DISPOSE_ON_CLOSE)
            ├── CurrencyFormatUtil.java # [File 22] Tiện ích định dạng tiền tệ Việt Nam (VNĐ) và ngày tháng
            ├── EmployeeComparator.java # [File 23] Bộ so sánh Comparator đa tiêu chí (BONUS)
            └── PayslipExporter.java    # [File 24] Tiện ích xuất phiếu lương ra file HTML/TXT (BONUS)
```

---

## 4. Ánh xạ 4 Nguyên lý OOP cốt lõi vào mã nguồn

| Nguyên lý OOP | Hiện thực cụ thể trong bài toán | File mã nguồn áp dụng |
| :--- | :--- | :--- |
| **Đóng gói (Encapsulation)** | Toàn bộ thuộc tính nghiệp vụ (`id`, `fullName`, `department`, `baseSalary`, `hireDate`, `allowance`, `hourlyRate`...) đều ở mức truy cập `private`. Kiểm tra hợp lệ nghiêm ngặt qua Setter/Constructor (không cho phép họ tên rỗng, lương cơ bản âm, hệ số nhỏ hơn 1). Không để lộ danh sách nội bộ của repository mà trả về bản sao hoặc `Collections.unmodifiableList()`. | `Employee.java`<br>`FullTimeEmployee.java`<br>`InMemoryEmployeeRepository.java` |
| **Kế thừa (Inheritance)** | **Kế thừa nghiệp vụ 2 tầng:** `Manager` kế thừa `FullTimeEmployee`, `FullTimeEmployee` kế thừa `Employee`. Đồng thời `PartTimeEmployee` và `Intern` cùng kế thừa `Employee`.<br>**Kế thừa giao diện Swing:** `MainWindow extends BaseWindow`, `EmployeeFormDialog extends JDialog`, `EmployeeTableModel extends AbstractTableModel`, `PayrollCellRenderer extends DefaultTableCellRenderer`. | `Employee.java`<br>`FullTimeEmployee.java`<br>`Manager.java`<br>`PartTimeEmployee.java`<br>`Intern.java`<br>`MainWindow.java` |
| **Đa hình (Polymorphism)** | Phương thức trừu tượng `calculateSalary()` được gọi đồng nhất qua tham chiếu kiểu cha `List<Employee>`. Trong `Manager`, lời gọi `super.calculateSalary()` thể hiện rõ tính tái sử dụng hành vi lớp cha và mở rộng thêm phụ cấp trách nhiệm. Interface `Taxable` được thực thi linh hoạt qua `calculateTax()`. `PayrollCellRenderer` ghi đè `getTableCellRendererComponent()`. Không sử dụng `instanceof` hay switch-case thủ công theo kiểu dữ liệu. | `PayrollService.java`<br>`Manager.java`<br>`Taxable.java`<br>`PayrollCellRenderer.java` |
| **Trừu tượng (Abstraction)** | Lớp trừu tượng `Employee` chứa phương thức trừu tượng `calculateSalary()` và phương thức bất biến `final int getSeniorityYears()`. Lớp trừu tượng bắt buộc `BaseWindow` áp dụng mẫu thiết kế **Template Method** với `initUI()`. Các interface độc lập `Taxable` và `EmployeeRepository` trừu tượng hóa tầng tính thuế và tầng thao tác dữ liệu. | `Employee.java`<br>`BaseWindow.java`<br>`Taxable.java`<br>`EmployeeRepository.java` |
| **Kết hợp (Composition)** | `MainWindow` chứa thanh menu, thanh công cụ, các bảng dữ liệu `JTable`, và `PayrollService`. `PayrollService` chứa tham chiếu tới `EmployeeRepository`. `Employee` kết hợp với thông tin ngày tuyển dụng `LocalDate`. | `MainWindow.java`<br>`PayrollService.java` |
| **Xử lý Ngoại lệ (Exception)** | Lớp ngoại lệ kiểm tra tự định nghĩa `InvalidEmployeeDataException extends Exception` được ném ra khi người dùng nhập dữ liệu không hợp lệ tại `EmployeeFormDialog` và được bắt lại, hiển thị hộp thoại `JOptionPane.showMessageDialog` thông báo lỗi thân thiện, bảo đảm chương trình không bị văng (`crash`). | `InvalidEmployeeDataException.java`<br>`EmployeeFormDialog.java` |

---

## 5. Quy tắc nghiệp vụ tính Lương & Bảng tính Thuế TNCN

### 5.1 Công thức tính tiền lương theo loại hợp đồng
1. **Nhân viên toàn thời gian (`FullTimeEmployee`):**
   $$\text{Lương} = (\text{Lương cơ bản} \times \text{Hệ số thưởng}) + \text{Phụ cấp}$$
2. **Trưởng phòng / Quản lý (`Manager` - kế thừa `FullTimeEmployee`):**
   $$\text{Lương Manager} = \text{super.calculateSalary()} + (\text{Số người quản lý} \times 500.000\,\text{VNĐ})$$
3. **Nhân viên bán thời gian (`PartTimeEmployee`):**
   $$\text{Lương} = \text{Lương theo giờ} \times \text{Số giờ làm việc thực tế}$$
4. **Thực tập sinh (`Intern`):**
   $$\text{Lương} = \text{Mức trợ cấp cố định}$$

---

### 5.2 Biểu tính thuế Thu nhập Cá nhân (TNCN) lũy tiến từng phần
* **Phạm vi áp dụng:** Chỉ dành cho nhân viên chính thức (`FullTimeEmployee` và `Manager` kế thừa `Taxable`).
* **Đặc thù nghiệp vụ:** `Intern` (Thực tập sinh) và `PartTimeEmployee` **không áp dụng** biểu tính thuế này (`Intern` tuyệt đối không implement `Taxable`, mức thuế bằng 0 VNĐ theo đúng quy định đề tài).
* **Bảng biểu thuế TNCN lũy tiến áp dụng trong ứng dụng:**

| Bậc thuế | Phần thu nhập tính thuế trong tháng (VNĐ) | Thuế suất | Số tiền thuế tính theo từng bậc |
| :---: | :--- | :---: | :--- |
| **Bậc 1** | Đến $10.000.000$ VNĐ | **0%** | $0$ VNĐ |
| **Bậc 2** | Trên $10.000.000$ đến $20.000.000$ VNĐ | **5%** | Tối đa $500.000$ VNĐ |
| **Bậc 3** | Trên $20.000.000$ đến $40.000.000$ VNĐ | **10%** | Tối đa $2.000.000$ VNĐ |
| **Bậc 4** | Trên $40.000.000$ VNĐ | **20%** | $20\%$ cho phần vượt trên $40.000.000$ VNĐ |

* **Công thức Thực lĩnh (Net Salary):**
  $$\text{Thực lĩnh} = \text{Tổng lương} - \text{Thuế TNCN}$$

---

## 6. Lưu ý kỹ thuật

> [!CAUTION]
> **1. Bẫy khởi tạo trong `BaseWindow`:**  
> Trong constructor của `BaseWindow`, phương thức trừu tượng `initUI()` được gọi trước khi constructor của `MainWindow` hoàn tất.  
> Nếu khai báo thuộc tính trong `MainWindow` theo kiểu:  
> `private EmployeeRepository repo = new InMemoryEmployeeRepository();`  
> thì sau khi `initUI()` chạy xong, thuộc tính này mới được gán giá trị mới, dẫn tới nguy cơ ghi đè dữ liệu hoặc văng lỗi `NullPointerException` bên trong `initUI()`.  
> **Giải pháp chuẩn:** Không gán giá trị tại nơi khai báo trường; luôn khởi tạo `repo` và các thành phần giao diện bên trong thân phương thức `initUI()`.

> [!IMPORTANT]
> **2. Chế độ đóng cửa sổ biểu đồ con `ChartWindow`:**  
> Lớp `ChartWindow extends JFrame` **bắt buộc phải sử dụng:**  
> `setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);`  
> Tuyệt đối không được dùng `EXIT_ON_CLOSE` vì khi người dùng tắt cửa sổ biểu đồ sẽ làm dừng luôn toàn bộ tiến trình ứng dụng chính.

> [!TIP]
> **3. Khắc phục lỗi tiếng Việt trên biểu đồ JFreeChart:**  
> Để biểu đồ không bị lỗi hiển thị ký tự ô vuông `[]` khi dùng tiếng Việt có dấu, phải cấu hình Font `Segoe UI` cho Chart Theme một lần duy nhất tại `Main.java` trước khi hiển thị biểu đồ:
> ```java
> StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
> theme.setExtraLargeFont(new Font("Segoe UI", Font.BOLD, 18)); // Tiêu đề
> theme.setLargeFont(new Font("Segoe UI", Font.BOLD, 14));      // Nhãn trục X, Y
> theme.setRegularFont(new Font("Segoe UI", Font.PLAIN, 12));   // Chú giải & số liệu
> ChartFactory.setChartTheme(theme);
> ```

> [!IMPORTANT]
> **4. Phản hồi mượt mà trên Event Dispatch Thread (EDT):**  
> Tuyệt đối không chạy tác vụ ghi file lớn hoặc `Thread.sleep()` trực tiếp bên trong `actionPerformed()` nhằm tránh làm "đơ" giao diện người dùng. Sử dụng `SwingWorker` hoặc luồng phụ trợ khi cần xử lý tệp tin.

---

## 7. Bảng phân công nhiệm vụ cho 10 thành viên

Bảng phân công công việc được chuẩn hóa theo đúng kiến trúc mô đun của hệ thống, bảo đảm tính gắn kết cao giữa các lớp và phân định rõ trách nhiệm cho từng thành viên:

|  STT  |    MSSV    |       Họ và tên        |                        Vai trò                        |                                Các lớp Java phụ trách                                |
| :---: | :--------: | :--------------------: | :---------------------------------------------------: | :----------------------------------------------------------------------------------: |
| **1** | N24DCAT040 |  *Nguyễn Quốc Khánh*   |   Quản trị dự án, Điều phối Git & Khung sườn chính    |         `Main.java`, `BaseWindow.java`, `MainWindow.java`, `AboutDialog.java`        |
| **2** | N24DCAT063 |   *Trần Vinh Quang*    |    Lớp trừu tượng cha & Ngoại lệ xác thực nghiệp vụ   |                 `Employee.java`, `InvalidEmployeeDataException.java`                 |
| **3** | N24DCAT059 | *Nguyễn Vũ Hoàng Phúc* |        Các mô hình hợp đồng lao động trực tiếp        |            `FullTimeEmployee.java`, `PartTimeEmployee.java`, `Intern.java`           |
| **4** | N24DCAT024 |   *Phan Trọng Hạnh*    |    Quản lý cấp cao & Nghiệp vụ tính thuế lũy tiến     |                 `Manager.java`, `Taxable.java`, `TaxCalculator.java`                 |
| **5** | N24DCAT019 |     *Hoàng Bá Duy*     |   Tầng lưu trữ dữ liệu & Dịch vụ tính toán quỹ lương  | `EmployeeRepository.java`, `InMemoryEmployeeRepository.java`, `PayrollService.java` |
| **6** | N24DCAT035 |     *Ngô Tiến Huy*     |   Tiện ích định dạng dữ liệu & Model bảng nhân viên   |                 `CurrencyFormatUtil.java`, `EmployeeTableModel.java`                 |
| **7** | N24DCAT041 |     *Bùi Hải Khôi*     |    Model bảng lương & Bộ vẽ ô định dạng giao diện     |                 `PayrollTableModel.java`, `PayrollCellRenderer.java`                 |
| **8** | N24DCAT061 |   *Nguyễn Ngọc Quân*   |     Hộp thoại tương tác & Form nhập liệu đa hình      |                              `EmployeeFormDialog.java`                               |
| **9** | N24DCAT039 |   *Nguyễn Nam Khánh*   |     Cửa sổ phân tích & Biểu đồ thống kê JFreeChart    |                        `ChartWindow.java`, `ChartHelper.java`                         |
| **10**| N24DCAT036 |    *Trần Minh Huy*     |    Tính năng cộng điểm BONUS & Sắp xếp đa tiêu chí    |                      `PayslipExporter.java`, `EmployeeComparator.java`               |

---

## 8. Bảng kiểm thử mẫu (Test Cases)

Đáp ứng đầy đủ yêu cầu kiểm thử dữ liệu biên và dữ liệu lỗi theo quy định Báo cáo kỹ thuật:

| Mã Test | Mô tả ca kiểm thử | Dữ liệu đầu vào (Input) | Kết quả kỳ vọng (Expected Output) | Kết quả thực tế | Đánh giá |
| :---: | :--- | :--- | :--- | :---: | :---: |
| **TC01** | Thêm mới FullTime hợp lệ | Tên: "Lê An", Lương: 15tr, HS: 1.2, PC: 2tr | Thêm thành công, hiển thị ở bảng và cập nhật status bar | Đạt | Pass |
| **TC02** | Bỏ trống họ tên nhân viên | Họ tên: "" (chuỗi rỗng), Lương: 10tr | Báo lỗi: "Họ và tên không được để trống!" | Đạt | Pass |
| **TC03** | Nhập lương cơ bản âm | Lương cơ bản: -2.000.000 VNĐ | Ném `InvalidEmployeeDataException`: Lương phải > 0 | Đạt | Pass |
| **TC04** | Tính lương Manager | Cơ bản 20tr, HS 1.0, Quản lý 4 người | Lương = 20tr + (4 * 500k) = 22tr VNĐ | Đạt | Pass |
| **TC05** | Miễn thuế cho Intern | Intern nhận trợ cấp 4tr VNĐ | Cột Thuế TNCN = 0 VNĐ, Thực lĩnh = 4tr VNĐ | Đạt | Pass |
| **TC06** | Tính thuế lũy tiến Bậc 2 | Thu nhập chịu thuế: 18.000.000 VNĐ | Thuế = (18tr - 10tr) * 5% = 400.000 VNĐ | Đạt | Pass |
| **TC07** | Tính thuế lũy tiến Bậc 3 | Thu nhập chịu thuế: 30.000.000 VNĐ | Thuế = 500k + (30tr - 20tr) * 10% = 1.500.000 VNĐ | Đạt | Pass |
| **TC08** | Lọc theo phòng ban | Chọn JComboBox "IT" | JTable chỉ hiển thị các nhân viên thuộc phòng IT | Đạt | Pass |
| **TC09** | Xóa nhân viên đã chọn | Chọn 1 dòng và bấm nút Xóa | Hộp thoại xác nhận, xóa thành công, quỹ lương giảm | Đạt | Pass |
| **TC10** | Xuất phiếu lương BONUS | Chọn nhân viên bất kỳ và bấm Xuất | Sinh file `payslip_NVxxx.html` định dạng chuẩn | Đạt | Pass |

---

## 9. Quy chuẩn nộp sản phẩm & Quản trị Git

### 9.1 Cấu trúc gói nộp sản phẩm cuối cùng (`OOP_Nhom08_HRPayrollSystem.zip`)
```
OOP_Nhom08_HRPayrollSystem.zip
├── source/
├── app/
│   └── OOP_Nhom08.jar
├── report/
│   └── BaoCao_Nhom08.pdf
├── slides/
│   └── Slide_Nhom08.pptx
├── docs/
│   ├── UML_ClassDiagram.drawio
│   └── screenshots/
└── README.md
```

### 9.2 Quy ước Commit trên Git
Mọi thành viên thực hiện commit thường xuyên theo cú pháp Conventional Commits:
* `feat: <nội dung>`: Thêm lớp mới hoặc chức năng mới (Ví dụ: `feat: implement TaxCalculator logic`).
* `fix: <nội dung>`: Khắc phục lỗi hoặc sửa giao diện (Ví dụ: `fix: correct status bar update on employee removal`).
* `docs: <nội dung>`: Cập nhật tài liệu, README hoặc sơ đồ UML.
* `refactor: <nội dung>`: Tối ưu cấu trúc code mà không thay đổi nghiệp vụ.
