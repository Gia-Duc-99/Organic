package spring.jsb_organic.admin.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import spring.jsb_organic.admin.donhang.DonHang.TrangThaiDonHang;

@Controller
@RequestMapping("admin")
public class QdlDashBoard {

    @Autowired
    private DvlThongKe dvl;

    @GetMapping({
            "/"
    })
    public String getTrangChu(Model model) {

        // Lấy thống kê doanh thu của tháng hiện tại
        BigDecimal monthlyRevenue = dvl.thongKeDoanhThuThangHienTai();
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO; // Gán giá trị mặc định nếu null
        }

        // Lấy thống kê tổng doanh thu của năm hiện tại
        int currentYear = LocalDate.now().getYear();
        BigDecimal annualRevenue = dvl.thongKeDoanhThuCuaNam(currentYear); // Truyền năm hiện tại để lấy tổng cả năm
        if (annualRevenue == null) {
            annualRevenue = BigDecimal.ZERO; // Gán giá trị mặc định nếu null
        }

        // Lấy số lượng đơn hàng mới
        Long pendingOrders = dvl.thongKeSoLuongDonHangTheoTrangThai(TrangThaiDonHang.MOI);
        if (pendingOrders == null) {
            pendingOrders = 0L; // Gán giá trị mặc định nếu null
        }

        // Lấy số lượng khách hàng tháng này
        Long totalCustomersThisMonth = dvl.thongKeSoLuongKhachHangThangNay();
        model.addAttribute("totalCustomersThisMonth", totalCustomersThisMonth);

        // Đưa các thông tin này vào model để hiển thị lên giao diện
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("annualRevenue", annualRevenue);
        model.addAttribute("totalCustomersThisMonth", totalCustomersThisMonth);
        model.addAttribute("pendingOrders", pendingOrders);

        model.addAttribute("title", "Trang Chủ");
        model.addAttribute("content", "/admin/trangchu.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/thongke/doanhthu")
    @ResponseBody // Đảm bảo rằng bạn sử dụng @ResponseBody
    public List<BigDecimal> getMonthlyRevenue() {
        List<BigDecimal> revenues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            BigDecimal revenue = dvl.thongKeDoanhThuCuaThang(month, LocalDate.now().getYear());
            revenues.add(revenue != null ? revenue : BigDecimal.ZERO);
        }
        return revenues; // Trả về danh sách doanh thu
    }
}
