package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishPosition;
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
    List<BishTeachers> findAllByPosition(BishPosition position);
    List<BishTeachers> findAllByEmailContainingIgnoringCase(String email);
}
