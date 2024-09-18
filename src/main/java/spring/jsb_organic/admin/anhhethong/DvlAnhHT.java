package spring.jsb_organic.admin.anhhethong;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

@Service
public class DvlAnhHT {
    @Autowired
    private KdlAnhHT kdl;

    public List<AnhHeThong> duyet() {
        return kdl.findAll();
    }

    public AnhHeThong timTheoId(int id) {

        AnhHeThong dl = null;

        Optional<AnhHeThong> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public AnhHeThong xem(int id) {
        AnhHeThong dl = null;
        Optional<AnhHeThong> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }
        return dl;
    }

    public void luu(AnhHeThong dl) {
        MultipartFile file = dl.getMtFile();
        String existingPath = dl.getDuongDan(); // Lưu đường dẫn ảnh cũ

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
                dl.setDuongDan("/anhhethong/" + savedFileName); // Cập nhật đường dẫn ảnh mới
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Nếu không có file mới, giữ nguyên đường dẫn ảnh cũ
            dl.setDuongDan(existingPath);
        }
        this.kdl.save(dl);
    }

    public void xoaAnhHeThong(int id) {
        this.kdl.deleteById(id);
    }

    public void xoaId(int id) {
        Optional<AnhHeThong> optional = kdl.findById(id);
        if (optional.isPresent()) {
            AnhHeThong dl = optional.get();
            String filePath = "src/main/resources/static" + dl.getDuongDan(); // Lấy đường dẫn ảnh
            try {
                Files.deleteIfExists(Paths.get(filePath)); // Xóa file nếu tồn tại
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.kdl.deleteById(id);
        }
    }

    public List<String> layDanhSachHinh() {
        List<String> danhSachHinh = new ArrayList<>();
        String uploadDir = "src/main/resources/static/anhhethong/";

        try {
            Files.list(Paths.get(uploadDir))
                .filter(Files::isRegularFile)
                .forEach(path -> danhSachHinh.add("/anhhethong/" + path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return danhSachHinh;
    }
}
