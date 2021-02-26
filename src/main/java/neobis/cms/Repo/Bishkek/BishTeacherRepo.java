package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishTeacherRepo extends JpaRepository<BishTeachers, Long> {
    BishTeachers findByNameContainingIgnoringCaseAndDeleted(String name, boolean deleted);
}
