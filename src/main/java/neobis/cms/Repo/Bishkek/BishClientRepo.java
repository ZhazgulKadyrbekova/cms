package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishClientRepo extends JpaRepository<BishClient, Long>, JpaSpecificationExecutor<BishClient> {
    List<BishClient> findAllByOrderByDateCreatedDesc();
    Page<BishClient> findAll(Pageable pageable);
    List<BishClient> findAllByStatusOrderByDateCreatedDesc(BishStatuses status);
    List<BishClient> findAllByNameContainingIgnoringCase(String name);
    List<BishClient> findAllBySurnameContainingIgnoringCase(String surname);
    List<BishClient> findAllByPhoneNoContaining(String phoneNo);
    List<BishClient> findAllByEmailContainingIgnoringCase(String email);
    List<BishClient> findAllByOccupation(BishOccupation occupation);
    List<BishClient> findAllByTarget(BishTarget target);
    List<BishClient> findAllByCourse(BishCourses course);

    BishClient findByNameContainingIgnoringCase(String name);
}
