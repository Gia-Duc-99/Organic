package spring.jsb_organic.admin.anhhethong;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import jakarta.persistence.Column;
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
public class AnhHeThong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private Boolean choPhep;
    private int thuTu;

    private String tuaDe;

    @Column(columnDefinition = "LONGTEXT")
    private String moTa;

    @Transient
    private MultipartFile mtFile;
    private String duongDan;

    public String getChoPhepVi() {

        if (this.choPhep == null)
            this.choPhep = false;

        return this.choPhep ? "Cho Phép" : "Cấm, Không Cho";
    }

    public Boolean KhongHopLe() {
        var khl = false;
        return khl;
    }

    // Getter và Setter cho mtFile
    public MultipartFile getMtFile() {
        return mtFile;
    }

    public void setMtFile(MultipartFile mtFile) {
        this.mtFile = mtFile;
    }
}
