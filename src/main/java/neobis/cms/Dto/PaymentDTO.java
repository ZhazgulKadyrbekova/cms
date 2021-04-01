package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDTO {
    private String month;
    private BigDecimal price;
    private boolean done;
    private String method;
}
