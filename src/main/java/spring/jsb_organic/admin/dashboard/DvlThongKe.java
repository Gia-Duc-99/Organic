package spring.jsb_organic.admin.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.jsb_organic.admin.donhang.DonHang.TrangThaiDonHang;
import spring.jsb_organic.admin.khachhang.KdlKhachHang;
import spring.jsb_organic.admin.donhang.KdlDonHang;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DvlThongKe {
    @Autowired
    private KdlDonHang kdl;

    @Autowired
    private KdlKhachHang kdlKH;

    // Thống kê tổng doanh thu theo tháng hiện tại
    public BigDecimal thongKeDoanhThuThangHienTai() {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();
        return kdl.getTotalRevenueByMonth(currentMonth, currentYear);
    }

    // Thống kê tổng doanh thu của năm hiện tại
    public BigDecimal thongKeDoanhThuCuaNam(int year) {
        return kdl.getTotalRevenueByYear(year);
    }

    // Thống kê số lượng đơn hàng theo trạng thái
    public Long thongKeSoLuongDonHangTheoTrangThai(TrangThaiDonHang status) {
        return kdl.countOrdersByStatus(status);
    }

    // Thống kê số lượng khách hàng trong khoảng thời gian
    public Long thongKeSoLuongKhachHangThangNay() {
        // Lấy thời gian bắt đầu và kết thúc của tháng hiện tại
        LocalDate startDate = LocalDate.now().withDayOfMonth(1); // Ngày đầu tháng
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // Ngày cuối tháng

        return kdlKH.countCustomersByDateRange(startDate, endDate);
    }

    public BigDecimal thongKeDoanhThuCuaThang(int month, int year) {
        return kdl.getTotalRevenueByMonth(month, year);
    }
}