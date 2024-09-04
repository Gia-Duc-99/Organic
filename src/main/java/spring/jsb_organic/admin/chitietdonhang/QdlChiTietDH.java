package spring.jsb_organic.admin.chitietdonhang;

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
public class QdlChiTietDH {
    @Autowired
    private DvlCHiTietDH dvl;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @GetMapping({
            "/chitietdh",
            "/chitietdh/duyet"
    })
    public String getDuyet(Model model) {
        // Lưu URI_BEFORE_LOGIN vào session
        session.setAttribute("URI_BEFORE_LOGIN", request.getRequestURI());

        if (Qdl.NVChuaDangNhap(request)) {
            return "redirect:/admin/nhanvien/dangnhap";
        }

        List<ChiTietDH> list = dvl.duyetDH();
        ChiTietDH dl = new ChiTietDH();

        model.addAttribute("dl", dl);

        model.addAttribute("ds", list);

        model.addAttribute("title", "Quản Lý Chi Tiết Đơn Hàng");   

        model.addAttribute("content", "/admin/chitietdh/duyet.html");

        return "layout/layout-admin.html";
    }

    @GetMapping("/chitietdh/xem")
    public String getXem(Model model, @RequestParam("id") int id) {
        if (Qdl.NVChuaDangNhap(request))
            return "redirect:/admin/nhanvien/dangnhap";

        ChiTietDH dl = dvl.xemDH(id);

        model.addAttribute("dl", dl);

        model.addAttribute("content", "/admin/chitietdh/xem.html");

        return "admin/chitietdh/xem.html";
    }
}
