package neobis.cms.Service.Osh;

import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Entity.Bishkek.BishHistory;
import neobis.cms.Entity.Osh.*;
import neobis.cms.Repo.Osh.OshHistoryRepo;
import neobis.cms.Repo.Osh.OshOccupationRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Repo.Osh.OshUTMRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OshHistoryServiceImpl implements OshHistoryService {
    @Autowired
    private OshHistoryRepo historyRepo;

    @Autowired
    private OshStatusesRepo statusesRepo;

    @Autowired
    private OshOccupationRepo occupationRepo;

    @Autowired
    private OshUTMRepo utmRepo;

    @Autowired
    private OshCoursesService coursesService;

    @Override
    public OshHistory create(OshHistory oshHistory) {
        return historyRepo.save(oshHistory);
    }

    @Override
    public Page<OshHistory> getAll(Pageable pageable) {
        List<OshHistory> histories = historyRepo.findAllByDateCreatedAfter(LocalDateTime.now().minusDays(3L));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), histories.size());
        final Page<OshHistory> page = new PageImpl<>(histories.subList(start, end), pageable, histories.size());

        return page;
    }

    @Override
    public List<StatisticResponse> getStatistic(LocalDateTime dateAfter, LocalDateTime dateBefore, String action) {
        List<StatisticResponse> responses = new ArrayList<>();
        List<String> actionList = new ArrayList<>();
        switch (action) {
            case "status" :
                for (OshStatuses status : statusesRepo.findAll())
                    actionList.add(status.getName());
                break;
            case "occupation" :
                for (OshOccupation occupation : occupationRepo.findAll())
                    actionList.add(occupation.getName());
                break;
            case "utm" :
                for (OshUTM utm : utmRepo.findAll())
                    actionList.add(utm.getName());
                break;
            case "course" :
                for (OshCourses courses : coursesService.findAll())
                    actionList.add(courses.getName());
                break;

        }
        for (String item : actionList) {
            List<OshHistory> histories = historyRepo.findAllByDateCreatedBetweenAndActionContainingAndNewDataContaining(
                    dateAfter, dateBefore, action, item);
            List<StatisticResponse.Dates> datesList = new ArrayList<>();
            Map<LocalDate, Integer> dates = new HashMap<>();
            int total = histories.size();
            for (OshHistory history : histories) {
                LocalDate date = history.getDateCreated().toLocalDate();
                if (dates.containsKey(date))
                    dates.put(date, dates.get(date) + 1);
                else dates.put(date, 1);
            }
            for (Map.Entry<LocalDate, Integer> map : dates.entrySet()) {
                datesList.add(new StatisticResponse.Dates(map.getKey(), map.getValue()));
            }
            StatisticResponse response = new StatisticResponse();
            response.setFilter(action);
            response.setName(item);
            response.setTotalValue(total);
            response.setDates(datesList);
            responses.add(response);
        }
        return responses;
    }
}
