package spring.jsb_organic.admin.khachhang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KdlKhachHang extends JpaRepository<KhachHang, Integer> {
    List<KhachHang> findByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDangNhap(String tenDangNhap);
    KhachHang findOneByTenDangNhap(String tenDangNhap);
    Boolean existsByTen(String ten);
}
