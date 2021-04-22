package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BishHistoryRepo extends JpaRepository<BishHistory, Long> {
    List<BishHistory> findAllByDateCreatedAfterOrderByDateCreatedDesc(LocalDateTime dateTime);
    List<BishHistory> findAllByDateCreatedBetweenAndActionContainingAndNewDataContaining(LocalDateTime dateAfter,
                                                             LocalDateTime dateBefore, String action, String newData);

}
