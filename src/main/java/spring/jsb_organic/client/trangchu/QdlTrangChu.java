package spring.jsb_organic.client.trangchu;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.admin.danhmuc.DvlDanhMuc;
import spring.jsb_organic.admin.quangcao.DvlQuangCao;
import spring.jsb_organic.admin.sanpham.DvlSanPham;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QdlTrangChu {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private DvlQuangCao dvlQuangCao;

    @Autowired
    private DvlSanPham dvlSanPham;

    @Autowired
    private DvlDanhMuc dvlDanhMuc;

    @GetMapping({
            "/",
            "/trangchu"
    })
    public String getTrangChu(Model model) {
        model.addAttribute("dsQuangCao", dvlQuangCao.dsQCChoPhep());
        model.addAttribute("dsSanPhamNoiBat", dvlSanPham.dsSanPham());

        model.addAttribute("dsSanPham", dvlSanPham.dsSanPham());
        model.addAttribute("dsDanhMuc", dvlDanhMuc.dsDanhMuc());

        model.addAttribute("title", "Trang Chủ");

        model.addAttribute("content", "/client/trangchu/trangchu.html");

        return "layout/layout-client.html";
    }

}
