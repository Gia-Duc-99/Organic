package spring.jsb_organic.admin.quangcao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class QuangCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tuaDe;
    private String noiDung;
    private String moTa;
    private float giaTien;
    private Boolean trangThai;
    private Boolean choPhep;
    @Transient
    private MultipartFile mtFile;
    private String anh;
    private String publicId;  // public_id của ảnh trên Cloudinary
    private String link;
    private String thuTu;

    private LocalDate ngayTao;
    private LocalDate ngaySua;
    
    public String getTrangThaiVi() {
        return this.trangThai ? "Đã Thanh Toán" : "Chưa Thanh Toán";
    }

    public String getChoPhepVi() {

        if (this.choPhep == null)
            this.choPhep = false;

        return this.choPhep ? "Cho Phép" : "Cấm, Không Cho";
    }

    public String getNgayTaoVi() {
        if (this.ngayTao == null)
            return "";
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgaySuaVi() {
        if (this.ngaySua == null)
            return "";
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }

    public String getGiaTienVi() {
        return String.format("%,d", Math.round(this.giaTien));
    }

    public Boolean KhongHopLe() {
        var khl = false;

        return khl;
    }
}
