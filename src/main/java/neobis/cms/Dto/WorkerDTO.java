package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class WorkerDTO {
    private long workerID;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNo;
    private String position;
    private String courseName;
    private String table;
    private String patent;
    private LocalDate startDate;
    private LocalDate endDate;
}