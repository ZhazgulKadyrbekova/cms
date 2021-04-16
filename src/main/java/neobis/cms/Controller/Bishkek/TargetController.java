package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.TargetDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishTarget;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshTarget;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishClientRepo;
import neobis.cms.Repo.Bishkek.BishTargetRepo;
import neobis.cms.Repo.Osh.OshClientRepo;
import neobis.cms.Repo.Osh.OshTargetRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/target")
public class TargetController {
    private final BishTargetRepo bishTargetRepo;
    private final OshTargetRepo oshTargetRepo;
    private final BishClientRepo bishClientRepo;
    private final OshClientRepo oshClientRepo;

    public TargetController(BishTargetRepo bishTargetRepo, OshTargetRepo oshTargetRepo, BishClientRepo bishClientRepo, OshClientRepo oshClientRepo) {
        this.bishTargetRepo = bishTargetRepo;
        this.oshTargetRepo = oshTargetRepo;
        this.bishClientRepo = bishClientRepo;
        this.oshClientRepo = oshClientRepo;
    }

    @GetMapping
    public List<BishTarget> getAllTargets() {
        return bishTargetRepo.findAll();
    }

    @PostMapping
    public BishTarget addTarget(@RequestBody TargetDTO targetDTO) {
        OshTarget oshTarget = new OshTarget();
        BishTarget bishTarget = new BishTarget();

        oshTarget.setName(targetDTO.getName());
        oshTargetRepo.save(oshTarget);
        bishTarget.setName(targetDTO.getName());
        return bishTargetRepo.save(bishTarget);
    }

    @PutMapping("/{id}")
    public BishTarget updateTarget(@RequestBody TargetDTO targetDTO, @PathVariable Long id) {
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
    public ResponseMessage deleteTargetByID(@PathVariable List<Long> id) {
        for (long occupation : id) {
            BishTarget bishTarget = bishTargetRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Target with id " + occupation + " has not found"));
            List<BishClient> bishClients = bishClientRepo.findAllByTarget(bishTarget);
            if (!bishClients.isEmpty())
                throw new IllegalArgumentException("Цель " + bishTarget.getName() + "используется, не может быть удалена.");

            OshTarget oshTarget = oshTargetRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Target with id " + occupation + " has not found"));
            List<OshClient> oshClients = oshClientRepo.findAllByTarget(oshTarget);
            if (!oshClients.isEmpty())
                throw new IllegalArgumentException("Цель " + oshTarget.getName() + "используется, не может быть удалена.");

            bishTargetRepo.delete(bishTarget);
            oshTargetRepo.delete(oshTarget);
        }
        return new ResponseMessage("Target with ID " + id + " has been deleted");
    }
}

