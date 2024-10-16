package spring.jsb_organic.admin.khachhang;

import java.util.List;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.DvlCloudinary;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("admin")
public class QdlKhachHang {
    @Autowired
    private DvlKhachHang dvl;

    @Autowired
    private DvlCloudinary dvlCloudinary;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/khachhang"
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

        return "redirect:/admin/khachhang";
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

        return "redirect:/admin/khachhang";
    }

    @PostMapping("/khachhang/sua")
    public String postSua(@ModelAttribute("dl") KhachHang dl, @RequestParam("mtFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";
        KhachHang existingProduct = dvl.xemKH(dl.getId());
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Khách Hàng không tồn tại!");
            return "redirect:/admin/khachhang";
        }

        // Lưu thông tin đường dẫn và public_id của ảnh cũ
        String existingImageUrl = existingProduct.getAnh();
        String existingPublicId = existingProduct.getPublicId();
        dl.setNgaySua(LocalDate.now());

        // Nếu có file ảnh mới được tải lên
        if (!file.isEmpty()) {
            try {
                // Xóa ảnh cũ từ Cloudinary nếu có
                if (existingPublicId != null && !existingPublicId.isEmpty()) {
                    dvlCloudinary.deleteImage(existingPublicId); // Tạo một hàm phụ để xóa ảnh từ Cloudinary
                }

                // Upload ảnh mới lên Cloudinary
                Map<String, Object> uploadResult = dvlCloudinary.uploadImage(file); // Hàm phụ để upload ảnh

                // Lấy URL và public_id mới từ Cloudinary
                String newImageUrl = (String) uploadResult.get("url");
                String newPublicId = (String) uploadResult.get("public_id");

                // Cập nhật URL và public_id cho sản phẩm
                dl.setAnh(newImageUrl);
                dl.setPublicId(newPublicId);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi tải ảnh lên!");
                return "redirect:/admin/khachhang";
            }
        } else {
            // Nếu không có file mới, giữ nguyên thông tin ảnh cũ
            dl.setAnh(existingImageUrl);
            dl.setPublicId(existingPublicId);
        }

        dvl.luuKH(dl); // Lưu khách hàng
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");

        return "redirect:/admin/khachhang";
    }

    @PostMapping("/khachhang/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes)
            throws IOException {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaKH(id);
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/khachhang";
    }
}
