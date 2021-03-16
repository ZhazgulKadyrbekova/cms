package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishLeavingReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BishLeavingReasonRepo extends JpaRepository<BishLeavingReason, Long> {
    List<BishLeavingReason> findAllByNameContainingIgnoringCase(String name);
    Optional<BishLeavingReason> findByNameContainingIgnoringCase(String name);
}
