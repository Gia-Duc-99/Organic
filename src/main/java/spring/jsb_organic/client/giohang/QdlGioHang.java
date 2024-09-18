package spring.jsb_organic.client.giohang;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spring.jsb_organic.admin.chitietdonhang.DvlCHiTietDH;
import spring.jsb_organic.admin.donhang.DvlDonHang;
import spring.jsb_organic.admin.khachhang.DvlKhachHang;
import spring.jsb_organic.admin.sanpham.DvlSanPham;
import spring.jsb_organic.admin.sanpham.SanPham;

import java.util.List;

@Controller
public class QdlGioHang {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private DvlSanPham dvlSanPham;

    @Autowired
    private DvlDonHang dvlDonHang;

    @Autowired
    private DvlCHiTietDH dvlChiTietDH;

    @Autowired
    private DvlKhachHang dvlKhachHang;

    @PostMapping(path = "/giohang/them/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postGioHangThemMoi(
            @RequestParam("id_sanpham") int id,
            @RequestParam("soluong") int soluong,
            @RequestParam("ten") String ten) {
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
            session.setAttribute("SoSanPhamTrongGioHang", 0);
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");
        Integer cartQuantity = (Integer) session.getAttribute("SoSanPhamTrongGioHang");

        if (cartMap.containsKey(id)) {
            cartMap.put(id, cartMap.get(id) + soluong);
        } else {
            cartMap.put(id, soluong);
        }
        cartQuantity += soluong;

        session.setAttribute("cart", cartMap);
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        Map<String, Object> data = new HashMap<>();
        data.put("success", "Đã thêm thành công vào giỏ hàng sản phẩm " + ten);
        data.put("total", cartQuantity);
        data.put("total", demSPTrongGioHang());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private int demSPTrongGioHang() {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cartMap == null || cartMap.isEmpty())
            return 0;

        int tongSoSanPham = 0;
        for (Integer maSanPham : cartMap.keySet()) {
            tongSoSanPham += cartMap.get(maSanPham);
        }

        return tongSoSanPham;
    }

    private boolean gioHangCoSanPham() {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cartMap == null || cartMap.isEmpty())
            return false;

        return true;
    }

    private String tongGiaTriGioHangVi() {
        return String.format("%,.0f", tongGiaTriGioHang());
    }

    private float tongGiaTriGioHang() {

        if (!gioHangCoSanPham())
            return 0;

        int tong = 0;
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        for (Integer maSanPham : cartMap.keySet()) {
            SanPham sp = dvlSanPham.xemSP(maSanPham);
            int soluong = cartMap.get(maSanPham);
            tong += sp.getDonGia() * soluong;
        }

        return tong;
    }

    @GetMapping("/giohang/ajax/get-html")
    public String getGioHangAjax(Model model) {
        if (!gioHangCoSanPham())
            return "trangchu/giohang-trong.html";
            
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        List<Map<String, String>> cartData = new ArrayList<Map<String, String>>();

        for (Integer maSanPham : cartMap.keySet()) {
            SanPham sp = dvlSanPham.xemSP(maSanPham);
            var donGiaStr = String.valueOf(sp.getDonGia());
            float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();

            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(sp.getId()));
            map.put("ten", sp.getTenSP());
            map.put("donGia", donGiaStr);

            map.put("donGiaVi", String.format("%,.0f", sp.getDonGia()));
            map.put("anh", sp.getAnh());
            map.put("soluong", String.valueOf(cartMap.get(maSanPham)));
            map.put("thanhTien", String.format("%,.0f", thanhTien));

            cartData.add(map);
        }

        // Gửi dữ liệu giỏ hàng sang bên View
        model.addAttribute("cartData", cartData);
        model.addAttribute("tongGiaTriGioHang", tongGiaTriGioHang());
        model.addAttribute("tongGiaTriGioHangVi", tongGiaTriGioHangVi());
        return "trangchu/giohang-ajax.html";

    }

    @GetMapping("/trangchu/giohang")
    public String getGioHangChiTiet(Model model) {
        if (!gioHangCoSanPham()) {
            model.addAttribute("content", "client/giohang/giohang-chitiet.html");

            return "layout/layout-client.html";
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        List<Map<String, String>> cartData = new ArrayList<Map<String, String>>();

        for (Integer maSanPham : cartMap.keySet()) {

            SanPham sp = dvlSanPham.xemSP(maSanPham);
            var donGiaStr = String.valueOf(sp.getDonGia());
            float thanhTien = cartMap.get(maSanPham) * sp.getDonGia();

            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(sp.getId()));
            map.put("ten", sp.getTenSP());
            map.put("donGia", donGiaStr);

            map.put("donGiaVi", String.format("%,.2f", sp.getDonGia()));
            map.put("anh", sp.getAnh());
            map.put("soluong", String.valueOf(cartMap.get(maSanPham)));
            map.put("thanhTien", String.format("%,.0f", thanhTien));

            cartData.add(map);
        }

        // Gửi dữ liệu giỏ hàng sang bên View
        model.addAttribute("cartData", cartData);
        model.addAttribute("tongGiaTriGioHangVi", tongGiaTriGioHangVi());

        model.addAttribute("content", "client/giohang/giohang-chitiet.html");

        return "layout/layout-client.html";

    }

    @PostMapping(path = "/giohang/sua/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postGioHangSuaAjax(Model model,
            @RequestParam("id_sanpham") int id,
            @RequestParam("so_luong") int soluong,
            @RequestParam("ten") String ten) {

        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
            session.setAttribute("SoSanPhamTrongGioHang", 0);
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");
        Integer cartQuantity = (Integer) session.getAttribute("SoSanPhamTrongGioHang");

        if (cartMap.containsKey(id)) {
            int so_luong_cu = cartMap.get(id);
            cartMap.put(id, soluong);
            cartQuantity += (soluong - so_luong_cu);
        }
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        // Cập nhật giỏ hàng trong Session
        session.setAttribute("cart", cartMap);
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        System.out.println("Đã cập nhật giỏ hàng với sản phẩm id: " + id);

        // Gửi dữ liệu json trở lại cho View
        Map<String, Object> data = new HashMap<>();
        data.put("success", "Đã cập nhật giỏ hàng, số lượng mới cho sản phẩm " + ten);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(path = "/giohang/xoa/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postGioHangXoaAjax(Model model,
            @RequestParam("id_sanpham") int id,
            @RequestParam("ten") String ten) {

        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
            session.setAttribute("SoSanPhamTrongGioHang", 0);
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");
        Integer cartQuantity = (Integer) session.getAttribute("SoSanPhamTrongGioHang");

        if (cartMap.containsKey(id)) {
            int so_luong_cu = cartMap.get(id);
            cartMap.remove(id);
            cartQuantity -= so_luong_cu;
        }

        session.setAttribute("cart", cartMap);
        session.setAttribute("SoSanPhamTrongGioHang", cartQuantity);

        System.out.println("Đã xóa bỏ giỏ hàng sản phẩm id: " + id);

        // Gửi dữ liệu json trở lại cho View
        Map<String, Object> data = new HashMap<>();
        data.put("success", "Đã xóa thành công khỏi giỏ hàng sản phẩm " + ten);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
