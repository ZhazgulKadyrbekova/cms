package neobis.cms.Controller.Osh;

import neobis.cms.Entity.Osh.OshHistory;
import neobis.cms.Service.Osh.OshHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/history")
public class OshHistoryController {
    @Autowired
    private OshHistoryService historyService;

    @GetMapping
    public List<OshHistory> getAll() {
        return historyService.getAll();
    }
}
