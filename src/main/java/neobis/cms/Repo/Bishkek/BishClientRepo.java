package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishClientRepo extends JpaRepository<BishClient, Long> {
    List<BishClient> findAllByIsDeletedOrderByDateCreatedDesc(boolean deleted);
    List<BishClient> findAllByIsDeletedAndStatusIgnoringCaseOrderByDateCreatedDesc(boolean deleted, String status);
}
