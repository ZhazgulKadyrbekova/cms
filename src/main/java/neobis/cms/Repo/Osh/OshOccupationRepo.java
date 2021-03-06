package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshOccupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshOccupationRepo extends JpaRepository<OshOccupation, Long> {
    OshOccupation findByNameContainingIgnoringCase(String name);
}
