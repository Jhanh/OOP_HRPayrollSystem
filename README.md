# HR Payroll System - Quản Lý Nhân Sự & Bảng Lương

[![Java 17+](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing GUI](https://img.shields.io/badge/GUI-Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![FlatLaf](https://img.shields.io/badge/UI-FlatLaf%203.6.2-008080?style=for-the-badge)](https://www.formdev.com/flatlaf/)
[![JFreeChart](https://img.shields.io/badge/Charts-JFreeChart%201.5.4-orange?style=for-the-badge)](https://www.jfree.org/jfreechart/)
![Profile Views](https://komarev.com/ghpvc/?username=Jhanh-OOP-Nhom08-HRPayrollSystem&label=Views&color=0e75b6&style=for-the-badge)

> **MÔN LẬP TRÌNH HƯỚNG ĐỐI TƯỢNG (INT1332)**  <br>
> **HỌC VIỆN CÔNG NGHỆ BƯU CHÍNH VIỄN THÔNG (PTIT)**  <br>
> **Nhóm thực hiện:** Nhóm 08 <br>
> **Chủ đề:** Quản lý Nhân sự & Bảng lương (*HR Payroll System*)  <br>
> **Công nghệ sử dụng:** Java SE 17+ (LTS) | Java Swing GUI | JFreeChart 1.5.4 | FlatLaf 3.6.2  

---

## 📑 MỤC LỤC
1. [Giới thiệu tổng quan](#1-giới-thiệu-tổng-quan)
2. [Yêu cầu môi trường & Hướng dẫn thiết lập](#2-yêu-cầu-môi-trường--hướng-dẫn-thiết-lập)
3. [Cấu trúc dự án](#3-cấu-trúc-dự-án)
4. [Ánh xạ 4 Nguyên lý OOP cốt lõi vào mã nguồn](#4-ánh-xạ-4-nguyên-lý-oop-cốt-lõi-vào-mã-nguồn)
5. [Quy tắc nghiệp vụ tính Lương & Bảng tính Thuế TNCN](#5-quy-tắc-nghiệp-vụ-tính-lương--bảng-tính-thuế-tncn)
6. [Lưu ý kỹ thuật](#6-lưu-ý-kỹ-thuật)
7. [Bảng phân công nhiệm vụ](#7-bảng-phân-công-nhiệm-vụ)
8. [Bảng kiểm thử mẫu (Test Cases)](#8-bảng-kiểm-thử-mẫu-test-cases)

---

## 1. Giới thiệu tổng quan

Hệ thống **Quản lý Nhân sự & Bảng lương (HR Payroll System)** phục vụ quản trị toàn diện thông tin nhân sự với đa dạng các loại hợp đồng lao động: *Nhân viên toàn thời gian (Full-time)*, *Nhân viên bán thời gian (Part-time)*, *Thực tập sinh (Intern)*, và *Trưởng phòng/Quản lý (Manager)*. Hệ thống tự động hóa:
* Tính toán chi tiết tiền lương, các khoản phụ cấp trách nhiệm, hệ số thưởng và trợ cấp.
* Tính thuế thu nhập cá nhân theo biểu lũy tiến từng phần minh bạch.
* Thống kê và trực quan hóa phân bổ quỹ lương theo phòng ban cùng cơ cấu nhân sự bằng biểu đồ **JFreeChart**.
* Xuất phiếu lương cá nhân chuyên nghiệp (định dạng HTML/TXT) và hỗ trợ sắp xếp bảng dữ liệu đa tiêu chí linh hoạt.
---

## 2. Yêu cầu môi trường & Hướng dẫn thiết lập

### 2.1 Môi trường yêu cầu
* **JDK:** Java Development Kit từ phiên bản 17 LTS trở lên (tương thích hoàn hảo với Java 17, Java 21, Java 25).
* **IDE hỗ trợ:** Eclipse IDE, Visual Studio Code (Java Extension Pack), hoặc IntelliJ IDEA.
* **Thư viện bên ngoài:**
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

## 3. Cấu trúc dự án

Dự án được nhóm thiết kế tinh gọn, bám sát kiến trúc phân tầng (Layered Architecture) với đúng **24 file `.java`**, bảo đảm tính rõ ràng, tách bạch trách nhiệm:

```
OOP_Nhom08_HRPayrollSystem/
├── .gitignore                          # Quy chuẩn các file tạm và thư mục rác bị loại bỏ
├── README.md                           # Tài liệu dự án đầy đủ cho cả nhóm
├── caythumuc.txt                       # Cây thư mục của toàn bộ dự án
├── data/                               # Dữ liệu lưu trữ bền vững (Persistence)
│   ├── employees.csv                   # Cơ sở dữ liệu mẫu mặc định 17 nhân sự chuẩn (tổng quỹ lương 300.150.000 VNĐ)
│   └── test.csv                        # Bộ dữ liệu thử nghiệm tải cao 1.000 nhân sự (stress test & benchmark)
├── docs/                               # Tài liệu báo cáo, đề tài & sơ đồ thiết kế UML
│   ├── Java_OOP_ThuyetTrinh_10Nhom_1.pdf # Đề bài & Hướng dẫn chính thức của Giảng viên
│   ├── UML_ClassDiagram.drawio         # Tệp nguồn vẽ thiết kế lớp chi tiết
│   └── UML_ClassDiagram.png            # Ảnh sơ đồ thiết kế lớp chất lượng cao
├── lib/                                # Các thư viện JAR mở rộng
│   ├── flatlaf-3.6.2.jar               # Giao diện phẳng FlatLaf hiện đại
│   └── jfreechart-1.5.4.jar            # Thư viện vẽ biểu đồ phân tích thống kê
├── resources/
│   └── icons/                          # Bộ icon 32x32 PNG trong suốt
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
        │   └── BaseWindow.java         # [File 02] Lớp trừu tượng cơ sở (Template Method)
        ├── exception/
        │   └── InvalidEmployeeDataException.java # [File 03] Ngoại lệ kiểm tra dữ liệu đầu vào
        ├── model/
        │   ├── Employee.java           # [File 04] Lớp trừu tượng cha chứa thông tin nhân sự
        │   ├── FullTimeEmployee.java   # [File 05] Lớp con: Nhân viên toàn thời gian
        │   ├── Intern.java             # [File 06] Lớp con: Thực tập sinh
        │   ├── Manager.java            # [File 07] Lớp con: Quản lý kế thừa 2 tầng
        │   ├── PartTimeEmployee.java   # [File 08] Lớp con: Nhân viên bán thời gian
        │   ├── TaxCalculator.java      # [File 09] Tiện ích tính thuế TNCN 4 bậc lũy tiến
        │   └── Taxable.java            # [File 10] Interface định nghĩa hành vi chịu thuế
        ├── repository/
        │   ├── EmployeeRepository.java # [File 11] Interface tầng lưu trữ (CRUD, Backup/Restore)
        │   └── InMemoryEmployeeRepository.java # [File 12] Bộ nhớ RAM O(1) đồng bộ CSV UTF-8
        ├── service/
        │   └── PayrollService.java     # [File 13] Dịch vụ tính toán quỹ lương và Stream API
        ├── ui/
        │   ├── MainWindow.java         # [File 14] Cửa sổ chính 2 Tab (Nhân sự & Bảng lương)
        │   ├── dialogs/
        │   │   ├── AboutDialog.java    # [File 15] Hộp thoại hiển thị thông tin đề tài
        │   │   └── EmployeeFormDialog.java # [File 16] Form Thêm/Sửa CardLayout, JSpinner & viền đỏ
        │   └── panels/
        │       ├── EmployeeTableModel.java # [File 17] TableModel hiển thị danh sách hồ sơ nhân viên
        │       ├── PayrollCellRenderer.java # [File 18] Renderer căn lề, định dạng tiền và tô màu Zebra
        │       └── PayrollTableModel.java  # [File 19] TableModel chi tiết lương và dòng TỔNG CỘNG
        └── util/
            ├── ChartHelper.java        # [File 20] Tiện ích tạo BarChart và PieChart JFreeChart
            ├── ChartWindow.java        # [File 21] Cửa sổ hiển thị biểu đồ thời gian thực
            ├── CurrencyFormatUtil.java # [File 22] Tiện ích định dạng tiền tệ VNĐ và ngày tháng
            ├── EmployeeComparator.java # [File 23] Bộ so sánh Comparator tiếng Việt đa tiêu chí
            └── PayslipExporter.java    # [File 24] Tiện ích xuất phiếu cá nhân & báo cáo HTML/CSV
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
1. Nhân viên toàn thời gian (`FullTimeEmployee`):<br>
   &emsp; $$\text{Lương} = (\text{Lương cơ bản} \times \text{Hệ số thưởng}) + \text{Phụ cấp}$$
2. Trưởng phòng / Quản lý (`Manager` - kế thừa `FullTimeEmployee`):<br>
   &emsp; $$\text{Lương Manager} = \text{super.calculateSalary()} + (\text{Số người quản lý} \times 500.000 \text{VNĐ})$$
3. Nhân viên bán thời gian (`PartTimeEmployee`):<br>
   &emsp; $$\text{Lương} = \text{Lương theo giờ} \times \text{Số giờ làm việc thực tế}$$
4. Thực tập sinh (`Intern`):<br>
   &emsp; $$\text{Lương} = \text{Mức trợ cấp cố định}$$

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

* Công thức Thực lĩnh (Net Salary):<br>
  &emsp; $$\text{Thực lĩnh} = \text{Tổng lương} - \text{Thuế TNCN}$$

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
> **4. Cơ chế lưu trữ 2 cấp (Dual-Level Storage) & An toàn dữ liệu:**  
> Mọi thao tác Thêm, Sửa, Xóa được thực hiện tức thì trên bộ nhớ RAM (`LinkedHashMap`) với tốc độ $O(1)$ và bật cờ `hasUnsavedChanges = true` (hiển thị trạng thái `[Chưa lưu *]`). Dữ liệu chỉ được ghi đè xuống tệp đĩa cứng `data/employees.csv` khi người dùng bấm **Lưu (Ctrl+S)**. Khi thoát ứng dụng, hệ thống tự động kiểm tra cờ này để hỏi xác nhận lưu, phòng ngừa mất mát dữ liệu do sơ ý.

> [!TIP]
> **5. Thẻ danh mục phòng ban `#DEPT` độc lập:**  
> Hệ thống duy trì các phòng ban được tạo mới bằng thẻ `#DEPT,Tên phòng ban` trong tệp CSV ngay cả khi phòng ban đó chưa có nhân viên nào, giúp doanh nghiệp thiết lập cơ cấu tổ chức trước khi tuyển dụng mà không bị mất dữ liệu khi khởi động lại ứng dụng.

---

## 7. Bảng phân công nhiệm vụ

| STT | MSSV | Họ và tên&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; | Vai trò | Các lớp Java phụ trách |
| :---: | :---: | :--- | :--- | :--- |
| **1** | N24DCAT040 | Nguyễn Quốc Khánh | Quản trị dự án, Điều phối Git & Khung sườn chính | `Main.java`, `BaseWindow.java`,<br> `MainWindow.java`, `AboutDialog.java` |
| **2** | N24DCAT063 | Trần Vinh Quang | Lớp trừu tượng cha & Ngoại lệ xác thực nghiệp vụ | `Employee.java`,<br> `InvalidEmployeeDataException.java` |
| **3** | N24DCAT059 | Nguyễn Vũ Hoàng Phúc | Các mô hình hợp đồng lao động trực tiếp | `FullTimeEmployee.java`,<br> `PartTimeEmployee.java`,<br> `Intern.java` |
| **4** | N24DCAT024 | Phan Trọng Hạnh | Quản lý cấp cao & Nghiệp vụ tính thuế lũy tiến | `Manager.java`, `Taxable.java`,<br> `TaxCalculator.java` |
| **5** | N24DCAT019 | Hoàng Bá Duy | Tầng lưu trữ dữ liệu & Dịch vụ tính toán quỹ lương | `EmployeeRepository.java`, `InMemoryEmployeeRepository.java`, `PayrollService.java` |
| **6** | N24DCAT035 | Ngô Tiến Huy | Tiện ích định dạng dữ liệu & Model bảng nhân viên | `CurrencyFormatUtil.java`,<br> `EmployeeTableModel.java` |
| **7** | N24DCAT041 | Bùi Hải Khôi | Model bảng lương & Bộ vẽ ô định dạng giao diện | `PayrollTableModel.java`,<br> `PayrollCellRenderer.java` |
| **8** | N24DCAT061 | Nguyễn Ngọc Quân | Hộp thoại tương tác & Form nhập liệu đa hình | `EmployeeFormDialog.java` |
| **9** | N24DCAT039 | Nguyễn Nam Khánh | Cửa sổ phân tích & Biểu đồ thống kê JFreeChart | `ChartWindow.java`,<br> `ChartHelper.java` |
| **10**| N24DCAT036 | Trần Minh Huy | Tính năng BONUS & Sắp xếp đa tiêu chí | `PayslipExporter.java`,<br> `EmployeeComparator.java` |

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
