package spring.jsb_organic.admin.donhang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface KdlDonHang extends JpaRepository<DonHang, Integer> {
    // Thống kê tổng doanh thu theo tháng và năm
    @Query("SELECT SUM(CAST(dh.tongTien AS BigDecimal)) FROM DonHang dh WHERE MONTH(dh.ngayTao) = :month AND YEAR(dh.ngayTao) = :year AND dh.trangThai = 'DA_XAC_NHAN'")
    BigDecimal getTotalRevenueByMonth(@Param("month") int month, @Param("year") int year);

    // Thống kê tổng doanh thu của năm
    @Query("SELECT SUM(CAST(dh.tongTien AS BigDecimal)) FROM DonHang dh WHERE YEAR(dh.ngayTao) = :year AND dh.trangThai = 'DA_XAC_NHAN'")
    BigDecimal getTotalRevenueByYear(@Param("year") int year);

    // Thống kê số lượng đơn hàng theo trạng thái
    @Query("SELECT COUNT(dh) FROM DonHang dh WHERE dh.trangThai = :status")
    Long countOrdersByStatus(@Param("status") DonHang.TrangThaiDonHang status);

    

    
}