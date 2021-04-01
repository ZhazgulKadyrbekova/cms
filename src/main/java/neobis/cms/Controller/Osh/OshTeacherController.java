package neobis.cms.Controller.Osh;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Service.Osh.OshTeacherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/worker")
public class OshTeacherController {

    private final Logger logger = LogManager.getLogger();
    private final OshTeacherService teacherService;

    public OshTeacherController(OshTeacherService teacherService) {
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
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "field", dataType = "string", paramType = "query",
                    value = "Name, surname, email, phone number by which to search.")})
    public Page<WorkerDTO> filter(Pageable pageable,
                                               @ApiIgnore @RequestParam(required = false) String field,
                                               @RequestParam(value = "position", required = false) String position,
                                               @RequestParam(value = "courseID", required = false) List<Long> courseID) {
        List<WorkerDTO> workers = new ArrayList<>(teacherService.getWithPredicate(field, position, courseID));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), workers.size());

        return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
    }

    @GetMapping("/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "field", dataType = "string", paramType = "query", required = true,
                    value = "Name, surname, email, phone number by which to search.")})
    public Page<Object> search(Pageable pageable, @ApiIgnore @RequestParam String field) {
        List<Object> workers = new ArrayList<>(teacherService.simpleSearch(field));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), workers.size());

        return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
    }

    @PostMapping
    public OshTeachers addNewTeacher(@RequestBody TeacherDTO teacherDTO) {
        logger.info("In Bishkek created new teacher {}", teacherDTO.toString());
        return teacherService.addTeacher(teacherDTO);
    }

    @PutMapping("/{id}")
    public OshTeachers updateTeacher(@PathVariable long id, @RequestBody TeacherDTO teacherDTO) {
        logger.info("In Bishkek updated teacher id {} info {}", id, teacherDTO.toString());
        return teacherService.updateTeacherInfo(id, teacherDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteTeacher(@PathVariable long id) {
        logger.info("In Bishkek deleted teacher id {}", id);
        return new ResponseMessage(teacherService.deleteTeacherById(id));
    }

}
