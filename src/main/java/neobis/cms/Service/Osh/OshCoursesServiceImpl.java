package neobis.cms.Service.Osh;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Osh.OshCoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OshCoursesServiceImpl implements OshCoursesService {
    @Autowired
    private OshCoursesRepo coursesRepo;

    @Autowired
    private OshTeacherService teacherService;

    @Override
    public OshCourses findCourseByFormName(String formName) {
        if (formName.equals("Заявка с quiz"))
            return null;
        formName = formName.substring(7);
        if (formName.contains("PM"))
            return coursesRepo.findByNameContainingIgnoringCaseAndDeleted("project manager", false);
        if (formName.contains("Java"))
            return coursesRepo.findByNameContainingIgnoringCaseAndDeleted("java", false);
        if (formName.contains("JS"))
            return coursesRepo.findByNameContainingIgnoringCaseAndDeleted("javascript", false);
        if (formName.contains("python"))
            return coursesRepo.findByNameContainingIgnoringCaseAndDeleted("python", false);
        if (formName.contains("design"))
            return coursesRepo.findByNameContainingIgnoringCaseAndDeleted("design", false);
        if (formName.contains("olympiad"))
            return coursesRepo.findByNameContainingIgnoringCaseAndDeleted("olympiad", false);
        return null;
    }

    @Override
    public OshCourses findCourseById(long id) {
        return coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " was not found"));
    }

    @Override
    public List<OshCourses> findAll() {
        return coursesRepo.findAllByDeleted(false);
    }

    @Override
    public OshCourses findCourseByName(String name) {
        OshCourses course = coursesRepo.findByNameContainingIgnoringCaseAndDeleted(name, false);
        if (course == null)
            throw new ResourceNotFoundException("Course with name " + name + " has not found");
        return course;
    }

    @Override
    public OshCourses addCourse(CoursesDTO courseDTO) {
        OshCourses course = new OshCourses();
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        course.setDescription(courseDTO.getDescription());
        course.setTeacher(teacherService.getTeacherById(courseDTO.getTeacher()));
        return coursesRepo.save(course);
    }

    @Override
    public OshCourses updateCourse(long id, CoursesDTO courseDTO) {
        OshCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        course.setDescription(courseDTO.getDescription());
        course.setTeacher(teacherService.getTeacherById(courseDTO.getTeacher()));
        return coursesRepo.save(course);
    }

    @Override
    public String deleteCourse(long id) {
        OshCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setDeleted(true);
        coursesRepo.save(course);
        return "Course with id " + id + " has not found";
    }
}
