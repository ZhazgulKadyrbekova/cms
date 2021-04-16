package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.PositionDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Entity.Osh.OshPosition;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishPositionRepo;
import neobis.cms.Repo.Bishkek.BishTeacherRepo;
import neobis.cms.Repo.Bishkek.UserRepo;
import neobis.cms.Repo.Osh.OshPositionRepo;
import neobis.cms.Repo.Osh.OshTeacherRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/position")
public class PositionController {
    private final BishPositionRepo bishPositionRepo;
    private final OshPositionRepo oshPositionRepo;
    private final BishTeacherRepo bishTeacherRepo;
    private final OshTeacherRepo oshTeacherRepo;
    private final UserRepo userRepo;

    public PositionController(BishPositionRepo bishPositionRepo, OshPositionRepo oshPositionRepo, BishTeacherRepo bishTeacherRepo, OshTeacherRepo oshTeacherRepo, UserRepo userRepo) {
        this.bishPositionRepo = bishPositionRepo;
        this.oshPositionRepo = oshPositionRepo;
        this.bishTeacherRepo = bishTeacherRepo;
        this.oshTeacherRepo = oshTeacherRepo;
        this.userRepo = userRepo;
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
            List<BishTeachers> bishTeachers = bishTeacherRepo.findAllByPosition(bishPosition);
            List<User> bishUsers = userRepo.findAllByPosition(bishPosition);
            if (!bishTeachers.isEmpty() || !bishUsers.isEmpty())
                throw new IllegalArgumentException("Должность " + bishPosition.getName() + "используется, не может быть удалена.");
            OshPosition oshPosition = oshPositionRepo.findById(occupation)
                    .orElseThrow(() -> new ResourceNotFoundException("Position with id " + occupation + " has not found"));
            List<OshTeachers> oshTeachers = oshTeacherRepo.findAllByPosition(oshPosition);
            if (!oshTeachers.isEmpty())
                throw new IllegalArgumentException("Должность " + oshPosition.getName() + "используется, не может быть удалена.");

            bishPositionRepo.delete(bishPosition);
            oshPositionRepo.delete(oshPosition);
        }
        return new ResponseMessage("Position with ID " + id + " has been deleted");
    }
}
