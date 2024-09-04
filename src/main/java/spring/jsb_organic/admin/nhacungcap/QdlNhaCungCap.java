package spring.jsb_organic.admin.nhacungcap;

import java.time.LocalDate;
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

@Controller
@RequestMapping("admin")
public class QdlNhaCungCap {
    @Autowired
    private DvlNhaCungCap dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/nhacungcap",
            "/nhacungcap/duyet"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<NhaCungCap> list = dvl.duyetNCC();
        NhaCungCap dl = new NhaCungCap();

        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Quảng Cáo");

        model.addAttribute("content", "/admin/nhacungcap/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/nhacungcap/them")
    public String getThem(Model model) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        NhaCungCap dl = new NhaCungCap();

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/nhacungcap/them.html");
        return "layout/layout-admin.html";
    }

    @GetMapping("/nhacungcap/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        NhaCungCap dl = dvl.xemNCC(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/nhacungcap/xem.html");

        return "admin/nhacungcap/sua.html";
    }

    @GetMapping("/nhacungcap/xoa")
    public String getXoa(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        try {
            this.dvl.xoaNCC(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra NCCi xóa: " + e.getMessage());
        }

        return "redirect:/admin/nhacungcap/duyet";
    }

    @GetMapping("/nhacungcap/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        NhaCungCap dl = dvl.xemNCC(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/nhacungcap/xem.html");

        return "admin/nhacungcap/xem.html";
    }

    @PostMapping("/nhacungcap/them")
    public String postThem(@ModelAttribute("nhacungcap") NhaCungCap dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgayTao(LocalDate.now());
        dl.setNgaySua(LocalDate.now());

        try {
            dvl.luuNCC(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc thêm mới!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra NCCi thêm mới: " + e.getMessage());
        }

        return "redirect:/admin/nhacungcap/duyet";
    }

    @PostMapping("/nhacungcap/sua")
    public String postSua(@ModelAttribute("dl") NhaCungCap dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgaySua(LocalDate.now());
        try {
            dvl.luuNCC(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra NCCi cập nhật: " + e.getMessage());
        }

        return "redirect:/admin/nhacungcap/duyet";
    }

    @PostMapping("/nhacungcap/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaNCC(id);
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/nhacungcap/duyet";
    }
}
