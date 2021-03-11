package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.StatusDTO;
import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/status")
public class StatusController {
    private final BishStatusesRepo bishStatusesRepo;

    private final OshStatusesRepo oshStatusesRepo;

    public StatusController(BishStatusesRepo bishStatusesRepo, OshStatusesRepo oshStatusesRepo) {
        this.bishStatusesRepo = bishStatusesRepo;
        this.oshStatusesRepo = oshStatusesRepo;
    }

    @GetMapping
    public List<BishStatuses> getStatuses() {
        return bishStatusesRepo.findAllByOrderByDateCreatedAsc();
    }

    @GetMapping("/doska")
    public List<BishStatuses> getStatusesDoska() {
        return bishStatusesRepo.findAllByDoska(true);
    }

    @GetMapping("/name/{name}")
    public List<BishStatuses> getAllByName(@PathVariable String name) {
        return bishStatusesRepo.findAllByNameContainingIgnoringCase(name);
    }

    @PostMapping
    public BishStatuses addStatus(@RequestBody StatusDTO status) {
        oshStatusesRepo.save(new OshStatuses(0, status.getName(), status.isDoska()));
        return bishStatusesRepo.save(new BishStatuses(0, status.getName(), status.isDoska()));
    }

    @PutMapping("/{id}")
    public BishStatuses updateStatusName(@RequestBody StatusDTO status, @PathVariable Long id) {
        OshStatuses oshStatuses = oshStatusesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        BishStatuses bishStatuses = bishStatusesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));

        oshStatuses.setName(status.getName());
        oshStatuses.setDoska(status.isDoska());
        oshStatusesRepo.save(oshStatuses);
        bishStatuses.setName(status.getName());
        bishStatuses.setDoska(status.isDoska());
        return bishStatusesRepo.save(bishStatuses);
    }
}
