package spring.jsb_organic.admin.sanpham;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        SanPham dl = null;

        Optional<SanPham> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public SanPham xemSP(int id) {

        SanPham dl = null;

        Optional<SanPham> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public void luuSP(SanPham dl) {
        this.kdl.save(dl);
    }

    public void xoaSP(int id) {
        this.kdl.deleteById(id);
    }

}
