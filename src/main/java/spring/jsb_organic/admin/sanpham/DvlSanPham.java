package spring.jsb_organic.admin.sanpham;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Service
public class DvlSanPham {
    @Autowired
    private KdlSanPham kdl;

    public List<SanPham> dsSanPham() {
        return kdl.findAll();
    }

    public List<SanPham> duyetSP() {
        return kdl.findAll();
    }

    public SanPham timSP(int id) {
        return kdl.findById(id).orElse(null);
    }

    public SanPham xemSP(int id) {
        return kdl.findById(id).orElse(null);
    }

    public void luuSP(SanPham dl) {
        MultipartFile file = dl.getMtFile();
        String existingPath = dl.getAnh(); // Lưu đường dẫn ảnh cũ
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
                dl.setAnh("/anhhethong/" + savedFileName); // Cập nhật đường dẫn ảnh mới
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Nếu không có file mới, giữ nguyên đường dẫn ảnh cũ
            dl.setAnh(existingPath);
        }
        this.kdl.save(dl);
    }

    public void xoaSP(int id) {
        Optional<SanPham> optional = kdl.findById(id);
        if (optional.isPresent()) {
            SanPham dl = optional.get();
            String filePath = "src/main/resources/static" + dl.getAnh(); // Lấy đường dẫn ảnh
            try {
                Files.deleteIfExists(Paths.get(filePath)); // Xóa file nếu tồn tại
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.kdl.deleteById(id);
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
                SanPham sanPham = timSP(productId);
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
}
