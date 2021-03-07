package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishPayment;
import neobis.cms.Repo.Bishkek.BishPaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class BishPaymentServiceImpl implements BishPaymentService {
    @Autowired
    private BishPaymentRepo paymentRepo;

    @Autowired
    private BishClientService clientService;

    @Override
    public List<BishPayment> getAll() {
        return paymentRepo.findAll();
    }

    @Override
    public List<BishPayment> getAllByClientID(long id) {
        BishClient client = clientService.getClientById(id);
        return paymentRepo.findAllByClient(client);
    }

    @Override
    public BishPayment create(PaymentDTO paymentDTO) {
        BishPayment payment = new BishPayment();
        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        payment.setMethod(paymentDTO.getMethod());
        payment.setClient(clientService.getClientById(paymentDTO.getClient()));
        return paymentRepo.save(payment);
    }

    @Override
    public BishPayment save(BishPayment payment) {
        return paymentRepo.save(payment);
    }

    @Override
    public void delete(BishPayment payment) {
        paymentRepo.delete(payment);
    }
}
