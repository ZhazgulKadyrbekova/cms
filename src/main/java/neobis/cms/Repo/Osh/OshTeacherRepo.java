package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshTeacherRepo extends JpaRepository<OshTeachers, Long> {
    OshTeachers findByNameContainingIgnoringCase(String name);
    List<OshTeachers> findAllByNameContainingIgnoringCase(String name);
    List<OshTeachers> findAllBySurnameContainingIgnoringCase(String name);
}
