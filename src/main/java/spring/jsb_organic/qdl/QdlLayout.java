package spring.jsb_organic.qdl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import spring.jsb_organic.admin.caidat.DvlCaiDat;

@ControllerAdvice
public class QdlLayout {
    @Autowired
    DvlCaiDat dvlCaiDat;

    @ModelAttribute("caiDatTinh")
    public HashMap<String, String> getCaiDatTinh(){
        var map = new HashMap<String, String>();
        map.put("staticAppNam", "Web Shop Blog");
        map.put("staticTel", "19001010");

        return map;
    }
    @ModelAttribute("caidat")
    public HashMap<String, String> getCaiDat() {
        var map = new HashMap<String, String>();

        var list = dvlCaiDat.duyetCaiDat();
        for(var obj : list){
            map.put(obj.getKhoa(), obj.getGiaTri());
        }
        return map;
    }
}
