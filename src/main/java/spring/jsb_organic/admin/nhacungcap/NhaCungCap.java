package spring.jsb_organic.admin.nhacungcap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
public class NhaCungCap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int thuTu;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private Boolean choPhep;
    private String phone;
    private String ten;
    private String anh;
    private String link;
    @Column(columnDefinition = "LONGTEXT")
    private String moTa;

    @Transient
    private MultipartFile mtFile;

    public String getChoPhepVi() {
        if (this.choPhep == null)
            this.choPhep = false;

        return this.choPhep ? "Được Phép" : "Bị Cấm";
    }

    public String getNgayTaoVi() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgaySuaVi() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }
}
