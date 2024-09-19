package spring.jsb_organic.admin.caidat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DvlCaiDat {

    @Autowired
    private KdlCaiDat kdl;

    public List<CaiDat> dsCaiDat()
    {
        return kdl.findAll();
    }
    public List<CaiDat> duyetCaiDat()
    {
        return kdl.findAll();
    }
    public CaiDat timCaiDatTheoId(int id)
    {
        CaiDat dl = null;
        Optional<CaiDat> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        }
        else{

        }
        return dl;
    }
    public CaiDat xemCaiDat(int id)
    {
        CaiDat dl = null;
        Optional<CaiDat> optional = kdl.findById(id);
        if (optional.isPresent()) {
            dl = optional.get();
        }else{

        }
        return dl;
    }
    public void luuCaiDat(CaiDat dl)
    {
        this.kdl.save(dl);
    }


    public void xoaCaiDat(int id)
    {
        this.kdl.deleteById(id);
    }
}