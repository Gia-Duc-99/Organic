package spring.jsb_organic.client.controller;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spring.jsb_organic.admin.chitietdonhang.ChiTietDH;
import spring.jsb_organic.admin.chitietdonhang.DvlCHiTietDH;
import spring.jsb_organic.admin.donhang.DonHang;
import spring.jsb_organic.admin.donhang.DvlDonHang;
import spring.jsb_organic.admin.khachhang.DvlKhachHang;
import spring.jsb_organic.admin.khachhang.KhachHang;
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

    private void capNhatThongTinGioHang() {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cartMap == null || cartMap.isEmpty()) {
            session.setAttribute("SoSanPhamTrongGioHang", 0);
            session.setAttribute("TongGiaTriGioHangVi", 0);
            return;
        }

        int tongSoLuong = 0;
        float tongGiaTri = 0;

        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            SanPham sp = dvlSanPham.xemSP(entry.getKey());
            tongSoLuong += entry.getValue();
            tongGiaTri += sp.getDonGia() * entry.getValue();
        }

        session.setAttribute("SoSanPhamTrongGioHang", tongSoLuong);
        session.setAttribute("TongGiaTriGioHangVi", String.format("%,.0f", tongGiaTri));
    }

    @PostMapping(path = "/giohang/them/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postGioHangThemMoi(
            @RequestParam("id_sanpham") int id,
            @RequestParam("soluong") int soluong,
            @RequestParam("ten") String ten) {

        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
        }
        System.out.println("TongGiaTriGioHangVi: " + session.getAttribute("TongGiaTriGioHangVi"));
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        // Cập nhật giỏ hàng
        cartMap.put(id, cartMap.getOrDefault(id, 0) + soluong);
        session.setAttribute("cart", cartMap);

        // Cập nhật lại số lượng và tổng giá trị giỏ hàng
        capNhatThongTinGioHang();

        SanPham sp = dvlSanPham.xemSP(id);
        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("total", session.getAttribute("SoSanPhamTrongGioHang"));
        data.put("price", sp.getDonGia());
        data.put("totalCart", session.getAttribute("TongGiaTriGioHang"));
        data.put("totalCartVi", tongGiaTriGioHangVi());

        return new ResponseEntity<>(data, HttpStatus.OK);
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
        try {
            if (!gioHangCoSanPham())
                return "client/giohang/giohang-trong.html";

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
            return "client/giohang/giohang-ajax.html";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/500"; // Return an error page
        }

    }

    @GetMapping("/trangchu/giohang")
    public String getGioHangChiTiet(Model model) {
        if (!gioHangCoSanPham()) {
            model.addAttribute("content", "client/giohang/giohang-trong.html");

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
    public ResponseEntity<Map<String, Object>> postGioHangSuaAjax(
            @RequestParam("id_sanpham") int id,
            @RequestParam("so_luong") int soluong,
            @RequestParam("ten") String ten) {

        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        // Cập nhật số lượng sản phẩm trong giỏ hàng
        if (soluong > 0) {
            cartMap.put(id, soluong);
        } else {
            cartMap.remove(id);
        }
        session.setAttribute("cart", cartMap);

        // Cập nhật lại thông tin giỏ hàng
        capNhatThongTinGioHang();

        SanPham sp = dvlSanPham.xemSP(id);
        float thanhTien = soluong * sp.getDonGia();

        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("donGiaVi", sp.getDonGia());
        data.put("total_sp", String.format("%,.0f", thanhTien));
        data.put("total_quantity", session.getAttribute("SoSanPhamTrongGioHang"));
        data.put("totalCartVi", tongGiaTriGioHangVi());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(path = "/giohang/xoa/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postGioHangXoaAjax(
            @RequestParam("id_sanpham") int id) {

        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cartMap.containsKey(id)) {
            cartMap.remove(id);
        }
        session.setAttribute("cart", cartMap);

        // Cập nhật lại thông tin giỏ hàng
        capNhatThongTinGioHang();

        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("total", session.getAttribute("SoSanPhamTrongGioHang"));
        data.put("totalCart", session.getAttribute("TongGiaTriGioHang"));
        data.put("totalCartVi", tongGiaTriGioHangVi());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/trangchu/giohang/thanhtoan")
    public String getGioHangThanhToan(Model model) {
        if (!gioHangCoSanPham()) {
            model.addAttribute("content", "trangchu/giohang-trong.html");

            return "layout/layout-client.html";
        }

        // Kiểm tra xem, khách hàng là vãng lai(anonymous), hay thành viên (membership)
        int maKhachHang = 0; // vãng lai
        var dh = new DonHang();

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
        model.addAttribute("tongGiaTriGioHang", tongGiaTriGioHang());
        model.addAttribute("tongGiaTriGioHangVi", tongGiaTriGioHangVi());

        model.addAttribute("dl", dh); // gửi dữ liệu khách hàng sang
        model.addAttribute("maKhachHang", maKhachHang); // gửi dữ liệu khách hàng sang
        model.addAttribute("content", "/client/giohang/thanhtoan.html");

        return "layout/layout-client.html";

    }

    @PostMapping("/trangchu/giohang/thanhtoan/thanhcong")
    public String xuLyDonHangThanhToan(@RequestParam("email") String email,
            @RequestParam("dienThoai") String dienThoai,
            @RequestParam("tenDayDu") String tenDayDu,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("ghiChu") String ghiChu,
            Model model) {
        try {
            // Lấy giỏ hàng từ session
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cartMap == null || cartMap.isEmpty()) {
                return "redirect:/trangchu/giohang"; // Nếu giỏ hàng trống, chuyển về giỏ hàng
            }

            // Tạo đơn hàng mới
            DonHang donHang = new DonHang();
            donHang.setNgayTao(LocalDate.now()); // Ngày tạo đơn hàng

            // Kiểm tra xem khách hàng đã đăng nhập hay chưa
            Integer maKhachHang = (Integer) session.getAttribute("maKhachHang");
            if (maKhachHang != null) {
                // Khách hàng là thành viên
                KhachHang khachHang = dvlKhachHang.xemKH(maKhachHang); // Lấy thông tin khách hàng từ database
                donHang.setKhachHang(khachHang); // Gán đối tượng khách hàng vào đơn hàng
            } else {
                // Khách vãng lai
                donHang.setEmail(email);
                donHang.setDienThoai(dienThoai);
                donHang.setTenDayDu(tenDayDu);
                donHang.setDiaChi(diaChi);
            }

            donHang.setGhiChu(ghiChu);
            donHang.setTrangThai(DonHang.TrangThaiDonHang.MOI); // Đơn hàng đã thanh toán thành công

            // Tính tổng tiền đơn hàng
            float tongTien = tongGiaTriGioHang();
            donHang.setTongTien(String.valueOf(tongTien));

            // Lưu đơn hàng vào cơ sở dữ liệu
            dvlDonHang.luuDH(donHang);

            // Tạo danh sách chi tiết đơn hàng và lưu vào cơ sở dữ liệu
            for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
                Integer idSanPham = entry.getKey();
                Integer soLuong = entry.getValue();
                SanPham sanPham = dvlSanPham.xemSP(idSanPham);

                ChiTietDH chiTiet = new ChiTietDH();
                chiTiet.setDonHang(donHang); // Gán đơn hàng vào chi tiết đơn hàng
                chiTiet.setSanPham(sanPham); // Gán sản phẩm vào chi tiết đơn hàng
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(sanPham.getDonGia());
                chiTiet.setTongTien(soLuong * sanPham.getDonGia());
                chiTiet.setNgayTao(LocalDate.now()); // Ngày tạo chi tiết đơn hàng

                // Lưu chi tiết đơn hàng vào cơ sở dữ liệu
                dvlChiTietDH.luuCTDH(chiTiet);
            }

            // Xóa giỏ hàng khỏi session sau khi thanh toán thành công
            session.removeAttribute("cart");
            session.setAttribute("SoSanPhamTrongGioHang", 0);
            session.setAttribute("TongGiaTriGioHangVi", "0");

            return "redirect:/trangchu";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/500"; // Xử lý lỗi và hiển thị trang lỗi
        }
    }

}
