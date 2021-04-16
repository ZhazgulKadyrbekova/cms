package neobis.cms.Controller.Bishkek;

import neobis.cms.Entity.Bishkek.BishHistory;
import neobis.cms.Service.Bishkek.BishHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/history")
public class BishkekHistoryController {
    private final BishHistoryService historyService;

    public BishkekHistoryController(BishHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public Page<BishHistory> getAll(Pageable pageable) {
        return historyService.getAll(pageable);
    }
}
