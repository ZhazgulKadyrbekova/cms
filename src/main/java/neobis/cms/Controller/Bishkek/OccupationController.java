package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.OccupationDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishOccupation;
import neobis.cms.Entity.Osh.OshOccupation;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishOccupationRepo;
import neobis.cms.Repo.Osh.OshOccupationRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/occupation")
public class OccupationController {
    private final BishOccupationRepo bishOccupationRepo;
    private final OshOccupationRepo oshOccupationRepo;

    public OccupationController(BishOccupationRepo bishOccupationRepo, OshOccupationRepo oshOccupationRepo) {
        this.bishOccupationRepo = bishOccupationRepo;
        this.oshOccupationRepo = oshOccupationRepo;
    }

    @GetMapping
    public List<BishOccupation> getOccupations() {
        return bishOccupationRepo.findAll();
    }

    @GetMapping("/name/{name}")
    public List<BishOccupation> getAllByName(@PathVariable String name) {
        return bishOccupationRepo.findAllByNameContainingIgnoringCase(name);
    }

    @PutMapping("/{id}")
    public BishOccupation updateOccupationName(@RequestBody OccupationDTO occupationDTO, @PathVariable Long id) {
        OshOccupation oshOccupation = oshOccupationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + id + " has not found"));
        BishOccupation bishOccupation = bishOccupationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + id + " has not found"));

        oshOccupation.setName(occupationDTO.getName());
        oshOccupationRepo.save(oshOccupation);
        bishOccupation.setName(occupationDTO.getName());
        return bishOccupationRepo.save(bishOccupation);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteOccupationByID(@PathVariable List<Long> id) {
        for (long occupation : id) {
            BishOccupation bishOccupation = bishOccupationRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + occupation + " has not found"));
            OshOccupation oshOccupation = oshOccupationRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + occupation + " has not found"));

            bishOccupationRepo.delete(bishOccupation);
            oshOccupationRepo.delete(oshOccupation);
        }
        return new ResponseMessage("Occupation with ID " + id + " has been deleted");
    }
}
