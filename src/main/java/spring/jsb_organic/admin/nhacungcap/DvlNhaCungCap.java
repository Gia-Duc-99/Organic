package spring.jsb_organic.admin.nhacungcap;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import spring.jsb_organic.DvlCloudinary;

@Service
public class DvlNhaCungCap {
    @Autowired
    private KdlNhaCungCap kdl;

    @Autowired
    private DvlCloudinary dvlCloudinary;

    public List<NhaCungCap> dsNhaCungCap() {
        List<NhaCungCap> nhaCungCaps = kdl.findAll();
        return nhaCungCaps;
    }

    public List<NhaCungCap> duyetNCC() {
        return kdl.findAll();
    }

    public NhaCungCap timNCC(int id) {

        NhaCungCap dl = null;

        Optional<NhaCungCap> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public NhaCungCap xemNCC(int id) {

        NhaCungCap dl = null;

        Optional<NhaCungCap> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }
        return dl;

    }

    public void luuNCC(NhaCungCap dl) {
        MultipartFile file = dl.getMtFile();
        String existingPath = dl.getAnh(); // Lưu đường dẫn ảnh cũ
        String existingPublicId = dl.getPublicId();

        if (file != null && !file.isEmpty()) {
            try {
                // Upload ảnh mới lên Cloudinary
                Map<String, Object> uploadResult = dvlCloudinary.uploadImage(file);
                // Lấy URL và public_id của ảnh sau khi upload
                String imageUrl = (String) uploadResult.get("url");
                String publicId = (String) uploadResult.get("public_id");

                dl.setAnh(imageUrl);
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
            dl.setAnh(existingPath);
        }
        this.kdl.save(dl);
    }

    public void xoaNCC(int id) throws IOException {
        Optional<NhaCungCap> optional = kdl.findById(id);
        if (optional.isPresent()) {
            NhaCungCap dl = optional.get();
            dvlCloudinary.deleteImage(dl.getPublicId()); // Xóa ảnh từ Cloudinary
            kdl.deleteById(id);
        }
    }

}
