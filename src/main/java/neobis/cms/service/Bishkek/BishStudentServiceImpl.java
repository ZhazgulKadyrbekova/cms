package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.BishStudent;
import neobis.cms.Repo.Bishkek.BishStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BishStudentServiceImpl implements BishStudentService {
    @Autowired
    private BishStudentRepo studentRepo;

    @Override
    public List<BishStudent> findAll() {
        return studentRepo.findAll();
    }

    @Override
    public BishStudent save(BishStudent bishStudent) {
        return studentRepo.save(bishStudent);
    }
}
