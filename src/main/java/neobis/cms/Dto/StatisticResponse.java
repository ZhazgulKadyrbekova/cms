package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class StatisticResponse {
    private String filter;
    private String name;
    private int totalValue;
    private List<Dates> dates;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public static class Dates{
        private LocalDate date;
        private int value;
    }
}


