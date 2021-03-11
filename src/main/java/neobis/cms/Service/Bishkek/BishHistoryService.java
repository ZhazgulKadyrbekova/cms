package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Entity.Bishkek.BishHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface BishHistoryService {
    BishHistory create(BishHistory bishHistory);
    List<BishHistory> getAll();
    List<StatisticResponse> getStatistic(LocalDateTime dateAfter, LocalDateTime dateBefore, String action);
}
