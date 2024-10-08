package spring.jsb_organic.admin.danhmuc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import spring.jsb_organic.admin.sanpham.SanPham;

@Service
public class DvlDanhMuc {

    @Autowired
    private KdlDanhMuc kdl;

    public List<DanhMuc> dsDanhMuc()
    {
        return kdl.findAll();
    }
    public List<DanhMuc> duyetDM()
    {
        return kdl.findAll();
    }
    public DanhMuc timDMTheoId(int id)
    {
        DanhMuc dl = null;
        Optional<DanhMuc> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        }
        else{

        }
        return dl;
    }
    public DanhMuc xemDM(int id)
    {
        DanhMuc dl = null;
        Optional<DanhMuc> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        }else{

        }
        return dl;
    }
    public void luuDM(DanhMuc dl)
    {
        MultipartFile file = dl.getMtFile();
        String existingPath = dl.getDuongDan(); // Lưu đường dẫn ảnh cũ
        System.out.println("Đường dẫn ảnh cũ: " + existingPath);
        if (file != null && !file.isEmpty()) {
            try {
                if (existingPath != null && !existingPath.isEmpty()) {
                    Files.deleteIfExists(Paths.get("src/main/resources/static" + existingPath));
                }
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


    public void xoaDM(int id)
    {
        Optional<DanhMuc> optional = kdl.findById(id);
        if (optional.isPresent()) {
            DanhMuc dl = optional.get();
            String filePath = "src/main/resources/static" + dl.getDuongDan(); // Lấy đường dẫn ảnh
            try {
                Files.deleteIfExists(Paths.get(filePath)); // Xóa file nếu tồn tại
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.kdl.deleteById(id);
        }
    }
}