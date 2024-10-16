package spring.jsb_organic.admin.danhmuc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import spring.jsb_organic.DvlCloudinary;

@Service
public class DvlDanhMuc {

    @Autowired
    private KdlDanhMuc kdl;

    @Autowired
    private DvlCloudinary dvlCloudinary;

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
        String existingPublicId = dl.getPublicId();

        if (file != null && !file.isEmpty()) {
            try {
                // Upload ảnh mới lên Cloudinary
                Map<String, Object> uploadResult = dvlCloudinary.uploadImage(file);
                // Lấy URL và public_id của ảnh sau khi upload
                String imageUrl = (String) uploadResult.get("url");
                String publicId = (String) uploadResult.get("public_id");

                dl.setDuongDan(imageUrl);
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
            dl.setDuongDan(existingPath);
        }
        this.kdl.save(dl);
    }


    public void xoaDM(int id) throws IOException
    {
        Optional<DanhMuc> optional = kdl.findById(id);
        if (optional.isPresent()) {
            DanhMuc dl = optional.get();
            dvlCloudinary.deleteImage(dl.getPublicId()); // Xóa ảnh từ Cloudinary
            kdl.deleteById(id);
        }
    }
}