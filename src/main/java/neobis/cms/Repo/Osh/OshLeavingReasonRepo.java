package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshLeavingReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OshLeavingReasonRepo extends JpaRepository<OshLeavingReason, Long> {
    List<OshLeavingReason> findAllByNameContainingIgnoringCase(String name);
    Optional<OshLeavingReason> findByNameContainingIgnoringCase(String name);
}
