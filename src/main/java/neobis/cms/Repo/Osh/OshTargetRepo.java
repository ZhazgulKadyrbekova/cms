package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshTargetRepo extends JpaRepository<OshTarget, Long> {
    OshTarget findByNameContainingIgnoringCase(String name);
}
