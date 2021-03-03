package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.History;
import neobis.cms.Repo.Bishkek.HistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryRepo historyRepo;

    @Override
    public History create(History history) {
        return historyRepo.save(history);
    }
}
