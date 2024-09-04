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
                dl.setDuongDan("/anhhethong/" + savedFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.kdl.save(dl);
    }

    public void xoaAnhHeThong(int id) {
        this.kdl.deleteById(id);
    }

    public void xoaId(int id) {
        this.kdl.deleteById(id);
    }
}
