package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TeacherDTO {
    private String name;
    private String surname;
    private String email;
    private String phoneNo;
    private long course;
    private String position;
    private String patent;
    private LocalDate startDate;
    private LocalDate endDate;
}
