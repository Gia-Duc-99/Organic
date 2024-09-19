package spring.jsb_organic.admin.chitietdonhang;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlCHiTietDH {
    @Autowired
    private KdlChiTietDH kdl;

    public List<ChiTietDH> dsChiTietDH() {
        return kdl.findAll();
    }

    public List<ChiTietDH> duyetCTDH() {
        return kdl.findAll();
    }

    public ChiTietDH timDH(int id) {

        ChiTietDH dl = null;

        Optional<ChiTietDH> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public ChiTietDH xemCTDH(int id) {

        ChiTietDH dl = null;

        Optional<ChiTietDH> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }
        return dl;

    }

    public void luuCTDH(ChiTietDH dl) {
        this.kdl.save(dl);
    }

    public void xoaCTDH(int id) {
        this.kdl.deleteById(id);
    }
}
