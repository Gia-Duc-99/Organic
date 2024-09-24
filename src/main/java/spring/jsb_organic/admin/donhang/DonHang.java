package spring.jsb_organic.admin.donhang;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import spring.jsb_organic.admin.chitietdonhang.ChiTietDH;
import spring.jsb_organic.admin.khachhang.KhachHang;

@Entity
@Getter
@Setter
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;
    private String email;
    private String dienThoai;
    private String tenDayDu;
    private String diaChi;
    private String ghiChu;
    private String tongTien;
    @Enumerated(EnumType.STRING)
    private TrangThaiDonHang trangThai;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    @OneToMany(mappedBy = "donHang")
    private List<ChiTietDH> chiTietDHs;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngaySua = LocalDate.now();
    }

    public enum TrangThaiDonHang {
        MOI("Mới"),
        DA_XAC_NHAN("Đã Xác Nhận"),
        CHUA_THANH_TOAN("Chưa Thanh Toán"),
        DA_THANH_TOAN("Đã Thanh Toán"),
        DA_HUY("Đã Hủy"),
        DANG_GIAO("Đang Giao"),
        DA_GIAO("Đã Giao");

        private final String trangThaiVi;

        // Constructor để ánh xạ giá trị tiếng Việt
        TrangThaiDonHang(String trangThaiTiengViet) {
            this.trangThaiVi = trangThaiTiengViet;
        }

        // Getter cho giá trị tiếng Việt
        public String getTrangThaiVi() {
            return trangThaiVi;
        }
    }
}
