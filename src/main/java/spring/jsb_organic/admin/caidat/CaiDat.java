package spring.jsb_organic.admin.caidat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CaiDat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String khoa;
    private String giaTri;
    
    public Boolean KhongHopLe(){
        var khl = false;
        return khl;
    }
}
