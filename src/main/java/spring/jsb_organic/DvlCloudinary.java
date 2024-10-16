package spring.jsb_organic;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class DvlCloudinary {
    private final Cloudinary cloudinary;

    public DvlCloudinary() {
        Dotenv dotenv = Dotenv.load();
        this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }

    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> params = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", true,
                "overwrite", false,
                "folder", "anhhethong");
        return cloudinary.uploader().upload(file.getBytes(), params);
    }

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
