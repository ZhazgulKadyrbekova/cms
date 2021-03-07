package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishOccupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BishOccupationRepo extends JpaRepository<BishOccupation, Long> {
    Optional<BishOccupation> findByNameContainingIgnoringCase(String name);
}
