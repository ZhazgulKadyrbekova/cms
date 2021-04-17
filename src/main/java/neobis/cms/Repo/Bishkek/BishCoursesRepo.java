package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishCoursesRepo extends JpaRepository<BishCourses, Long> {
    List<BishCourses> findAllByNameContainingIgnoringCase(String name);
    BishCourses findByNameContainingIgnoringCase(String name);
    BishCourses findByTeacher(BishTeachers teacher);
}
