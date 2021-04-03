package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.TargetDTO;
import neobis.cms.Entity.Bishkek.BishTarget;
import neobis.cms.Entity.Osh.OshTarget;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishTargetRepo;
import neobis.cms.Repo.Osh.OshTargetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/target")
public class TargetController {
    @Autowired private BishTargetRepo bishTargetRepo;
    @Autowired private OshTargetRepo oshTargetRepo;

    @GetMapping
    public List<BishTarget> getAllTargets() {
        return bishTargetRepo.findAll();
    }

    @PutMapping("/{id}")
    public BishTarget updatePosition(@RequestBody TargetDTO targetDTO, @PathVariable Long id) {
        OshTarget oshTarget = oshTargetRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target with id " + id + " has not found"));
        BishTarget bishTarget = bishTargetRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target with id " + id + " has not found"));

        oshTarget.setName(targetDTO.getName());
        oshTargetRepo.save(oshTarget);
        bishTarget.setName(targetDTO.getName());
        return bishTargetRepo.save(bishTarget);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deletePositionByID(@PathVariable List<Long> id) {
        for (long occupation : id) {
            BishTarget bishTarget = bishTargetRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Target with id " + occupation + " has not found"));
            OshTarget oshTarget = oshTargetRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Target with id " + occupation + " has not found"));

            bishTargetRepo.delete(bishTarget);
            oshTargetRepo.delete(oshTarget);
        }
        return new ResponseMessage("Target with ID " + id + " has been deleted");
    }
}

