package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientDTO {
    private String phoneNo;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private long status;
    private long occupation;
    private long target;
    private boolean experience;
    private boolean laptop;
    private long course;
    private String description;
    private LocalDateTime timer;
    private BigDecimal prepayment;
    private long leavingReason;
    private long UTM;

}
