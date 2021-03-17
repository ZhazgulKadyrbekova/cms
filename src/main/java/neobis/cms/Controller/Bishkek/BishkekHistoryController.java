package neobis.cms.Controller.Bishkek;

import neobis.cms.Entity.Bishkek.BishHistory;
import neobis.cms.Service.Bishkek.BishHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/history")
public class BishkekHistoryController {
    @Autowired
    private BishHistoryService historyService;

    @GetMapping
    public Page<BishHistory> getAll(Pageable pageable) {
        return historyService.getAll(pageable);
    }

//    @GetMapping("/search")
//    public String getWithPredicate() {
//        return null;
//    }
}
