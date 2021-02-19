package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishStudentRepo extends JpaRepository<BishStudent, Long> {
}
