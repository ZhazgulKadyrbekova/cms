package neobis.cms.Service.Osh;

import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Entity.Osh.OshHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OshHistoryService {
    OshHistory create(OshHistory oshHistory);
    Page<OshHistory> getAll(Pageable pageable);
    List<StatisticResponse> getStatistic(LocalDateTime dateAfter, LocalDateTime dateBefore, String action);
}
