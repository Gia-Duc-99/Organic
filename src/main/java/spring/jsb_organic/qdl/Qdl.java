package spring.jsb_organic.qdl;

import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class Qdl {
    public static Boolean NVChuaDangNhap(HttpServletRequest request) {
        if (request.getSession().getAttribute("NhanVien_Id") == null) {
            luuUrlTruocKhiDangNhap(request);
            return true;
        }
        return false;
    }

    // Kiểm tra xem khách hàng đã đăng nhập chưa
    public static Boolean KHChuaDangNhap(HttpServletRequest request) {
        if (request.getSession().getAttribute("KhachHang_Id") == null) {
            luuUrlTruocKhiDangNhap(request);
            return true;
        }
        return false;
    }

    // Phương thức dùng chung để lưu URL trước khi đăng nhập
    private static void luuUrlTruocKhiDangNhap(HttpServletRequest request) {
        String currentURL = request.getRequestURI();

        // Kiểm tra xem currentURL có giá trị hợp lệ không
        if (currentURL == null || currentURL.isEmpty()) {
            currentURL = "/"; // Gán giá trị mặc định nếu currentURL là null
        }

        // Nếu có query string, thêm vào URL
        if (request.getQueryString() != null) {
            currentURL += "?" + request.getQueryString();
        }

        // Lưu URL vào session
        request.getSession().setAttribute("URL_BEFORE_LOGIN", currentURL);
    }
}
