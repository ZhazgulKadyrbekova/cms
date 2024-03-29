package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BishClientRepo extends JpaRepository<BishClient, Long>, JpaSpecificationExecutor<BishClient> {
    List<BishClient> findAllByOrderByDateCreatedDesc();
    List<BishClient> findAllByStatusOrderByDateCreatedDesc(BishStatuses status);
    List<BishClient> findAllByNameContainingIgnoringCase(String name);
    List<BishClient> findAllBySurnameContainingIgnoringCase(String surname);
    List<BishClient> findAllByPhoneNoContaining(String phoneNo);
    List<BishClient> findAllByEmailContainingIgnoringCase(String email);
    List<BishClient> findAllByOccupation(BishOccupation occupation);
    List<BishClient> findAllByTarget(BishTarget target);
    List<BishClient> findAllByCourse(BishCourses course);
    List<BishClient> findAllByTimerBeforeOrderByTimerAsc(LocalDateTime dateTime);
    BishClient findByNameContainingIgnoringCase(String name);
}
