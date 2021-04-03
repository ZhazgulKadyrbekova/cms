package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishMethodRepo extends JpaRepository<BishMethod, Long> {
    BishMethod findByNameContainingIgnoringCase(String name);
}
