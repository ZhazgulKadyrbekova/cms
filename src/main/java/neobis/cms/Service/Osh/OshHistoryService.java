package neobis.cms.Service.Osh;

import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Entity.Osh.OshHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface OshHistoryService {
    OshHistory create(OshHistory oshHistory);
    List<OshHistory> getAll();
    List<StatisticResponse> getStatistic(LocalDateTime dateAfter, LocalDateTime dateBefore, String action);
}
