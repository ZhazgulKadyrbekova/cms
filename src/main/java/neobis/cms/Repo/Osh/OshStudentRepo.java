package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshStudentRepo extends JpaRepository<Student, Long> {
}
