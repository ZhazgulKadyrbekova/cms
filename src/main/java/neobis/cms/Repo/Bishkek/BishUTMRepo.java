package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishUTM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BishUTMRepo extends JpaRepository<BishUTM, Long> {
    List<BishUTM> findAllByNameContainingIgnoringCase(String name);
    Optional<BishUTM> findByNameContainingIgnoringCase(String name);
}
