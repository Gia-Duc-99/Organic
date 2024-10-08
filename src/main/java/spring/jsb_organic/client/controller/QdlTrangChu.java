package spring.jsb_organic.client.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import spring.jsb_organic.admin.danhmuc.DvlDanhMuc;
import spring.jsb_organic.admin.khachhang.DvlKhachHang;
import spring.jsb_organic.admin.khachhang.KhachHang;
import spring.jsb_organic.admin.quangcao.DvlQuangCao;
import spring.jsb_organic.admin.sanpham.DvlSanPham;
import spring.jsb_organic.admin.sanpham.SanPham;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class QdlTrangChu {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private DvlKhachHang dvl;

    @Autowired
    private DvlQuangCao dvlQuangCao;

    @Autowired
    private DvlSanPham dvlSanPham;

    @Autowired
    private DvlDanhMuc dvlDanhMuc;

    @GetMapping({
            "/",
            "/trangchu"
    })
    public String getTrangChu(Model model) {
        model.addAttribute("dsQuangCao", dvlQuangCao.dsQCChoPhep());
        model.addAttribute("dsDanhMuc", dvlDanhMuc.dsDanhMuc());
        model.addAttribute("sanPhamNoiBat", dvlSanPham.getSanPhamNoiBat());
        model.addAttribute("sanPhamMoi", dvlSanPham.getSanPhamMoi());
        model.addAttribute("sanPhamBanChay", dvlSanPham.getSanPhamBanChay());
        model.addAttribute("sanPhamKhuyenMai", dvlSanPham.getSanPhamKhuyenMai());

        model.addAttribute("title", "Trang Chủ");

        model.addAttribute("content", "/client/trangchu/trangchu.html");

        return "layout/layout-client.html";
    }

    @GetMapping({
            "/trangchu/shop"
    })
    public String getShopList(Model model) {
        model.addAttribute("dsQuangCao", dvlQuangCao.dsQCChoPhep());
        model.addAttribute("dsSanPhamNoiBat", dvlSanPham.dsSanPham());

        model.addAttribute("dsSanPham", dvlSanPham.dsSanPham());
        model.addAttribute("dsDanhMuc", dvlDanhMuc.dsDanhMuc());

        model.addAttribute("title", "Shop List");

        model.addAttribute("content", "/client/trangchu/shop.html");

        return "layout/layout-client.html";
    }

    @GetMapping({
            "/trangchu/chitietsanpham/{id}"
    })
    public String getChiTietSP(@PathVariable("id") int id, Model model) {
        SanPham sanPham = dvlSanPham.timSP(id);
        if (sanPham == null) {
            // Nếu sản phẩm không tồn tại, chuyển hướng đến trang lỗi hoặc trang khác
            return "redirect:/trangchu";
        }

        // Thêm sản phẩm chi tiết vào model
        model.addAttribute("sanPham", sanPham);

        model.addAttribute("dsDanhMuc", dvlDanhMuc.dsDanhMuc());

        model.addAttribute("dsSanPham", dvlSanPham.dsSanPham());
        model.addAttribute("title", "Chi Tiết Sản Phẩm");

        model.addAttribute("content", "/client/trangchu/chitietsanpham.html");

        return "layout/layout-client.html";
    }

    @GetMapping({ "/trangchu/dangnhap" })
    public String getDangNhap(Model model) {
        model.addAttribute("dl", new KhachHang());
        model.addAttribute("content", "/client/trangchu/dangnhap.html");
        return "layout/layout-admin-login.html";
    }

    @PostMapping("/trangchu/dangnhap")
    public String postDangNhap(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("TenDangNhap") String TenDangNhap,
            @RequestParam("MatKhau") String MatKhau,
            HttpServletRequest request,
            HttpSession session) {

        String old_password = null;

        if (dvl.tonTaiTenDangNhap(TenDangNhap)) {
            var old_dl = dvl.timKHTheoTenDangNhap(TenDangNhap);
            old_password = old_dl.getMatKhau();

            var mật_khẩu_khớp = BCrypt.checkpw(MatKhau, old_password);

            if (mật_khẩu_khớp) {
                System.out.println("\n Đúng tài khoản, đăng nhập thành công");

                request.getSession().setAttribute("KhachHang_Id", old_dl.getId());
                request.getSession().setAttribute("KhachHang_Ten", old_dl.getTen());
                request.getSession().setAttribute("KhachHang_AnhDaiDien", old_dl.getAnh());
            } else {
                System.out.println("\n Sai mật khẩu");

                redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Sai mật khẩu !");
                return "redirect:/client/nhanvien/loidangnhap";
            }
        } else {
            System.out.println("\n Không tồn tại tên đăng nhập");
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Sai tên đăng nhập !");
            return "redirect:/client/nhanvien/loidangnhap";
        }

        var uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
        if (uriBeforeLogin == null)
            uriBeforeLogin = "/trangchu";
        return "redirect:" + uriBeforeLogin;
    }

    @GetMapping({ "/trangchu/dangky" })
    public String getDangKy(Model model) {
        model.addAttribute("dl", new KhachHang());
        model.addAttribute("content", "/client/trangchu/dangky.html");
        return "layout/layout-admin-login.html";
    }

    @PostMapping("/trangchu/dangky")
    public String postDangKy(@ModelAttribute("khachHang") @Valid KhachHang khachHang, BindingResult result,
            @RequestParam("xacNhanMatKhau") String xacNhanMatKhau,
            RedirectAttributes redirectAttributes) {
        // Kiểm tra lỗi validate
        if (result.hasErrors()) {
            return "client/trangchu/dangky.html"; // Nếu có lỗi, trả lại form để người dùng sửa
        }

        if (dvl.tonTaiTenDangNhap(khachHang.getTenDangNhap())) {
            result.rejectValue("tenDangNhap", null, "Tên Đăng Nhập đã được sử dụng");
            return "client/trangchu/dangky.html"; // Trả lại form với lỗi email trùng
        }
        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!khachHang.getMatKhau().equals(xacNhanMatKhau)) {
            result.rejectValue("matKhau", null, "Mật khẩu không khớp!");
            return "client/trangchu/dangky.html";
        }

        // Thiết lập các thông tin mặc định
        khachHang.setNgayTao(LocalDate.now());
        khachHang.setNgaySua(LocalDate.now());
        khachHang.setChoPhep(true); // Thiết lập mặc định cho phép đăng nhập

        // Mã hóa mật khẩu trước khi lưu (giả sử có PasswordEncoder)
        String encodedPassword = BCrypt.hashpw(khachHang.getMatKhau(), BCrypt.gensalt());
        khachHang.setMatKhau(encodedPassword);

        // Lưu khách hàng vào cơ sở dữ liệu
        dvl.luuKH(khachHang);

        // Gửi thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công!");
        return "redirect:/trangchu/dangnhap"; // Sau khi đăng ký thành công, chuyển đến trang đăng nhập
    }

    @GetMapping("/trangchu/dangxuat")
    public String dangXuat(HttpSession session) {
        session.invalidate(); // Xoá tất cả thông tin trong session
        return "redirect:/trangchu"; // Quay về trang chủ sau khi đăng xuất
    }
}
