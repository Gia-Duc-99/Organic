package spring.jsb_organic.admin.sanpham;

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
public class QdlSanPham {
    @Autowired
    private DvlSanPham dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/sanpham",
            "/sanpham/duyet"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<SanPham> list = dvl.duyetSP();
        SanPham dl = new SanPham();

        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Sản Phẩm");

        model.addAttribute("content", "/admin/sanpham/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/sanpham/them")
    public String getThem(Model model) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        SanPham dl = new SanPham();

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/sanpham/them.html");
        return "layout/layout-admin.html";
    }

    @GetMapping("/sanpham/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        SanPham dl = dvl.xemSP(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/sanpham/xem.html");

        return "admin/sanpham/sua.html";
    }

    @GetMapping("/sanpham/xoa")
    public String getXoa(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        try {
            this.dvl.xoaSP(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/sanpham/duyet";
    }

    @GetMapping("/sanpham/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        SanPham dl = dvl.xemSP(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/sanpham/xem.html");

        return "admin/sanpham/xem.html";
    }

    @PostMapping("/sanpham/them")
    public String postThem(@ModelAttribute("sanpham") SanPham dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgayTao(LocalDate.now());
        dl.setNgaySua(LocalDate.now());

        try {
            dvl.luuSP(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc thêm mới!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi thêm mới: " + e.getMessage());
        }

        return "redirect:/admin/sanpham/duyet";
    }

    @PostMapping("/sanpham/sua")
    public String postSua(@ModelAttribute("dl") SanPham dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgaySua(LocalDate.now());
        try {
            dvl.luuSP(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
        }

        return "redirect:/admin/sanpham/duyet";
    }

    @PostMapping("/sanpham/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaSP(id);
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/sanpham/duyet";
    }
}

