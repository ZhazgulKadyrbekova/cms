package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Service.Bishkek.BishCoursesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/course")
public class BishkekCourseController {
    private final Logger log = LogManager.getLogger();

    @Autowired
    private BishCoursesService coursesService;

    @GetMapping
    public List<BishCourses> getAll() {
        return coursesService.findAll();
    }

    @GetMapping("/{id}")
    public BishCourses getById(@PathVariable long id) {
        return coursesService.findCourseById(id);
    }

    @GetMapping("/name/{name}")
    public List<BishCourses> getByName(@PathVariable String name) {
        return coursesService.findCoursesByName(name);
    }

    @PostMapping
    public BishCourses addNewCourse(@RequestBody CoursesDTO coursesDTO) {
        log.info("In Bishkek created new course {}", coursesDTO.toString());
        return coursesService.addCourse(coursesDTO);
    }

    @PutMapping("/{id}")
    public BishCourses updateCourse(@PathVariable long id, @RequestBody CoursesDTO coursesDTO) {
        log.info("In Bishkek updated course id {} info {}", id, coursesDTO.toString());
        return coursesService.updateCourse(id, coursesDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteCourse(@PathVariable List<Long> id) {
        log.info("In Bishkek deleted course id {}", id);
        return new ResponseMessage(coursesService.deleteCourse(id));
    }

}
