package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.StatusDTO;
import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/status")
public class BishkekStatusController {
    @Autowired
    private BishStatusesRepo bishStatusesRepo;

    @Autowired
    private OshStatusesRepo oshStatusesRepo;

    @GetMapping
    public List<BishStatuses> getStatuses() {
        return bishStatusesRepo.findAll();
    }

    @PostMapping
    public BishStatuses addStatus(@RequestBody StatusDTO status) {
        oshStatusesRepo.save(new OshStatuses(0, status.getName()));
        return bishStatusesRepo.save(new BishStatuses(0, status.getName()));
    }

    @PutMapping("/{id}")
    public BishStatuses updateStatusName(@RequestBody StatusDTO status, @PathVariable Long id) {
        OshStatuses oshStatuses = oshStatusesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        BishStatuses bishStatuses = bishStatusesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));

        oshStatuses.setName(status.getName());
        oshStatusesRepo.save(oshStatuses);
        bishStatuses.setName(status.getName());
        return bishStatusesRepo.save(bishStatuses);
    }
}
