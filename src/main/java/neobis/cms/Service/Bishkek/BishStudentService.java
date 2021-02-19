package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.BishStudent;

import java.util.List;

public interface BishStudentService {
    List<BishStudent> findAll();
    BishStudent save(BishStudent bishStudent);
}
