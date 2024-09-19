package spring.jsb_organic.admin.khachhang;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlKhachHang {
    @Autowired
    private KdlKhachHang kdl;

    public List<KhachHang> dskhachhang() {
        return kdl.findAll();
    }

    public List<KhachHang> duyetKH() {
        return kdl.findAll();
    }

    public KhachHang timKH(int id) {

        KhachHang dl = null;

        Optional<KhachHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public KhachHang xemKH(int id) {

        KhachHang dl = null;

        Optional<KhachHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }
        return dl;

    }

    public void luuKH(KhachHang dl) {
        this.kdl.save(dl);
    }

    public void xoaKH(int id) {
        this.kdl.deleteById(id);
    }

}
