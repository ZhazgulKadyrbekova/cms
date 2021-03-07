package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishClientRepo extends JpaRepository<BishClient, Long>, JpaSpecificationExecutor<BishClient> {
    List<BishClient> findAllByOrderByDateCreatedDesc();
    List<BishClient> findAllByStatusOrderByDateCreatedDesc(BishStatuses status);
    List<BishClient> findAllByNameContainingIgnoringCase(String name);
    List<BishClient> findAllBySurnameContainingIgnoringCase(String surname);
    BishClient findByNameContainingIgnoringCase(String name);
}
