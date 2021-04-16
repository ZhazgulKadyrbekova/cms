package neobis.cms.Controller.Osh;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Service.Osh.OshCoursesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/course")
public class OshCourseController {

    private final Logger log = LogManager.getLogger();

    private final OshCoursesService coursesService;

    public OshCourseController(OshCoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping
    public List<OshCourses> getAll() {
        return coursesService.findAll();
    }

    @GetMapping("/{id}")
    public OshCourses getById(@PathVariable long id) {
        return coursesService.findCourseById(id);
    }

    @GetMapping("/name/{name}")
    public List<OshCourses> getByName(@PathVariable String name) {
        return coursesService.findCoursesByName(name);
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
    public ResponseMessage deleteCourse(@PathVariable List<Long> id) {
        log.info("In Bishkek deleted course id {}", id);
        return new ResponseMessage(coursesService.deleteCourse(id));
    }

}
