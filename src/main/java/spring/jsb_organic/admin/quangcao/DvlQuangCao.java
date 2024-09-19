package spring.jsb_organic.admin.quangcao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlQuangCao {
    @Autowired
    private KdlQuangCao kdl;

    public List<QuangCao> dsQuangCao() {
        return kdl.findAll();
    }

    public List<QuangCao> dsQCChoPhep() {
        return kdl.findByChoPhep(true);
    }

    public List<QuangCao> duyetQC() {
        return kdl.findAll();
    }

    public QuangCao timQC(int id) {

        QuangCao dl = null;

        Optional<QuangCao> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public QuangCao xemQC(int id) {

        QuangCao dl = null;

        Optional<QuangCao> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {
        }

        return dl;

    }

    public void luuQC(QuangCao dl) {
        this.kdl.save(dl);
    }

    public void xoaQC(int id) {
        this.kdl.deleteById(id);
    }

}
