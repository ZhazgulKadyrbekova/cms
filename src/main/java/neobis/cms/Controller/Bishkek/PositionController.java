package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.PositionDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Osh.OshPosition;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishPositionRepo;
import neobis.cms.Repo.Osh.OshPositionRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/position")
public class PositionController {
    private final BishPositionRepo bishPositionRepo;
    private final OshPositionRepo oshPositionRepo;

    public PositionController(BishPositionRepo bishPositionRepo, OshPositionRepo oshPositionRepo) {
        this.bishPositionRepo = bishPositionRepo;
        this.oshPositionRepo = oshPositionRepo;
    }

    @GetMapping
    public List<BishPosition> getAllPositions() {
        return bishPositionRepo.findAll();
    }

    @PutMapping("/{id}")
    public BishPosition updatePosition(@RequestBody PositionDTO positionDTO, @PathVariable Long id) {
        OshPosition oshPosition = oshPositionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position with id " + id + " has not found"));
        BishPosition bishPosition = bishPositionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position with id " + id + " has not found"));

        oshPosition.setName(positionDTO.getName());
        oshPositionRepo.save(oshPosition);
        bishPosition.setName(positionDTO.getName());
        return bishPositionRepo.save(bishPosition);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deletePositionByID(@PathVariable List<Long> id) {
        for (long occupation : id) {
            BishPosition bishPosition = bishPositionRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Position with id " + occupation + " has not found"));
            OshPosition oshPosition = oshPositionRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Position with id " + occupation + " has not found"));

            bishPositionRepo.delete(bishPosition);
            oshPositionRepo.delete(oshPosition);
        }
        return new ResponseMessage("Position with ID " + id + " has been deleted");
    }
}
