package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshTeacherRepo extends JpaRepository<OshTeachers, Long>, JpaSpecificationExecutor<OshTeachers> {
    List<OshTeachers> findAllByPositionContainingIgnoringCaseAndCourseNameContainingIgnoringCase(String position, 
    	String course);
    List<OshTeachers> findAllByPositionContainingIgnoringCase(String position);
    List<OshTeachers> findAllByCourseNameContaining(String course);
    List<OshTeachers> findAllByEmailContainingIgnoringCase(String email);
    OshTeachers findByNameContainingIgnoringCase(String name);
    List<OshTeachers> findAllByNameContainingIgnoringCase(String name);
    List<OshTeachers> findAllBySurnameContainingIgnoringCase(String name);
    List<OshTeachers> findAllByPhoneNoContaining(String phoneNo);
}
