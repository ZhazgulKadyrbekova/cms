package neobis.cms.Service.Osh;

import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Entity.Osh.OshPayment;
import neobis.cms.Repo.Osh.OshPaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class OshPaymentServiceImpl implements OshPaymentService{
    @Autowired private OshPaymentRepo paymentRepo;
    @Autowired private OshClientService clientService;

    @Override
    public List<OshPayment> getAll() {
        return paymentRepo.findAll();
    }

    @Override
    public List<OshPayment> getAllByClientID(long clientID) {
        return paymentRepo.findAllByClient(clientService.getClientByID(clientID));
    }

    @Override
    public OshPayment create(PaymentDTO paymentDTO) {
        OshPayment payment = new OshPayment();
        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        payment.setMethod(paymentDTO.getMethod());
        payment.setClient(clientService.getClientByID(paymentDTO.getClient()));
        return paymentRepo.save(payment);
    }

    @Override
    public OshPayment save(OshPayment payment) {
        return paymentRepo.save(payment);
    }

    @Override
    public void delete(OshPayment payment) {
        paymentRepo.delete(payment);
    }
}
