package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshCoursesRepo extends JpaRepository<OshCourses, Long> {
    List<OshCourses> findAllByNameContainingIgnoringCase(String name);
    OshCourses findByNameContainingIgnoringCase(String name);
}
