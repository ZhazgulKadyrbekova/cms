package neobis.cms.Service.Osh;

import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Entity.Osh.OshPayment;

import java.util.List;

public interface OshPaymentService {
    List<OshPayment> getAll();
    List<OshPayment> getAllByClientID(long clientID);
    OshPayment create(PaymentDTO paymentDTO);
    OshPayment save(OshPayment payment);
    void delete(OshPayment payment);
}
