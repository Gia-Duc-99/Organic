package spring.jsb_organic.admin.chitietdonhang;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import spring.jsb_organic.admin.donhang.DonHang;
import spring.jsb_organic.admin.sanpham.SanPham;

@Entity
@Getter
@Setter
public class ChiTietDH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ten;
    private int soLuong;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private String donGia;
    private int tongTien;

    // #region Nếu không có FK thì bỏ mã này đi
    private int maDonHang;
    private int maSanPham;

    // Mỗi chi tiết đơn hàng phải liên quan đến 1 đơn hàng
    @ManyToOne
    @JoinColumn(name = "maDonHang", insertable = false, updatable = false)
    private DonHang donHang;

    // Mỗi chi tiết đơn hàng phải liên quan đến 1 sản phẩm
    @ManyToOne
    @JoinColumn(name = "maSanPham", insertable = false, updatable = false)
    private SanPham sanPham;
}
