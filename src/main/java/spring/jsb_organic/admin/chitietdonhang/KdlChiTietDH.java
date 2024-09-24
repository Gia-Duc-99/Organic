package spring.jsb_organic.admin.chitietdonhang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KdlChiTietDH extends JpaRepository<ChiTietDH, Integer> {
    List<ChiTietDH> findByDonHangId(int maDonHang);
}
