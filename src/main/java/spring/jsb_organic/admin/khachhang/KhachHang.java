package spring.jsb_organic.admin.khachhang;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int thuTu;
    @NotBlank(message = "Tên không được để trống")
    private String ten;
    @Transient
    private MultipartFile mtFile;
    private String anh;
    private String diaChi;
    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Số Điện Thoại không được để trống")
    private String phone;
    private String tenDangNhap;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải chứa ít nhất 6 ký tự")
    private String matKhau;

    private String xacNhanMatKhau;

    private Boolean choPhep;

    private LocalDate ngayTao;
    private LocalDate ngaySua;

    public MultipartFile getMtFile() {
        return mtFile;
    }

    public void setMtFile(MultipartFile mtFile) {
        this.mtFile = mtFile;
    }

    public String getChoPhepVi() {
        if (this.choPhep == null)
            this.choPhep = false;

        return this.choPhep ? "Được Phép" : "Bị Cấm";
    }

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngaySua = LocalDate.now();
    }
}