package spring.jsb_organic.admin.nhanvien;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import spring.jsb_organic.qdl.Qdl;

@Controller
@RequestMapping("admin")
public class QdlNhanVien {
    @Autowired
    private DvlNhanVien dvl;

    @Autowired
    private HttpServletRequest request;

    @GetMapping({
            "/nhanvien",
            "/nhanvien/duyet"
    })
    public String getDuyet(Model model) {
        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }
        List<NhanVien> list = dvl.duyetNhanVien();
        model.addAttribute("ds", list);
        NhanVien dl = new NhanVien();
        model.addAttribute("dl", dl);
        model.addAttribute("title", "Quản Lý Nhân Viên");
        model.addAttribute("content", "/admin/nhanvien/duyet.html");
        return "layout/layout-admin.html";
    }

    @GetMapping("/nhanvien/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        NhanVien dl = dvl.xemNhanVien(id);
        model.addAttribute("dl", dl);
        model.addAttribute("content", "/admin/nhanvien/sua.html");
        return "admin/nhanvien/sua.html";
    }

    @GetMapping("/nhanvien/xoa")
    public String getXoa(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        try {
            this.dvl.xoaNhanVien(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi xóa: " + e.getMessage());
        }
        return "redirect:/admin/nhanvien/duyet";
    }

    @GetMapping("/nhanvien/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        NhanVien dl = dvl.xemNhanVien(id);
        model.addAttribute("dl", dl);
        model.addAttribute("content", "/admin/nhanvien/xem.html");
        return "admin/nhanvien/xem.html";
    }

    @PostMapping("/nhanvien/them")
    public String postThem(@ModelAttribute("dl") NhanVien dl, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "layout/layout-admin.html";
        }
        String inputPassword = dl.getMatKhau();

        String hash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
        dl.setMatKhau(hash);
        dl.setNgayTao(LocalDate.now());
        dl.setNgaySua(LocalDate.now());

        try {
            dvl.luuNhanVien(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc thêm mới!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi thêm mới: " + e.getMessage());
        }
        return "redirect:/admin/nhanvien/duyet";
    }

    @GetMapping("/nhanvien/dangnhap")
    public String getDangNhap(Model model, HttpSession session) {
        System.out.println("\n uri before login: " + (String) session.getAttribute("URI_BEFORE_LOGIN"));
        model.addAttribute("dl", new NhanVien());
        model.addAttribute("content", "/admin/nhanvien/dangnhap.html");
        return "layout/layout-admin-login.html";
    }

    @PostMapping("/nhanvien/dangnhap")
    public String postDangNhap(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("TenDangNhap") String TenDangNhap,
            @RequestParam("MatKhau") String MatKhau,
            HttpServletRequest request,
            HttpSession session) {

        String old_password = null;

        if (dvl.tonTaiTenDangNhap(TenDangNhap)) {
            var old_dl = dvl.timNVTheoTenDangNhap(TenDangNhap);
            old_password = old_dl.getMatKhau();

            var mật_khẩu_khớp = BCrypt.checkpw(MatKhau, old_password);

            if (mật_khẩu_khớp) {
                System.out.println("\n Đúng tài khoản, đăng nhập thành công");

                request.getSession().setAttribute("NhanVien_Id", old_dl.getId());
                request.getSession().setAttribute("NhanVien_TenDayDu", old_dl.getTenDayDu());
                request.getSession().setAttribute("NhanVien_AnhDaiDien", old_dl.getAnhDaiDien());
            } else {
                System.out.println("\n Sai mật khẩu");

                redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Sai mật khẩu !");
                return "redirect:/admin/nhanvien/loidangnhap";
            }
        } else {
            System.out.println("\n Không tồn tại tên đăng nhập");
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Sai tên đăng nhập !");
            return "redirect:/admin/nhanvien/loidangnhap";
        }

        var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
        if (uriBeforeLogin == null)
            uriBeforeLogin = "/admin/trangchu";
        return "redirect:" + uriBeforeLogin;
    }

    @GetMapping("/nhanvien/loidangnhap")
    public String loiDangNhap(Model model) {
        model.addAttribute("dl", new NhanVien());
        model.addAttribute("content", "/admin/nhanvien/loidangnhap.html");
        return "layout/layout-admin.html";
    }

    @GetMapping("/nhanvien/dangxuat")
    public String getDangThoat(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/admin/nhanvien/dangnhap";
    }

    @PostMapping("/nhanvien/sua")
    public String postSua(@ModelAttribute("dl") NhanVien dl, RedirectAttributes redirectAttributes) {
        // Kiểm tra nếu mật khẩu không thay đổi
        if (dl.getMatKhau() == null || dl.getMatKhau().isEmpty()) {
            NhanVien existingNv = dvl.xemNhanVien(dl.getId());
            if (existingNv != null) { // Kiểm tra nếu existingNv không phải là null
                dl.setMatKhau(existingNv.getMatKhau()); // Giữ nguyên mật khẩu cũ
            } else {
                redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Không tìm thấy nhân viên với ID: " + dl.getId());
                return "redirect:/admin/nhanvien/duyet"; // Quay lại trang duyệt nếu không tìm thấy
            }
        } else {
            dl.setMatKhau(BCrypt.hashpw(dl.getMatKhau(), BCrypt.gensalt(12)));
        }

        // Chỉnh sửa để lưu nhân viên thay vì thêm mới
        try {
            dvl.luuNhanVien(dl); // Đảm bảo phương thức này thực hiện cập nhật
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/nhanvien/duyet";
    }

    @PostMapping("/nhanvien/xoa")
    public String postXoa(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaNhanVien(id);

        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");
        return "redirect:/admin/nhanvien/duyet";
    }
}
