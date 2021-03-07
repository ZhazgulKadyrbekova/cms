package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Entity.Osh.OshClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshClientRepo extends JpaRepository<OshClient, Long>, JpaSpecificationExecutor<OshClient> {
    List<OshClient> findAllByOrderByDateCreatedDesc();
    List<OshClient> findAllByStatusOrderByDateCreatedDesc(OshStatuses status);
    List<OshClient> findAllByNameContainingIgnoringCase(String name);
    List<OshClient> findAllBySurnameContainingIgnoringCase(String surname);
    OshClient findByNameContainingIgnoringCase(String name);
}
