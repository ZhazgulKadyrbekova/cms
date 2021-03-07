package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshOccupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OshOccupationRepo extends JpaRepository<OshOccupation, Long> {
    Optional<OshOccupation> findByNameContainingIgnoringCase(String name);
}
