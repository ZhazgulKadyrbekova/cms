package neobis.cms.Service.Osh;

import neobis.cms.Entity.Osh.OshStudent;

import java.util.List;

public interface OshStudentService {
    List<OshStudent> findAll();
    OshStudent save(OshStudent oshStudent);
}
