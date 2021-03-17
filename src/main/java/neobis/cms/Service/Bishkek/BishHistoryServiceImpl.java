package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.StatisticResponse;
import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Repo.Bishkek.BishHistoryRepo;
import neobis.cms.Repo.Bishkek.BishOccupationRepo;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Bishkek.BishUTMRepo;
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
public class BishHistoryServiceImpl implements BishHistoryService {
    @Autowired
    private BishHistoryRepo bishHistoryRepo;

    @Autowired
    private BishStatusesRepo statusesRepo;

    @Autowired
    private BishOccupationRepo occupationRepo;

    @Autowired
    private BishUTMRepo utmRepo;

    @Autowired
    private BishCoursesService coursesService;

    @Override
    public BishHistory create(BishHistory bishHistory) {
        return bishHistoryRepo.save(bishHistory);
    }

    @Override
    public Page<BishHistory> getAll(Pageable pageable) {
        List<BishHistory> histories = bishHistoryRepo.findAllByDateCreatedAfter(LocalDateTime.now().minusDays(3L));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), histories.size());
        final Page<BishHistory> page = new PageImpl<>(histories.subList(start, end), pageable, histories.size());

        return page;
    }

    @Override
    public List<StatisticResponse> getStatistic(LocalDateTime dateAfter, LocalDateTime dateBefore, String action) {
        List<StatisticResponse> responses = new ArrayList<>();
        List<String> actionList = new ArrayList<>();
        switch (action) {
            case "status" :
            	for (BishStatuses status : statusesRepo.findAll()) 
                	actionList.add(status.getName());
                break;
            case "occupation" :
            	for (BishOccupation occupation : occupationRepo.findAll()) 
                	actionList.add(occupation.getName());
                break;
            case "utm" :
            	for (BishUTM utm : utmRepo.findAll()) 
                	actionList.add(utm.getName());
                break;
            case "course" :
            	for (BishCourses courses : coursesService.findAll())
                	actionList.add(courses.getName());
                break;
            
        }
        for (String item : actionList) {
            List<BishHistory> histories = bishHistoryRepo.findAllByDateCreatedBetweenAndActionContainingAndNewDataContaining(
                    dateAfter, dateBefore, action, item);
            List<StatisticResponse.Dates> datesList = new ArrayList<>();
            Map<LocalDate, Integer> dates = new HashMap<>();
            int total = histories.size();
            for (BishHistory history : histories) {
                LocalDate date = history.getDateCreated().toLocalDate();
                if (dates.containsKey(date))
                    dates.put(date, dates.get(date) + 1);
                else dates.put(date, 1);
            }
            for (Map.Entry<LocalDate, Integer> map : dates.entrySet()) {
                datesList.add(new StatisticResponse.Dates(map.getKey(), map.getValue()));
            }
//            List<BishHistory> histories = bishHistoryRepo.findAllByDateCreatedBetweenAndActionContainingAndNewDataContaining(
//                    dateAfter, dateBefore, action, item);
//            List<StatisticResponse.Dates> dates = new ArrayList<>();
//            int total = histories.size();
//            for (BishHistory history : histories) {
//                StatisticResponse.Dates date = new StatisticResponse.Dates();
//                LocalDate localDate = history.getDateCreated().toLocalDate();
//                if (dates.contains(new StatisticResponse.Dates(localDate, 0)))
//                    dates.put(date, dates.get(date) + 1);
//                else dates.put(date, 1);
//            }
            StatisticResponse response = new StatisticResponse();
            response.setFilter(action);
            response.setName(item);
            response.setTotalValue(total);
            response.setDates(datesList);
            responses.add(response);
        }
//        List<BishHistory> histories = bishHistoryRepo.findAllByDateCreatedBetweenAndActionContaining(dateAfter, dateBefore, action);
//        List<BishHistory> list1 =
//        List<StatisticResponse> responses = new ArrayList<>();
//        List<BishHistory> histories = bishHistoryRepo.findAllByDateCreatedBetween(dateAfter, dateBefore);
//        for (BishHistory history : histories) {
//
//        }
        return responses;
    }
}
