package spring.jsb_organic.admin.phanhoi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlPhanHoi {
    @Autowired
    private KdlPhanHoi kdl;

    public List<PhanHoi> dsPhanHoi() {
        return kdl.findAll();
    }

    public List<PhanHoi> duyetPH() {
        return kdl.findAll();
    }

    public PhanHoi timPH(int id) {

        PhanHoi dl = null;

        Optional<PhanHoi> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public PhanHoi xemPH(int id) {

        PhanHoi dl = null;

        Optional<PhanHoi> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }
        return dl;

    }

    public void luuPH(PhanHoi dl) {
        this.kdl.save(dl);
    }

    public void xoaPH(int id) {
        this.kdl.deleteById(id);
    }
}
