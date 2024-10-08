package spring.jsb_organic.admin.danhmuc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.admin.sanpham.SanPham;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("admin")
public class QdlDanhMuc {
    @Autowired
    private DvlDanhMuc dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/danhmuc",
            "/danhmuc/duyet"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<DanhMuc> list = dvl.duyetDM();
        DanhMuc dl = new DanhMuc();

        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Cài Đặt");

        model.addAttribute("content", "/admin/danhmuc/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/danhmuc/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        DanhMuc dl = dvl.xemDM(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/danhmuc/xem.html");

        return "admin/danhmuc/sua.html";
    }

    @GetMapping("/danhmuc/xoa")
    public String getXoa(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        try {
            this.dvl.xoaDM(id);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/danhmuc/duyet";
    }

    @GetMapping("/danhmuc/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        DanhMuc dl = dvl.xemDM(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/danhmuc/xem.html");

        return "admin/danhmuc/xem.html";
    }

    @PostMapping("/danhmuc/them")
    public String postThem(@ModelAttribute("danhmuc") DanhMuc dl, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        try {
            dvl.luuDM(dl);
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc thêm mới!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi thêm mới: " + e.getMessage());
        }

        return "redirect:/admin/danhmuc/duyet";
    }

    @PostMapping("/danhmuc/sua")
    public String postSua(@ModelAttribute("dl") DanhMuc dl, RedirectAttributes redirectAttributes, @RequestParam("mtFile") MultipartFile file) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        DanhMuc existingProduct = dvl.xemDM(dl.getId());
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Sản phẩm không tồn tại!");
            return "redirect:/admin/sanpham/duyet";
        }

        // Lưu đường dẫn ảnh cũ
        String existingPath = existingProduct.getDuongDan();
        // Xóa ảnh cũ nếu có
        if (existingPath != null && !existingPath.isEmpty()) {
            String existingFilePath = "src/main/resources/static" + existingPath;
            try {
                if (Files.exists(Paths.get(existingFilePath))) {
                    Files.delete(Paths.get(existingFilePath)); // Xóa file cũ
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Lưu ảnh mới nếu có
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String uploadDir = "src/main/resources/static/anhhethong/";

                if (!Files.exists(Paths.get(uploadDir))) {
                    Files.createDirectories(Paths.get(uploadDir));
                }

                String filePath = uploadDir + UUID.randomUUID().toString() + "_" + fileName;

                Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

                String savedFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                dl.setDuongDan("/anhhethong/" + savedFileName); // Cập nhật đường dẫn ảnh mới
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Nếu không có tệp mới, giữ nguyên đường dẫn ảnh cũ
            dl.setDuongDan(existingPath);
        }

        dvl.luuDM(dl); // Lưu sản phẩm
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");

        return "redirect:/admin/danhmuc/duyet";
    }

    @PostMapping("/danhmuc/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaDM(id);

        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/danhmuc/duyet";
    }
}
