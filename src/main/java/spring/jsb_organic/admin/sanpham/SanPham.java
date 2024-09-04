package spring.jsb_organic.admin.sanpham;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import spring.jsb_organic.admin.danhmuc.DanhMuc;
import spring.jsb_organic.admin.nhacungcap.NhaCungCap;

@Entity
@Getter
@Setter
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int thuTu;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private Boolean choPhep;
    private String ten;
    private String anh;
    private String link;
    private int soLuong;

    @Column(columnDefinition = "LONGTEXT")
    private String moTa;

    @Transient
    private MultipartFile mtFile;
    private float donGia;
    private Boolean trangThai = true;
    private int daBan;
    private BigDecimal giamGia;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayHetHan;
    
    @Column(name = "maNhaCungCap")
    private int maNhaCungCap;
    @ManyToOne @JoinColumn(name="maNhaCungCap",insertable=false,updatable=false)
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "danhMuc", insertable = false, updatable = false)
    private DanhMuc danhMuc;

    public String getDonGiaVi() {
        return String.format("%,d", Math.round(this.donGia));
    }

    public String getChoPhepVi() {
        if (this.choPhep == null)
            this.choPhep = false;

        return this.choPhep ? "Được Phép" : "Bị Cấm";
    }

    public String getNgayTaoVi() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayTao);
    }

    public String getNgayTaoSQL() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(this.ngayTao);
    }

    public String getNgayHetHanSQL() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(this.ngayHetHan);
    }

    public String getNgaySuaVi() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngaySua);
    }

    public String getNgayHetHanVi() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.ngayHetHan);
    }

}
