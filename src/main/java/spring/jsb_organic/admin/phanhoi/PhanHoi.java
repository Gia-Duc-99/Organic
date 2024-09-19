package spring.jsb_organic.admin.phanhoi;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PhanHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ten; // bắt nhập
    private String phone;
    private String email; // bắt nhập
    private String webSite;
    private String diaChi;

    private String tieuDe; // bắt nhập

    @Column(columnDefinition = "LONGTEXT")
    private String tinNhan;// bắt nhập, mặc định: varchar(255)

    private LocalDate ngayTao;
    private LocalDate ngaySua;

    public Boolean KhongHopLe() {
        var khl = false;

        return khl;
    }

}
