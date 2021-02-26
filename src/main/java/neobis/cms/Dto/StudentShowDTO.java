package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Bishkek.BishPayment;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentShowDTO {
    private String name;
    private String phoneNo;
    private List<BishPayment> payments;
    private BigDecimal prepayment;
}
