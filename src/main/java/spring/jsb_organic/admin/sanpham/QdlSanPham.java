package spring.jsb_organic.admin.sanpham;

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
import spring.jsb_organic.admin.danhmuc.DvlDanhMuc;
import spring.jsb_organic.admin.danhmuc.DanhMuc;
import spring.jsb_organic.admin.nhacungcap.DvlNhaCungCap;
import spring.jsb_organic.admin.nhacungcap.NhaCungCap;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Controller
@RequestMapping("admin")
public class QdlSanPham {
    @Autowired
    private DvlSanPham dvl;

    @Autowired
    private DvlNhaCungCap service;

    @Autowired
    private DvlDanhMuc dichvu;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/sanpham",
            "/sanpham/duyet"
    })
    public String getDuyet(Model model) {
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<SanPham> list = dvl.duyetSP();
        SanPham dl = new SanPham();
        List<NhaCungCap> nhaCungCap = service.dsNhaCungCap();
        model.addAttribute("ncc", nhaCungCap);

        List<DanhMuc> danhmuc = dichvu.dsDanhMuc();
        model.addAttribute("danhmuc", danhmuc);

        model.addAttribute("dl", dl);
        model.addAttribute("ds", list);
        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("content", "/admin/sanpham/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/sanpham/sua")
    public String getSua(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        SanPham dl = dvl.xemSP(id);
        List<NhaCungCap> nhaCungCap = service.dsNhaCungCap();
        model.addAttribute("ncc", nhaCungCap);

        List<DanhMuc> danhmuc = dichvu.dsDanhMuc();
        model.addAttribute("danhmuc", danhmuc);
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
        List<NhaCungCap> nhaCungCap = service.dsNhaCungCap();
        model.addAttribute("ncc", nhaCungCap);

        List<DanhMuc> danhmuc = dichvu.dsDanhMuc();
        model.addAttribute("danhmuc", danhmuc);
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
    public String postSua(@ModelAttribute("dl") SanPham dl, @RequestParam("mtFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        SanPham existingProduct = dvl.xemSP(dl.getId());
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Sản phẩm không tồn tại!");
            return "redirect:/admin/sanpham/duyet";
        }

        // Lưu đường dẫn ảnh cũ
        String existingPath = existingProduct.getAnh();
        dl.setNgaySua(LocalDate.now());

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
                dl.setAnh("/anhhethong/" + savedFileName); // Cập nhật đường dẫn ảnh mới
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Nếu không có tệp mới, giữ nguyên đường dẫn ảnh cũ
            dl.setAnh(existingPath);
        }

        dvl.luuSP(dl); // Lưu sản phẩm
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");

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
