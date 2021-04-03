package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishTargetRepo extends JpaRepository<BishTarget, Long> {
    BishTarget findByNameContainingIgnoringCase(String name);
}
