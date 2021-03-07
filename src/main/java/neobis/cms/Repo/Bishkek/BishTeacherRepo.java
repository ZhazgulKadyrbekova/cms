package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishTeacherRepo extends JpaRepository<BishTeachers, Long> {
    List<BishTeachers> findAllByNameContainingIgnoringCase(String name);
    List<BishTeachers> findAllBySurnameContainingIgnoringCase(String surname);
    BishTeachers findByNameContainingIgnoringCase(String name);
}
