package spring.jsb_organic.admin.khachhang;

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
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int thuTu;

    private String ten;
    private String anh;
    private String diaChi;

    private String email;
    private String phone;
    private String password;

    private Boolean choPhep;

    private LocalDate ngayTao;
    private LocalDate ngaySua;

    public String getChoPhepVi() {
        if (this.choPhep == null)
            this.choPhep = false;

        return this.choPhep ? "Được Phép" : "Bị Cấm";
    }

    public Boolean KhongHopLe() {
        var khl = false;
        return khl;
    }

}