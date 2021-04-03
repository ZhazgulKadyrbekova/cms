package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishCoursesRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BishCoursesServiceImpl implements BishCoursesService{
    private final BishCoursesRepo coursesRepo;

    public BishCoursesServiceImpl(BishCoursesRepo coursesRepo) {
        this.coursesRepo = coursesRepo;
    }

    @Override
    public BishCourses findCourseByName(String name) {
        return coursesRepo.findByNameContainingIgnoringCase(name);
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
    public List<BishCourses> findCoursesByName(String name) {
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
        return coursesRepo.save(course);
    }

    @Override
    public BishCourses updateCourse(long id, CoursesDTO courseDTO) {
        BishCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        return coursesRepo.save(course);
    }

    @Override
    public String deleteCourse(List<Long> courses) {
        for (long courseID : courses) {
            BishCourses course = this.findCourseById(courseID);
            coursesRepo.delete(course);
        }
        if (courses.size() > 1)
            return "Courses were successfully deleted";
        else
            return "Course with ID " + courses.get(0) + " was successfully deleted";
    }
}
