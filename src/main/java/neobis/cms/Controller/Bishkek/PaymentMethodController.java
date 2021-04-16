package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.MethodDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishMethod;
import neobis.cms.Entity.Bishkek.BishPayment;
import neobis.cms.Entity.Osh.OshMethod;
import neobis.cms.Entity.Osh.OshPayment;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishMethodRepo;
import neobis.cms.Repo.Bishkek.BishPaymentRepo;
import neobis.cms.Repo.Osh.OshMethodRepo;
import neobis.cms.Repo.Osh.OshPaymentRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/method")
public class PaymentMethodController {
    private final BishMethodRepo bishMethodRepo;
    private final OshMethodRepo oshMethodRepo;
    private final BishPaymentRepo bishPaymentRepo;
    private final OshPaymentRepo oshPaymentRepo;

    public PaymentMethodController(BishMethodRepo bishMethodRepo, OshMethodRepo oshMethodRepo, OshPaymentRepo oshPaymentRepo, BishPaymentRepo bishPaymentRepo) {
        this.bishMethodRepo = bishMethodRepo;
        this.oshMethodRepo = oshMethodRepo;
        this.oshPaymentRepo = oshPaymentRepo;
        this.bishPaymentRepo = bishPaymentRepo;
    }

    @GetMapping
    public List<BishMethod> getAllMethods() {
        return bishMethodRepo.findAll();
    }

    @PutMapping("/{id}")
    public BishMethod updateMethodByID(@PathVariable Long id, @RequestBody MethodDTO methodDTO) {
        BishMethod bishMethod = bishMethodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Method with id " + id + " has not found"));
        OshMethod oshMethod = oshMethodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Method with id " + id + " has not found"));

        oshMethod.setName(methodDTO.getName());
        oshMethodRepo.save(oshMethod);
        bishMethod.setName(methodDTO.getName());
        return bishMethodRepo.save(bishMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteMethodByID(@PathVariable List<Long> id) {
        for (long methodID : id) {
            BishMethod bishMethod = bishMethodRepo.findById(methodID)
                    .orElseThrow(() -> new ResourceNotFoundException("Method with id " + id + " has not found"));
            List<BishPayment> bishPayments = bishPaymentRepo.findAllByMethod(bishMethod);
            if (!bishPayments.isEmpty())
                throw new IllegalArgumentException("Метод " + bishMethod.getName() + " используется, не может быть удален.");
            OshMethod oshMethod = oshMethodRepo.findById(methodID)
                    .orElseThrow(() -> new ResourceNotFoundException("Method with id " + id + " has not found"));
            List<OshPayment> oshPayments = oshPaymentRepo.findAllByMethod(oshMethod);
            if (!oshPayments.isEmpty())
                throw new IllegalArgumentException("Метод " + oshMethod.getName() + " используется, не может быть удален.");

            oshMethodRepo.delete(oshMethod);
            bishMethodRepo.delete(bishMethod);
        }
        return new ResponseMessage("Methods with ID " + id + " has been deleted");
    }
}
