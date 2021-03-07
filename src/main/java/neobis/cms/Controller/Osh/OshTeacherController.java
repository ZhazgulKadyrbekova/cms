package neobis.cms.Controller.Osh;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Service.Osh.OshTeacherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/worker")
public class OshTeacherController {

    private final Logger logger = LogManager.getLogger();
    @Autowired
    private OshTeacherService teacherService;

    @GetMapping
    public List<OshTeachers> getAll() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public OshTeachers getById(@PathVariable long id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping("/name/{name}")
    public OshTeachers getByName(@PathVariable String name) {
        return teacherService.getTeacherByName(name);
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
