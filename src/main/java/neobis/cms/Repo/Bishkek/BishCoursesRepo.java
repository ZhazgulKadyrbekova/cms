package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishCoursesRepo extends JpaRepository<BishCourses, Long> {
    BishCourses findByNameIgnoringCase(String name);
}
