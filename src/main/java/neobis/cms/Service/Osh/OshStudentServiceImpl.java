package neobis.cms.Service.Osh;

import neobis.cms.Entity.Osh.Student;
import neobis.cms.Repo.Osh.OshStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OshStudentServiceImpl implements OshStudentService {
    @Autowired
    private OshStudentRepo studentRepo;

    @Override
    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    @Override
    public Student save(Student student) {
        return studentRepo.save(student);
    }
}
