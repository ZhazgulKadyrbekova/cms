package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OshClientRepo extends JpaRepository<OshClient, Long>, JpaSpecificationExecutor<OshClient> {
    List<OshClient> findAllByOrderByDateCreatedDesc();
    List<OshClient> findAllByStatusOrderByDateCreatedDesc(OshStatuses status);
    List<OshClient> findAllByNameContainingIgnoringCase(String name);
    List<OshClient> findAllBySurnameContainingIgnoringCase(String surname);
    List<OshClient> findAllByPhoneNoContaining(String phoneNo);
    List<OshClient> findAllByEmailContainingIgnoringCase(String email);
    List<OshClient> findAllByOccupation(OshOccupation occupation);
    List<OshClient> findAllByTarget(OshTarget target);
    List<OshClient> findAllByCourse(OshCourses course);
    List<OshClient> findAllByTimerBefore(LocalDateTime timer);
    OshClient findByNameContainingIgnoringCase(String name);
}
