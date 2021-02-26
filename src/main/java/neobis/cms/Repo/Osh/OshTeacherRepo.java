package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshTeacherRepo extends JpaRepository<OshTeachers, Long> {
    OshTeachers findByNameContainingIgnoringCaseAndDeleted(String name, boolean deleted);
}
