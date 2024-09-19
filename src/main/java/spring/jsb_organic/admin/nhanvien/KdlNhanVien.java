package spring.jsb_organic.admin.nhanvien;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KdlNhanVien extends JpaRepository<NhanVien, Integer>
{
    // Các hàm truy vấn này được tạo tự động phần thân
    // Bạn chỉ cần khai báo tên hàm theo đúng chuẩn là được: hànhĐộng+TênCột(tham số nếu có)
    List<NhanVien> findByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDangNhap(String tenDangNhap);
    NhanVien findOneByTenDangNhap(String tenDangNhap);
    Boolean existsByTenDayDu(String tdd);
}
