package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishTeachers;

import java.util.List;

public interface BishCoursesService {
    BishCourses findCourseByName(String name);
    BishCourses findCourseById(long id);
    List<BishCourses> findAll();
    List<BishCourses> findCoursesByName(String name);
    BishCourses addCourse(CoursesDTO courseDTO);
    BishCourses updateCourse(long id, CoursesDTO courseDTO);
    String deleteCourse(List<Long> courses);
    BishCourses findCourseByTeacher(BishTeachers teacher);
    void save(BishCourses course);
}
