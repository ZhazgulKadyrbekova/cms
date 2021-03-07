package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.UtmDTO;
import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Bishkek.BishUTM;
import neobis.cms.Entity.Osh.OshUTM;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishUTMRepo;
import neobis.cms.Repo.Osh.OshUTMRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/utm")
public class UTMController {
    @Autowired private BishUTMRepo bishUTMRepo;
    @Autowired private OshUTMRepo oshUTMRepo;

    @GetMapping
    public List<BishUTM> getUTMs() {
        return bishUTMRepo.findAll();
    }

    @PostMapping
    public BishUTM addOccupation(@RequestBody UtmDTO utmDTO) {
        oshUTMRepo.save(new OshUTM(0, utmDTO.getName()));
        return bishUTMRepo.save(new BishUTM(0, utmDTO.getName()));
    }

    @GetMapping("/name/{name}")
    public List<BishUTM> getAllByName(@PathVariable String name) {
        return bishUTMRepo.findAllByNameContainingIgnoringCase(name);
    }

    @PutMapping("/{id}")
    public BishUTM updateOccupationName(@RequestBody UtmDTO occupationDTO, @PathVariable Long id) {
        OshUTM oshUTM = oshUTMRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + id + " has not found"));
        BishUTM bishUTM = bishUTMRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + id + " has not found"));

        oshUTM.setName(occupationDTO.getName());
        oshUTMRepo.save(oshUTM);
        bishUTM.setName(occupationDTO.getName());
        return bishUTMRepo.save(bishUTM);
    }
}
