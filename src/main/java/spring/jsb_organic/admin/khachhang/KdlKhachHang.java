package spring.jsb_organic.admin.khachhang;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface KdlKhachHang extends JpaRepository<KhachHang, Integer> {
    List<KhachHang> findByTenDangNhap(String tenDangNhap);

    Boolean existsByTenDangNhap(String tenDangNhap);

    KhachHang findOneByTenDangNhap(String tenDangNhap);

    Boolean existsByTen(String ten);

    // Thống kê các đơn hàng theo trạng thái trong một khoảng thời gian
    @Query("SELECT COUNT(kh) FROM KhachHang kh WHERE kh.ngayTao BETWEEN :startDate AND :endDate")
    Long countCustomersByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
