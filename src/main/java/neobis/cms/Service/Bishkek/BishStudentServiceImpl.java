package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.Student;
import neobis.cms.Repo.Bishkek.BishStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BishStudentServiceImpl implements BishStudentService {
    @Autowired
    private BishStudentRepo studentRepo;

    @Override
    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    @Override
    public Student save(Student student) {
        return studentRepo.save(student);
    }
}
