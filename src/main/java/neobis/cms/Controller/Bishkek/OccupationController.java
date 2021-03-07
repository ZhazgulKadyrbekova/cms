package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.OccupationDTO;
import neobis.cms.Entity.Bishkek.BishOccupation;
import neobis.cms.Entity.Osh.OshOccupation;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishOccupationRepo;
import neobis.cms.Repo.Osh.OshOccupationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/occupation")
public class OccupationController {
    @Autowired private BishOccupationRepo bishOccupationRepo;
    @Autowired private OshOccupationRepo oshOccupationRepo;

    @GetMapping
    public List<BishOccupation> getOccupations() {
        return bishOccupationRepo.findAll();
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
}
