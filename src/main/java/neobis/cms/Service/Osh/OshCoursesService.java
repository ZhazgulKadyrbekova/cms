package neobis.cms.Service.Osh;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Entity.Osh.OshTeachers;

import java.util.List;

public interface OshCoursesService {
    OshCourses findCourseByFormName(String formName);
    OshCourses findCourseById(long id);
    List<OshCourses> findAll();
    OshCourses findCourseByName(String name);
    List<OshCourses> findCoursesByName(String name);
    OshCourses addCourse(CoursesDTO courseDTO);
    OshCourses updateCourse(long id, CoursesDTO courseDTO);
    String deleteCourse(List<Long> courses);
    OshCourses findCourseByTeacher(OshTeachers teacher);
    void save(OshCourses course);
}
