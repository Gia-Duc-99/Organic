package spring.jsb_organic.admin.danhmuc;

import java.io.IOException;
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

@Controller
@RequestMapping("admin")
public class QdlDanhMuc {
    @Autowired
    private DvlDanhMuc dvl;

    @Autowired
    private DvlCloudinary dvlCloudinary;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/danhmuc"
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

        return "redirect:/admin/danhmuc";
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

        return "redirect:/admin/danhmuc";
    }

    @PostMapping("/danhmuc/sua")
    public String postSua(@ModelAttribute("dl") DanhMuc dl, RedirectAttributes redirectAttributes,
            @RequestParam("mtFile") MultipartFile file) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        DanhMuc existingProduct = dvl.xemDM(dl.getId());
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Danh Mục không tồn tại!");
            return "redirect:/admin/danhmuc";
        }

        // Lưu đường dẫn ảnh cũ
        String existingImageUrl = existingProduct.getDuongDan();
        String existingPublicId = existingProduct.getPublicId();
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
                dl.setDuongDan(newImageUrl);
                dl.setPublicId(newPublicId);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("THONG_BAO_LOI", "Có lỗi xảy ra khi tải ảnh lên!");
                return "redirect:/admin/danhmuc";
            }
        } else {
            // Nếu không có file mới, giữ nguyên thông tin ảnh cũ
            dl.setDuongDan(existingImageUrl);
            dl.setPublicId(existingPublicId);
        }

        dvl.luuDM(dl); // Lưu sản phẩm
        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc cập nhật!");

        return "redirect:/admin/danhmuc";
    }

    @PostMapping("/danhmuc/xoa")
    public String postXoa(Model model, @RequestParam("Id") int id, RedirectAttributes redirectAttributes)
            throws IOException {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        this.dvl.xoaDM(id);

        redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Đã hoàn tất việc xóa !");

        return "redirect:/admin/danhmuc";
    }
}
