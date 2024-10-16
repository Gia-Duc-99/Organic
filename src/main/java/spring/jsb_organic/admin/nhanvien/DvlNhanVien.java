package spring.jsb_organic.admin.nhanvien;

import java.util.Arrays;
import java.util.List;

import java.util.Optional;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import spring.jsb_organic.DvlCloudinary;
import spring.jsb_organic.admin.sanpham.SanPham;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.IOException;

@Service
public class DvlNhanVien {
    @Autowired
    private KdlNhanVien kdl;

    @Autowired
    private DvlCloudinary dvlCloudinary;

    public List<NhanVien> dsNhanVien() {
        return kdl.findAll();
    }

    public List<NhanVien> duyetNhanVien() {
        return kdl.findAll();
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
        String existingPath = dl.getAnhDaiDien(); // Lưu đường dẫn ảnh cũ
        String existingPublicId = dl.getPublicId();

        if (file != null && !file.isEmpty()) {
            try {
                // Upload ảnh mới lên Cloudinary
                Map<String, Object> uploadResult = dvlCloudinary.uploadImage(file);
                // Lấy URL và public_id của ảnh sau khi upload
                String imageUrl = (String) uploadResult.get("url");
                String publicId = (String) uploadResult.get("public_id");

                dl.setAnhDaiDien(imageUrl);
                dl.setPublicId(publicId);

                // Xóa ảnh cũ từ Cloudinary
                if (existingPublicId != null && !existingPublicId.isEmpty()) {
                    dvlCloudinary.deleteImage(existingPublicId);
                }
            } catch (IOException e) {
                e.printStackTrace();
                dl.setPublicId(existingPublicId);
            }
        } else {
            dl.setAnhDaiDien(existingPath);
        }
        this.kdl.save(dl);
    }

    public void xoaNhanVien(int id) throws IOException {
        Optional<NhanVien> optional = kdl.findById(id);
        if (optional.isPresent()) {
            NhanVien dl = optional.get();
            dvlCloudinary.deleteImage(dl.getPublicId()); // Xóa ảnh từ Cloudinary
            kdl.deleteById(id);
        }
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
