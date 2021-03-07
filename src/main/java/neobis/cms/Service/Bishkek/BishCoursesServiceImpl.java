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
            return coursesRepo.findByNameContainingIgnoringCase("project manager");
        if (formName.contains("Java"))
            return coursesRepo.findByNameContainingIgnoringCase("java");
        if (formName.contains("JS"))
            return coursesRepo.findByNameContainingIgnoringCase("javascript");
        if (formName.contains("python"))
            return coursesRepo.findByNameContainingIgnoringCase("python");
        if (formName.contains("design"))
            return coursesRepo.findByNameContainingIgnoringCase("design");
        if (formName.contains("olympiad"))
            return coursesRepo.findByNameContainingIgnoringCase("olympiad");
        return null;
    }

    @Override
    public BishCourses findCourseById(long id) {
        return coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " was not found"));
    }

    @Override
    public List<BishCourses> findAll() {
        return coursesRepo.findAll();
    }

    @Override
    public List<BishCourses> findCourseByName(String name) {
        List<BishCourses> courses = coursesRepo.findAllByNameContainingIgnoringCase(name);
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
        coursesRepo.delete(course);
        return "Course with id " + id + " has not found";
    }
}
