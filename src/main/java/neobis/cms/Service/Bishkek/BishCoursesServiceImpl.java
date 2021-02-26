package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishCoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BishCoursesServiceImpl implements BishCoursesService{
    @Autowired
    private BishCoursesRepo coursesRepo;

    @Autowired
    private BishTeacherService teacherService;

    @Override
    public BishCourses findCourseByFormName(String formName) {
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
    public BishCourses findCourseById(long id) {
        return coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " was not found"));
    }

    @Override
    public List<BishCourses> findAll() {
        return coursesRepo.findAllByDeleted(false);
    }

    @Override
    public List<BishCourses> findCourseByName(String name) {
        List<BishCourses> courses = coursesRepo.findAllByNameContainingIgnoringCaseAndDeleted(name, false);
        if (courses.isEmpty())
            throw new ResourceNotFoundException("Course with name " + name + " has not found");
        return courses;
    }

    @Override
    public BishCourses addCourse(CoursesDTO courseDTO) {
        BishCourses course = new BishCourses();
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        course.setDescription(courseDTO.getDescription());
        course.setTeacher(teacherService.getTeacherById(courseDTO.getTeacher()));
        return coursesRepo.save(course);
    }

    @Override
    public BishCourses updateCourse(long id, CoursesDTO courseDTO) {
        BishCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        course.setDescription(courseDTO.getDescription());
        course.setTeacher(teacherService.getTeacherById(courseDTO.getTeacher()));
        return coursesRepo.save(course);
    }

    @Override
    public String deleteCourse(long id) {
        BishCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setDeleted(true);
        coursesRepo.save(course);
        return "Course with id " + id + " has not found";
    }
}
