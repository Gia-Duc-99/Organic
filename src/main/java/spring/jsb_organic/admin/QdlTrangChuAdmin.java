package spring.jsb_organic.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class QdlTrangChuAdmin {
    @GetMapping({
            "/",
            "/trangchu"
    })
    public String getTrangChu(Model model) {

        model.addAttribute("title", "Trang Chủ");

        model.addAttribute("content", "/admin/trangchu.html");

        return "layout/layout-admin.html";
    }
}


