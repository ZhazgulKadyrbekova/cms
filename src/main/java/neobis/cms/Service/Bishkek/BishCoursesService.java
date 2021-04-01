package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Bishkek.BishCourses;

import java.util.List;

public interface BishCoursesService {
    BishCourses findCourseByName(String name);
    BishCourses findCourseById(long id);
    List<BishCourses> findAll();
    List<BishCourses> findCoursesByName(String name);
    BishCourses addCourse(CoursesDTO courseDTO);
    BishCourses updateCourse(long id, CoursesDTO courseDTO);
    String deleteCourse(long id);
    BishCourses setTeacher(long courseID, long teacherID);
}
