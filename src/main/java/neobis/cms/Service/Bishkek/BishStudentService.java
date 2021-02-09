package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.Student;

import java.util.List;

public interface BishStudentService {
    List<Student> findAll();
    Student save(Student student);
}
