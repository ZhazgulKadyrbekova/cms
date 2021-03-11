package neobis.cms.Controller.Osh;

import io.swagger.annotations.ApiParam;
import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Service.Osh.OshHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/statistic")
public class OshStatisticController {
    @Autowired
    private OshHistoryService historyService;

    @GetMapping
    public List<StatisticResponse> getStatistic(@ApiParam(value="yyyy-MM-dd HH:mm") @RequestParam String dateAfter,
                                                @ApiParam(value="yyyy-MM-dd HH:mm") @RequestParam String dateBefore,
                                                @RequestParam String filter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime1 = LocalDateTime.parse(dateAfter, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateBefore, formatter);
        return historyService.getStatistic(dateTime1, dateTime2, filter);
    }
}
