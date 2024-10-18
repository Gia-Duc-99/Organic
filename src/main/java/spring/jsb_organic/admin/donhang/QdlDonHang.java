package spring.jsb_organic.admin.donhang;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.admin.chitietdonhang.ChiTietDH;
import spring.jsb_organic.admin.chitietdonhang.DvlCHiTietDH;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class QdlDonHang {
    @Autowired
    private DvlDonHang dvl;

    @Autowired
    private DvlCHiTietDH dvlCTDH;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/donhang"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<DonHang> list = dvl.duyetDH();
        DonHang dl = new DonHang();

        Map<Integer, List<ChiTietDH>> chiTietDonHangMap = new HashMap<>();
        for (DonHang donHang : list) {
            List<ChiTietDH> chiTietDHList = dvlCTDH.timTheoDonHang(donHang.getId());
            if (chiTietDHList != null && !chiTietDHList.isEmpty()) {
                chiTietDonHangMap.put(donHang.getId(), chiTietDHList);
            } else {
                System.out.println("Không có chi tiết đơn hàng cho DonHang ID: " + donHang.getId());
            }
        }
        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        // Thêm danh sách chi tiết đơn hàng vào model
        model.addAttribute("chiTietDonHangMap", chiTietDonHangMap);
        model.addAttribute("title", "Quản Lý Đơn Hàng");

        model.addAttribute("content", "/admin/donhang/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/donhang/xem")
    public String xemChiTietDonHang(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        List<ChiTietDH> chiTietDHList = dvlCTDH.timTheoDonHang(id);
        DonHang donHang = dvl.xemDH(id);
        model.addAttribute("donHang", donHang);

        model.addAttribute("chiTietDHList", chiTietDHList);

        return "admin/donhang/chitietdh :: chiTietDonHangFragment";
    }

    @PostMapping("/donhang/capnhattrangthai")
    @ResponseBody
    public String capNhatTrangThaiDonHang(@RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        // Cập nhật trạng thái đơn hàng
        DonHang donHang = dvl.xemDH(id);
        if (donHang != null) {
            // Cập nhật trạng thái đơn hàng theo thứ tự: Mới -> Đã Xác Nhận -> Đang Giao ->
            // Đã Giao
            if (donHang.getTrangThai() == DonHang.TrangThaiDonHang.MOI) {
                donHang.setTrangThai(DonHang.TrangThaiDonHang.DA_XAC_NHAN);
            } else if (donHang.getTrangThai() == DonHang.TrangThaiDonHang.DA_XAC_NHAN) {
                donHang.setTrangThai(DonHang.TrangThaiDonHang.DANG_GIAO);
            } else if (donHang.getTrangThai() == DonHang.TrangThaiDonHang.DANG_GIAO) {
                donHang.setTrangThai(DonHang.TrangThaiDonHang.DA_GIAO);
                // Nếu trạng thái thanh toán là chưa thanh toán, thì cập nhật thành đã thanh
                // toán
                if (donHang.getTrangThaiThanhToan() == null || !donHang.getTrangThaiThanhToan()) {
                    donHang.setTrangThaiThanhToan(true); // Đặt là đã thanh toán
                }
            }
            dvl.luuDH(donHang); // Gọi phương thức cập nhật trong service
            return "success";
        } else {
            return "error";
        }
    }

}
