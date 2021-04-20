package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.StatusDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Service.Bishkek.BishClientService;
import neobis.cms.Service.Osh.OshClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/status")
public class StatusController {
    private final BishStatusesRepo bishStatusesRepo;
    private final OshStatusesRepo oshStatusesRepo;
    private final BishClientService bishClientService;
    private final OshClientService oshClientService;

    public StatusController(BishStatusesRepo bishStatusesRepo, OshStatusesRepo oshStatusesRepo, BishClientService bishClientService, OshClientService oshClientService) {
        this.bishStatusesRepo = bishStatusesRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishClientService = bishClientService;
        this.oshClientService = oshClientService;
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
        oshStatusesRepo.save(new OshStatuses(status.getName(), status.isDoska()));
        return bishStatusesRepo.save(new BishStatuses(status.getName(), status.isDoska()));
    }

    @PutMapping("/{id}")
    public BishStatuses updateStatusName(@RequestBody StatusDTO status, @PathVariable Long id) {
        OshStatuses oshStatuses = oshStatusesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + id + " не найден."));
        BishStatuses bishStatuses = bishStatusesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + id + " не найден."));

        oshStatuses.setName(status.getName());
        oshStatuses.setDoska(status.isDoska());
        oshStatusesRepo.save(oshStatuses);
        bishStatuses.setName(status.getName());
        bishStatuses.setDoska(status.isDoska());
        return bishStatusesRepo.save(bishStatuses);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteStatusByID(@PathVariable List<Long> id) {
        for (long status : id) {
            BishStatuses bishStatus = bishStatusesRepo.findById(status)
                    .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + status + " не найден."));
            List<BishClient> bishClients = bishClientService.getAllByStatus(status);
            if (!bishClients.isEmpty())
                throw new IllegalArgumentException("Статус " + bishStatus.getName() + "используется, не может быть удален.");

            List<OshClient> oshClients = oshClientService.getAllByStatus(status);
            if (!oshClients.isEmpty())
                throw new IllegalArgumentException("Статус " + bishStatus.getName() + "используется, не может быть удален.");

            bishStatusesRepo.delete(bishStatus);
            oshStatusesRepo.deleteById(status);
        }
        return new ResponseMessage("Status with ID " + id + " has been deleted");
    }
}
