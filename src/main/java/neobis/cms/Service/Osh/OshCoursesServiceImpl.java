package neobis.cms.Service.Osh;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Osh.OshClientRepo;
import neobis.cms.Repo.Osh.OshCoursesRepo;
import neobis.cms.Repo.Osh.OshTeacherRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OshCoursesServiceImpl implements OshCoursesService {
    private final OshCoursesRepo coursesRepo;
    private final OshTeacherRepo teacherRepo;
    private final OshClientRepo clientRepo;

    public OshCoursesServiceImpl(OshCoursesRepo coursesRepo, OshTeacherRepo teacherRepo, OshClientRepo clientRepo) {
        this.coursesRepo = coursesRepo;
        this.teacherRepo = teacherRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public OshCourses findCourseByFormName(String formName) {
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
    public OshCourses findCourseById(long id) {
        return coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " was not found"));
    }

    @Override
    public List<OshCourses> findAll() {
        return coursesRepo.findAll();
    }

    @Override
    public OshCourses findCourseByName(String name) {
        OshCourses course = coursesRepo.findByNameContainingIgnoringCase(name);
        if (course == null)
            throw new ResourceNotFoundException("Course with name " + name + " has not found");
        return course;
    }

    @Override
    public List<OshCourses> findCoursesByName(String name) {
        return coursesRepo.findAllByNameContainingIgnoringCase(name);
    }

    @Override
    public OshCourses addCourse(CoursesDTO courseDTO) {
        OshCourses course = new OshCourses();
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        long teacherID = courseDTO.getTeacherID();
        if (teacherID != 0) {
            OshTeachers teacher = teacherRepo.findById(teacherID)
                    .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с идентификатором " + teacherID + " не найден."));
            course.setTeacher(teacher);
        }
        return coursesRepo.save(course);
    }

    @Override
    public OshCourses updateCourse(long id, CoursesDTO courseDTO) {
        OshCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        long teacherID = courseDTO.getTeacherID();
        if (teacherID != 0) {
            OshTeachers teacher = teacherRepo.findById(teacherID)
                    .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с идентификатором " + teacherID + " не найден."));
            course.setTeacher(teacher);
        }
        return coursesRepo.save(course);
    }

    @Override
    public String deleteCourse(List<Long> courses) {
        for (long courseID : courses) {
            OshCourses course = this.findCourseById(courseID);
            List<OshClient> clients = clientRepo.findAllByCourse(course);
            if (!clients.isEmpty())
                throw new IllegalArgumentException("Курс " + course.getName() + "используется, не может быть удален.");

            coursesRepo.delete(course);
        }
        if (courses.size() > 1)
            return "Courses were successfully deleted";
        else
            return "Course with ID " + courses.get(0) + " was successfully deleted";
    }

    @Override
    public OshCourses findCourseByTeacher(OshTeachers teacher) {
        return coursesRepo.findByTeacher(teacher);
    }

    @Override
    public void save(OshCourses course) {
        coursesRepo.save(course);
    }
}
