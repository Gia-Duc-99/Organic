package spring.jsb_organic.admin.quangcao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KdlQuangCao extends JpaRepository<QuangCao, Integer> {
    List<QuangCao> findByChoPhep(Boolean choPhep);
}
