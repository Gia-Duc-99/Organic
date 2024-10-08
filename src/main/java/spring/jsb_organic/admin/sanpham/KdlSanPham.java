package spring.jsb_organic.admin.sanpham;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KdlSanPham extends JpaRepository<SanPham, Integer> {
    @Query("SELECT sp FROM SanPham sp ORDER BY sp.ngayTao DESC")
    List<SanPham> findTop10ByNgayTao();

    @Query("SELECT sp FROM SanPham sp ORDER BY sp.daBan DESC")
    List<SanPham> findTop10ByDaBan();

    @Query("SELECT sp FROM SanPham sp WHERE sp.giamGia > 0 ORDER BY sp.giamGia DESC")
    List<SanPham> findSanPhamKhuyenMai();

    @Query("SELECT sp FROM SanPham sp WHERE sp.noiBat = true")
    List<SanPham> findTopSanPhamNoiBat(Pageable pageable);
}
