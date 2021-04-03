package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.MethodDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishMethod;
import neobis.cms.Entity.Osh.OshMethod;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishMethodRepo;
import neobis.cms.Repo.Osh.OshMethodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/method")
public class PaymentMethodController {
    @Autowired private BishMethodRepo bishMethodRepo;
    @Autowired private OshMethodRepo oshMethodRepo;

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
            OshMethod oshMethod = oshMethodRepo.findById(methodID)
                    .orElseThrow(() -> new ResourceNotFoundException("Method with id " + id + " has not found"));

            oshMethodRepo.delete(oshMethod);
            bishMethodRepo.delete(bishMethod);
        }
        return new ResponseMessage("Methods with ID " + id + " has been deleted");
    }
}
