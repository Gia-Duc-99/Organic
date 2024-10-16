package spring.jsb_organic.client.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import spring.jsb_organic.admin.danhmuc.DvlDanhMuc;
import spring.jsb_organic.admin.donhang.DonHang;
import spring.jsb_organic.admin.donhang.DvlDonHang;
import spring.jsb_organic.admin.khachhang.DvlKhachHang;
import spring.jsb_organic.admin.khachhang.KhachHang;
import spring.jsb_organic.admin.quangcao.DvlQuangCao;
import spring.jsb_organic.admin.sanpham.DvlSanPham;
import spring.jsb_organic.admin.sanpham.SanPham;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private DvlDonHang dvlDonHang;

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
        model.addAttribute("sanPhamMoi", dvlSanPham.getSanPhamMoi());
        model.addAttribute("dsSanPham", dvlSanPham.dsSanPham());
        model.addAttribute("dsDanhMuc", dvlDanhMuc.dsDanhMuc());

        model.addAttribute("title", "Shop List");

        model.addAttribute("content", "/client/trangchu/shop.html");

        return "layout/layout-client.html";
    }

    @GetMapping("/trangchu/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<SanPham> resultList = dvlSanPham.searchProducts(query);
        if (resultList == null || resultList.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy sản phẩm nào khớp với từ khóa.");
            resultList = new ArrayList<>(); // Khởi tạo danh sách trống nếu không có kết quả
        }
        List<SanPham> sanPhamMoi = dvlSanPham.getSanPhamMoi();
        if (sanPhamMoi == null) {
            sanPhamMoi = new ArrayList<>();
        }
        model.addAttribute("sanPhamMoi", sanPhamMoi);
        model.addAttribute("dsSanPham", resultList);
        model.addAttribute("title", "Search Results");
        model.addAttribute("totalProducts", resultList.size());
        model.addAttribute("query", query);
        model.addAttribute("content", "/client/trangchu/shop.html"); // Đảm bảo bạn chỉ định content
        return "layout/layout-client.html"; // Trả về layout với content
    }

    @GetMapping({
            "/trangchu/chitietsanpham/{id}"
    })
    public String getChiTietSP(@PathVariable("id") int id, Model model) {
        SanPham sanPham = dvlSanPham.xemSP(id);
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
            @RequestParam("TenDangNhap") String tenDangNhap,
            @RequestParam("MatKhau") String matKhau,
            HttpServletRequest request,
            HttpSession session) {

        // Kiểm tra xem tên đăng nhập có tồn tại không
        if (dvl.tonTaiTenDangNhap(tenDangNhap)) {
            var khachHang = dvl.timKHTheoTenDangNhap(tenDangNhap);
            String oldPassword = khachHang.getMatKhau();

            // Kiểm tra xem mật khẩu có khớp không
            boolean isPasswordMatched = BCrypt.checkpw(matKhau, oldPassword);
            if (isPasswordMatched) {
                System.out.println("\n Đăng nhập thành công");

                // Lưu thông tin khách hàng vào session
                request.getSession().setAttribute("KhachHang_Id", khachHang.getId());
                request.getSession().setAttribute("KhachHang_Ten", khachHang.getTen());
                request.getSession().setAttribute("KhachHang_AnhDaiDien", khachHang.getAnh());
                // Kiểm tra xem có URL trước khi đăng nhập không
                String uriBeforeLogin = (String) session.getAttribute("URI_BEFORE_LOGIN");
                if (uriBeforeLogin == null || uriBeforeLogin.isEmpty()) {
                    uriBeforeLogin = "/trangchu"; // Nếu không có, chuyển hướng về trang chủ
                }

                return "redirect:" + uriBeforeLogin;

            } else {
                // Sai mật khẩu
                System.out.println("\n Sai mật khẩu");
                redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Sai mật khẩu !");
                return "redirect:/client/nhanvien/loidangnhap";
            }
        } else {
            // Tên đăng nhập không tồn tại
            System.out.println("\n Không tồn tại tên đăng nhập");
            redirectAttributes.addFlashAttribute("THONG_BAO_OK", "Sai tên đăng nhập !");
            return "redirect:/client/nhanvien/loidangnhap";
        }
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

    @GetMapping({
            "/trangchu/thongtintaikhoan"
    })
    public String getThongTinTaiKhoan(Model model) {
        // Lấy thông tin khách hàng từ session
        Integer khachHangId = (Integer) session.getAttribute("KhachHang_Id");
        if (khachHangId != null) {
            // Khách hàng đã đăng nhập
            KhachHang khachHang = dvl.xemKH(khachHangId); // Lấy thông tin khách hàng từ database
            model.addAttribute("khachHang", khachHang); // Gửi thông tin khách hàng sang view
    
            // Lấy danh sách đơn hàng cho khách hàng này
            List<DonHang> danhSachDonHang = dvlDonHang.findByKhachHangId(khachHangId);
            model.addAttribute("danhSachDonHang", danhSachDonHang);
        } else {
            // Khách hàng chưa đăng nhập, hiển thị thông báo hoặc giao diện khác
            model.addAttribute("khachHang", null); // Không có thông tin khách hàng
            model.addAttribute("danhSachDonHang", null); // Không có đơn hàng
            model.addAttribute("THONG_BAO", 
                    "Bạn hiện đang ở chế độ khách vãng lai. Vui lòng đăng nhập để xem thông tin tài khoản và đơn hàng.");
        }

        model.addAttribute("content", "/client/trangchu/thongtinkh.html");
        return "layout/layout-client.html";
    }
}
