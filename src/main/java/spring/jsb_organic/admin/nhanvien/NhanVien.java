package spring.jsb_organic.admin.nhanvien;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tenDangNhap;
    private String email;
    private String tenDayDu;
    private String matKhau;
    private String xacNhanMatKhau;
    @Transient
    private MultipartFile mtFile;
    private String anhDaiDien;
    
    private Boolean trangThai;
    private String dienThoai;
    private String moTa;
    private int thuTu;

    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private LocalDate ngayHetHan;

    public MultipartFile getMtFile() {
        return mtFile;
    }

    public void setMtFile(MultipartFile mtFile) {
        this.mtFile = mtFile;
    }

    public String getTrangThaiVi() {
        if (this.trangThai == null)
            this.trangThai = false;

        return this.trangThai ? "Được Phép" : "Bị Cấm";
    }

    public String getNgayTaoText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgayHetHanText() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayHetHan);
    }

    public Boolean KhongHopLe() {
        var khl = false;
        return khl;
    }
}
