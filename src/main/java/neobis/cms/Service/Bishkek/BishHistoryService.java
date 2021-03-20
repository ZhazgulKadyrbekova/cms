package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Entity.Bishkek.BishHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BishHistoryService {
    BishHistory create(BishHistory bishHistory);
    Page<BishHistory> getAll(Pageable pageable);
    List<StatisticResponse> getStatistic(LocalDateTime dateAfter, LocalDateTime dateBefore, List<Long> status_id, List<Long> course_id);
}
