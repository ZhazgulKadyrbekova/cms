package neobis.cms.Service.Osh;

import neobis.cms.Entity.Osh.OshStudent;
import neobis.cms.Repo.Osh.OshStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OshStudentServiceImpl implements OshStudentService {
    @Autowired
    private OshStudentRepo studentRepo;

    @Override
    public List<OshStudent> findAll() {
        return studentRepo.findAll();
    }

    @Override
    public OshStudent save(OshStudent oshStudent) {
        return studentRepo.save(oshStudent);
    }
}
