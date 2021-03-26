package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishOccupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BishOccupationRepo extends JpaRepository<BishOccupation, Long> {
    List<BishOccupation> findAllByNameContainingIgnoringCase(String name);
    BishOccupation findByNameContainingIgnoringCase(String name);
}
