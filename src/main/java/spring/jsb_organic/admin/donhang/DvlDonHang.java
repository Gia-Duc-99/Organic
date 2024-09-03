package spring.jsb_organic.admin.donhang;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlDonHang {
    @Autowired
    private KdlDonHang kdl;

    public List<DonHang> dsDonHang() {
        return kdl.findAll();
    }

    public List<DonHang> duyetDH() {
        return kdl.findAll();
    }

    public DonHang timDH(int id) {

        DonHang dl = null;

        Optional<DonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public DonHang xemDH(int id) {

        DonHang dl = null;

        Optional<DonHang> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }
        return dl;

    }

    public void luuDH(DonHang dl) {
        this.kdl.save(dl);
    }

    public void xoaDH(int id) {
        this.kdl.deleteById(id);
    }

}
