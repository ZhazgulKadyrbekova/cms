package neobis.cms.Controller.Bishkek;

import neobis.cms.Service.Bishkek.BishCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/course")
public class BishkekCourseController {
    @Autowired
    private BishCoursesService coursesService;


}
