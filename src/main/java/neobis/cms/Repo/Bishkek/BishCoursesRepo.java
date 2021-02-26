package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishCoursesRepo extends JpaRepository<BishCourses, Long> {
    List<BishCourses> findAllByNameContainingIgnoringCaseAndDeleted(String name, boolean deleted);
    BishCourses findByNameContainingIgnoringCaseAndDeleted(String name, boolean deleted);
    List<BishCourses> findAllByDeleted(boolean deleted);
}
