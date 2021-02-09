package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishStudentRepo extends JpaRepository<Student, Long> {
}
