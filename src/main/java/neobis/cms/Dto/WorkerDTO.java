package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class WorkerDTO {
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNo;
    private String position;
    private String courseName;
}