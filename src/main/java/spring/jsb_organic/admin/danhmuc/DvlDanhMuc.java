package spring.jsb_organic.admin.danhmuc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlDanhMuc {

    @Autowired
    private KdlDanhMuc kdl;

    public List<DanhMuc> dsDanhMuc()
    {
        return kdl.findAll();
    }
    public List<DanhMuc> duyetDM()
    {
        return kdl.findAll();
    }
    public DanhMuc timDMTheoId(int id)
    {
        DanhMuc dl = null;
        Optional<DanhMuc> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        }
        else{

        }
        return dl;
    }
    public DanhMuc xemDM(int id)
    {
        DanhMuc dl = null;
        Optional<DanhMuc> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        }else{

        }
        return dl;
    }
    public void luuDM(DanhMuc dl)
    {
        this.kdl.save(dl);
    }


    public void xoaDM(int id)
    {
        this.kdl.deleteById(id);
    }
}