package neobis.cms.Controller.Bishkek;

import io.swagger.annotations.ApiParam;
import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Service.Bishkek.BishHistoryService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/statistic")
public class BishkekStatisticController {
    private final BishHistoryService historyService;

    public BishkekStatisticController(BishHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<StatisticResponse> getStatistic(@ApiParam(value="yyyy-MM-dd HH:mm") @RequestParam String dateAfter,
                                                @ApiParam(value="yyyy-MM-dd HH:mm") @RequestParam String dateBefore,
                                                @RequestParam(required = false) List<Long> status_id,
                                                @RequestParam(required = false) List<Long> course_id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime1 = LocalDateTime.parse(dateAfter, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateBefore, formatter);
        return historyService.getStatistic(dateTime1, dateTime2, status_id, course_id);
    }
}
