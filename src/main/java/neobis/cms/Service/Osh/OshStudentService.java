package neobis.cms.Service.Osh;

import neobis.cms.Entity.Osh.Student;

import java.util.List;

public interface OshStudentService {
    List<Student> findAll();
    Student save(Student student);
}
