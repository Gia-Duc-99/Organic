package spring.jsb_organic.admin.danhmuc;

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
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tenDM;
    @Transient
    private MultipartFile mtFile;
    private String duongDan;
    private String publicId;  // public_id của ảnh trên Cloudinary

    public MultipartFile getMtFile() {
        return mtFile;
    }

    public void setMtFile(MultipartFile mtFile) {
        this.mtFile = mtFile;
    }
}
