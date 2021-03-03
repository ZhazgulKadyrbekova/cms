package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshStatusesRepo extends JpaRepository<OshStatuses, Long> {
    OshStatuses findByNameContainingIgnoringCase(String name);
}
