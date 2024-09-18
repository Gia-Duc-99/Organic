package spring.jsb_organic.admin.donhang;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String maKH;
    private String email;
    private String dienThoai;
    private String tenDayDu;
    private String diaChi;
    private String ghiChu;
    private String tongTien;
    private Boolean trangThai;
    private LocalDate ngayTao;
    private LocalDate ngaySua;


}
