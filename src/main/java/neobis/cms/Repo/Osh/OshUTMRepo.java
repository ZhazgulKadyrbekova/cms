package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshUTM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OshUTMRepo extends JpaRepository<OshUTM, Long> {
    Optional<OshUTM> findByNameContainingIgnoringCase(String name);
    List<OshUTM> findAllByNameContainingIgnoringCase(String name);
}
