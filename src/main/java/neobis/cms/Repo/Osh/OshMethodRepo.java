package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshMethodRepo extends JpaRepository<OshMethod, Long> {
    OshMethod findByNameContainingIgnoringCase(String name);
}
