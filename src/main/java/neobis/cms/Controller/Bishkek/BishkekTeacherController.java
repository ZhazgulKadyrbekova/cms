package neobis.cms.Controller.Bishkek;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Service.Bishkek.BishTeacherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/worker")
public class BishkekTeacherController {

    private final Logger logger = LogManager.getLogger();
    private final BishTeacherService teacherService;

    public BishkekTeacherController(BishTeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20")
    })
    public Page<WorkerDTO> getAll(Pageable pageable) {
        return teacherService.getAllWorkers(pageable);
    }

    @GetMapping("/filter")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20")})
    public Page<WorkerDTO> filter(Pageable pageable,
                                  @RequestParam(value = "position", required = false) String position,
                                  @RequestParam(value = "courseID", required = false) List<Long> courseID) {
        return teacherService.getWithPredicate(pageable, position, courseID);
    }

//    @GetMapping("/advancedSearch")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
//                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
//            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
//                    value = "Number of records per page.", defaultValue = "20")})
//    public Page<WorkerDTO> advancedSearch(Pageable pageable,
//                                             @RequestParam(required = false) List<Long> course_id) {
//        return teacherService.advancedSearch(pageable, course_id);
//    }

    @PostMapping
    public BishTeachers addNewTeacher(@RequestBody TeacherDTO teacherDTO) {
        logger.info("In Bishkek created new teacher {}", teacherDTO.toString());
        return teacherService.addTeacher(teacherDTO);
    }

    @PutMapping("/{id}")
    public BishTeachers updateTeacher(@PathVariable long id, @RequestBody TeacherDTO teacherDTO) {
        logger.info("In Bishkek updated teacher id {} info {}", id, teacherDTO.toString());
        return teacherService.updateTeacherInfo(id, teacherDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteTeacher(@PathVariable long id) {
        logger.info("In Bishkek deleted teacher id {}", id);
        return new ResponseMessage(teacherService.deleteTeacherById(id));
    }

}
