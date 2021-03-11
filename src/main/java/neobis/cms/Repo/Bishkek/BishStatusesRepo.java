package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishStatusesRepo extends JpaRepository<BishStatuses, Long> {
    BishStatuses findByNameContainingIgnoringCase(String name);
    List<BishStatuses> findAllByNameContainingIgnoringCase(String name);
    List<BishStatuses> findAllByDoska(boolean doska);
    List<BishStatuses> findAllByOrderByDateCreatedAsc();
//    List<BishStatuses> findAllOrderByDateCreatedDesc();
}
