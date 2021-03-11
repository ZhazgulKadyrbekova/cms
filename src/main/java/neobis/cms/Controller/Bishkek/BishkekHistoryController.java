package neobis.cms.Controller.Bishkek;

import neobis.cms.Entity.Bishkek.BishHistory;
import neobis.cms.Service.Bishkek.BishHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/history")
public class BishkekHistoryController {
    @Autowired
    private BishHistoryService historyService;

    @GetMapping
    public List<BishHistory> getAll() {
        return historyService.getAll();
    }

//    @GetMapping("/search")
//    public String getWithPredicate() {
//        return null;
//    }
}
