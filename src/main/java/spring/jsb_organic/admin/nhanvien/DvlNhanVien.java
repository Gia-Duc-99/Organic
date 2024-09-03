package spring.jsb_organic.admin.nhanvien;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlNhanVien {
    @Autowired
    private KdlNhanVien kdl;

    public List<NhanVien> dsNhanVien() {
        return kdl.findAll();
    }

    public List<NhanVien> duyetNhanVien() {
        return kdl.findAll();
    }

    public NhanVien timNhanVienTheoId(int id) {

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public NhanVien xemNhanVien(int id) {

        NhanVien dl = null;

        Optional<NhanVien> optional = kdl.findById(id);

        if (optional.isPresent()) {
            dl = optional.get();
        } else {

        }

        return dl;

    }

    public void luuNhanVien(NhanVien dl) {
        this.kdl.save(dl);
    }

    public void xoaNhanVien(int id) {
        this.kdl.deleteById(id);
    }

    public NhanVien timNVTheoTenDangNhap(String tdn) {

        NhanVien dl = null;

        dl = kdl.findOneByTenDangNhap(tdn);

        return dl;
    }

    public Boolean tonTaiTenDangNhap(String tdn) {
        return kdl.existsByTenDangNhap(tdn);
    }

}
