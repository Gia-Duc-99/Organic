package spring.jsb_organic.admin.nhacungcap;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlNhaCungCap {
    @Autowired
    private KdlNhaCungCap kdl;

    public List<NhaCungCap> dsNhaCungCap() {
        return kdl.findAll();
    }

    public List<NhaCungCap> duyetNCC() {
        return kdl.findAll();
    }

    public NhaCungCap timNCC(int id) {

        NhaCungCap dl = null;

        Optional<NhaCungCap> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public NhaCungCap xemNCC(int id) {

        NhaCungCap dl = null;

        Optional<NhaCungCap> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }
        return dl;

    }

    public void luuNCC(NhaCungCap dl) {
        this.kdl.save(dl);
    }

    public void xoaNCC(int id) {
        this.kdl.deleteById(id);
    }

}
