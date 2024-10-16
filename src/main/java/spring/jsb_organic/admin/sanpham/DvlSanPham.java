package spring.jsb_organic.admin.sanpham;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.DvlCloudinary;

@Service
public class DvlSanPham {
    @Autowired
    private KdlSanPham kdl;

    @Autowired
    private DvlCloudinary dvlCloudinary;

    public List<SanPham> dsSanPham() {
        return kdl.findAll();
    }

    public SanPham xemSP(int id) {
        return kdl.findById(id).orElse(null);
    }

    public void luuSP(SanPham dl) {
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

    public void xoaSP(int id) throws IOException {
        Optional<SanPham> optional = kdl.findById(id);
        if (optional.isPresent()) {
            SanPham dl = optional.get();
            dvlCloudinary.deleteImage(dl.getPublicId()); // Xóa ảnh từ Cloudinary
            kdl.deleteById(id);
        }
    }

    public boolean addToCart(int productId, HttpSession session) {
        // Lấy giỏ hàng từ session
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Thêm sản phẩm vào giỏ hàng
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
        session.setAttribute("cart", cart);
        return true; // Trả về true nếu thành công
    }

    public Map<Integer, Object> getCartData(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        Map<Integer, Object> cartData = new HashMap<>();

        if (cart != null) {
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();
                SanPham sanPham = xemSP(productId);
                if (sanPham != null) {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("ten", sanPham.getTenSP());
                    productData.put("anh", sanPham.getAnh());
                    productData.put("soluong", quantity);
                    productData.put("thanhTien", sanPham.getDonGia() * quantity); // Tính thành tiền
                    cartData.put(productId, productData);
                }
            }
        }
        return cartData;
    }

    public List<SanPham> getSanPhamNoiBat() {
        Pageable limit = PageRequest.of(0, 8);
        return kdl.findTopSanPhamNoiBat(limit);
    }

    public List<SanPham> getSanPhamMoi() {
        return kdl.findTop10ByNgayTao();
    }

    public List<SanPham> getSanPhamBanChay() {
        return kdl.findTop10ByDaBan();
    }

    public List<SanPham> getSanPhamKhuyenMai() {
        return kdl.findSanPhamKhuyenMai();
    }

    public List<SanPham> searchProducts(String query) {
        return kdl.findByTenSPContaining(query);
    }

}
