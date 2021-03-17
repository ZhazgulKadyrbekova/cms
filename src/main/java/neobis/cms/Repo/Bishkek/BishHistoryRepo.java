package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BishHistoryRepo extends JpaRepository<BishHistory, Long> {
//    Page<BishHistory> findAllByDateCreatedAfter(Pageable pageable);
    List<BishHistory> findAllByDateCreatedAfter(LocalDateTime dateTime);
    List<BishHistory> findAllByDateCreatedBetweenAndActionContainingAndNewDataContaining(LocalDateTime dateAfter, LocalDateTime dateBefore, String action, String newData);

}
