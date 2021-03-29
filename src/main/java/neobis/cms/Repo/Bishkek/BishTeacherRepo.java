package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishTeacherRepo extends JpaRepository<BishTeachers, Long>, JpaSpecificationExecutor<BishTeachers> {
    List<BishTeachers> findAllByNameContainingIgnoringCase(String name);
    List<BishTeachers> findAllBySurnameContainingIgnoringCase(String surname);
    List<BishTeachers> findAllByPhoneNoContaining(String phoneNo);
    BishTeachers findByNameContainingIgnoringCase(String name);
    List<BishTeachers> findAllByPositionContainingIgnoringCaseAndCourseNameContainingIgnoringCase(String position, 
    	String courseName);
    List<BishTeachers> findAllByPositionContainingIgnoringCase(String position);
    List<BishTeachers> findAllByCourseNameContainingIgnoringCase(String course);
    List<BishTeachers> findAllByEmailContainingIgnoringCase(String email);
}
