package spring.jsb_organic.qdl;

import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class Qdl {
    public static Boolean NVChuaDangNhap(HttpServletRequest request){
        if (request.getSession().getAttribute("NhanVien_Id") == null) {
            String currentURL = request.getRequestURI();
            
            // Kiểm tra xem currentURL có giá trị hợp lệ không
            if (currentURL == null || currentURL.isEmpty()) {
                currentURL = "/"; // Gán giá trị mặc định nếu currentURL là null
            }

            if (request.getQueryString() != null) {
                currentURL += "?" + request.getQueryString();
            }
            request.getSession().setAttribute("URL_BEFORE_LOGIN", currentURL);
            return true;
        }
        return false;
    }
    
}
