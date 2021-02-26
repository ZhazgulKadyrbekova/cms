package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishClientRepo extends JpaRepository<BishClient, Long> {
    List<BishClient> findAllByDeletedOrderByDateCreatedDesc(boolean deleted);
    List<BishClient> findAllByDeletedAndStatusIgnoringCaseOrderByDateCreatedDesc(boolean deleted, String status);
//    BishClient findByNameContainingIgnoringCase(String name);
    BishClient findByNameContainingIgnoringCaseAndDeleted(String name, boolean deleted);
}
