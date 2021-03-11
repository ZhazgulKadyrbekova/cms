package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OshHistoryRepo extends JpaRepository<OshHistory, Long> {
    List<OshHistory> findAllByDateCreatedAfter(LocalDateTime dateTime);
    List<OshHistory> findAllByDateCreatedBetweenAndActionContainingAndNewDataContaining(LocalDateTime dateAfter, LocalDateTime dateBefore, String action, String newData);
}
