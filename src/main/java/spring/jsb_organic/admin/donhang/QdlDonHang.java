package spring.jsb_organic.admin.donhang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import spring.jsb_organic.qdl.Qdl;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("admin")
public class QdlDonHang {
    @Autowired
    private DvlDonHang dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/donhang",
            "/donhang/duyet"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<DonHang> list = dvl.duyetDH();
        DonHang dl = new DonHang();

        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Quảng Cáo");

        model.addAttribute("content", "/admin/donhang/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/donhang/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        DonHang dl = dvl.xemDH(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/donhang/xem.html");

        return "admin/donhang/xem.html";
    }
}
