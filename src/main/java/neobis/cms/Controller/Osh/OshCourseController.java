package neobis.cms.Controller.Osh;

import lombok.extern.log4j.Log4j2;
import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Service.Bishkek.BishCoursesService;
import neobis.cms.Service.Osh.OshCoursesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/course")
public class OshCourseController {

    private final Logger log = LogManager.getLogger();

    @Autowired
    private OshCoursesService coursesService;

    @GetMapping
    public List<OshCourses> getAll() {
        return coursesService.findAll();
    }

    @GetMapping("/{id}")
    public OshCourses getById(@PathVariable long id) {
        return coursesService.findCourseById(id);
    }

    @GetMapping("/name/{name}")
    public OshCourses getByName(@PathVariable String name) {
        return coursesService.findCourseByName(name);
    }

    @PostMapping
    public OshCourses addNewCourse(@RequestBody CoursesDTO coursesDTO) {
        log.info("In Bishkek created new course {}", coursesDTO.toString());
        return coursesService.addCourse(coursesDTO);
    }

    @PutMapping("/{id}")
    public OshCourses updateCourse(@PathVariable long id, @RequestBody CoursesDTO coursesDTO) {
        log.info("In Bishkek updated course id {} info {}", id, coursesDTO.toString());
        return coursesService.updateCourse(id, coursesDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteCourse(@PathVariable long id) {
        log.info("In Bishkek deleted course id {}", id);
        return new ResponseMessage(coursesService.deleteCourse(id));
    }

}
