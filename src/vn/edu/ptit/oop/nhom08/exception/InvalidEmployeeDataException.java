package vn.edu.ptit.oop.nhom02.exception;

//Lop ngoai le tu dinh nghia dung khi du lieu nhan vien nhap vao bi sai hoac khong hop le.

public class InvalidEmployeeDataException extends Exception {
    /**
     * @param message Thong bao chi tiet ve loi du lieu
     */
    public InvalidEmployeeDataException(String message) {
        super(message);
    }
}
