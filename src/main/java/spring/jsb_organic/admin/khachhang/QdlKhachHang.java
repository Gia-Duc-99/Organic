package spring.jsb_organic.admin.khachhang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("admin")
public class QdlKhachHang {
    @Autowired
    private DvlKhachHang dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/khachhang",
            "/khachhang/duyet"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<KhachHang> list = dvl.duyetKH();
        KhachHang dl = new KhachHang();

        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Quảng Cáo");

        model.addAttribute("content", "/admin/khachhang/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/khachhang/them")
    public String getThem(Model model) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        KhachHang dl = new KhachHang();

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/khachhang/them.html");
        return "layout/layout-admin.html";
    }

    @GetMapping("/khachhang/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        KhachHang dl = dvl.xemKH(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/khachhang/xem.html");

        return "admin/khachhang/sua.html";
    }

    @GetMapping("/khachhang/xoa")
    public String getXoa(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        try {
            this.dvl.xoaKH(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/khachhang/duyet";
    }

    @GetMapping("/khachhang/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        KhachHang dl = dvl.xemKH(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/khachhang/xem.html");

        return "admin/khachhang/xem.html";
    }

    @PostMapping("/khachhang/them")
    public String postThem(@ModelAttribute("khachhang") KhachHang dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgayTao(LocalDate.now());
        dl.setNgaySua(LocalDate.now());

        try {
            dvl.luuKH(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc thêm mới!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi thêm mới: " + e.getMessage());
        }

        return "redirect:/admin/khachhang/duyet";
    }

    @PostMapping("/khachhang/sua")
    public String postSua(@ModelAttribute("dl") KhachHang dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgaySua(LocalDate.now());
        try {
            dvl.luuKH(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
        }

        return "redirect:/admin/khachhang/duyet";
    }

    @PostMapping("/khachhang/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaKH(id);
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/khachhang/duyet";
    }
}
