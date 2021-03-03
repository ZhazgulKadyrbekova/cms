package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishStatusesRepo extends JpaRepository<BishStatuses, Long> {
    BishStatuses findByNameContainingIgnoringCase(String name);
}
