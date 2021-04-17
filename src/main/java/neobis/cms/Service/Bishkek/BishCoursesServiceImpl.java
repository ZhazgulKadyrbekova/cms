package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishClientRepo;
import neobis.cms.Repo.Bishkek.BishCoursesRepo;
import neobis.cms.Repo.Bishkek.BishTeacherRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BishCoursesServiceImpl implements BishCoursesService{
    private final BishCoursesRepo coursesRepo;
    private final BishTeacherRepo teacherRepo;
    private final BishClientRepo clientRepo;

    public BishCoursesServiceImpl(BishCoursesRepo coursesRepo, BishTeacherRepo teacherRepo, BishClientRepo clientRepo) {
        this.coursesRepo = coursesRepo;
        this.teacherRepo = teacherRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public BishCourses findCourseByFormName(String formName) {
        if (formName.equals("Заявка с quiz") || formName.equals("Заявка на перезвон"))
            return null;
        if (formName.contains("PM") || formName.contains("Проектный менеджмент"))
            return coursesRepo.findByNameContainingIgnoringCase("ПМ");
        if (formName.contains("Java"))
            return coursesRepo.findByNameContainingIgnoringCase("Java");
        if (formName.contains("JS") || formName.contains("JavaScript"))
            return coursesRepo.findByNameContainingIgnoringCase("JavaScript");
        if (formName.contains("Python"))
            return coursesRepo.findByNameContainingIgnoringCase("Python");
        if (formName.contains("UX/UI"))
            return coursesRepo.findByNameContainingIgnoringCase("Design");
        if (formName.contains("olympiad") || formName.contains("Алгоритмы и структура данных"))
            return coursesRepo.findByNameContainingIgnoringCase("ОП");
        return null;
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
        long teacherID = courseDTO.getTeacherID();
        if (teacherID != 0) {
            BishTeachers teacher = teacherRepo.findById(teacherID)
                    .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с идентификатором " + teacherID + " не найден."));
            course.setTeacher(teacher);
        }
        return coursesRepo.save(course);
    }

    @Override
    public BishCourses updateCourse(long id, CoursesDTO courseDTO) {
        BishCourses course = coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " has not found"));
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        long teacherID = courseDTO.getTeacherID();
        if (teacherID != 0) {
            BishTeachers teacher = teacherRepo.findById(teacherID)
                    .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с идентификатором " + teacherID + " не найден."));
            course.setTeacher(teacher);
        }
        return coursesRepo.save(course);
    }

    @Override
    public String deleteCourse(List<Long> courses) {
        for (long courseID : courses) {
            BishCourses course = this.findCourseById(courseID);
            List<BishClient> bishClients = clientRepo.findAllByCourse(course);
            if (!bishClients.isEmpty())
                throw new IllegalArgumentException("Курс " + course.getName() + "используется, не может быть удален.");

            coursesRepo.delete(course);
        }
        if (courses.size() > 1)
            return "Courses were successfully deleted";
        else
            return "Course with ID " + courses.get(0) + " was successfully deleted";
    }

    @Override
    public BishCourses findCourseByTeacher(BishTeachers teacher) {
        return coursesRepo.findByTeacher(teacher);
    }

    @Override
    public void save(BishCourses course) {
        coursesRepo.save(course);
    }
}
