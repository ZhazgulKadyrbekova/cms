package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshStatusesRepo extends JpaRepository<OshStatuses, Long> {
    OshStatuses findByNameContainingIgnoringCase(String name);
    List<OshStatuses> findAllByDoska(boolean doska);
    List<OshStatuses> findAllByOrderByDateCreatedDesc();
}
