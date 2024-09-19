package spring.jsb_organic.admin.anhhethong;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("admin")
public class QdlAnhHT {
    @Autowired
    private DvlAnhHT dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/anhhethong",
            "/anhhethong/duyet"
    })
    public String getDuyet(Model model) {
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        List<AnhHeThong> list = dvl.duyet();

        AnhHeThong dl = new AnhHeThong();
        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Ảnh Hệ Thống");

        model.addAttribute("content", "/admin/anhhethong/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/anhhethong/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        AnhHeThong dl = dvl.xem(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/anhhethong/sua.html");

        return "/admin/anhhethong/sua.html";
    }

    @GetMapping("/anhhethong/xoa")
    public String getXoa(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        try {
            this.dvl.xoaId(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi xóa: " + e.getMessage());
        }
        return "redirect:/admin/anhhethong/duyet";
    }

    @GetMapping("/anhhethong/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        AnhHeThong dl = dvl.xem(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/anhhethong/xem.html");

        return "admin/anhhethong/xem.html";
    }

    @PostMapping("/anhhethong/them")
    public String postThem(@ModelAttribute("AnhHeThong") AnhHeThong dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        dl.setNgayTao(LocalDate.now());
        dl.setNgaySua(LocalDate.now());
        try {
            dvl.luu(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc thêm mới!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi thêm mới: " + e.getMessage());
        }

        return "redirect:/admin/anhhethong/duyet";
    }

    @PostMapping("/anhhethong/sua")
    public String postSua(@ModelAttribute("dl") AnhHeThong dl,
            @RequestParam("mtFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        dl.setNgaySua(LocalDate.now());

        // Kiểm tra xem có tệp mới không
        if (!file.isEmpty()) {
            dvl.luu(dl);
        } else {
            // Nếu không có tệp mới, chỉ cần lưu lại thông tin hiện tại
            dvl.luu(dl);
        }
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");
        return "redirect:/admin/anhhethong/duyet";
    }

    @PostMapping("/anhhethong/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaId(id);

        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/anhhethong/duyet";
    }
}
