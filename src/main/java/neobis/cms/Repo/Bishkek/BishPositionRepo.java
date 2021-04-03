package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishPositionRepo extends JpaRepository<BishPosition, Long> {
    BishPosition findByNameContainingIgnoringCase(String name);
}
