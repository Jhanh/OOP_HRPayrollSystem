package vn.edu.ptit.oop.nhom08.exception;

// Ngoại lệ khi dữ liệu nhân viên không hợp lệ
public class InvalidEmployeeDataException extends Exception {

    public InvalidEmployeeDataException(String message) {
        super(message);
    }

    public InvalidEmployeeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
