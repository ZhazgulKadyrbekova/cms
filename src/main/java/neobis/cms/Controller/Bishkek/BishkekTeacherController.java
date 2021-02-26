package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Service.Bishkek.BishTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/worker")
public class BishkekTeacherController {

    private final Logger logger = LogManager.getLogger();
    @Autowired
    private BishTeacherService teacherService;

    @GetMapping("/getAll")
    public List<BishTeachers> getAll() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public BishTeachers getById(@PathVariable long id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping("/{name}")
    public BishTeachers getByName(@PathVariable String name) {
        return teacherService.getTeacherByName(name);
    }

    @PostMapping("/add")
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
