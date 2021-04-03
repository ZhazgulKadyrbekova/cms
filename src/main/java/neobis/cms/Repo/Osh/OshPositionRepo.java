package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshPositionRepo extends JpaRepository<OshPosition, Long> {
    OshPosition findByNameContainingIgnoringCase(String name);
}
