package spring.jsb_organic.admin.nhanvien;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.IOException;

@Service
public class DvlNhanVien {
    @Autowired
    private KdlNhanVien kdl;

    public List<NhanVien> dsNhanVien() {
        return kdl.findAll();
    }

    public List<NhanVien> duyetNhanVien() {
        return kdl.findAll();
    }

    public NhanVien timNhanVienTheoId(int id) {

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public NhanVien xemNhanVien(int id) {

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luuNhanVien(NhanVien dl) {
        MultipartFile file = dl.getMtFile();

        if (file != null && !file.isEmpty()) {
            try {

                String fileName = file.getOriginalFilename();
                String uploadDir = "src/main/resources/static/anhhethong/";

                if (!Files.exists(Paths.get(uploadDir))) {
                    Files.createDirectories(Paths.get(uploadDir));
                }

                String filePath = uploadDir + UUID.randomUUID().toString() + "_" + fileName;

                Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

                String savedFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                dl.setAnhDaiDien("/anhhethong/" + savedFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.kdl.save(dl);
    }

    public void xoaNhanVien(int id) {
        this.kdl.deleteById(id);
    }

    public NhanVien timNVTheoTenDangNhap(String tdn) {

        NhanVien dl = null;

        dl = kdl.findOneByTenDangNhap(tdn);

        return dl;
    }

    public Boolean tonTaiTenDangNhap(String tdn) {
        return kdl.existsByTenDangNhap(tdn);
    }

}
