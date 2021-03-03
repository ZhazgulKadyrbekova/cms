package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Entity.Osh.OshClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshClientRepo extends JpaRepository<OshClient, Long> {
    List<OshClient> findAllByDeletedOrderByDateCreatedDesc(boolean deleted);
    List<OshClient> findAllByDeletedAndStatusOrderByDateCreatedDesc(boolean deleted, OshStatuses status);
//    List<OshClient> findAllByDeletedAndStatusIgnoringCaseOrderByDateCreatedDesc(boolean deleted, String status);
    OshClient findByNameContainingIgnoringCaseAndDeleted(String name, boolean deleted);
}
