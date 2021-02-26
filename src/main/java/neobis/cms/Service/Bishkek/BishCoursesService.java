package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.CoursesDTO;
import neobis.cms.Entity.Bishkek.BishCourses;

import java.util.List;

public interface BishCoursesService {
    BishCourses findCourseByFormName(String formName);
    BishCourses findCourseById(long id);
    List<BishCourses> findAll();
    List<BishCourses> findCourseByName(String name);
    BishCourses addCourse(CoursesDTO courseDTO);
    BishCourses updateCourse(long id, CoursesDTO courseDTO);
    String deleteCourse(long id);
}
