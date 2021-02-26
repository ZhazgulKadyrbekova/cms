package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Entity.Bishkek.BishPayment;

import java.util.List;

public interface BishPaymentService {
    List<BishPayment> getAll();
    List<BishPayment> getAllByClient(long id);
    BishPayment create(PaymentDTO paymentDTO);
}
